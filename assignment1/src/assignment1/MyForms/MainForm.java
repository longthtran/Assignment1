package assignment1.MyForms;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainForm extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm frame = new MainForm();
					frame.setTitle("Welcome to BKEDT");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainForm() {
		setTitle("Welcome to BKEDT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnDes = new JButton("DES");
		btnDes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DESForm().setVisible(true);
				dispose();
				
			}
		});
		btnDes.setBounds(44, 10, 100, 47);
		contentPane.add(btnDes);
		
		JButton btnRSA = new JButton("RSA");
		btnRSA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RSAForm().setVisible(true);
				dispose();
			}
		});
		btnRSA.setBounds(44, 69, 100, 47);
		contentPane.add(btnRSA);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(227, 219, 117, 29);
		contentPane.add(btnExit);
		
		JButton btnCheckFile = new JButton("Check File");
		btnCheckFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CheckFileForm().setVisible(true);
				dispose();
			}
		});
		btnCheckFile.setBounds(44, 201, 100, 47);
		contentPane.add(btnCheckFile);
		
		JButton btnAES = new JButton("AES");
		btnAES.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AESForm().setVisible(true);
				dispose();
			}
		});
		btnAES.setBounds(44, 142, 100, 47);
		contentPane.add(btnAES);
		
		JLabel lblNewLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/img/ok.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(0, 0, 434, 262);
		contentPane.add(lblNewLabel);
		
		
	}
}
