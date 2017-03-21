package assignment1.MyForms;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import sun.misc.*;
public class AES {
	private static String algorithm = "AES";
	private static byte[] keyValue = new byte[] { 'A', 'S', 'e', 'c', 'u', 'r', 'e', 'S', 'e', 'c', 'r', 'e', 't','K', 'e', 'y',
			'A', 'S', 'e', 'c', 'u', 'r', 'e', 'S', 'e', 'c', 'r', 'e', 't','K', 'e', 'y'};

	// Performs Encryption
	public static String encrypt(String plainText) throws Exception {
		Key key = generateKey();
		Cipher chiper = Cipher.getInstance(algorithm);
		chiper.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = chiper.doFinal(plainText.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	// Performs decryption
	public static String decrypt(String encryptedText) throws Exception {
		// generate key
		Key key = generateKey();
		Cipher chiper = Cipher.getInstance(algorithm);
		chiper.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
		byte[] decValue = chiper.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	// generateKey() is used to generate a secret key for AES algorithm
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, algorithm);
		return key;
		
	}

	// performs encryption & decryption
	public static void main(String[] args) throws Exception {

		String plainText = "testing algorithm for nothing dude";
		String encryptedText = AES.encrypt(plainText);
		String decryptedText = AES.decrypt(encryptedText);

		System.out.println("Plain Text : " + plainText);
		System.out.println("Encrypted Text : " + encryptedText);
		System.out.println("Decrypted Text : " + decryptedText);
	}
	/*public static void main(String[] args) throws Exception {
	    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	    KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
	    generator.init(128);

	    Key keyToBeWrapped = generator.generateKey();

	    System.out.println("input    : " + new String(keyToBeWrapped.getEncoded()));
	    Cipher cipher = Cipher.getInstance("AESWrap", "BC");

	    KeyGenerator KeyGen = KeyGenerator.getInstance("AES", "BC");
	    KeyGen.init(256);

	    Key wrapKey = KeyGen.generateKey();
	    cipher.init(Cipher.WRAP_MODE, wrapKey);
	    byte[] wrappedKey = cipher.wrap(keyToBeWrapped);

	    System.out.println("wrapped : " + new String(wrappedKey));

	    cipher.init(Cipher.UNWRAP_MODE, wrapKey);
	    Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
	    System.out.println("unwrapped: " + new String(key.getEncoded()));
	  }*/
}

