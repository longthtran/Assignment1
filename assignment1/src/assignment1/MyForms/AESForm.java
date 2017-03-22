package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AESForm extends JFrame {

	private JPanel contentPane;
	private ButtonGroup buttonGroup;
	private JTextField pathFileTextField;
	private JComboBox<Integer> comboBoxKeyLength;
	private JComboBox<String> comboBoxMode;
	private String plainFileName;
	private boolean isSelectedFolder = false;
	private JTextField keyPathTextField;
	private JLabel lblKeyPath;
	private JLabel lblKeyLength;
	private JLabel lblNote;
	private JLabel lblNoteName;
	private JButton btnKeyPath;
	/**
	 * Create the frame.
	 */
	public AESForm() {
		setTitle("AES Encrypting");
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
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pathFileTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Please provide the path of a file!");
				}
				/*
				 * Encrypt here
				 */
				else if(buttonGroup.getSelection().getActionCommand().equals("Encrypt")) {
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = new Date();
					String outputFolderName = dateFormat.format(date);
					KeyGenerator keyGenerator;
					
					
					
					//Prepare some file path
					String keyPath;
					String infoPath;
					try {
						String outputFolderPath;
						// outputFolderPath: ./<DatetimeFolder>
						// keyPath: ./<DatetimeFolder>/key.txt
						// infoPath: ./<DatetimeFolder>/info.txt
						// encryptFilePath: ./<DatetimeFolder>/<filename>_encrypt
						if(System.getProperties().getProperty("os.name").contains("Windows")) {
							if(isSelectedFolder) 
								outputFolderPath = pathFileTextField.getText() + "\\" + outputFolderName;
							else 
								outputFolderPath = pathFileTextField.getText().substring(0, pathFileTextField.getText().lastIndexOf("\\")) + "\\" + outputFolderName;
							keyPath = outputFolderPath + "\\key.txt";
							infoPath = outputFolderPath + "\\info.txt";
						}
						else {
							if(isSelectedFolder) 
								outputFolderPath = pathFileTextField.getText() + "/" + outputFolderName;
							else
								outputFolderPath = pathFileTextField.getText().substring(0, pathFileTextField.getText().lastIndexOf("/")) + "/" + outputFolderName;
							keyPath = outputFolderPath + "/key.txt";
							infoPath = outputFolderPath + "/info.txt";
						}
						File folder = new File(outputFolderPath);
						
						keyGenerator = KeyGenerator.getInstance("AES");
						int keySize = (Integer)comboBoxKeyLength.getSelectedItem();
						keyGenerator.init(keySize);
						SecretKey secretKey = keyGenerator.generateKey();
						
						if(!folder.exists()) {
							boolean createFolder = folder.mkdir();
							if(!createFolder) System.out.println("Create folder failed!");
						}
						// start encrypt
						if (folder.isDirectory()) {
							// Write file key here
							// Only do this after creating folder
							
							
							String encryptFilePath;
							String mode = (String) comboBoxMode.getSelectedItem();
							Cipher cipher = Cipher.getInstance("AES/" + mode + "/PKCS5Padding");
							cipher.init(Cipher.ENCRYPT_MODE, secretKey);
							
							
							
							byte[] keyBytes = secretKey.getEncoded();
							List<byte[]> keyAndParams = new ArrayList<>();
							keyAndParams.add(keyBytes);
							if(mode.equals("CBC")) {
								byte[] params = cipher.getParameters().getEncoded();
								keyAndParams.add(params);
							}
							ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyPath));
							out.writeObject(keyAndParams);
							out.flush();
							out.close();
							
							//execute 1 file
							if(!isSelectedFolder) {
								if(System.getProperties().getProperty("os.name").contains("Windows")) {
									encryptFilePath = outputFolderPath + "\\" + plainFileName + "_encrypt";								
								}
								else {
									encryptFilePath = outputFolderPath + "/" + plainFileName + "_encrypt";
								}
								
								//Write encrypted file here
								byte[] cipherBytes = encrypt(pathFileTextField.getText(), secretKey, cipher);
								FileOutputStream fos = new FileOutputStream(encryptFilePath);
								fos.write(cipherBytes);
								fos.flush();
								fos.close();
								
								//Write file info here
								BufferedWriter bw = new BufferedWriter(new FileWriter(new File(infoPath)));
								bw.write("Plain file path: " + pathFileTextField.getText());
								bw.newLine();
								bw.write("Cipher file name: " + plainFileName + "_encrypt");
								bw.newLine();
								bw.write("Mode: " + comboBoxMode.getSelectedItem());
								bw.newLine();
								bw.write("Key file path: " + keyPath);
								bw.close();
								JOptionPane.showMessageDialog(contentPane, "Execution completed :D");
							}							
							//execute many files
							else {
								File selectedFolder = new File(pathFileTextField.getText());
								if(selectedFolder.isDirectory()) {
									File[] listOfFiles = selectedFolder.listFiles();
									BufferedWriter bw = new BufferedWriter(new FileWriter(new File(infoPath)));
									for(File file: listOfFiles) {
										String plainFilePath = file.getAbsolutePath();
										plainFileName = file.getName();
										if(plainFileName.charAt(0) == '.' || file.isDirectory()) 
											//avoid system file. ex: .DS_Store
											continue;
										plainFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
										 
										if(System.getProperties().getProperty("os.name").contains("Windows")) {
											encryptFilePath = outputFolderPath + "\\" + plainFileName + "_encrypt";
										}
										else {
											encryptFilePath = outputFolderPath + "/" + plainFileName + "_encrypt";
										}
										//Write encrypted file here
										byte[] cipherBytes = encrypt(plainFilePath, secretKey, cipher);
										FileOutputStream fos = new FileOutputStream(encryptFilePath);
										fos.write(cipherBytes);
										fos.flush();
										fos.close();
										
										bw.write("Plain file path: " + plainFilePath);
										bw.newLine();
										bw.write("Cipher file name:" + plainFileName + "_encrypt");
										bw.newLine();
										bw.newLine();
									}
									bw.write("Mode: " + comboBoxMode.getSelectedItem());
									bw.newLine();
									bw.write("Key file path: " + keyPath);
									bw.close();
									JOptionPane.showMessageDialog(contentPane, "Execution completed :D");
								}
								else {
									JOptionPane.showMessageDialog(contentPane, "You did not choose a folder!");
								}
							}
						}
					} catch (IOException e1) {
						
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidKeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalBlockSizeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
				/*
				 * Decrypt here
				 * 
				 */
				else {
					if(keyPathTextField.getText().isEmpty()) 
						JOptionPane.showMessageDialog(contentPane, "Please provide a file named \"key.txt\"");
					else {
						String decryptedFilePath;
						try {
							Cipher cipher = Cipher.getInstance("AES/" + comboBoxMode.getSelectedItem() + "/PKCS5Padding");
							String mode = (String) comboBoxMode.getSelectedItem();
							//cipher.init(Cipher.DECRYPT_MODE, key, params);
							//execute 1 file
							if(!isSelectedFolder) {
								if(System.getProperties().getProperty("os.name").contains("Windows"))
									decryptedFilePath = pathFileTextField.getText().substring(0, pathFileTextField.getText().lastIndexOf("\\")) + "\\" + plainFileName + "_decrypt";
								else decryptedFilePath = pathFileTextField.getText().substring(0, pathFileTextField.getText().lastIndexOf("/")) + "/" + plainFileName + "_decrypt";
								
								//Read key and params here
								ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyPathTextField.getText()));
								@SuppressWarnings("unchecked")
								List<byte[]> keyAndParams = (List<byte[]>)in.readObject();
								byte[] keyBytes = keyAndParams.get(0);
								
								
								SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
								AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
								if(mode.equals("CBC")) {
									byte[] paramsBytes = keyAndParams.get(1);
									params.init(paramsBytes);		
									cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
								}
								else cipher.init(Cipher.DECRYPT_MODE, secretKey);
								in.close();
								
								byte[] decryptedBytes = decrypt(pathFileTextField.getText(), cipher);
								FileOutputStream fos = new FileOutputStream(decryptedFilePath);
								fos.write(decryptedBytes);
								fos.flush();
								fos.close();
							}
							//execute many files
							else {
								File selectedFolder = new File(pathFileTextField.getText());
								if(selectedFolder.isDirectory()) {
									File[] listOfFiles = selectedFolder.listFiles();
									
									//Read key and params here
									ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyPathTextField.getText()));
									@SuppressWarnings("unchecked")
									List<byte[]> keyAndParams = (List<byte[]>)in.readObject();
									byte[] keyBytes = keyAndParams.get(0);
													
									SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
									AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
									if(mode.equals("CBC")) {
										byte[] paramsBytes = keyAndParams.get(1);
										params.init(paramsBytes);		
										cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
									}
									else cipher.init(Cipher.DECRYPT_MODE, secretKey);
									in.close();
									JOptionPane.showMessageDialog(contentPane, "You decrypt all files named ends with _\"encrypt\" inside the selected folder.");
									for(File file: listOfFiles) {
										//avoid system file, ex: .DS_store
										if(file.getName().charAt(0) == '.' || file.isDirectory() || !file.getName().endsWith("_encrypt"))
											continue;
										decryptedFilePath = file.getAbsolutePath() + "_decrypt";

										byte[] decryptedBytes = decrypt(file.getAbsolutePath(), cipher);
										FileOutputStream fos = new FileOutputStream(decryptedFilePath);
										fos.write(decryptedBytes);
										fos.flush();
										fos.close();

									}
								}							
							}
							JOptionPane.showMessageDialog(contentPane, "Execution completed");
						} catch (IOException e1) {

						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchPaddingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvalidKeyException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvalidAlgorithmParameterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalBlockSizeException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (BadPaddingException e1) {
							JOptionPane.showMessageDialog(contentPane, "You have selected wrong key file or mode!");
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnExecute.setBounds(43, 218, 171, 45);
		contentPane.add(btnExecute);
		
		comboBoxKeyLength = new JComboBox<Integer>();
		comboBoxKeyLength.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {128}));
		comboBoxKeyLength.setBounds(133, 68, 81, 27);
		contentPane.add(comboBoxKeyLength);
		
		JRadioButton rdbtnEncrypt = new JRadioButton("Encrypt");
		rdbtnEncrypt.setSelected(true);
		rdbtnEncrypt.setBounds(43, 124, 141, 23);
		rdbtnEncrypt.setActionCommand("Encrypt");
		contentPane.add(rdbtnEncrypt);
		rdbtnEncrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("AES Encrypting");
				lblKeyPath.setVisible(false);
				lblKeyLength.setVisible(true);
				keyPathTextField.setVisible(false);
				comboBoxKeyLength.setVisible(true);
				btnKeyPath.setVisible(false);
			}
		});
		
		JRadioButton rdbtnDecrypt = new JRadioButton("Decrypt");
		rdbtnDecrypt.setBounds(43, 159, 141, 23);
		rdbtnDecrypt.setActionCommand("Decrypt");
		contentPane.add(rdbtnDecrypt);
		rdbtnDecrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("AES Decrypting");
				lblKeyPath.setVisible(true);
				lblKeyLength.setVisible(false);
				keyPathTextField.setVisible(true);
				comboBoxKeyLength.setVisible(false);
				btnKeyPath.setVisible(true);
			}
		});
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnEncrypt);
		buttonGroup.add(rdbtnDecrypt);
		
		JLabel lblNewLabel = new JLabel("File path");
		lblNewLabel.setBounds(43, 18, 61, 16);
		contentPane.add(lblNewLabel);
		
		pathFileTextField = new JTextField();
		pathFileTextField.setEditable(false);
		pathFileTextField.setBounds(133, 13, 242, 26);
		contentPane.add(pathFileTextField);
		pathFileTextField.setColumns(10);
		
		JButton btnSelectFile = new JButton("Select file");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNoteName.setVisible(true);
				lblNote.setVisible(false);
				isSelectedFolder = false;
				JFileChooser fileChooser = new JFileChooser();
				if(fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					pathFileTextField.setText(file.getAbsolutePath());
					plainFileName = file.getName();
					//get the name without extension
					if(plainFileName.contains(".")) plainFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); 
					
				}
			}
		});
		btnSelectFile.setBounds(211, 158, 105, 29);
		contentPane.add(btnSelectFile);
		
		lblKeyLength = new JLabel("Key length");
		lblKeyLength.setBounds(43, 72, 74, 16);
		contentPane.add(lblKeyLength);
		
		JLabel lblNewLabel_2 = new JLabel("Mode");
		lblNewLabel_2.setBounds(43, 46, 61, 16);
		contentPane.add(lblNewLabel_2);
		
		comboBoxMode = new JComboBox<String>();
		comboBoxMode.setModel(new DefaultComboBoxModel<String>(new String[] {"ECB", "CBC"}));
		comboBoxMode.setBounds(133, 42, 81, 27);
		contentPane.add(comboBoxMode);
		
		JButton btnSelectFolder = new JButton("Select folder");
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNote.setVisible(true);
				isSelectedFolder = true;
				JFileChooser folderChooser = new JFileChooser();
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				folderChooser.setAcceptAllFileFilterUsed(false);
				folderChooser.showSaveDialog(null);
				if(System.getProperties().getProperty("os.name").contains("Windows")) 
					pathFileTextField.setText(folderChooser.getSelectedFile().getAbsolutePath());
				else pathFileTextField.setText(folderChooser.getCurrentDirectory().getAbsolutePath());
			}
		});
		btnSelectFolder.setBounds(315, 158, 117, 29);
		contentPane.add(btnSelectFolder);
		
		lblKeyPath = new JLabel("Key path");
		lblKeyPath.setBounds(43, 72, 61, 16);
		lblKeyPath.setVisible(false);
		contentPane.add(lblKeyPath);
		
		keyPathTextField = new JTextField();
		keyPathTextField.setColumns(10);
		keyPathTextField.setBounds(133, 67, 242, 26);
		keyPathTextField.setVisible(false);
		keyPathTextField.setEditable(false);
		contentPane.add(keyPathTextField);
		
		btnKeyPath = new JButton("...");
		btnKeyPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if(fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if(!file.getName().contains("key")) 
						JOptionPane.showMessageDialog(contentPane, "Please provide file key");
					else keyPathTextField.setText(file.getAbsolutePath());
				}
			}
		});
		btnKeyPath.setBounds(384, 67, 48, 29);
		btnKeyPath.setVisible(false);
		contentPane.add(btnKeyPath);
		
		lblNote = new JLabel("<html>Please dont put any subfolders under your selected folder!</html>");
		lblNote.setForeground(Color.RED);
		lblNote.setBounds(167, 107, 253, 40);
		lblNote.setVisible(false);
		contentPane.add(lblNote);
		
		lblNoteName = new JLabel("Please dont put any symbol \"\\\" or \"/\" in your files' name");
		lblNoteName.setForeground(Color.BLUE);
		lblNoteName.setVisible(false);
		lblNoteName.setBounds(43, 194, 377, 16);
		contentPane.add(lblNoteName);
	}
	private byte[] encrypt(String pathOfExecutedFile, SecretKey secretKey, Cipher cipher) throws IOException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException {

		byte[] keyToBytes = secretKey.getEncoded();
		System.out.println("You encrypt with key " + keyToBytes.length + " bytes.");
		byte[] data = Files.readAllBytes(Paths.get(pathOfExecutedFile));
		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;
	}
	private byte[] decrypt(String pathOfExecutedFile, Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {

		byte[] data = Files.readAllBytes(Paths.get(pathOfExecutedFile));
		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;
	}
}
