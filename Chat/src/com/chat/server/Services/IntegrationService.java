package com.chat.server.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.UserDataService;
import com.chat.server.Services.DatabaseProcedures.ConversationDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Iterator;
import java.util.List;

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

    public static void sendNewUser(User user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("id",user.getUserID());
        root.put("name", user.getUserID());
        root.put("token", getToken());
        IntegrationClient.newUser(mapper.writeValueAsString(root));
    }

    public static void sendNewRoom(Conversation conversation) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("token", getToken());
        root.put("id", conversation.getId());
        root.put("name", conversation.getId());
        if (PrivateConversation.class.isInstance(conversation)) {
            root.put("type", "PRIVATE");
        } else {
            root.put("type", "GLOBAL");
        }
        IntegrationClient.newRoom(mapper.writeValueAsString(root));
    }


    public static void receiveNewUser(JsonNode json) {
        String userId = json.get("platform").asText() + "_" + json.get("name").asText();
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
        String roomId = json.get("id").asText();
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
                    userIds.add(platformName + "_" + userId);
                }
            }
            System.out.println(userIds.get(0));
            System.out.println(userIds.get(1));
            User hostUser = new User(userIds.get(0));
            User inviteUser = new User(userIds.get(1));
            try {
                PrivateConversation conversation = new PrivateConversation(hostUser, inviteUser);
                conversation.setExternalId(platform + "_" + roomId);
                db.insert(conversation);
            } catch (Exception exc) {
                System.out.println(exc.toString() + "---" + exc.getMessage());
            }
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
