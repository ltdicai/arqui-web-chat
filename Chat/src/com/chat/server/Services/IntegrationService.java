package com.chat.server.Services;

import com.chat.client.Models.*;
import com.chat.client.Services.UserDataService;
import com.chat.server.Services.DatabaseProcedures.ConversationDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.MessageDataBaseProcedures;
import com.chat.server.Services.DatabaseProcedures.TextMessageDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IntegrationService {


    public IntegrationService() {

    }

    public static String getToken() {
        String token = System.getenv("API_TOKEN");
        if (token == null) {
            token = "1111";
        }
        return token;
    }

    public static String getAppName() {
        String appName = System.getenv("APP_NAME");
        if (appName == null) {
            appName = "GWT_CHAT";
        }
        return appName;
    }

    public static String getPlatformNameForUser(User user) {
        String userId = user.getUserID();
        if (!userId.matches(".*/.*")) {
            return getAppName();
        } else {
            return userId.split("/")[0];
        }
    }

    public static String getPlatformNameForConversation(Conversation conv) {
        String convId = conv.getId();
        if (!convId.matches(".*:.*")) {
            return getAppName();
        } else {
            return convId.split(":")[0];
        }
    }

    public static String getIdForConversation(Conversation conv) {
        String convId = conv.getId();
        if (!convId.matches(".*:.*")) {
            return convId;
        } else {
            return convId.split(":")[1];
        }
    }

    public static List<User> getAllUsers() {
        List<User> results = new ArrayList<>();
        try {
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            results.addAll(userDatabaseProcedures.getAll());
        } catch (Exception exc) {
            //
        }
        return results;
    }

    private static Map<String, List<String>> getUsersPerPlatform(List<User> users) {
        String appName = getAppName();
        Map<String, List<String>> usersDict = new HashMap<>();
        for (User user : users) {
            String[] split = {"", ""};
            String userId = user.getUserID();
            if (!userId.matches(".*/.*")) {
                split[0] = appName;
                split[1] = userId;
            } else {
                split = userId.split("/");
            }
            if (!usersDict.containsKey(split[0])) {
                usersDict.put(split[0], new ArrayList<>());
            }
            usersDict.get(split[0]).add(split[1]);
        }
        return usersDict;
    }

    public static void sendNewUser(User user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("id",user.getUserID());
        root.put("name", user.getUserID());
        root.put("token", getToken());
        IntegrationClient.newUser(mapper.writeValueAsString(root));
    }

    public static void sendNewRoom(Conversation conversation) throws Exception {
        String appName = getAppName();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("token", getToken());
        root.put("id", conversation.getId());
        root.put("name", conversation.getId());
        ArrayNode users = root.putArray("users");
        Map<String, List<String>> usersDict;

        if (PrivateConversation.class.isInstance(conversation)) {
            PrivateConversation conv = (PrivateConversation) conversation;
            root.put("type", "private");
            usersDict = getUsersPerPlatform(
                    new ArrayList<>(Arrays.asList(conv.getUserHost(), conv.getUserInvite()))
            );
        } else {
            GroupConversation conv = (GroupConversation) conversation;
            root.put("type", "group");
            usersDict = getUsersPerPlatform(new ArrayList<>(conv.getMember()));
        }
        // Convert to json
        for(Map.Entry<String, List<String>> entry : usersDict.entrySet()) {
            ObjectNode node = users.addObject();
            ArrayNode array = node.putArray(entry.getKey());
            for (String userIdStr : entry.getValue()) {
                array.add(userIdStr);
            }
        }

        IntegrationClient.newRoom(mapper.writeValueAsString(root));
    }


    public static void receiveNewUser(JsonNode json) {
        String userId = json.get("platform").asText() + "/" + json.get("name").asText();
        String password = "default";
        User user = new User(userId);
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            userDatabaseProcedures.insert(user, encrypt(password));
        }
        catch (SQLException exc){
            System.out.println(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void receiveNewRoom(JsonNode json) throws Exception {
        ConversationDatabaseProcedures db = new ConversationDatabaseProcedures();
        String type = json.get("type").asText();
        String roomId = json.get("platform").asText() + ":" +json.get("id").asText();
        String platform = json.get("platform").asText();
        if ("private".equals(type)) {
            JsonNode platformsNode = json.get("users");
            List<String> userIds = new ArrayList<>();
            for (JsonNode platformNode : platformsNode) {
                Iterator<String> names = platformNode.fieldNames();
                String platformName = names.next();
                JsonNode userIdsNodes = platformNode.get(platformName);
                for (JsonNode userIdNode : userIdsNodes) {
                    String userId = userIdNode.asText();
                    if(!getAppName().equals(platformName)) {
                        userIds.add(platformName + "/" + userId);
                    } else {
                        userIds.add(userId);
                    }
                }
            }
            System.out.println(userIds.get(0));
            System.out.println(userIds.get(1));
            User hostUser = new User(userIds.get(0));
            User inviteUser = new User(userIds.get(1));
            try {
                db.insert(hostUser, inviteUser, roomId);
            } catch (Exception exc) {
                System.out.println(exc.toString() + "---" + exc.getMessage());
            }
        }
    }

    public static void sendNewMessage(Conversation conversation, TextMessage textMessage) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("roomOriginalPlatform", getPlatformNameForConversation(conversation));
        root.put("roomId", getIdForConversation(conversation));
        root.put("senderId", textMessage.getUser().getUserID());
        root.put("token", getToken());
        root.put("text", textMessage.getMessage());
        IntegrationClient.newMessage(mapper.writeValueAsString(root));
    }

    public static void receiveNewMessage(JsonNode json) {
        String userId = json.get("senderPlatform").asText() + "/" + json.get("senderId").asText();
        String conversationId;
        if (getAppName().equals(json.get("roomOriginalPlatform").asText())) {
            conversationId = json.get("roomId").asText();
        } else {
            conversationId = json.get("roomOriginalPlatform").asText() + ":" + json.get("roomId").asText();
        }
        String message = json.get("text").asText();
        User user = new User(userId);
        TextMessage textMessage = new TextMessage(user, message);
        try{
            TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
            textMessageDatabaseProcedures.insert(textMessage, conversationId);
        }
        catch (SQLException exc){
            System.out.println(exc.toString() + "---" + exc.getMessage());
        }
    }

    private static String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

}
