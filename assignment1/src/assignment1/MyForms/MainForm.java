package assignment1.MyForms;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
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
		
		JButton btnAES = new JButton("AES");
		btnAES.setBounds(227, 10, 117, 47);
		contentPane.add(btnAES);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(227, 219, 117, 29);
		contentPane.add(btnExit);
		
		
	}

}
