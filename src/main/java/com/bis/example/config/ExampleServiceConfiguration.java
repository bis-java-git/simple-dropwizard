package com.bis.example.config;


import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class ExampleServiceConfiguration extends Configuration {

    @NotNull
    @Valid
    private Messages messages;

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
