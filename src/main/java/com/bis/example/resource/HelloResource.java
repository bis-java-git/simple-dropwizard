package com.bis.example.resource;

import com.bis.example.config.Messages;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(value = "/hello")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    private final Messages conf;

    public HelloResource(final Messages conf) {
        this.conf = conf;
    }

    @GET
    public String sayHello() {
        return conf.getHello();
    }

}
