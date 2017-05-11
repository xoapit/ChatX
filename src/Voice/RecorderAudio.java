package Voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class RecorderAudio extends Thread {
	private TargetDataLine audio_in;
	byte byte_buff[] = new byte[512];
	byte buffer[] = new byte[512];
	private DatagramSocket dout = null;
	private String yourIP;
	private int portAddress;
	
	public RecorderAudio(String yourIP, int portAddress) {
		this.yourIP=yourIP;
		this.portAddress=portAddress;
		AudioFormat format = getaudioformat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		try {
			dout = new DatagramSocket();
			audio_in = (TargetDataLine) AudioSystem.getLine(info);
			audio_in.open(format);
			audio_in.start();
		} catch (LineUnavailableException e) {
			System.out.println(e.getMessage());
		} catch (SocketException e) {
		}
	}

	public void run() {
		int i = 0;
		while (true) {
			try {
				audio_in.read(byte_buff, 0, byte_buff.length);
				DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, InetAddress.getByName(yourIP),
						portAddress);
				System.out.println("Send #" + i++);
				dout.send(data);
			} catch (IOException ex) {
				System.out.println("123loi");
			}
		}

	}
	
	public static AudioFormat getaudioformat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
}