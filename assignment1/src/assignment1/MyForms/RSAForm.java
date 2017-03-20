package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
import java.util.Random;

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

public class RSAForm extends JFrame{
	private JPanel contentPane;
	private JTextField keyTextField;
	private JTextField inputTextField;
	private JTextField outputTextField;
	private JComboBox<String> myComboBox;
	private JProgressBar myProgressBar;
	private ButtonGroup myGroupButton;
	//
	private String n_value;
	private String e_value;
	private String d_value;
	private String PLAIN_FILE_NAME = "";
	private String ENCRYPTED_FILE_NAME= "";
	private boolean isEncrypted = true;
	private String filePath = "";
	private String DES_MODE;
	private String DES_OPT;
	private final int DES_ENCRYPT = 1;
	private final int DES_DECRYPT = 0;
	//
	private byte[] inputByteArray;
	private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength =1024;
    private Random r;
    public RSAForm(){
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Key lenght");
		lblNewLabel.setBounds(26, 21, 61, 16);
		contentPane.add(lblNewLabel);

		JLabel lblInput = new JLabel("Input");
		lblInput.setBounds(26, 49, 61, 16);
		contentPane.add(lblInput);


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
		/*btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ENCRYPTED_FILE_NAME = PLAIN_FILE_NAME + "_encrypted";
					DES_OPT = myGroupButton.getSelection().getActionCommand();
					byte[] output_res;
					byte[] fileInByte;
					//TO-DO: convert file to byte
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
						if (DES_OPT.equals("EnCrypt"))
							output_res=encrypt(fileInByte);
						else output_res=decrypt(fileInByte);
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
		});*/
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
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isEncrypted = true;
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					filePath = file.getAbsolutePath();
					//
					Path path = Paths.get(filePath);
					try {
						inputByteArray = Files.readAllBytes(path);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//
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
		/*btnGenerateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//get key length value and generate
					int bitlength=Integer.parseInt(keyTextField.getText());
					generateKey(bitlength);
					// TO-DO: write to file
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});*/
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
	//set value of public key or private key
    public RSAForm(BigInteger e, BigInteger d, BigInteger N)
    {
        this.e = e;
        this.d = d;
        this.N = N;
    }
	//
    public void generateKey(int bitlen) throws IOException
    {
    	r = new Random();
        bitlength = bitlen;
        p = BigInteger.probablePrime(bitlength/2, r);
        q = BigInteger.probablePrime(bitlength/2, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        //
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        //d= e^-1 mod phi
        d = e.modInverse(phi);
        //private key
        createKeyFile(N,e);
        //public key
        createKeyFile(N,d);
    }
    public void createKeyFile(BigInteger n,BigInteger eod) throws IOException{
    	String pk1 = new String(n.toByteArray());
    	Path pathOfEncryptedFile = Paths.get("/home/pi/pk1");
    	FileWriter fileWriter = new FileWriter(new File("/home/pi/pk1"));
    	BufferedWriter bw = new BufferedWriter(fileWriter);
    	bw.write(pk1);
    	bw.newLine();
    	bw.close();
    }
    public static void main(String[] args) throws IOException
    {
        RSAForm rsa = new RSAForm();
        rsa.generateKey(2048);
        DataInputStream in = new DataInputStream(System.in);
        String teststring;
        System.out.println("Enter the plain text:");
        teststring = in.readLine();
        Path path  =Paths.get(teststring);
        byte[] data = Files.readAllBytes(path);
        System.out.println("Encrypting String: " + teststring);
        System.out.println("String in Bytes: "
                + bytesToString(teststring.getBytes()));
        // encrypt
        byte[] encrypted = rsa.encrypt(data);
        //write to file
        Path pathOfEncryptedFile = Paths.get("/home/pi/testass2");
        Files.write(pathOfEncryptedFile,encrypted);
        // decrypt
        byte[] decrypted = rsa.decrypt(encrypted);
        //write to file
        Path pathOfDecryptedFile = Paths.get("/home/pi/testass3");
        Files.write(pathOfDecryptedFile,decrypted);
        
        System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted String: " + new String(decrypted));
    }
    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }
    // Encrypt message
    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }
    // Decrypt message
    public byte[] decrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
}
