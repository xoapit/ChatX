package client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JPanel;
import java.awt.Color;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.xml.parsers.FactoryConfigurationError;

import org.omg.CORBA.Environment;

import model.User;

import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class Login {

	private JFrame frmLogin;
	private JTextField txtUsername;
	private JTextField txtIPSV;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Login window = new Login();
		window.frmLogin.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		frmLogin.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setResizable(false);
		frmLogin.setTitle("Login");
		frmLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/image/iconchat.png")));
		frmLogin.getContentPane().setBackground(new Color(0x3d, 0x5a, 0x99));
		frmLogin.setType(Type.POPUP);
		frmLogin.setBounds(200, 100, 310, 406);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel lblMessage = new JLabel("Message ChatX");
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 25));
		lblMessage.setBounds(83, 39, 216, 52);
		frmLogin.getContentPane().add(lblMessage);

		JLabel lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon(Login.class.getResource("/image/iconchat.png")));
		lbLogo.setBounds(23, 39, 50, 50);
		frmLogin.getContentPane().add(lbLogo);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtUsername.setBackground(SystemColor.info);
		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
		txtUsername.setBounds(48, 164, 210, 30);
		txtUsername.setBorder(new EmptyBorder(2, 2, 2, 2));
		frmLogin.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBackground(SystemColor.controlShadow);
		lblUsername.setBounds(48, 147, 210, 14);
		frmLogin.getContentPane().add(lblUsername);
		lblUsername.setForeground(SystemColor.controlLtHighlight);
		lblUsername.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 13));

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(48, 276, 210, 35);
		frmLogin.getContentPane().add(btnLogin);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(new Color(0x22, 0x3b, 0x73));
		btnLogin.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 16));
		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!txtUsername.getText().equals("")) {
						String myIP = Inet4Address.getLocalHost().getHostAddress();
						Client client = new Client(new User(txtUsername.getText(), myIP, txtIPSV.getText(), "00:10"));
						frmLogin.setVisible(false);
					}
				} catch (UnknownHostException e) {
					// e.printStackTrace();
				}
			}
		});

		JLabel lblIpServer = new JLabel("IP Server");
		lblIpServer.setBackground(SystemColor.controlShadow);
		lblIpServer.setForeground(Color.WHITE);
		lblIpServer.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 13));
		lblIpServer.setBounds(48, 204, 210, 14);
		frmLogin.getContentPane().add(lblIpServer);

		txtIPSV = new JTextField();
		txtIPSV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtIPSV.setBackground(SystemColor.info);
		txtIPSV.setHorizontalAlignment(SwingConstants.CENTER);
		txtIPSV.setColumns(10);
		txtIPSV.setBorder(new EmptyBorder(2, 2, 2, 2));
		txtIPSV.setBounds(48, 220, 210, 30);
		frmLogin.getContentPane().add(txtIPSV);
	}

}
