package com.example.moja.oauth2.credentials;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * Created by moja on 08.06.2017.
 */

public class CipherUtils {

    private static KeyStore mKeyStore = null;

    private static void init() {
        if (mKeyStore == null) {
            try {
                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
                mKeyStore.load(null);
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void decryptString(String toBeDecrypted, Context context) {
        init();
        KeyPair keyPair = createNewKey(toBeDecrypted, context);
        if (keyPair != null) {
            try {
                Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
                output.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void encryptString(String toBeEncrypted, Context context) {
        init();
        KeyPair keyPair = createNewKey(toBeEncrypted, context);
        if (keyPair != null) {
            try {
                Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
                input.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, input);
                cipherOutputStream.write(toBeEncrypted.getBytes("UTF-8"));
                cipherOutputStream.close();

                byte[] vals = outputStream.toByteArray();
                String s = Base64.encodeToString(vals, Base64.DEFAULT);
                Log.i("Cipher", toBeEncrypted);
                Log.i("Cipher", s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static KeyPair createNewKey(String alias, Context context) {
        init();
        try {
            if (!mKeyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=MIA, O=T-Systems MMS"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                return generator.generateKeyPair();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
