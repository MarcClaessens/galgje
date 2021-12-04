package marcclaessens.galgje;

import java.io.InputStream;
import marcclaessens.galgje.sound.ResourceSound;
import marcclaessens.galgje.sound.Sound;
import marcclaessens.galgje.sound.SoundType;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javazoom.jl.player.Player;

public class SoundPlayer {

	private Sound guess = ResourceSound.GUESS_DEFAULT;

	private Sound won = ResourceSound.WON_DEFAULT;

	private Sound lost = ResourceSound.LOST_DEFAULT;

	public SoundPlayer() {
	}

	public void playGuess() {
		play(guess);
	}

	public void playGameWon() {
		play(won);
	}

	public void playGameLost() {
		play(lost);
	}

	public void setGuess(Sound guess) {
		this.guess = guess;
	}

	public void setWon(Sound won) {
		this.won = won;
	}

	public void setLost(Sound lost) {
		this.lost = lost;
	}

	public void setSound(SoundType type, Sound sound) {
		switch (type) {
		case GUESS:
			setGuess(sound);
			break;
		case WON:
			setWon(sound);
			break;
		case LOST:
			setLost(sound);
			break;
		default:
			throw new IllegalArgumentException("Invalid sound type " + type);
		}
	}

	private void play(final Sound sound) {
		if (sound != null) {
			new Thread() {
				public void run() {
					try (InputStream is = sound.getSoundStream()) {
						switch (sound.getType()) {
						case MP3:
							Player player = new Player(is);
							player.play();
							break;
						case WAV:
						case AU:
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}
