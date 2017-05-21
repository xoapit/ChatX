package voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PlayerAudio extends Thread {
	private DatagramSocket din = null;
	private SourceDataLine audio_out;
	byte[] buffer = new byte[512];
	private DatagramPacket incoming;
	private int portAddress;
	public PlayerAudio(int portAddress) {
		try {
			this.portAddress=portAddress;
			din = new DatagramSocket(this.portAddress);
			AudioFormat format = getaudioformat();

			DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
			if (!AudioSystem.isLineSupported(info_out)) {
				System.out.println("not suport");
				System.exit(0);
			}

			audio_out = (SourceDataLine) AudioSystem.getLine(info_out);
			audio_out.open(format);
			audio_out.start();
		} catch (SocketException e) {
			// e.printStackTrace();
		} catch (LineUnavailableException e) {

		}
	}

	public void closeSocket(){
		audio_out.close();
		din.close();
	}
	
	@Override
	public void run() {
		int i = 0;
		incoming = new DatagramPacket(buffer, buffer.length);
		while (true) {
			try {
				String myIP = Inet4Address.getLocalHost().getHostAddress();
				din.receive(incoming);
				if ((incoming.getAddress().getHostAddress().equals(myIP) == false)) {
					buffer = incoming.getData();
					audio_out.write(buffer, 0, buffer.length);
					System.out.println("#" + i++);
				}
			} catch (IOException ex) {
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