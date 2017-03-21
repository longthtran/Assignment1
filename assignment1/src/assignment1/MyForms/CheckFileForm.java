package assignment1.MyForms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.DatatypeConverter;
import javax.swing.SwingConstants;

public class CheckFileForm extends JFrame {

	private JPanel contentPane;
	private JTextField dataTextField;
	private JTextField md5TextField;
	private JCheckBox chckbxMd5;
	private JCheckBox chckbxSha1;
	private JCheckBox chckbxSha256;
	private JCheckBox chckbxSha512;
	private JTextField sha1TextField;
	private JTextField sha256TextField;
	private JTextField sha512TextField;


	/**
	 * Create the frame.
	 */
	public CheckFileForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 733, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Data");
		lblNewLabel.setBounds(41, 22, 61, 16);
		contentPane.add(lblNewLabel);
		
		dataTextField = new JTextField();
		dataTextField.setBounds(91, 17, 276, 26);
		contentPane.add(dataTextField);
		dataTextField.setColumns(10);
		
		JButton btnSelectFile = new JButton("...");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					dataTextField.setText(file.getAbsolutePath());
				}
			}
		});
		btnSelectFile.setBounds(365, 17, 54, 29);
		contentPane.add(btnSelectFile);
		
		chckbxMd5 = new JCheckBox("MD5");
		chckbxMd5.setBounds(6, 51, 70, 23);
		contentPane.add(chckbxMd5);
		
		md5TextField = new JTextField();
		md5TextField.setBackground(Color.LIGHT_GRAY);
		md5TextField.setEditable(false);
		md5TextField.setColumns(10);
		md5TextField.setBounds(91, 50, 615, 26);
		contentPane.add(md5TextField);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnExit.setBounds(632, 220, 74, 29);
		contentPane.add(btnExit);
		
		JButton btnGoBack = new JButton("Go back");
		btnGoBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainForm().setVisible(true);
				dispose();
			}
		});
		btnGoBack.setBounds(503, 220, 117, 29);
		contentPane.add(btnGoBack);
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					byte[] data = Files.readAllBytes(Paths.get(dataTextField.getText()));				
					String md5 = "";
					String sha1 = "";
					String sha256 = "";
					String sha512 = "";
					if(chckbxMd5.isSelected()) {
						byte[] hashMd5 = MessageDigest.getInstance("MD5").digest(data);
						md5 = DatatypeConverter.printHexBinary(hashMd5).toLowerCase();
					}
					if(chckbxSha1.isSelected()) {
						byte[] hashSha1 = MessageDigest.getInstance("SHA-1").digest(data);
						sha1 = DatatypeConverter.printHexBinary(hashSha1).toLowerCase();
					}
					if(chckbxSha256.isSelected()) {
						byte[] hashSha256 = MessageDigest.getInstance("SHA-256").digest(data);
						sha256 = DatatypeConverter.printHexBinary(hashSha256).toLowerCase();
					}
					if(chckbxSha512.isSelected()) {
						byte[] hashSha512 = MessageDigest.getInstance("SHA-512").digest(data);
						sha512 = DatatypeConverter.printHexBinary(hashSha512).toLowerCase();
					}
					md5TextField.setText(md5);
					sha1TextField.setText(sha1);
					sha256TextField.setText(sha256);
					sha512TextField.setText(sha512);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCalculate.setBounds(41, 220, 117, 29);
		contentPane.add(btnCalculate);
		
		sha1TextField = new JTextField();
		sha1TextField.setEditable(false);
		sha1TextField.setColumns(10);
		sha1TextField.setBackground(Color.LIGHT_GRAY);
		sha1TextField.setBounds(91, 88, 615, 26);
		contentPane.add(sha1TextField);
		
		sha256TextField = new JTextField();
		sha256TextField.setEditable(false);
		sha256TextField.setColumns(10);
		sha256TextField.setBackground(Color.LIGHT_GRAY);
		sha256TextField.setBounds(91, 126, 615, 26);
		contentPane.add(sha256TextField);
		
		sha512TextField = new JTextField();
		sha512TextField.setHorizontalAlignment(SwingConstants.TRAILING);
		sha512TextField.setEditable(false);
		sha512TextField.setColumns(10);
		sha512TextField.setBackground(Color.LIGHT_GRAY);
		sha512TextField.setBounds(91, 164, 615, 26);
		contentPane.add(sha512TextField);
		
		chckbxSha1 = new JCheckBox("SHA-1");
		chckbxSha1.setBounds(6, 89, 80, 23);
		contentPane.add(chckbxSha1);
		
		chckbxSha256 = new JCheckBox("SHA-256");
		chckbxSha256.setBounds(6, 127, 90, 23);
		contentPane.add(chckbxSha256);
		
		chckbxSha512 = new JCheckBox("SHA-512");
		chckbxSha512.setBounds(6, 165, 90, 23);
		contentPane.add(chckbxSha512);
	}
}
