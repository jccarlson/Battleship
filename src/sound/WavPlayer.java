package sound;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;


/**
 * Provides a simple player for *.wav files.
 * 
 * @author Jason Carlson <jccarlson@miners.utep.edu>
 * @version 1.1
 * @since 2015-09-03
 */
public class WavPlayer implements LineListener {

	private boolean isComplete = false;

	/**
	 * Plays the *.wav file given in the parameter.
	 * 
	 * @param filename
	 *            Filename must be a build-path resource. Format:
	 *            "/filename.wav"
	 */
	public void playClip(String filename) {

		try {
			isComplete = false;
			AudioInputStream aStream = AudioSystem.getAudioInputStream(WavPlayer.class.getResource(filename));
			//AudioFormat f = aStream.getFormat();
			//DataLine.Info i = new DataLine.Info(Clip.class, f);
			Clip c = (Clip) AudioSystem.getClip();

			c.addLineListener(this);

			c.open(aStream);
			c.start();
			while (!isComplete) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("\nCouldn't Sleep while playing audio. (Interrupted Exception)");
				}
			}
			c.close();

		} catch (UnsupportedAudioFileException e) {
			System.out.println("\nERROR: Can't play file: " + filename + ". (UnsupportedAudioFileException)");
		} catch (IOException e) {
			System.out.println("\nERROR: Can't play file: " + filename + ". (IOException)");
		} catch (LineUnavailableException e) {
			System.out.println("\nERROR: Can't play file: " + filename + ". (LineUnavailableException)");
		}

	}

	/**
	 * needed for {@code implements LineListener}.
	 * <p>
	 * Lets the player know when playback is complete.
	 */
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.STOP) {
			isComplete = true;
		}
	}

}
