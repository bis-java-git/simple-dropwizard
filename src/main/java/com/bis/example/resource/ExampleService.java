package com.bis.example.resource;

import com.bis.example.config.ExampleServiceConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ExampleService extends Application<ExampleServiceConfiguration> {


    public static void main(String[] args) throws Exception {
        new ExampleService().run("server", "configuration.yaml");
    }

    @Override
    public void initialize(final Bootstrap<ExampleServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ClasspathConfigurationSourceProvider());
        bootstrap.addBundle(new SystemAndEnvironmentPropertyInYamlBundle());

    }


    @Override
    public void run(final ExampleServiceConfiguration exampleServiceConfiguration, final Environment environment) throws Exception {
        environment.jersey().register(new HelloResource(exampleServiceConfiguration.getMessages()));


        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {

            }
        });

    }
}
