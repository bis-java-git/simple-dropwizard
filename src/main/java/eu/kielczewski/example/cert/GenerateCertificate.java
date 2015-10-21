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

    public static void oldmain(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, SignatureException {

        //KeyStore creation
        final KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(null, null);

        final CertAndKeyGen certAndKeyGen = new CertAndKeyGen("RSA", "SHA1WithRSA");
        certAndKeyGen.generate(1024);

        //Private Key
        final Key serverPrivateKey = certAndKeyGen.getPrivateKey();
        final X509Certificate x509CertificateServer = certAndKeyGen.getSelfCertificate(new X500Name("BIS",
                "GBM-IT",
                "BIS",
                "LONDON",
                "UK",
                "GB"), (long) 365 * 24 * 3600);
        final X509Certificate[] chainServer = new X509Certificate[1];
        chainServer[0] = x509CertificateServer;
        keystore.setKeyEntry("bisserverkey", serverPrivateKey, "password".toCharArray(), chainServer);
        keystore.setCertificateEntry("bisserver", x509CertificateServer);
        //Saved
        keystore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\keyStore.p12"), "password".toCharArray());

        keystore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisserver_cert.p12"), "password".toCharArray());

//        //Now build certificate for export for server
//        final KeyStore exportCertServer = KeyStore.getInstance("PKCS12");
//        exportCertServer.load(null, null);
//        exportCertServer.setKeyEntry("bisserverkey", serverPrivateKey, "password".toCharArray(), chainServer);
//        exportCertServer.setCertificateEntry("bisserver", chainServer[0]);
//        exportCertServer.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisserver_cert.p12"), "password".toCharArray());

//        //Client
//        final CertAndKeyGen certAndKeyGenClient = new CertAndKeyGen("RSA", "SHA1WithRSA");
//        certAndKeyGenClient.generate(1024);
//        //Private Key
//        final Key clientPrivateKey = certAndKeyGenClient.getPrivateKey();
//        final X509Certificate x509CertificateClient = certAndKeyGen.getSelfCertificate(new X500Name("BIS",
//                "GBM-IT",
//                "BIS",
//                "LONDON",
//                "UK",
//                "GB"), (long) 365 * 24 * 3600);
//        final X509Certificate[] chainClient = new X509Certificate[1];
//        chainClient[0] = x509CertificateClient;


        //TrustStore creation
        final KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(null, null);
        //construct private key we have private key
        // we have chain and privateKey
     trustStore.setCertificateEntry("bisserver", x509CertificateServer);
//        trustStore.setCertificateEntry("bisclientcertificate", x509CertificateClient);
        trustStore.setKeyEntry("bisserverkey", serverPrivateKey, "password".toCharArray(), chainServer);
//       trustStore.setKeyEntry("bisclientKey", clientPrivateKey, "password".toCharArray(), chainClient);
        //trustStore Saved
        trustStore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\trustStore.p12"), "password".toCharArray());

//        //Now build certificate for export for client
//        final KeyStore exportCertClient = KeyStore.getInstance("PKCS12");
//        exportCertClient.load(null, null);
//        exportCertClient.setKeyEntry("bisclientkey", clientPrivateKey, "password".toCharArray(), chainClient);
//        exportCertClient.setCertificateEntry("bisclientcertificate", chainClient[0]);
//        exportCertClient.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisclient_cert.p12"), "password".toCharArray());

    }


    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, SignatureException {

        //KeyStore creation
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(null, null);

        CertAndKeyGen certAndKeyGen = new CertAndKeyGen("RSA", "SHA1WithRSA");
        certAndKeyGen.generate(1024);

        //Private Key
        Key privateKey = certAndKeyGen.getPrivateKey();
        final X509Certificate x509CertificateServer = certAndKeyGen.getSelfCertificate(new X500Name("BIS",
                "GBM-IT",
                "BIS",
                "LONDON",
                "UK",
                "GB"), (long) 365 * 24 * 3600);
        final X509Certificate[] chainServer = new X509Certificate[1];
        chainServer[0] = x509CertificateServer;
        keystore.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        keystore.setCertificateEntry("certificate", x509CertificateServer);
        keystore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\\\main\\resources\\certificates\\keyStore.p12"), "password".toCharArray());

                //Client
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



        //TrustStore creation
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(null, null);
        // we have chainServer and privateKey
        trustStore.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        trustStore.setCertificateEntry("certificate", chainServer[0]);
        trustStore.setKeyEntry("bisclient", clientPrivateKey, "password".toCharArray(), chainClient);
        trustStore.setCertificateEntry("bisclientcertificate", chainClient[0]);

        trustStore.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\trustStore.p12"), "password".toCharArray());

        //Now build certificate for export
        KeyStore exportCert = KeyStore.getInstance("PKCS12");
        exportCert.load(null, null);
        exportCert.setKeyEntry("bisserver", privateKey, "password".toCharArray(), chainServer);
        exportCert.setCertificateEntry("certificate", chainServer[0]);
        exportCert.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisserver_cert.p12"), "password".toCharArray());


        //Now build certificate for export for client
        final KeyStore exportCertClient = KeyStore.getInstance("PKCS12");
        exportCertClient.load(null, null);
        exportCertClient.setKeyEntry("bisclient", clientPrivateKey, "password".toCharArray(), chainClient);
        exportCertClient.setCertificateEntry("bisclientcertificate", chainClient[0]);
        exportCertClient.store(new FileOutputStream("C:\\dev\\workspace\\example-dropwizard\\src\\main\\resources\\certificates\\bisclient_cert.p12"), "password".toCharArray());


    }
}
