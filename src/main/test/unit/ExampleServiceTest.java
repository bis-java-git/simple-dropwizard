package unit;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Test;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by hemant joshi on 21/10/2015.
 * Agile Framework Team
 */
public class ExampleServiceTest {

    @Test
    public void apiHelloTest() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {

        SslConfigurator sslConfig = SslConfigurator.newInstance()
                .keyStore(getKeyStoreWithClientCert())
                .keyPassword("password");

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(getKeyStoreWithServerCert());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(getKeyStoreWithClientCert(),"password".toCharArray());

        SSLContext sslContext = sslConfig.createSSLContext();

  //      SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        sslContext.init(kmf.getKeyManagers(),trustManagers, null);

        Client client = ClientBuilder.newBuilder().
                hostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .withConfig(clientConfig)
                .sslContext(sslContext)
                .build();
       Response response = client.target("https://localhost:8443//hello").request().get();
        System.out.println("Response is "+ response.getStatus());

        System.out.println("Response is "+ response.readEntity(String.class));

    }

    private KeyStore getKeyStoreWithClientCert() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(Files.newInputStream(Paths.get("./src/main/resources/certificates/bisclient_cert.p12")), "password".toCharArray());
        return keyStore;
    }

    private KeyStore getKeyStoreWithServerCert() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(Files.newInputStream(Paths.get("./src/main/resources/certificates/bisserver_cert.p12")), "password".toCharArray());
        return keyStore;
    }
}
