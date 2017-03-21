package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DESForm extends JFrame {

	private JPanel contentPane;
	private JLabel lblIv;
	private JLabel lblKey;
	private JLabel lblNote;
	private JLabel lblNote1;
	
	private JTextField keyTextField;
	private JTextField pathTextField;
	private JTextField outputTextField;
	private JTextField ivTextField;
	
	
	private JComboBox<String> myComboBox;
	private JProgressBar myProgressBar;
	private ButtonGroup myGroupButton;


	private SecretKey key;
	private String PLAIN_FILE_NAME = "";
	private String DES_FILE_NAME= "";
	private String FILE_PATH = "";
	private long FILE_SIZE;
	private byte[] FILE_BYTES;
	private String DES_MODE;
	private String DES_OPT;
	private String outputFolderName;
	private boolean isSelectedFolder;
	//	/**
	//	 * Launch the application.
	//	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					DESForm frame = new DESForm();
	//					frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the frame.
	 */
	public DESForm() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblKey = new JLabel("Key");
		lblKey.setBounds(26, 21, 94, 16);
		contentPane.add(lblKey);

		JLabel lblInput = new JLabel("Path");
		lblInput.setBounds(26, 49, 61, 16);
		contentPane.add(lblInput);

		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(26, 77, 61, 16);
		contentPane.add(lblMode);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(26, 105, 61, 16);
		contentPane.add(lblOutput);

		keyTextField = new JTextField();
		keyTextField.setBackground(Color.LIGHT_GRAY);
		keyTextField.setEditable(false);
		keyTextField.setBounds(122, 16, 130, 26);
		contentPane.add(keyTextField);
		keyTextField.setColumns(10);

		pathTextField = new JTextField();
		pathTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				lblNote.setVisible(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		pathTextField.setEditable(false);
		pathTextField.setColumns(10);
		pathTextField.setBounds(122, 44, 219, 26);
		contentPane.add(pathTextField);

		outputTextField = new JTextField();
		outputTextField.setEditable(false);
		outputTextField.setColumns(10);
		outputTextField.setBounds(122, 100, 219, 26);
		contentPane.add(outputTextField);

		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DES_MODE  = myComboBox.getSelectedItem().toString();
				DES_OPT = myGroupButton.getSelection().getActionCommand();
				DES_FILE_NAME = PLAIN_FILE_NAME + "_" + DES_OPT;
				FILE_PATH = pathTextField.getText();
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Date dateExecute = new Date();
				outputFolderName = dateFormat.format(dateExecute);
				if (FILE_PATH.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Please select file or folder!");
				}
				else if (DES_OPT.equals("Encrypt") && keyTextField.getText().isEmpty()) {
					KeyGenerator keyGenerator;
					try {
						//Generate DES key
						keyGenerator = KeyGenerator.getInstance("DES");
						key = keyGenerator.generateKey();
						
						// get base64 encoded version of the key
						String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
						keyTextField.setText(encodedKey);
						System.out.println("Your key is " + encodedKey.length() + "  bytes long");
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally {				
						JOptionPane.showMessageDialog(contentPane, "You haven't clicked \"Generate Key\" button, so we generate the key for you :)");
					}
					
				}
				/*
				 *  Decrypt here
				 */
				else if (DES_OPT.equals("Decrypt")) {
					
					if(keyTextField.getText().length() != 12) {
						JOptionPane.showMessageDialog(contentPane, "Please provide a 12 byted-key");
					}
					else if(DES_MODE.equals("CBC") && ivTextField.getText().length() != 16) {
						JOptionPane.showMessageDialog(contentPane, "Please provide a 16 byted-IV");
					}
					else {
						String encodedKey = keyTextField.getText();
						// decode the base64 encoded string
						byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
						try {
							// rebuild key using SecretKeySpec
							key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
							File folder = new File(FILE_PATH);
							/*
							 * Select folder
							 */
							if (folder.isDirectory()) {
								File[] listOfFiles = folder.listFiles();
								for(File file: listOfFiles) {
									FILE_PATH = file.getAbsolutePath();
									PLAIN_FILE_NAME = file.getName();
									if(PLAIN_FILE_NAME.charAt(0) != '.' && PLAIN_FILE_NAME.endsWith("_Encrypt")) {
										DES_FILE_NAME = PLAIN_FILE_NAME + "_" + DES_OPT;
										DESAlgorithm(DES_OPT, DES_MODE);
									}
								}
								System.out.println("Execution completed :D");
							}
							/*
							 * Select file
							 */
							else {
								DESAlgorithm(DES_OPT, DES_MODE); 
								System.out.println("Execution completed :D");
							}
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException | InvalidAlgorithmParameterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				/*
				 * * Encrypt here
				 */
				else {
					try {
						File folder = new File(FILE_PATH);
						/*
						 * Select folder
						 */
						if (folder.isDirectory()) {
							File[] listOfFiles = folder.listFiles();
							for(File file: listOfFiles) {
								FILE_PATH = file.getAbsolutePath();
								PLAIN_FILE_NAME = file.getName();
								if(PLAIN_FILE_NAME.charAt(0) != '.') {
									DES_FILE_NAME = PLAIN_FILE_NAME.substring(0, PLAIN_FILE_NAME.lastIndexOf(".")) + "_" + DES_OPT;
									DESAlgorithm(DES_OPT, DES_MODE);
								}	
							}
							/*
							 *  Note that we append to the existing file info
							 */
							String operationSystemName = System.getProperties().getProperty("os.name");
							File info;
							if (operationSystemName.contains("Windows")) info = new File(folder.getAbsolutePath() + "\\" + outputFolderName + "\\" + "info.txt");
							else info = new File(folder.getAbsolutePath() + "/" + outputFolderName + "/" + "info.txt");
							FileWriter infoWriter = new FileWriter(info, true); //different here!
							BufferedWriter bw = new BufferedWriter(infoWriter);
							bw.write("Mode: " + DES_MODE);
							bw.newLine();
							bw.write("Key: " + keyTextField.getText());
							bw.newLine();
							if(DES_MODE.equals("CBC")) {
								bw.write("Initialization vector: " + ivTextField.getText());
								bw.newLine();
							}
							bw.write("Date execute: " + outputFolderName);
							bw.close();
							System.out.println("Execution completed :D");
						}
						/*
						 * Select file
						 */
						else {
							DESAlgorithm(DES_OPT, DES_MODE);
							System.out.println("Execution completed :D");
						}
					} catch (InvalidKeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalBlockSizeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidAlgorithmParameterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnExecute.setBounds(43, 218, 171, 45);
		contentPane.add(btnExecute);

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

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(52, 188, 376, 26);
		contentPane.add(progressBar);
		myProgressBar = progressBar;
		myProgressBar.setMaximum(100);
		myProgressBar.setMinimum(0);

		JComboBox<String> comboBoxMode = new JComboBox<String>();
		comboBoxMode.setModel(new DefaultComboBoxModel<String>(new String[] {"ECB", "CBC"}));
		comboBoxMode.setBounds(122, 73, 219, 27);
		comboBoxMode.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
			          Object item = e.getItem();
			          if (item.equals("CBC")) {
			        	  ivTextField.setEditable(true);
			        	  ivTextField.setBackground(Color.WHITE);
			          }
			          else {
			        	  ivTextField.setEditable(false);
			        	  ivTextField.setBackground(Color.LIGHT_GRAY);
			          }
			       }
			}
		});
		contentPane.add(comboBoxMode);
		myComboBox = comboBoxMode;

		JButton btnOpenFile = new JButton("Select File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNote1.setVisible(false);
				isSelectedFolder = false;
				myProgressBar.setValue(0);
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					pathTextField.setText(file.getAbsolutePath());
					PLAIN_FILE_NAME = file.getName();
					if(PLAIN_FILE_NAME.contains(".")) PLAIN_FILE_NAME = PLAIN_FILE_NAME.substring(0, PLAIN_FILE_NAME.lastIndexOf('.'));
				}

			}
		});
		btnOpenFile.setBounds(184, 153, 117, 29);
		contentPane.add(btnOpenFile);

		JButton btnOpenFolder = new JButton("Select folder");
		btnOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNote1.setVisible(true);
				isSelectedFolder = true;
				myProgressBar.setValue(0);
				JFileChooser folderChooser = new JFileChooser();
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				folderChooser.setAcceptAllFileFilterUsed(false);
				folderChooser.showSaveDialog(null);
				if(System.getProperties().getProperty("os.name").contains("Windows")) FILE_PATH = folderChooser.getSelectedFile().getAbsolutePath();
				else FILE_PATH = folderChooser.getCurrentDirectory().getAbsolutePath();
				pathTextField.setText(FILE_PATH);
			}
		});
		btnOpenFolder.setBounds(311, 153, 117, 29);
		contentPane.add(btnOpenFolder);

		JButton btnGenerateKey = new JButton("Generate Key");
		btnGenerateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
					key = keyGenerator.generateKey();
					// get base64 encoded version of the key
					String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
					keyTextField.setText(encodedKey);
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnGenerateKey.setBounds(184, 128, 115, 29);
		contentPane.add(btnGenerateKey);

		JRadioButton rdbtnEncryptButton = new JRadioButton("Encrypt");
		rdbtnEncryptButton.setActionCommand("Encrypt");
		rdbtnEncryptButton.setSelected(true);
		rdbtnEncryptButton.setBounds(26, 129, 141, 23);
		contentPane.add(rdbtnEncryptButton);
		rdbtnEncryptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lblKey.setText("Key");
				
				keyTextField.setEditable(false);
				keyTextField.setBackground(Color.LIGHT_GRAY);
				
				ivTextField.setVisible(false);
				
				lblIv.setVisible(false);
			}
		});

		JRadioButton rdbtnDecryptButton = new JRadioButton("Decrypt");
		rdbtnDecryptButton.setActionCommand("Decrypt");
		rdbtnDecryptButton.setBounds(26, 154, 141, 23);
		contentPane.add(rdbtnDecryptButton);
		rdbtnDecryptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lblKey.setText("Key (12 bytes)");
				
				keyTextField.setEditable(true);
				keyTextField.setBackground(Color.WHITE);
				
				ivTextField.setVisible(true);
				
				lblIv.setVisible(true);
			}
		});

		ButtonGroup groupButton = new ButtonGroup();
		groupButton.add(rdbtnEncryptButton);
		groupButton.add(rdbtnDecryptButton);
		myGroupButton = groupButton;
		
		lblIv = new JLabel("IV (16 bytes)");
		lblIv.setBounds(264, 21, 94, 16);
		contentPane.add(lblIv);
		lblIv.setVisible(false);
		
		ivTextField = new JTextField();
		ivTextField.setBounds(358, 16, 130, 26);
		contentPane.add(ivTextField);
		ivTextField.setColumns(10);
		ivTextField.setVisible(false);
		ivTextField.setBackground(Color.LIGHT_GRAY);
		ivTextField.setEditable(false);
		
		lblNote = new JLabel("Note: Please exclude symbols as \"/\" or \"\\\" from your files ' name");
		lblNote.setForeground(new Color(34, 139, 34));
		lblNote.setBounds(43, 275, 418, 16);
		lblNote.setVisible(false);
		contentPane.add(lblNote);
		
		lblNote1 = new JLabel("<html>Note: Please dont put any subfolders under choosen folder</html>");
		lblNote1.setForeground(new Color(0, 0, 139));
		lblNote1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNote1.setVerticalAlignment(SwingConstants.TOP);
		lblNote1.setBounds(353, 49, 123, 72);
		lblNote1.setVisible(false);
		contentPane.add(lblNote1);
	}

	private void DESAlgorithm(String DES_OPTION, String DES_MODE) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException {
		String operationSystemName = System.getProperties().getProperty("os.name");
		try {
			FILE_BYTES = Files.readAllBytes(Paths.get(FILE_PATH));
			FILE_SIZE = FILE_BYTES.length;
			System.out.println("You " + DES_OPT.toLowerCase() + " with mode " + DES_MODE);
			Cipher cipher = null;
			
			if(DES_OPTION.equals("Decrypt")) {
				cipher = Cipher.getInstance("DES/" + DES_MODE + "/PKCS5Padding");
				if(DES_MODE.equals("CBC")) {
					//cipher = Cipher.getInstance("DES/" + DES_MODE + "/PKCS5Padding");
					
					String encodedIV = ivTextField.getText();
					// decode the base64 encoded iv
					byte[] decodedIV = Base64.getDecoder().decode(encodedIV);
					
					AlgorithmParameters params = AlgorithmParameters.getInstance("DES");
					params.init(decodedIV);	
					cipher.init(Cipher.DECRYPT_MODE, key, params);
				}
				else {
					//cipher = Cipher.getInstance("DES/" + DES_MODE + "/NoPadding");
					cipher.init(Cipher.DECRYPT_MODE, key);
				}
			}
			else if (DES_OPTION.equals("Encrypt")) {
				cipher = Cipher.getInstance("DES/" + DES_MODE + "/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				if (DES_MODE.equals("CBC")) {
					String encodedParams = Base64.getEncoder().encodeToString(cipher.getParameters().getEncoded());				
					ivTextField.setText(encodedParams);
				}
			}


			byte[] bytesOutputOfSegment;
			long FILE_SEGMENT = FILE_SIZE/ 100;
			FILE_SEGMENT -= FILE_SEGMENT % 8;
//			long times = FILE_SIZE/FILE_SEGMENT;
//			long count = 0;
			
			/*
			 * Encrypt here
			 */
			if (DES_OPTION.equals("Encrypt")) {
				//Create an output directory contains 2 file results
				File outputDirectory;
				//Create output directory here
				if (operationSystemName.contains("Windows")) outputDirectory = new File(FILE_PATH.substring(0, FILE_PATH.lastIndexOf('\\')) + "\\" + outputFolderName);
				else outputDirectory = new File(FILE_PATH.substring(0, FILE_PATH.lastIndexOf('/')) + "/" + outputFolderName);

				if (!outputDirectory.exists()){ //folder non-exist
					boolean createDirectory = outputDirectory.mkdir();
					if (createDirectory) {
						System.out.println("Create output directory successfully - Encrypt all files in folder");
						//Create result files here
						File file;
						if (operationSystemName.contains("Windows")) file = new File(outputDirectory.getAbsolutePath() + "\\" + DES_FILE_NAME);
						else file = new File(outputDirectory.getAbsolutePath() + "/" + DES_FILE_NAME);
						file.createNewFile();
						
						FileOutputStream fileStream = new FileOutputStream(file);
						bytesOutputOfSegment = cipher.doFinal(FILE_BYTES);
						fileStream.write(bytesOutputOfSegment);
						fileStream.flush();				
						fileStream.close();
						
						// Create file info here
						File info;
						if (operationSystemName.contains("Windows")) info = new File(outputDirectory.getAbsolutePath() + "\\" + "info.txt");
						else info = new File(outputDirectory.getAbsolutePath() + "/" + "info.txt");
						info.createNewFile();
						FileWriter infoWriter = new FileWriter(info);
						BufferedWriter bw = new BufferedWriter(infoWriter);
						if(!isSelectedFolder) {
							bw.write("Plain file path: " + FILE_PATH);
							bw.newLine();
							bw.write("Cipher file name: " + DES_FILE_NAME);
							bw.newLine();
							bw.write("Mode: " + DES_MODE);
							bw.newLine();
							bw.write("Key: " + keyTextField.getText());
							bw.newLine();
							if(DES_MODE.equals("CBC")) {
								bw.write("Initialization vector: " + ivTextField.getText());
								bw.newLine();
							}
							bw.write("Date execute: " + outputFolderName);
							bw.close();
						}
						else {
							bw.write("Plain file path: " + FILE_PATH);
							bw.newLine();
							bw.write("Cipher file name: " + DES_FILE_NAME);
							bw.newLine();
							bw.close();
						}
					}
					else System.out.println("Create output directory failed"); //created folder failed
				}
				else { //folder existed
					File file;
					if (operationSystemName.contains("Windows")) file = new File(outputDirectory.getAbsolutePath() + "\\" + DES_FILE_NAME);
					else file = new File(outputDirectory.getAbsolutePath() + "/" + DES_FILE_NAME);
					file.createNewFile();
					
					FileOutputStream fileStream = new FileOutputStream(file);
					bytesOutputOfSegment = cipher.doFinal(FILE_BYTES);
					fileStream.write(bytesOutputOfSegment);
					fileStream.flush();
					fileStream.close();
					
					// Append to file info here
					File info;
					if (operationSystemName.contains("Windows")) info = new File(outputDirectory.getAbsolutePath() + "\\" + "info.txt");
					else info = new File(outputDirectory.getAbsolutePath() + "/" + "info.txt");
					info.createNewFile();
					FileWriter infoWriter = new FileWriter(info, true); //different here
					BufferedWriter bw = new BufferedWriter(infoWriter);
					bw.write("Plain file path: " + FILE_PATH);
					bw.newLine();
					bw.write("Cipher file name: " + DES_FILE_NAME);
					bw.newLine();
					bw.close();
				}
			}
			/*
			 * Decrypt here
			 */
			else if (DES_OPTION.equals("Decrypt")) {
				File file;
				if (operationSystemName.contains("Windows")) file = new File(FILE_PATH.substring(0, FILE_PATH.lastIndexOf("\\")) + "\\" + DES_FILE_NAME);
				else file = new File(FILE_PATH.substring(0, FILE_PATH.lastIndexOf("/")) + "/" + DES_FILE_NAME);
				file.createNewFile();
				FileOutputStream fileStream = new FileOutputStream(file);
				bytesOutputOfSegment = cipher.doFinal(FILE_BYTES);
				fileStream.write(bytesOutputOfSegment);
				fileStream.flush();

				fileStream.close();
			}
		} catch (IOException e) {
		}
		
	}
}
