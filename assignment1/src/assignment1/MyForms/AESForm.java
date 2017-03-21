package assignment1.MyForms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
//
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;

public class AESForm extends JFrame {

	private JPanel contentPane;
	private JTextField keyTextField;
	private JTextField pathTextField;
	private JTextField pathOutTextField;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { AESForm frame = new AESForm();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 * }); }
	 */

	/**
	 * Create the frame.
	 */
	public AESForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblKey = new JLabel("Key");
		lblKey.setBounds(40, 41, 91, 29);
		contentPane.add(lblKey);
		
		JRadioButton radioButton128 = new JRadioButton("128");
		radioButton128.setBounds(149, 44, 54, 23);
		contentPane.add(radioButton128);
		
		JRadioButton radioButton196 = new JRadioButton("196");
		radioButton196.setBounds(207, 44, 63, 23);
		contentPane.add(radioButton196);
		
		JRadioButton radioButton256 = new JRadioButton("256");
		radioButton256.setBounds(274, 44, 54, 23);
		contentPane.add(radioButton256);
		
		keyTextField = new JTextField();
		keyTextField.setEnabled(false);
		keyTextField.setBackground(Color.DARK_GRAY);
		keyTextField.setEditable(false);
		keyTextField.setBounds(149, 46, 209, 19);
		contentPane.add(keyTextField);
		keyTextField.setColumns(10);
		
		JButton keyPathBtn = new JButton("...");
		keyPathBtn.setEnabled(false);
		keyPathBtn.setBounds(362, 43, 38, 25);
		contentPane.add(keyPathBtn);
		
		JLabel lblPathInput = new JLabel("Input file");
		lblPathInput.setBounds(40, 82, 70, 15);
		contentPane.add(lblPathInput);
		
		pathTextField = new JTextField();
		pathTextField.setEditable(false);
		pathTextField.setBounds(149, 80, 114, 19);
		contentPane.add(pathTextField);
		pathTextField.setColumns(10);
		
		JButton pathPathBtn = new JButton("...");
		pathPathBtn.setBounds(362, 77, 38, 25);
		contentPane.add(pathPathBtn);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(40, 118, 70, 15);
		contentPane.add(lblType);
		
		JRadioButton rdbtnEncrypt = new JRadioButton("encrypt");
		rdbtnEncrypt.setSelected(true);
		rdbtnEncrypt.setBounds(149, 114, 91, 23);
		contentPane.add(rdbtnEncrypt);
		
		JRadioButton rdbtnDecrypt = new JRadioButton("decrypt");
		rdbtnDecrypt.setBounds(262, 114, 149, 23);
		contentPane.add(rdbtnDecrypt);
		
		JLabel lblPathOut = new JLabel("Output file");
		lblPathOut.setBounds(40, 154, 91, 15);
		contentPane.add(lblPathOut);
		
		pathOutTextField = new JTextField();
		pathOutTextField.setEditable(false);
		pathOutTextField.setBounds(149, 152, 114, 19);
		contentPane.add(pathOutTextField);
		pathOutTextField.setColumns(10);
		
		JButton pathOutPathBtn = new JButton("...");
		pathOutPathBtn.setBounds(362, 149, 38, 25);
		contentPane.add(pathOutPathBtn);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(290, 230, 117, 25);
		contentPane.add(btnExecute);
		
		JButton button = new JButton("");
		button.setBounds(169, 230, 117, 25);
		contentPane.add(button);
	}

	//
	public static class AES {
		private static String algorithm = "AES";
		private static byte[] keyValue = new byte[] { 'A', 'S', 'e', 'c', 'u', 'r', 'e', 'S', 'e', 'c', 'r', 'e', 't',
				'K', 'e', 'y' };

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
		
	}
	public void main(String[] args) throws Exception {

		String plainText = "testing algorithm for nothing dude";
		String encryptedText = AES.encrypt(plainText);
		String decryptedText = AES.decrypt(encryptedText);

		System.out.println("Plain Text : " + plainText);
		System.out.println("Encrypted Text : " + encryptedText);
		System.out.println("Decrypted Text : " + decryptedText);
	}
}
