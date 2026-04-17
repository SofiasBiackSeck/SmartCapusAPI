package com.mycompany.smartcampusapi;


import com.mycompany.smartcampusapi.filter.ApiLoggingFilter;
import com.mycompany.smartcampusapi.mapper.GlobalExceptionMapper;
import com.mycompany.smartcampusapi.mapper.LinkedResourceNotFoundExceptionMapper;
import com.mycompany.smartcampusapi.mapper.NotFoundExceptionMapper;
import com.mycompany.smartcampusapi.mapper.RoomNotEmptyExceptionMapper;
import com.mycompany.smartcampusapi.mapper.SensorUnavailableExceptionMapper;
import com.mycompany.smartcampusapi.resource.DiscoveryResource;
import com.mycompany.smartcampusapi.resource.RoomResource;
import com.mycompany.smartcampusapi.resource.SensorResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8081/api/v1/";

    public static HttpServer startServer() {
        final ResourceConfig config = new ResourceConfig()
                .register(DiscoveryResource.class)
                .register(RoomResource.class)
                .register(SensorResource.class)
                .register(RoomNotEmptyExceptionMapper.class)
                .register(LinkedResourceNotFoundExceptionMapper.class)
                .register(SensorUnavailableExceptionMapper.class)
                .register(NotFoundExceptionMapper.class)
                .register(GlobalExceptionMapper.class)
                .register(ApiLoggingFilter.class)
                .register(JacksonFeature.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Smart Campus API running at: " + BASE_URI);
        System.out.println("Press Enter to stop the server...");
        System.in.read();
        server.shutdownNow();
    }
}