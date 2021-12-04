package marcclaessens.galgje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

	private static final int MAX_ATTEMPTS = 8;
	
	private String wordListResource = null;
	private List<String> words = null;
	private final GameListener listener;
	private final SoundPlayer soundPlayer = new SoundPlayer();
	private int word = 0;
	private int invalidGuesses = 0;
	private final char blank;
	
	private String currentWord = "";
	private StringBuilder guessedWord = null;
	private final List<Character> guessedChars = new ArrayList<>(20);
	
	public Game(final GameListener listener) {
		this(listener, '\u25cf');
	}
	
	public Game(final GameListener listener, final char blank) {
		this.listener = listener;
		this.blank = blank;
	}

	public void newGame(final String wordListResource) {
		if (! wordListResource.equals(this.wordListResource)) {
			this.wordListResource = wordListResource;
			words = WordLists.readList(wordListResource);
			Collections.shuffle(words);
			word = -1;
		}
		newGame();
	}
	
	public void newGame() {
		invalidGuesses = 0;
		word += 1;
		if (word >= words.size()) {
			word = 0;
		}
		currentWord = words.get(word);
		
		guessedWord = new StringBuilder(currentWord.length());
		for (int i=0; i<currentWord.length(); i++) {
			guessedWord.append(blank);	
		}
		guessedChars.clear();
		
		listener.newGameStarted(guessedWord.toString());
	}
	
	public void guess (final char c) {
		if (invalidGuesses < MAX_ATTEMPTS) {
			if (! guessedChars.contains(c)) {
				soundPlayer.playGuess();
				guessedChars.add(c);
				if (currentWord.indexOf(c) > -1) {
					updateGuessedWord(c);
					listener.correctGuess(c, guessedWord.toString());
					if (currentWord.equals(new String(guessedWord))) {
						soundPlayer.playGameWon();
						listener.won(guessedWord.toString());
					}
				} else {
					invalidGuesses++;
					listener.wrongGuess(c, invalidGuesses, guessedWord.toString());
					if (invalidGuesses >= MAX_ATTEMPTS) {
						soundPlayer.playGameLost();
						listener.lost(currentWord);
					} else {
						
					}
				}
			}
		}		
	}
	
	public List<Character> getGuessedChars() {
		return guessedChars;
	}

	private void updateGuessedWord(char c) {
		for (int i=0; i<currentWord.length(); i++) {
			if (currentWord.charAt(i) == c) {
				guessedWord.setCharAt(i, c);
			}
		}
	}

	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}
}
