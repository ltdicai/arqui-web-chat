package com.chat.server.Services;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class IntegrationClient {

    public IntegrationClient () {
    }

    public static void ping() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient((Configuration) config);
        WebTarget target = client.target("http://localhost:5000").path("ping");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }



}
