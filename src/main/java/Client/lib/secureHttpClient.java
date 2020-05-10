package Client.lib;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class secureHttpClient {


    private secureHttpClient() {
    }

    /**
     * This will generate an instance of OkHttpClient that only accepts encrypted connections
     * to the server with a certificate that is signed by our own certificate authority. This
     * we prevent reverse engineering of the protocol.
     * @return Secure version of OkHttpClient.
     */
    public static OkHttpClient build() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            InputStream certificateStream = secureHttpClient.class
                    .getResourceAsStream("/certificate");
            trustManager = trustManagerForCertificates(certificateStream);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                //accept all hostnames, since we are verifying the certificate ourselves anyway
                .hostnameVerifier((String hostname, SSLSession sslSession) -> true)
                .build();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services
     * whose certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a
     * custom set. This is useful in development where certificate authority-trusted
     * certificates aren't available. Or in production, to avoid reliance on third-party
     * certificate authorities.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory
                .generateCertificates(in);

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
