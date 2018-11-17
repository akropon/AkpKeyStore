package com.akropon.akpkeystore;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

public class CryptographyUtils {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String CIPHER_ALGORITHM = "AES";
    private static final int CIPHER_KEY_WIDTH_BYTES = 16; // 128 bits


    static byte[] getHash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] stringAsBytes = string.getBytes();
        digest.update(stringAsBytes);
        return digest.digest();
    }

    static byte[] encode(String openText, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] notNormalizedKey = password.getBytes();
        byte[] key128 = normalizeKeyWidth(notNormalizedKey, CIPHER_KEY_WIDTH_BYTES);

        // Create key and cipher
        Key aesKey = new SecretKeySpec(key128, CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(openText.getBytes());
    }

    static String decode(byte[] encodedText, String password) throws GeneralSecurityException {

        byte[] notNormalizedKey = password.getBytes();
        byte[] key128 = normalizeKeyWidth(notNormalizedKey, CIPHER_KEY_WIDTH_BYTES);

        // Create key and cipher
        Key aesKey = new SecretKeySpec(key128, CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(encodedText));
    }

    private static byte[] normalizeKeyWidth(byte[] notNormalizedKey, int keyWidth) throws InvalidKeyException {
        if (notNormalizedKey == null) {
            throw new InvalidKeyException("key parameter is null");
        } else if (notNormalizedKey.length == 0) {
            throw new InvalidKeyException("key parameter has zero length");
        } else if (keyWidth < 1) {
            throw new InvalidKeyException("normalized key width parameter is invalid. it's value is "+keyWidth);
        }

        if (notNormalizedKey.length == keyWidth) {
            return notNormalizedKey;
        } else if (notNormalizedKey.length > keyWidth) {
            return Arrays.copyOf(notNormalizedKey, keyWidth);
        } else {
            byte[] normalizedKey = new byte[keyWidth];
            for (int i = 0; i < keyWidth; i++) {
                normalizedKey[i] = notNormalizedKey[i % notNormalizedKey.length];
            }
            return normalizedKey;
        }
    }

    private static String bytesToHex(byte[] _bytesArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte _byte : _bytesArray)
            stringBuilder.append(String.format("%02X ", _byte));
        return stringBuilder.toString();
    }

}
