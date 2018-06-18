package com.chat.server.Services;

import com.chat.client.Models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class IntegrationServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger("IntegrationServlet");

    // GET Methods
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        logger.info(requestURI);
        response.setContentType("application/json");
        try {
            if(requestURI.endsWith("/users")) {
                handleUsers(response);
            }
            else {
                markAsError(response, "Don't understand '" + requestURI + "'");
            }
        } catch (Exception exc) {
            String error = exc.toString() + "---" + exc.getMessage();
            markAsError(response, error);
        }
    }

    // POST Methods
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        logger.info(requestURI);
        response.setContentType("application/json");
        try {
            if (request.getContentLength() > 0) {
                if (requestURI.endsWith("/ping")) {
                    handlePing(response);
                }
                else if(requestURI.endsWith("/pong")) {
                    handlePong(response);
                }
                else if (requestURI.endsWith("/user")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(request.getReader());
                    handleNewUser(json, response);
                }
                else if (requestURI.endsWith("/room")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(request.getReader());
                    handleNewRoom(json, response);
                }
                else {
                    markAsError(response, "Don't understand '" + requestURI + "'");
                }
            } else {
                markAsError(response, "Empty body");
            }
        } catch (Exception exc) {
            String error = exc.toString() + "---" + exc.getMessage();
            markAsError(response, error);
        }
    }

    // Handlers

    private void handlePing(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("status", "ok");
        root.put("message", "pong");
        response.getWriter().write(mapper.writeValueAsString(root));
    }

    private void handlePong(HttpServletResponse response) throws IOException {
        logger.info("Received \"pong\"!");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        response.getWriter().write(mapper.writeValueAsString(root));
    }

    private void handleUsers(HttpServletResponse response) throws IOException {
        List<User> users = IntegrationService.getAllUsers();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode usersArray = root.putArray("users");
        for (User user:users) {
            ObjectNode userObj = usersArray.addObject();
            userObj.put("id", user.getUserID());
            userObj.put("name", user.getUserID());
            userObj.put("platform", "GWT_CHAT");
        }
        response.getWriter().write(mapper.writeValueAsString(root));
    }

    private void handleNewUser(JsonNode json, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        try {
            IntegrationService.receiveNewUser(json);
            root.put("status", "ok");
        } catch (Exception exc) {
            String error = exc.toString() + "---" + exc.getMessage();
            root.put("status", "failed");
        }
        response.getWriter().write(mapper.writeValueAsString(root));
    }

    private void handleNewRoom(JsonNode json, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        try {
            IntegrationService.receiveNewRoom(json);
            root.put("status", "ok");
        } catch (Exception exc) {
            String error = exc.toString() + "---" + exc.getMessage();
            root.put("status", "failed");
        }
        response.getWriter().write(mapper.writeValueAsString(root));
    }

    // Utils

    private void markAsError(HttpServletResponse response, String message) {
        response.setStatus(500);
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("status", "error");
            root.put("message", message);
            response.getWriter().write(mapper.writeValueAsString(root));
        } catch (IOException exc) {
            //
        }
        logger.warning(message);
    }

}
