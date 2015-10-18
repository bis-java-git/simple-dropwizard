package eu.kielczewski.example.config;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;


public class Messages {

    @NotNull
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

}
