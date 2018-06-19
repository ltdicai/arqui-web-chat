package com.chat.server.Services;

import com.chat.client.Models.*;
import com.chat.server.Services.DatabaseProcedures.ConversationDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.GroupConversationDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.TextMessageDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class IntegrationService {

    private static final Logger logger = Logger.getLogger("IntegrationService");

    public IntegrationService() {}

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
            logger.severe(exc.toString() + "---" + exc.getMessage());
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

    public static void sendNewUser(User user) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("id", user.getUserID());
            root.put("name", user.getUserID());
            root.put("token", getToken());
            logger.info("Sending new user\n" + mapper.writeValueAsString(root));
            IntegrationClient.newUser(mapper.writeValueAsString(root));
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void sendNewRoom(Conversation conversation){
        try {
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
            for (Map.Entry<String, List<String>> entry : usersDict.entrySet()) {
                ObjectNode node = users.addObject();
                ArrayNode array = node.putArray(entry.getKey());
                for (String userIdStr : entry.getValue()) {
                    array.add(userIdStr);
                }
            }
            logger.info("Sending new room\n" + mapper.writeValueAsString(root));
            IntegrationClient.newRoom(mapper.writeValueAsString(root));
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }


    public static void receiveNewUser(JsonNode json) {
        try {
            logger.info("Receiving new user\n" + json.toString());
            String userId = json.get("platform").asText() + "/" + json.get("id").asText();
            String password = "default";
            User user = new User(userId);
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            userDatabaseProcedures.insert(user, encrypt(password));
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void receiveNewRoom(JsonNode json) throws Exception {
        try {
            logger.info("Receiving new room\n" + json.toString());
            ConversationDatabaseProcedures db = new ConversationDatabaseProcedures();
            GroupConversationDatabaseProcedures groupDB = new GroupConversationDatabaseProcedures();
            String type = json.get("type").asText();
            String roomId = json.get("platform").asText() + ":" + json.get("id").asText();
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
                        if (!getAppName().equals(platformName)) {
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
                    logger.severe(exc.toString() + "---" + exc.getMessage());
                }
            } else {
                JsonNode platformsNode = json.get("users");
                List<String> userIds = new ArrayList<>();
                for (JsonNode platformNode : platformsNode) {
                    Iterator<String> names = platformNode.fieldNames();
                    String platformName = names.next();
                    JsonNode userIdsNodes = platformNode.get(platformName);
                    for (JsonNode userIdNode : userIdsNodes) {
                        String userId = userIdNode.asText();
                        if (!getAppName().equals(platformName)) {
                            userIds.add(platformName + "/" + userId);
                        } else {
                            userIds.add(userId);
                        }
                    }
                }
                List<User> users = new ArrayList<>();
                for (String userId : userIds) {
                    users.add(new User(userId));
                }
                try {
                    //groupDB.insert();
                    //db.insert(hostUser, inviteUser, roomId);
                } catch (Exception exc) {
                    logger.severe(exc.toString() + "---" + exc.getMessage());
                }


            }
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void sendNewMessage(Conversation conversation, TextMessage textMessage){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("roomOriginalPlatform", getPlatformNameForConversation(conversation));
            root.put("roomId", getIdForConversation(conversation));
            root.put("senderId", textMessage.getUser().getUserID());
            root.put("token", getToken());
            root.put("text", textMessage.getMessage());
            logger.info("Sending new message\n" + mapper.writeValueAsString(root));
            IntegrationClient.newMessage(mapper.writeValueAsString(root));
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void receiveNewMessage(JsonNode json) {
        try {
            logger.info("Receiving new message\n" + json.toString());
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
            TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
            textMessageDatabaseProcedures.insert(textMessage, conversationId);

        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void syncUsers(JsonNode json) {
        logger.info("Syncing users\n" + json.toString());
        JsonNode platformsNode = json.get("platforms");
        for(JsonNode platformNode : platformsNode) {
            String platformName = platformNode.get("name").asText();
            if (getAppName().equals(platformName)) {
                continue;
            }
            JsonNode usersNode = platformNode.get("users");
            for(JsonNode userNode : usersNode) {
                try {
                    IntegrationService.receiveNewUser(userNode);
                } catch (Exception exc) {
                    // Es normal, no va a agregar usuarios repetidos
                    logger.info(exc.toString() + "---" + exc.getMessage());
                }
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
