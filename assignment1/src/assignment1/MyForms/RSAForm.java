package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RSAForm extends JFrame {

	private JPanel contentPane;
	private JLabel lblKey;
	private JLabel lblPrivateKeyFile;
	private JTextField keyTextField;
	private JTextField pathTextField;
	private JTextField privateKeyTextField;
	private JButton btnPrivateKey;
	private JProgressBar progressBar;
	private ButtonGroup groupButton;

	private BigInteger p;
	private BigInteger q;
	private BigInteger N;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d;
	private int bitlength = 1024;
	private Random r;
	private String plainFileName;
	private String plainFilePath;

	/**
	 * Create the frame.
	 */
	public RSAForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnReturnMainForm = new JButton("Go back");
		btnReturnMainForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainForm().setVisible(true);
				dispose();
			}
		});
		btnReturnMainForm.setBounds(241, 226, 117, 29);
		contentPane.add(btnReturnMainForm);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(358, 226, 74, 29);
		contentPane.add(btnExit);

		lblKey = new JLabel("Key length");
		lblKey.setBounds(30, 21, 87, 16);
		contentPane.add(lblKey);

		keyTextField = new JTextField();
		keyTextField.setText("2048");
		keyTextField.setBounds(140, 16, 148, 26);
		contentPane.add(keyTextField);
		keyTextField.setColumns(10);
		keyTextField.setEditable(true);
		keyTextField.setBackground(Color.WHITE);

		JLabel lblNewLabel = new JLabel("Path");
		lblNewLabel.setBounds(30, 49, 61, 16);
		contentPane.add(lblNewLabel);

		pathTextField = new JTextField();
		pathTextField.setEditable(false);
		pathTextField.setBounds(140, 44, 148, 26);
		contentPane.add(pathTextField);
		pathTextField.setColumns(10);

		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// RSAForm1 rsa = new RSAForm1();
				try {

					Path path = Paths.get(pathTextField.getText());
					byte[] data = Files.readAllBytes(path);

					/*
					 * Encrypt here ./outputFolderName/<file_execute>
					 */
					if (groupButton.getSelection().getActionCommand().equals("Encrypt")) {
						int keyLength = Integer.parseInt(keyTextField.getText());
						if (checkFileSize(pathTextField.getText(), keyLength)) {
							JOptionPane.showMessageDialog(contentPane, "File 's size must not bigger than key length");
						} else {
							//DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
							//Date date = new Date();
							//String outputFolderName = dateFormat.format(date);
							String outputFolderName = "RSA_transformKey";
							File outputFolder;
							if (System.getProperties().getProperty("os.name").contains("Windows")) {
								outputFolder = new File(plainFilePath.substring(0, plainFilePath.lastIndexOf("\\"))
										+ "\\" + outputFolderName);
								outputFolder.mkdir();
								plainFilePath = plainFilePath.substring(0, plainFilePath.lastIndexOf("\\")) + "\\"
										+ outputFolderName + "\\";
							} else {
								outputFolder = new File(plainFilePath.substring(0, plainFilePath.lastIndexOf("/")) + "/"
										+ outputFolderName);
								outputFolder.mkdir();
								plainFilePath = plainFilePath.substring(0, plainFilePath.lastIndexOf("/")) + "/"
										+ outputFolderName + "/";
							}

							generateKey(keyLength);
							byte[] encrypted = encrypt(data);
							String pathEncryptedFile = plainFilePath + plainFileName + "_Encrypt";
							// write to file
							Path pathOfEncryptedFile = Paths.get(pathEncryptedFile);
							Files.write(pathOfEncryptedFile, encrypted);
						}
					}
					/*
					 * Decrypt here ./<file_execute>
					 */
					else if (groupButton.getSelection().getActionCommand().equals("Decrypt")) {
						String pathDecryptedFile = plainFilePath + "_Decrypt";
						String pathPrivateKeyFile = privateKeyTextField.getText();
						if (checkFileSize(pathTextField.getText(), privateKeyTextField.getText())) {
							JOptionPane.showMessageDialog(contentPane, "File and key are not matched");
						} else {
							ObjectInputStream in = new ObjectInputStream(new FileInputStream(pathPrivateKeyFile));
							@SuppressWarnings("unchecked")
							List<byte[]> pkList = (List<byte[]>) in.readObject();
							N = new BigInteger(pkList.get(0));
							d = new BigInteger(pkList.get(1));
							in.close();
							/*
							 * File privateKeyFile = new
							 * File(pathPrivateKeyFile); BufferedReader br = new
							 * BufferedReader(new FileReader(privateKeyFile));
							 * String nString = br.readLine(); br.readLine();
							 * String dString = br.readLine(); // convert byte[]
							 * to big integer N = new
							 * BigInteger(Base64.getDecoder().decode(nString));
							 * d = new
							 * BigInteger(Base64.getDecoder().decode(dString));
							 * br.close();
							 */
							Path pathOfDecryptedFile = Paths.get(pathDecryptedFile);
							byte[] decrypted = decrypt(data);
							Files.write(pathOfDecryptedFile, decrypted);
						}
					}

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(contentPane, "Please provide a valid key length");
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnExecute.setBounds(30, 226, 117, 29);
		contentPane.add(btnExecute);

		JRadioButton rdbtnEncrypt = new JRadioButton("Encrypt");
		rdbtnEncrypt.setSelected(true);
		rdbtnEncrypt.setActionCommand("Encrypt");
		rdbtnEncrypt.setBounds(36, 116, 141, 23);
		contentPane.add(rdbtnEncrypt);
		rdbtnEncrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lblPrivateKeyFile.setVisible(false);
				privateKeyTextField.setVisible(false);
				btnPrivateKey.setVisible(false);
				keyTextField.setEditable(true);
				keyTextField.setBackground(Color.WHITE);
			}
		});

		JRadioButton rdbtnDecrypt = new JRadioButton("Decrypt");
		rdbtnDecrypt.setActionCommand("Decrypt");
		rdbtnDecrypt.setBounds(36, 151, 141, 23);
		contentPane.add(rdbtnDecrypt);
		rdbtnDecrypt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lblPrivateKeyFile.setVisible(true);
				privateKeyTextField.setVisible(true);
				btnPrivateKey.setVisible(true);
				keyTextField.setEditable(false);
				keyTextField.setBackground(Color.LIGHT_GRAY);
			}
		});

		groupButton = new ButtonGroup();
		groupButton.add(rdbtnEncrypt);
		groupButton.add(rdbtnDecrypt);

		JButton btnFile = new JButton("Select file");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					pathTextField.setText(file.getAbsolutePath());
					plainFileName = file.getName();
					plainFilePath = pathTextField.getText();
					if (plainFileName.contains("."))
						plainFileName = plainFileName.substring(0, plainFileName.lastIndexOf('.'));
				}
			}
		});
		btnFile.setBounds(189, 163, 117, 29);
		contentPane.add(btnFile);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(52, 188, 376, 26);
		contentPane.add(progressBar);

		lblPrivateKeyFile = new JLabel("Private key file");
		lblPrivateKeyFile.setBounds(30, 77, 106, 16);
		lblPrivateKeyFile.setVisible(false);
		contentPane.add(lblPrivateKeyFile);

		privateKeyTextField = new JTextField();
		privateKeyTextField.setEditable(false);
		privateKeyTextField.setBounds(140, 72, 148, 26);
		contentPane.add(privateKeyTextField);
		privateKeyTextField.setColumns(10);
		privateKeyTextField.setVisible(false);

		btnPrivateKey = new JButton("...");
		btnPrivateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file.getName().equals("PrivateKey.txt")) {
						privateKeyTextField.setText(file.getAbsolutePath());
					} else
						JOptionPane.showMessageDialog(contentPane, "Please select a file named \"PrivateKey.txt\".");
				}
			}
		});
		btnPrivateKey.setBounds(290, 72, 42, 29);
		btnPrivateKey.setVisible(false);
		contentPane.add(btnPrivateKey);
	}

	private void generateKey(int bitlen) throws IOException {
		r = new Random();
		bitlength = bitlen;
		p = BigInteger.probablePrime(bitlength / 2, r);
		q = BigInteger.probablePrime(bitlength / 2, r);
		N = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		e = BigInteger.probablePrime(bitlength / 2, r);
		//
		while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
			e.add(BigInteger.ONE);
		}
		// d= e^-1 mod phi
		d = e.modInverse(phi);
		// public key
		createKeyFile(N, e, "PublicKey.txt");
		// private key
		createKeyFile(N, d, "PrivateKey.txt");
	}

	public void createKeyFile(BigInteger n, BigInteger eod, String fileName) throws IOException {
		// String firstResult =
		// Base64.getEncoder().encodeToString(n.toByteArray());
		// String secondResult =
		// Base64.getEncoder().encodeToString(eod.toByteArray());
		String osName = System.getProperties().getProperty("os.name");
		String pathInfo;
		if (osName.contains("Windows")) {
			pathInfo = plainFilePath.substring(0, plainFilePath.lastIndexOf("\\")) + "\\" + fileName;
		} else
			pathInfo = plainFilePath.substring(0, plainFilePath.lastIndexOf("/")) + "/" + fileName;
		List<byte[]> list = new ArrayList<>();
		list.add(n.toByteArray());
		list.add(eod.toByteArray());
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pathInfo));
		out.writeObject(list);
		out.close();
		/*
		 * File info = new File(pathInfo); info.createNewFile(); FileWriter
		 * fileWriter = new FileWriter(new File(pathInfo)); BufferedWriter bw =
		 * new BufferedWriter(fileWriter); bw.write(firstResult); bw.newLine();
		 * bw.write("*******"); bw.newLine();
		 * if(fileName.equals("PublicKey.txt")) bw.write(secondResult); else
		 * bw.write(secondResult); bw.newLine(); bw.close();
		 */
	}

	// private static String bytesToString(byte[] encrypted)
	// {
	// String test = "";
	// for (byte b : encrypted)
	// {
	// test += Byte.toString(b);
	// }
	// return test;
	// }
	private byte[] encrypt(byte[] message) {
		return (new BigInteger(message)).modPow(e, N).toByteArray();
	}

	/*
	 * Decrypt message
	 */
	private byte[] decrypt(byte[] message) {
		return (new BigInteger(message)).modPow(d, N).toByteArray();
	}

	private boolean checkFileSize(String path, int key_length) {
		File f = new File(path);
		long fsize = f.length();
		if (fsize > key_length / 8) {
			return true;
		} else
			return false;
	}

	private boolean checkFileSize(String pathFile, String pathKey) {
		File f = new File(pathFile);
		long fsize = f.length();
		File fk = new File(pathKey);
		long fksize = fk.length();
		long dif = fksize - fsize * 2;
		if (dif == 92)
			return false;
		else
			return true;
	}
}