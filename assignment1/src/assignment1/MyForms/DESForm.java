package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
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
import javax.swing.border.EmptyBorder;

public class DESForm extends JFrame {

	private JPanel contentPane;
	private JTextField keyTextField;
	private JTextField inputTextField;
	private JTextField outputTextField;
	private JComboBox<String> myComboBox;
	private JProgressBar myProgressBar;
	private ButtonGroup myGroupButton;
	
	
	private SecretKey key;
	private String PLAIN_FILE_NAME = "";
	private String ENCRYPTED_FILE_NAME= "";
	private boolean isEncrypted = true;
	private String filePath = "";
	private String DES_MODE;
	private String DES_OPT;
	private final int DES_ENCRYPT = 1;
	private final int DES_DECRYPT = 0;
	
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Key");
		lblNewLabel.setBounds(26, 21, 61, 16);
		contentPane.add(lblNewLabel);

		JLabel lblInput = new JLabel("Input");
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
		keyTextField.setBounds(99, 16, 230, 26);
		contentPane.add(keyTextField);
		keyTextField.setColumns(10);

		inputTextField = new JTextField();
		inputTextField.setEditable(false);
		inputTextField.setColumns(10);
		inputTextField.setBounds(99, 44, 230, 26);
		contentPane.add(inputTextField);

		outputTextField = new JTextField();
		outputTextField.setEditable(false);
		outputTextField.setColumns(10);
		outputTextField.setBounds(99, 100, 230, 26);
		contentPane.add(outputTextField);

		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ENCRYPTED_FILE_NAME = PLAIN_FILE_NAME + "_encrypted";
					DES_MODE  = myComboBox.getSelectedItem().toString();
					DES_OPT = myGroupButton.getSelection().getActionCommand();
					if (filePath.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Please select file to encrypt!");
					}
					else if (DES_OPT.equals("Encrypt") && keyTextField.getText().isEmpty()) {
						KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
						key = keyGenerator.generateKey();
						keyTextField.setText(new String(key.getEncoded()));
						JOptionPane.showMessageDialog(contentPane, "You haven't clicked \"Generate Key\" button, so we generate the key for you :)");
					}
					else  {
						DESAlgorithm(DES_OPT, DES_MODE);
					}
				} catch (InvalidKeyException e1) {
					System.out.println("Key is invalid");
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
		progressBar.setBounds(52, 194, 376, 20);
		contentPane.add(progressBar);
		myProgressBar = progressBar;

		JComboBox<String> comboBoxMode = new JComboBox<String>();
		comboBoxMode.setModel(new DefaultComboBoxModel<String>(new String[] {"ECB", "CBC", "CFC"}));
		comboBoxMode.setBounds(99, 72, 230, 27);
		contentPane.add(comboBoxMode);
		myComboBox = comboBoxMode;
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isEncrypted = true;
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					filePath = file.getAbsolutePath();
					inputTextField.setText(filePath);
					PLAIN_FILE_NAME = file.getName();
				}
				//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				
			}
		});
		btnOpenFile.setBounds(209, 153, 90, 29);
		contentPane.add(btnOpenFile);
		
		JButton btnOpenFolder = new JButton("Open folder");
		btnOpenFolder.setBounds(311, 153, 117, 29);
		contentPane.add(btnOpenFolder);
		
		JButton btnGenerateKey = new JButton("Generate Key");
		btnGenerateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
					key = keyGenerator.generateKey();
					keyTextField.setText(new String(key.getEncoded()));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnGenerateKey.setBounds(329, 16, 115, 29);
		contentPane.add(btnGenerateKey);
		
		JButton btnReveal = new JButton("Reveal File");
		btnReveal.setBounds(327, 100, 117, 29);
		contentPane.add(btnReveal);
		
		JRadioButton rdbtnEncryptButton = new JRadioButton("Encrypt");
		rdbtnEncryptButton.setActionCommand("Encrypt");
		rdbtnEncryptButton.setSelected(true);
		rdbtnEncryptButton.setBounds(26, 129, 141, 23);
		contentPane.add(rdbtnEncryptButton);
		rdbtnEncryptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				keyTextField.setEditable(false);
				keyTextField.setBackground(Color.LIGHT_GRAY);
			}
		});
		
		JRadioButton rdbtnDecryptButton = new JRadioButton("Decrypt");
		rdbtnDecryptButton.setActionCommand("Decrypt");
		rdbtnDecryptButton.setBounds(26, 154, 141, 23);
		contentPane.add(rdbtnDecryptButton);
		rdbtnDecryptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				keyTextField.setEditable(true);
				keyTextField.setBackground(Color.WHITE);
			}
		});
			
		ButtonGroup groupButton = new ButtonGroup();
		groupButton.add(rdbtnEncryptButton);
		groupButton.add(rdbtnDecryptButton);
		myGroupButton = groupButton;
	}
	
//	public Component getProgressBar() {
//		Component[] components = contentPane.getComponents();
//		for (Component component: components) {
//			if(component.getClass() == JProgressBar.class)
//				return component;	
//		}
//		return null;
//	}
	
	public void DESAlgorithm(String DES_OPTION, String DES_MODE) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		byte[] bytesOfPlainFile = Files.readAllBytes(Paths.get(filePath));
		myProgressBar.setMaximum(bytesOfPlainFile.length);
		try {
			Cipher cipher = null;
			cipher = Cipher.getInstance("DES/" + DES_MODE + "/PKCS5Padding");
			if(DES_OPTION.equals("Decrypt")) cipher.init(Cipher.DECRYPT_MODE, key);
			else if (DES_OPTION.equals("Encrypt")) cipher.init(Cipher.ENCRYPT_MODE, key);
		} finally {
			
		}
	}
}
