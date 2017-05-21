package video;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import config.Config;
import voice.PlayRingtone;
import voice.PlayerAudio;
import voice.RecorderAudio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;

public class VideoCall extends JFrame {

	private JPanel contentPane;
	private JButton btnCall, btnDeny;
	private static JLabel lbImageVideo = new JLabel();
	private static JLabel lbMyVideo;
	private String yourIP = "192.168.1.7";
	private InetAddress inetAddress;
	private VideoCapture camera;
	private Mat frame;
	private RecorderVideo recorderVideo;
	private PlayerVideo playerVideo;
	private RecorderAudio recorderAudio;
	private PlayerAudio playerAudio;
	private PlayRingtone playRingtone;
	private boolean agreedCalling= false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		VideoCall frame = new VideoCall("Xoapit","192.168.1.12");
	}
	

	public class PlayerVideo extends Thread {
		public DatagramSocket din = null;

		byte[] buffer = new byte[60000];
		private DatagramPacket packet;

		public PlayerVideo() {
			try {
				din = new DatagramSocket(Config.portUDPVideo);
			} catch (SocketException e) {
			}
		}

		@Override
		public void run() {
			int i = 0;
			packet = new DatagramPacket(buffer, buffer.length);
			while (true) {
				try {
					String myIP = Inet4Address.getLocalHost().getHostAddress();
					din.receive(packet);
					byte[] buffer_img = packet.getData();

					BufferedImage img = ImageIO.read(new ByteArrayInputStream(buffer_img));

					displayImage(img);
					System.out.println("V#" + i++);
				} catch (IOException ex) {
				}
			}
		}
		
		public void closeSocket(){
			din.close();
		}

		@Override
		public void finalize() {
			din.close();
		}
	}

	public class RecorderVideo extends Thread {

		byte byte_buff[] = new byte[60000];
		DatagramSocket dout = null;

		public void run() {
			int i = 0;
			try {
				dout = new DatagramSocket();
			} catch (SocketException e) {

			}
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			camera = new VideoCapture(0);
			frame = new Mat();
			camera.read(frame);
			if (!camera.isOpened()) {
				System.out.println("Error Camera!");
			} else {
				while (true) {
					try {
						// read image from Mat
						if (camera.read(frame)) {
							BufferedImage image = MatToBufferedImage(frame);
							ImageIcon imageIcon = new ImageIcon(image);
							ImageIcon imageMyVideo = new ImageIcon(
									imageIcon.getImage().getScaledInstance(140, 100, Image.SCALE_DEFAULT));
							lbMyVideo.setIcon(imageMyVideo);
							// convert bufferedImage to byte array
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(image, "jpg", baos);
							baos.flush();
							byte[] byte_arr = baos.toByteArray();

							DatagramPacket data = new DatagramPacket(byte_arr, byte_arr.length,
									InetAddress.getByName(yourIP), Config.portUDPVideo);
							System.out.println("Send V#" + i++);
							dout.send(data);
						}
					} catch (Exception ex) {

					}					
				}				
			}
		}
	}

	public VideoCall(String yourName, String yourIP) {
		this.yourIP=yourIP;
		setIconImage(Toolkit.getDefaultToolkit().getImage(VideoCall.class.getResource("/image/iconVideo.png")));
		setResizable(false);
		setTitle("Video Call");
		setBounds(200, 50, 645, 500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(agreedCalling) playRingtone(false);
				releaseMemory();
			}
		});

		JPanel panelVideo = new JPanel();
		panelVideo.setBackground(Color.DARK_GRAY);
		panelVideo.setBounds(0, 0, 650, 482);

		btnDeny = new JButton("");
		btnDeny.setBounds(347, 382, 89, 71);
		panelVideo.add(btnDeny);
		btnDeny.setBackground(null);
		btnDeny.setOpaque(false);
		btnDeny.setContentAreaFilled(false);
		btnDeny.setBorderPainted(false);
		btnDeny.setIcon(new ImageIcon(VideoCall.class.getResource("/image/calldeny.png")));
		btnDeny.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnDeny.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(agreedCalling) playRingtone(false);
				releaseMemory();
			}
		});

		btnCall = new JButton("");
		btnCall.setBounds(201, 382, 89, 71);
		panelVideo.add(btnCall);
		btnCall.setForeground(Color.DARK_GRAY);
		btnCall.setOpaque(false);
		btnCall.setContentAreaFilled(false);
		btnCall.setBorderPainted(false);
		btnCall.setIcon(new ImageIcon(VideoCall.class.getResource("/image/callphone.png")));
		btnCall.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnCall.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				agreedCalling=true;
				startRecorderVideo();
				playRingtone(false);
				startPlayerVideo();				
			}
		});

		lbMyVideo = new JLabel();
		lbMyVideo.setBounds(480, 21, 140, 100);
		lbMyVideo.setOpaque(true);
		panelVideo.add(lbMyVideo);
		lbMyVideo.setBackground(Color.DARK_GRAY);
		lbImageVideo.setBackground(Color.GRAY);
		lbImageVideo.setBounds(0, 0, 650, 480);
		panelVideo.add(lbImageVideo);
		contentPane.add(panelVideo);
		panelVideo.setLayout(null);
		panelVideo.setFocusable(true);
		this.setTitle("Video Call - "+yourName+" contacting...");
		this.show();	
	}

	public void startPlayerVideo() {
		playerVideo = new PlayerVideo();
		playerVideo.start();
		playerAudio= new PlayerAudio(Config.portUDPAudio);
		playerAudio.start();
		
		btnCall.setVisible(false);
		btnDeny.setBounds(300, 382, 89, 71);
	}
	
	public void startRecorderVideo(){
		recorderVideo= new RecorderVideo();
		recorderVideo.start();
		recorderAudio= new RecorderAudio(yourIP, Config.portUDPAudio);
		recorderAudio.start();
	}

	public void releaseMemory() {
		try {
			recorderVideo.stop();
			recorderAudio.stop();
			
			camera.release();
			playerVideo.closeSocket();
			playerVideo.stop();
			playerAudio.closeSocket();
			playerAudio.stop();
		} catch (Exception e) {

		}
		dispose();
	}

	public static void displayImage(BufferedImage img) {
		ImageIcon imageIcon = new ImageIcon(img);
		lbImageVideo.setIcon(imageIcon);
	}

	public BufferedImage MatToBufferedImage(Mat frame) {
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);
		return image;
	}
	
	public void playRingtone(boolean state){
		if(state){
			playRingtone= new PlayRingtone();
			playRingtone.start();
		}else{
			playRingtone.stop();
		}
	}
}
