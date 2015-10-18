package eu.kielczewski.example.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hemant joshi on 17/10/2015.
 * Agile Framework Team
 */
public class ClasspathConfigurationSourceProvider implements io.dropwizard.configuration.ConfigurationSourceProvider {
    @Override
    public InputStream open(String path) throws IOException {
        InputStream resourceAsStream = ClasspathConfigurationSourceProvider.class.getClassLoader().getResourceAsStream(path);

        if (resourceAsStream == null) {
            throw new IllegalArgumentException("could not find resource [path=" + path + "]");
        }

        return resourceAsStream;
    }
}
