package superkey.keychain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ico
 */
public class CipherTool {

    private String masterkey = null;

    public CipherTool(String masterkey) {
        this.masterkey = masterkey;
    }

    private SecretKeySpec buildSecretKeySpec() {
        String key = new String(masterkey);

        int length = key.length();
        if (length > 16 && length != 16) {
            key = key.substring(0, 15);
        }
        if (length < 16 && length != 16) {
            for (int i = 0; i < 16 - length; i++) {
                key = key + "0";
            }
        }

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        return keySpec;
    }

    public void writeProtectedKeychain(String allChain, File target) throws IOException {

        InputStream fis;
        FileOutputStream fos;
        CipherInputStream cis;

        SecretKeySpec secretKeySpec = buildSecretKeySpec();

        //Creation of Cipher objects
        Cipher encrypt;
        try {
            encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // Open the Plaintext file
            fis = new ByteArrayInputStream(allChain.getBytes("UTF-8"));
            cis = new CipherInputStream(fis, encrypt);
            // Write to the Encrypted file
            fos = new FileOutputStream(target);
            byte[] b = new byte[8];
            int i = cis.read(b);
            while (i != -1) {
                fos.write(b, 0, i);
                i = cis.read(b);
            }

            fos.close();
            cis.close();
            fis.close();

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(CipherTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readProtectedKeychain(KeyChain chain, File target) throws IOException {
        InputStream fis;
        ByteArrayOutputStream fos;
        CipherInputStream cis;
        String allEntries;

        SecretKeySpec keySpec = buildSecretKeySpec();

        //Creation of Cipher objects
        Cipher decrypt;
        try {
            decrypt = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            decrypt.init(Cipher.DECRYPT_MODE, keySpec);
            // Open the Encrypted file
            fis = new FileInputStream(target);
            cis = new CipherInputStream(fis, decrypt);
            // Write to the Decrypted file
            fos = new ByteArrayOutputStream();
            byte[] b = new byte[8];
            int i = cis.read(b);
            while (i != -1) {
                fos.write(b, 0, i);
                i = cis.read(b);
            }

            allEntries = new String(fos.toByteArray(), "UTF-8");

            Scanner stringReader = new Scanner(allEntries);
            while (stringReader.hasNext()) {
                chain.put(KeyEntry.parse(stringReader.nextLine()));

            }

            fos.close();
            cis.close();
            fis.close();

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(CipherTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            throw new IOException("Failed to read keychain. Bad master key?", ex);
        }
    }

}
