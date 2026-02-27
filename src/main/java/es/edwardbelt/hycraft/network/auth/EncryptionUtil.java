package es.edwardbelt.hycraft.network.auth;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;

public class EncryptionUtil {
    private static final KeyPair SERVER_KEY_PAIR = generateRSAKeyPair();

    private static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    public static PublicKey getPublicKey() {
        return SERVER_KEY_PAIR.getPublic();
    }

    public static PrivateKey getPrivateKey() {
        return SERVER_KEY_PAIR.getPrivate();
    }

    public static byte[] getEncodedPublicKey() {
        return SERVER_KEY_PAIR.getPublic().getEncoded();
    }

    public static byte[] generateVerifyToken() {
        byte[] token = new byte[4];
        new SecureRandom().nextBytes(token);
        return token;
    }

    public static byte[] rsaDecrypt(byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        return cipher.doFinal(data);
    }

    public static Cipher createAESCipher(int mode, byte[] sharedSecret) throws GeneralSecurityException {
        SecretKey key = new SecretKeySpec(sharedSecret, "AES");
        IvParameterSpec iv = new IvParameterSpec(sharedSecret);
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(mode, key, iv);
        return cipher;
    }

    public static String computeServerHash(byte[] sharedSecret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update("".getBytes("ISO_8859_1"));
            digest.update(sharedSecret);
            digest.update(getEncodedPublicKey());
            return new BigInteger(digest.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute server hash", e);
        }
    }
}