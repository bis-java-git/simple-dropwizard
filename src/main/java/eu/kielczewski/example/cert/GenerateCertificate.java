package eu.kielczewski.example.cert;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by hemant joshi on 21/10/2015.
 * Agile Framework Team
 */
public class GenerateCertificate {

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, SignatureException {

        //KeyStore creation for PKCS12
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(null, null);
        //Get Certificate and private key for KeyStore
        CertAndKeyGen certAndKeyGen = new CertAndKeyGen("RSA", "SHA1WithRSA");
        certAndKeyGen.generate(1024);

        //get Private Key certAndKeyGen
        Key privateKey = certAndKeyGen.getPrivateKey();
        //get self signed certificate
        final X509Certificate x509CertificateServer = certAndKeyGen.getSelfCertificate(new X500Name("BIS",
                "GBM-IT",
                "BIS",
                "LONDON",
                "UK",
                "GB"), (long) 365 * 24 * 3600);
        final X509Certificate[] chainServer = new X509Certificate[1];
        chainServer[0] = x509CertificateServer;
        //Set private key in the KeyStore with bisserver alias
        keystore.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        //Set certificate in the KeyStore
        keystore.setCertificateEntry("certificate", x509CertificateServer);
        //Save certificate and key in KeyStore finally
        keystore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\\\main\\resources\\certificates\\keyStore.p12"), "password".toCharArray());

        //Creat Client Private Key and Certificate
        final CertAndKeyGen certAndKeyGenClient = new CertAndKeyGen("RSA", "SHA1WithRSA");
        certAndKeyGenClient.generate(1024);
        //Private Key
        final Key clientPrivateKey = certAndKeyGenClient.getPrivateKey();
        final X509Certificate x509CertificateClient = certAndKeyGenClient.getSelfCertificate(new X500Name("BIS",
                "GBM-IT",
                "BIS",
                "LONDON",
                "UK",
                "GB"), (long) 365 * 24 * 3600);
        final X509Certificate[] chainClient = new X509Certificate[1];
        chainClient[0] = x509CertificateClient;

        //TrustStore creation in PKCS12 format
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(null, null);
        // 1. Save Server private key and the certificate in the trust store
        trustStore.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        trustStore.setCertificateEntry("certificate", chainServer[0]);
        //2. Save Client private key and the certificate in the trust store
        trustStore.setKeyEntry("bisclient", clientPrivateKey, "password".toCharArray(), chainClient);
        trustStore.setCertificateEntry("bisclientcertificate", chainClient[0]);
        //finally save it into truststore in PKCS12 format
        trustStore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\trustStore.p12"), "password".toCharArray());

        //Now build server certificate for export for browser or any consumer for trust
        KeyStore exportCert = KeyStore.getInstance("PKCS12");
        exportCert.load(null, null);
        exportCert.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        exportCert.setCertificateEntry("certificate", chainServer[0]);
        exportCert.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisserver_cert.p12"), "password".toCharArray());

        //Now build client certificate for export for trust
        final KeyStore exportCertClient = KeyStore.getInstance("PKCS12");
        exportCertClient.load(null, null);
        exportCertClient.setKeyEntry("bisclient", clientPrivateKey, "password".toCharArray(), chainClient);
        exportCertClient.setCertificateEntry("bisclientcertificate", chainClient[0]);
        exportCertClient.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisclient_cert.p12"), "password".toCharArray());

    }
}
