package net.mcl.galgje;

public interface GameListener {

	void newGameStarted(String guessWord);
	void won(String foundWord);
	void correctGuess(char c, String guessWord);
	void wrongGuess(char c, int attempt, String guessWord);
	void lost(String missedWord);
	
}
