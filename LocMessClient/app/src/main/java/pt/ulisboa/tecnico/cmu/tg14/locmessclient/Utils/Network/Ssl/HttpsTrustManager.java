package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Utils.Network.Ssl;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.R;

/**
 * Created by trosado on 5/13/17.
 */

public class HttpsTrustManager implements X509TrustManager {

    private static TrustManager[] trustManagers;
    private static List<X509Certificate> _AcceptedIssuers;
    private static Context mContext;
    private static KeyStore trustedKs;

    public HttpsTrustManager(Context context) {
        try{
            mContext = context;
            trustedKs = KeyStore.getInstance("BKS");
            InputStream inStream  = (InputStream) mContext.getResources().openRawResource(R.raw.server);
            trustedKs.load(inStream,"serverCert".toCharArray());
            _AcceptedIssuers = new ArrayList<>();
            _AcceptedIssuers.add((X509Certificate) trustedKs.getCertificate("myAlias"));

            inStream.close();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkClientTrusted(
            java.security.cert.X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        try {
            X509Certificate caCertificate = (X509Certificate) trustedKs.getCertificate("myAlias");

        if (certs == null || certs.length == 0) {
            throw new IllegalArgumentException("null or zero-length certificate chain");
        }

        if (authType == null || authType.length() == 0) {
            throw new IllegalArgumentException("null or zero-length authentication type");
        }

        //Check if certificate send is your CA's
        if(!certs[0].equals(caCertificate)){
            try
            {   //Not your CA's. Check if it has been signed by your CA
                certs[0].verify(caCertificate.getPublicKey());
            }
            catch(Exception e){
                throw new CertificateException("Certificate not trusted",e);
            }
        }
        //If we end here certificate is trusted. Check if it has expired.

            certs[0].checkValidity();
        }
        catch(Exception e){
            throw new CertificateException("Certificate not trusted. It has expired",e);
        }
    }


    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certificates = new X509Certificate[_AcceptedIssuers.size()];
        return _AcceptedIssuers.toArray(certificates);
    }

    public static void allowServerCertificate() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {

                return true;
            }

        });



        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new HttpsTrustManager(mContext)};
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }

}
