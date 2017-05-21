package server;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.EventQueue;
import java.awt.Font;

import java.awt.SystemColor;
import java.awt.Toolkit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.multi.MultiScrollBarUI;
import javax.swing.plaf.synth.SynthScrollBarUI;

import config.Config;
import model.User;
import other.UserRenderer;

import javax.swing.JTextField;

public class Server {

	private JFrame frame;
	private JList<User> listUser = new JList<User>();
	private JTextField txtIPSV;
	private JTextArea txtDisplay;
	private JLabel lbReady;
	private JPanel pnListUser;
	private JButton btnReply;
	private JTextArea txtChat;

	private DefaultListModel<User> listModelUser = new DefaultListModel<User>();
	private Vector<Socket> socketClients = new Vector<Socket>();
	private Vector<Socket> socketObjects = new Vector<Socket>();

	private static ServerSocket serverSocket;
	private static ServerSocket objectSocket;
	private Config config;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Server window = new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */
	public Server() {
		config = new Config();
		initialize();
		InitSocketTCPObject initSocketTCPObject = new InitSocketTCPObject();
		initSocketTCPObject.start();
		InitSocketTCPMessage initSocketTCPMessage = new InitSocketTCPMessage();
		initSocketTCPMessage.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.control);
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Server.class.getResource("/image/server.png")));
		frame.setTitle("Server");
		frame.setBounds(100, 100, 715, 469);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel pnFooter = new JPanel();
		pnFooter.setBackground(new Color(0, 153, 255));
		pnFooter.setBounds(0, 407, 709, 27);
		frame.getContentPane().add(pnFooter);
		pnFooter.setLayout(null);

		JLabel lbCopyright = new JLabel("Copyright by Xoapit 2017");
		lbCopyright.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		lbCopyright.setBounds(10, 7, 179, 14);
		lbCopyright.setForeground(SystemColor.controlLtHighlight);
		pnFooter.add(lbCopyright);

		JPanel pnAll = new JPanel();
		pnAll.setBackground(SystemColor.controlLtHighlight);
		pnAll.setBorder(new LineBorder(SystemColor.controlShadow));
		pnAll.setBounds(207, 82, 487, 313);
		frame.getContentPane().add(pnAll);
		pnAll.setLayout(null);

		txtDisplay = new JTextArea();
		txtDisplay.setLineWrap(true);
		txtDisplay.setBounds(10, 11, 467, 292);

		UIManager.put("Knob.background", Color.white);
		UIManager.put("Button.foreground", new javax.swing.plaf.ColorUIResource(0, 0, 0));

		JScrollPane scrollPanelDisplay = new JScrollPane(txtDisplay);
		scrollPanelDisplay.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPanelDisplay.setBounds(10, 11, 467, 292);
		scrollPanelDisplay.setOpaque(true);

		pnAll.add(scrollPanelDisplay);

		pnListUser = new JPanel();
		pnListUser.setBounds(10, 82, 187, 314);
		pnListUser.setLayout(new BorderLayout(0, 0));
		pnListUser.add(new JScrollPane(listUser = createListUsers(listModelUser)));
		frame.getContentPane().add(pnListUser);

		JPanel pnHeader = new JPanel();
		pnHeader.setBackground(new Color(0, 153, 255));
		pnHeader.setBounds(0, 0, 726, 75);
		frame.getContentPane().add(pnHeader);
		pnHeader.setLayout(null);

		JPanel pnControlRoot = new JPanel();
		pnControlRoot.setBackground(SystemColor.controlLtHighlight);
		pnControlRoot.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		pnControlRoot.setBounds(371, 11, 323, 45);
		pnHeader.add(pnControlRoot);
		pnControlRoot.setLayout(null);

		JPanel pnControl = new JPanel();
		pnControl.setBounds(5, 5, 312, 35);
		pnControlRoot.add(pnControl);
		pnControl.setBackground(SystemColor.controlLtHighlight);
		pnControl.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		pnControl.setLayout(null);

		lbReady = new JLabel("Ready");
		lbReady.setHorizontalAlignment(SwingConstants.RIGHT);
		lbReady.setBounds(584, 2, 115, 22);
		pnFooter.add(lbReady);
		lbReady.setForeground(SystemColor.text);
		lbReady.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));

		JButton btnCall = new JButton("");
		btnCall.setIcon(new ImageIcon(Server.class.getResource("/image/call.png")));
		btnCall.setBounds(175, 0, 35, 35);
		btnCall.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		btnCall.setBackground(SystemColor.controlLtHighlight);
		pnControl.add(btnCall);

		JButton btnVideo = new JButton("");
		btnVideo.setIcon(new ImageIcon(Server.class.getResource("/image/video1.png")));
		btnVideo.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		btnVideo.setBackground(Color.WHITE);
		btnVideo.setBounds(208, 0, 70, 35);
		pnControl.add(btnVideo);

		JButton btnSetting = new JButton("");
		btnSetting.setBounds(277, 0, 35, 35);
		pnControl.add(btnSetting);
		btnSetting.setIcon(new ImageIcon(Server.class.getResource("/image/setting2.png")));
		btnSetting.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		btnSetting.setBackground(Color.WHITE);

		JLabel lblIpsv = new JLabel("IPSV:");
		lblIpsv.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		lblIpsv.setBounds(15, 6, 30, 24);
		pnControl.add(lblIpsv);

		txtIPSV = new JTextField();
		txtIPSV.setHorizontalAlignment(SwingConstants.CENTER);
		txtIPSV.setText("192.168.1.5");
		try {
			txtIPSV.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
		}
		txtIPSV.setEditable(false);
		txtIPSV.setBounds(50, 5, 120, 25);
		pnControl.add(txtIPSV);
		txtIPSV.setColumns(10);

		JLabel lbLogo = new JLabel("");
		lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lbLogo.setForeground(SystemColor.text);
		lbLogo.setIcon(new ImageIcon(Server.class.getResource("/image/server.png")));
		lbLogo.setBounds(10, 8, 60, 60);
		pnHeader.add(lbLogo);

		JLabel lbUser = new JLabel("SERVER");
		lbUser.setForeground(SystemColor.controlLtHighlight);
		lbUser.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 24));
		lbUser.setBounds(85, 10, 279, 53);
		pnHeader.add(lbUser);

		this.frame.setVisible(true);
	}

	private class InitSocketTCPMessage extends Thread {
		public void run() {
			try {
				serverSocket = new ServerSocket(config.portTCPMessage);
				while (true) {
					Socket socket = serverSocket.accept();
					try {
						MessageThreadServer messageThreadServer = new MessageThreadServer(socket);
						lbReady.setText("Ready");
					} catch (Exception e) {
						lbReady.setText("Not Ready");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				lbReady.setText("Not Ready");
			}
		}
	}

	private class InitSocketTCPObject extends Thread {
		public void run() {
			try {
				objectSocket = new ServerSocket(config.portTCPObject);
				while (true) {
					Socket socket = objectSocket.accept();
					try {
						ObjectThead objectThead = new ObjectThead(socket);
					} catch (Exception e) {
						lbReady.setText("Not Ready");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				lbReady.setText("Not Ready");
			}
		}
	}

	private class MessageThreadServer extends Thread {
		private Socket socket;
		private DataInputStream din = null;

		public MessageThreadServer(Socket socket) throws Exception {
			this.socket = socket;
			socketClients.add(this.socket);
			start();
		}

		@SuppressWarnings("deprecation")
		public void run() {
			try {
				din = new DataInputStream(socket.getInputStream());
				String s = "";

				while (true) {
					s = din.readUTF();
					String[] arr = s.split("-");
					txtDisplay.setText(txtDisplay.getText() + s + "\n");
					for (int i = 0; i < socketClients.size(); i++) {
						if (listModelUser.get(i).getName().equals(arr[1])
								|| listModelUser.get(i).getName().equals(arr[0])) {
							DataOutputStream dout = new DataOutputStream(socketClients.get(i).getOutputStream());
							dout.writeUTF(s);
						}
					}
				}
			} catch (Exception e) {
				socketClients.remove(this.socket);
				try {
					this.socket.close();
				} catch (IOException e1) {
				}
				this.stop();
			}
		}

	}

	private class ObjectThead extends Thread {
		private Socket socket;
		private User user;

		public ObjectThead(Socket socket) {
			this.socket = socket;
			socketObjects.add(socket);
			this.start();
		}

		public void run() {
			try {
				while (true) {
					try {
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

						user = (User) ois.readObject();
						listModelUser.addElement(user);

						for (int i = 0; i < socketObjects.size(); i++) {
							ObjectOutputStream oos = new ObjectOutputStream(socketObjects.get(i).getOutputStream());
							oos.writeObject(listModelUser);

						}
						updateListUser(createListUsers(listModelUser));
					} catch (ClassNotFoundException e1) {

					} catch (IOException e2) {
						listModelUser.removeElement(user);
						socketObjects.remove(this.socket);
						for (int i = 0; i < socketObjects.size(); i++) {
							ObjectOutputStream oos = new ObjectOutputStream(socketObjects.get(i).getOutputStream());
							oos.writeObject(listModelUser);
							oos.flush();
						}
						socket.close();
						break;
					}
				}
				this.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private JList<User> createListUsers(DefaultListModel<User> listModelUser) {
		// create Jlist with model
		JList<User> list = new JList<User>(listModelUser);
		list.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		list.setBackground(SystemColor.controlLtHighlight);
		// set cell renderer
		list.setCellRenderer(new UserRenderer());
		return list;
	}

	private void updateListUser(JList<User> users) {
		listUser = users;
		pnListUser.removeAll();
		pnListUser.add(new JScrollPane(listUser));
		pnListUser.setVisible(false);
		pnListUser.setVisible(true);
	}
}
