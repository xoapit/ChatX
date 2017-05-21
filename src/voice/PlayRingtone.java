package voice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

public class PlayRingtone extends Thread {
	public void run() {
		while (true) {
			try {
				String fileName="/file/audio/nhac2.wav";
				AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(PlayRingtone.class.getResource(fileName));
				Clip clip= AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				Thread.sleep(7000);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
}