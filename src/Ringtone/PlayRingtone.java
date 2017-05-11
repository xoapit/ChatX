package Ringtone;

import java.io.InputStream;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

public class PlayRingtone extends Thread {
	public void run() {
		while (true) {
			try {
				InputStream in = getClass().getResourceAsStream("/audio/nhac2.wav");
				// create an audioStream from the inputStream
				//AudioStream audioStream = new AudioStream(in);
				// play audio with audioPlayer
				//AudioPlayer.player.start(audioStream);
				Thread.sleep(7000);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
}