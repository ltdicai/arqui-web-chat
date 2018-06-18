package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.ConversationNotFoundException;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupConversationDatabaseProcedures {
    private Connection connection;
    private final UserDatabaseProcedures userDatabaseProcedures;
    private final MessageDataBaseProcedures messageDataBaseProcedures;

    public GroupConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
        userDatabaseProcedures = new UserDatabaseProcedures();
        messageDataBaseProcedures = new MessageDataBaseProcedures();
    }

    public void addMessage(Conversation conversation, Message message) throws SQLException {
        ConversationDatabaseProcedures conversationDatabaseProcedures = new ConversationDatabaseProcedures();
        conversationDatabaseProcedures.addMessage(conversation, message);
    }

    public void insert(String conversationName, User hostUser) throws SQLException {

        GroupConversation groupConversation = new GroupConversation(conversationName);

        String insert = "INSERT INTO gwtdbschema.conversations " +
                "(conversationid) VALUES " +
                "(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, conversationName);
        preparedStatement.execute();

        groupConversation.setId(conversationName);
        groupConversation.addUser(hostUser);


        insert = "INSERT INTO gwtdbschema.groupconversations"
                + "(conversationid, groupname) VALUES"
                + "(?,?)";

        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, groupConversation.getId());
        preparedStatement.setString(2, groupConversation.getName());
        preparedStatement.execute();

        for (User user:
                groupConversation.getMember()) {
            addUser(groupConversation, user);
        }
    }

    public void addUser(Conversation conversation, User user) throws SQLException{
        String insert = "INSERT INTO gwtdbschema.groups"
                + "(conversationid, userid) VALUES"
                + "(?,?)";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, conversation.getId());
        preparedStatement.setString(2, user.getUserID());
        preparedStatement.execute();
    }

    public List<GroupConversation> getGroupConversationsForUser(User user, int lastnumber)
            throws SQLException, ConversationNotFoundException, UserNotFoundException {
        String query = "SELECT * "
                + "FROM gwtdbschema.groupconversations gc "
                + "WHERE Exists(SELECT * FROM gwtdbschema.groups g WHERE " +
                "g.conversationid = gc.conversationid AND g.userid = ?) ";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, user.getUserID());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<GroupConversation> result = new ArrayList<>();

        while(resultSet.next()){
            GroupConversation groupConversation = new GroupConversation(resultSet.getString("groupname"));
            groupConversation.setId(resultSet.getString("conversationid"));
            for (Message message: messageDataBaseProcedures.get(groupConversation.getId(), lastnumber)) {
                groupConversation.addMessage(message);
            }

            query = "SELECT * FROM gwtdbschema.groups g WHERE " +
                    "g.conversationid = ?";

            preparedStatement = connection.prepareCall(query);
            preparedStatement.setString(1, groupConversation.getId());
            ResultSet resultSetUser = preparedStatement.executeQuery();

            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            while(resultSetUser.next()){
                User userToAdd = userDatabaseProcedures.get(resultSetUser.getString("userid"));
                groupConversation.addUser(userToAdd);
            }
            if(groupConversation.getMember().size() == 0){
                throw new ConversationNotFoundException();
            }
            result.add(groupConversation);
        }
        return result;
    }

    public GroupConversation getGroupConversation(String conversationid, int lastnumber)
            throws SQLException, ConversationNotFoundException, UserNotFoundException {
        String query = "SELECT * "
                + "FROM gwtdbschema.groupconversations"
                + " WHERE conversationid = ? LIMIT 1";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, conversationid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new ConversationNotFoundException();
        }
        GroupConversation result = new GroupConversation(resultSet.getString("groupname"));
        result.setId(resultSet.getString("conversationid"));

        query = "SELECT * FROM gwtdbschema.groups WHERE " +
                "conversationid = ? ";

        preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, result.getId());
        resultSet = preparedStatement.executeQuery();

        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while(resultSet.next()){
            User userToAdd = userDatabaseProcedures.get(resultSet.getString("userid"));
            result.addUser(userToAdd);
        }

        if(result.getMember().size() == 0) {
            throw new ConversationNotFoundException();
        }

        for (Message message: messageDataBaseProcedures.get(result.getId(), lastnumber)) {
            result.addMessage(message);
        }

        return result;
    }

    public List<User> getUserPosibleAdd(Conversation conversation)
            throws SQLException, ConversationNotFoundException, UserNotFoundException {
        String query = "SELECT * FROM gwtdbschema.users"
                + " WHERE not EXISTS (select * from gwtdbschema.groups where " +
                "users.userid = groups.userid and groups.conversationid = ?) ";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, conversation.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        while(resultSet.next()){
            User user = new User(resultSet.getString("userid"));
            users.add(user);
        }
        return users;
    }
}
