Dropwizard Example
==================

Minimal Dropwizard application

Check out: [http://kielczewski.eu/2013/04/developing-restful-web-services-using-dropwizard/](http://kielczewski.eu/2013/04/developing-restful-web-services-using-dropwizard/)

Requirements
------------
* [Java Platform (JDK) 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Apache Maven 3.x](http://maven.apache.org/)

Quick start
-----------
1. `mvn package`
2. `java -jar target/example-dropwizard-1.0-SNAPSHOT.jar`
3. Point your browser to [http://localhost:8080/hello](http://localhost:8080/hello)




//1
//Create a keystore and generate a certifcate
// The above process has created the keystore and the 'server' certificate, stored in the file keystore.jks.
//Alternatively, you can create a new certificate in a keystore non-interactively by specifying the passwords and certificate contents on the command-line:
keytool -genkey -alias bisserver -keyalg RSA -keystore keystore.jks -dname "cn=localhost, ou=IT, o=Continuent, c=US" -storepass password -keypass password

//2
//This KeyStore contains an entry with an alias of bisserver. This entry consists of the generated private key and information needed for generating a CSR as follows:
keytool -keystore keystore.jks -certreq -alias bisserver -keyalg RSA -file client.cer

//3 NOT SURE
//Now you need to export the certificate so that it can be added to the truststore as the trusted certificate:
keytool -export -alias bisserver -file client.cer -keystore keystore.jks


//4
//This has created a certificate file in client.cer that can now be used to populate your truststore. When added the certificate to the truststore, it must be identified as a trusted certificate to be valid. The password for the truststore must be provided. It can be the same, or different, to the one for the keystore, but must be known so that it can be added to the Continuent Tungsten configuration.
keytool -import -trustcacerts -alias bisserver -file client.cer -keystore truststore.ts -storepass password -noprompt
//This has created the truststore file, truststore.ts.

//5 does not work
keytool -import -keystore keystore.jks -file CARoot.cer -alias theCARoot

//6 does not work
keytool -import -keystore keystore.jks -file client.cer -alias bisserver


//using openssl

openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem

type key.pem cert.pem >key_and_cert.pem.txt

openssl pkcs12 -export -in key_and_cert.pem.txt -out client.p12 -name "bisserver"  -noiter -nomaciter

keytool -import -trustcacerts -alias bisserver -file cert.pem -keystore truststore.ts -storepass password -noprompt

keytool -import -alias bisserver -file cert.pem -keystore truststore.ts

keytool -keystore keystore.jks -certreq -alias bisserver -keyalg RSA -file client.pem


//keytool -genkey -alias bisserver -keyalg RSA -keystore keystore.jks

//1. Create a keystore and generate a certifcate
// The above process has created the keystore and the 'server' certificate, stored in the file keystore.jks.
//Alternatively, you can create a new certificate in a keystore non-interactively by specifying the passwords and certificate contents on the command-line:
keytool -genkey -alias bisserver -keyalg RSA -keystore keystore.jks -dname "cn=localhost, ou=IT, o=Continuent, c=US" -storepass password -keypass password

//2 Now you need to export the certificate so that it can be added to the truststore as the trusted certificate:
keytool -export -alias bisserver -file client.cer -keystore keystore.jks

//keytool -import -v -trustcacerts -alias bisserver -file client.cer -keystore truststore.ts

//This has created a certificate file in client.cer that can now be used to populate your truststore. When added the certificate to the truststore, it must be identified as a trusted certificate to be valid. The password for the truststore must be provided. It can be the same, or different, to the one for the keystore, but must be known so that it can be added to the Continuent Tungsten configuration.
keytool -import -trustcacerts -alias bisserver -file client.cer -keystore truststore.ts -storepass password -noprompt
//This has created the truststore file, truststore.ts.

//Creating a Custom Certificate and Getting it Signed
//keytool -genkey -alias bisserver -keyalg RSA -keystore keystore.jks

keytool -certreq -alias bisserver -file certrequest.pem -keypass  password -keystore keystore.jks -storepass password
//This creates a certificate request, certrequest.pem. This must be sent the to the signing authority to be signed.

// Official Signing
//Send the certificate file to your signing authority. They will send a signed certificate back, and also include a root CA and/or intermediary CA certificate. Both these and the signed certificate must be included in the keystore and truststore files.
keytool -import -alias bisserver -file signedcert.pem -keypass password -keystore keystore.jks -storepass password

keytool -import -alias careplserver -file cacert.pem -keypass password \
    -keystore keystore.jks -storepass password

keytool -import -alias interreplserver -file intercert.pem -keypass password \
    -keystore keystore.jks -storepass password

keytool -export -alias bisserver -file client.cer -keystore keystore.jks

keytool -import -trustcacerts -alias bisserver -file client.cer \
    -keystore truststore.ts -storepass password -noprompt

keytool -import -trustcacerts -alias careplserver -file cacert.pem \
    -keystore truststore.ts -storepass password -noprompt

keytool -import -trustcacerts -alias interreplserver -file intercert.pem \
    -keystore truststore.ts -storepass password -noprompt

openssl ca -in certrequest.pem -out certificate.pem

openssl x509 -in certificate.pem -out certificate.pem -outform PEM

cat certificate.pem cacert.pem > certfull.pem

keytool -import -alias bisserver -file certificate.pem -keypass password -keystore keystore.jks -storepass password

keytool -export -alias bisserver -file client.cer -keystore keystore.jks

keytool -import -trustcacerts -alias replserver -file client.cer \
    -keystore truststore.ts -storepass password -noprompt

openssl x509 -in signedcert.crt -out certificate.pem -outform PEM

keytool -import -file certificate.pem -keypass password \
    -keystore keystore.jks -storepass password

keytool -import -file cacert.pem -keypass password \
    -keystore keystore.jks -storepass password

keytool -import -file intercert.pem -keypass password \
    -keystore keystore.jks -storepass password

keytool -export -alias bisserver -file client.cer -keystore keystore.jks

keytool -import -trustcacerts -alias bisserver -file client.cer \
    -keystore truststore.ts -storepass password -noprompt

keytool -import -trustcacerts -alias bisserver -file cacert.pem \
    -keystore truststore.ts -storepass password -noprompt

keytool -import -trustcacerts -alias bisserver -file intercert.pem \
    -keystore truststore.ts -storepass password -noprompt

openssl pkcs12 -export -in client-cert.pem -inkey client-key.pem >client.p12

keytool -importkeystore -srckeystore client.p12 -destkeystore keystore.jks -srcstoretype pkcs12

openssl pkcs12 -export -in server-cert.pem -inkey server-key.pem >server.p12

keytool -importkeystore -srckeystore server.p12 -destkeystore truststore.ts -srcstoretype pkcs12



