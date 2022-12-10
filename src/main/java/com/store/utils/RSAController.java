package com.store.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import javax.crypto.Cipher;

import jakarta.xml.bind.DatatypeConverter;

public class RSAController {
	private static final long serialVersionUID = 1L;

    protected static Map<Object, Object> configuracionServicioReferencia;
    public static final String CONFING_NAME = "config";
    public static final String PATH_CONFIG_RSA = "CTS_MF/plugins/fna/felp/config/certs/config.properties";
    public static final String CLASS_NAME = "[RSAController]";
    public static final String CONFIG_KEY_PRIVATE = "private_key";
    public enum ModeEnum {
        PKCS1,
        OAEP
    }

    public enum DataTypeEnum {
        HEX,
        BASE64
    }
 
    private DataTypeEnum dataType = DataTypeEnum.BASE64;
    private ModeEnum mode = ModeEnum.PKCS1;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String key;

    public String getKey() {
        key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALPXOpAe0e8pzMeHnx4+JE2A7KIcTHqVkM+W/AFNoeaQ2VDLInTpIiUxTWmYYrEguNPufTqfEbsuUVVQ3kyRb+PAXf3/52uQczkHSSH6E9DKAWNGrzXLG8vCiW35WruH4JNTRnXCQUAFxN5BCV1mQmVOnLHRzf6OrSjjvAgWOrl/AgMBAAECgYAgA0YHdZUFL7mmIvwuE/2+Vh7JVKRAhfM7ILNHQBx7wHkOqro9eWp8mGQhUeDvitWb1C4yizJK0Znkx/pqQtFZuoatUsggocjXFl86FElQwrBp08DvfKfd0bGgy0VTFQVmCtxiqhpAmC7xmXNZXfBD41rl9CKbFfZw05QC5BoQ0QJBAO7LSku97NgFBJQ+vbmVDonuvgnQjVNb7SnwrcpJHEUAGbaVq1a50jz+s6n39TOagASaW6pcY0uwiygYu6xDnkMCQQDAzIGNKFKomTI6djcOyHfQ1ZXqyDQ3guX6nHhzZnNHFF8ZD3fPyyIRSZ3JvPK5iEzJLhB7FRtyWkGcdXgJTWoVAkBfx9zKGqkYUJLwn2XcPWRygPdq2mMFb5bmPqqGu+KB7rNhoBD0nV4tpwALifCpPSxiLEPeRmZxoqN+dsU4KHsfAkAyQt4fK3zpAQ8MGJdf3jkGEzhC/bBHLHPB8pqgEvxIcnIcOWEVpbIa6aMd3Yk1fuftpnmbbLQ8CnWCUUlau3jFAkEAk6bOZIWhTYRwIZcwBdkpyLlbatQFoTTM3i444YutXt3FrFfaWBxge+eYKId+J4dCrt/EmHhSfWKEzHibf6N5Sg==";
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
  
    public static String getBase64PublicKey(PublicKey publicKey) {
        return toBase64(publicKey.getEncoded());
    }

    public static String getBase64PrivateKey(PrivateKey privateKey) {
        return toBase64(privateKey.getEncoded());
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(fromBase64(base64PublicKey));
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {

        }
        return null;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(fromBase64(base64PrivateKey));
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            
        }
        return null;
    }

    public byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = getCipher();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes());
    }

    public byte[] decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherText);
    } 

    public String encrypt(String plainText, String base64PublicKey) throws Exception {
        byte[] cipherText = encrypt(plainText, getPublicKey(base64PublicKey));
        if (DataTypeEnum.BASE64.equals(dataType)) {
            return toBase64(cipherText);
        } else {
            return toHex(cipherText);
        }
    }

    public String decrypt(String cipherText, String base64PrivateKey) throws Exception {
        byte[] cipherBytes;
        if (DataTypeEnum.BASE64.equals(dataType)) {
            cipherBytes = fromBase64(cipherText);
        } else {
            cipherBytes = fromHex(cipherText);
        }
        return new String(decrypt(cipherBytes, getPrivateKey(base64PrivateKey)));
    }

    public String encrypt(String plainText) throws Exception {
        return encrypt(plainText, getBase64PublicKey(publicKey));
    }

    public String decrypt(String cipherText) throws Exception {
        return decrypt(cipherText, getBase64PrivateKey(privateKey));
    }

 
    private Cipher getCipher() throws Exception {
        if (ModeEnum.OAEP.equals(mode)) {
            return Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        } else {
            return Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }
    }

    private static byte[] fromBase64(String str) {
    	return DatatypeConverter.parseBase64Binary(str);
    }

    private static String toBase64(byte[] ba) {
        return DatatypeConverter.printBase64Binary(ba);
    }

 

    private static byte[] fromHex(String str) {
        return DatatypeConverter.parseHexBinary(str);
    }
 
    private static String toHex(byte[] ba) {
        return DatatypeConverter.printHexBinary(ba);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

     public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

     public ModeEnum getMode() {
        return mode;
    }

     public void setMode(ModeEnum mode) {
        this.mode = mode;
    }

     public DataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;

    }
}
