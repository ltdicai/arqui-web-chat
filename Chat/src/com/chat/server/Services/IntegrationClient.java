package com.chat.server.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class IntegrationClient {

    private static final Logger logger = Logger.getLogger("IntegrationClient");

    public IntegrationClient () {
    }

    private static ClientConfig getConfig() {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, 500);
        return config;
    }

    public static String getEndpoint() {
        String endpoint = System.getenv("ENDPOINT");
        if (endpoint == null) {
            endpoint = "http://localhost:5000";
        }
        return endpoint;
    }

    public static void ping() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient((Configuration) config);
        WebTarget target = client.target(getEndpoint()).path("ping");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }
    public static void syncUsers() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient((Configuration) config);
        WebTarget target = client.target(getEndpoint()).path("platforms");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.get();
        logger.info(String.valueOf(response.getStatus()));
        ObjectMapper mapper = new ObjectMapper();
        String content = response.readEntity(String.class);
        logger.info(content);
        try {
            JsonNode json = mapper.readTree(content);
            IntegrationService.syncUsers(json);
        } catch (Exception exc) {
            logger.severe(exc.toString() + "---" + exc.getMessage());
        }
    }

    public static void newUser(String data) {
        Client client = ClientBuilder.newClient((Configuration) getConfig());
        WebTarget target = client.target(getEndpoint()).path("user");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public static void newRoom(String data) {
        Client client = ClientBuilder.newClient((Configuration) getConfig());
        WebTarget target = client.target(getEndpoint()).path("room");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public static void newMessage(String data) {
        Client client = ClientBuilder.newClient((Configuration) getConfig());
        WebTarget target = client.target(getEndpoint()).path("message");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }



}
