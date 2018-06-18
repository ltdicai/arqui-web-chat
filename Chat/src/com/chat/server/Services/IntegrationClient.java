package com.chat.server.Services;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class IntegrationClient {

    public IntegrationClient () {
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

    public static void newUser(String data) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient((Configuration) config);
        WebTarget target = client.target(getEndpoint()).path("user");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public static void newRoom(String data) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient((Configuration) config);
        WebTarget target = client.target(getEndpoint()).path("room");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }



}
