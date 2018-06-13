package com.chat.server.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;


public class IntegrationServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger("IntegrationServlet");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        logger.info(requestURI);
        response.setContentType("application/json");
        try {
            if (request.getContentLength() > 0) {
                if (requestURI.endsWith("/ping")) {
                    handlePing(response);
                } else if(requestURI.endsWith("/pong")) {
                    handlePong(response);
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
