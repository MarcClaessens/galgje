package net.mcl.galgje.ui.swing;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.mcl.galgje.Game;
import net.mcl.galgje.GameListener;
import net.mcl.galgje.SoundPlayer;
import net.mcl.galgje.ui.I18N;
import net.mcl.galgje.ui.KeyboardLayout;
import net.miginfocom.swing.MigLayout;

public class GameUI extends JFrame implements GameListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// default should be cross-platform look-and-feel
			e.printStackTrace();
		}
		GameUI gameUi = new GameUI();
		gameUi.setVisible(true);
		gameUi.optionsPanel.loadPrefs();
	}

	private final Game game;
	private final GameUI ui = this;
	private final I18N i18n = new I18N();

	private final WordPanel wordPanel = new WordPanel();
	private final KeyboardPanel keyboardPanel = new KeyboardPanel(ui);
	private final OptionsPanel optionsPanel = new OptionsPanel(ui);
	private final ProgressImagePanel progressImagePanel  = new ProgressImagePanel ();

	private boolean started;

	public GameUI() {
		super("Galgje 4.0");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		game = new Game(this);

		try {
			URL iconURL = getClass().getResource("/images/icon.png");
			ImageIcon icon = new ImageIcon(iconURL);
			setIconImage(icon.getImage());
		} catch (Exception e) {
			// annoying but not critical
			e.printStackTrace();
		}
	}

	public void applyFontSize(int value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					keyboardPanel.applyFontSize(value);
					wordPanel.applyFontSize(value);
					ui.pack();
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void applyKeyboardLayout(KeyboardLayout kbl) {
		keyboardPanel.apply(kbl);
		for (Character c : game.getGuessedChars()) {
			keyboardPanel.disableKey(c);
		}			
		pack();
	}

	@Override
	public void correctGuess(char c, String guessWord) {
		wordPanel.setText(guessWord);
	}

	private void init() {
		setSize(400, 150);
		initPanels();
		initKeyListener();
		this.pack();
	}

	private void initKeyListener() {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(e -> dispatchKeyEvent(e));
	}
	
	private boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_TYPED) {
			Character c = e.getKeyChar();
			if (started && c != KeyEvent.CHAR_UNDEFINED  && Character.isAlphabetic(c)) {
					ui.keyPressed(c);
			}
		}
		return false;
	}

	private void initPanels() {
		final JPanel realContent = new JPanel(new MigLayout("align center",
				"[grow, fill]", "[]"));

		
		CollapsablePanel cKeyboardPanel = new CollapsablePanel (keyboardPanel, i18n.getShowKeyboard(), i18n.getHideKeyboard(), true);
		CollapsablePanel cOptionsPanel = new CollapsablePanel (optionsPanel, i18n.getShowOptions(), i18n.getHideOptions(), false);
		CollapsablePanel cprogressPanel = new CollapsablePanel (progressImagePanel, i18n.getShowProgress(), i18n.getHideProgress(), true);

		realContent.add(wordPanel, "wrap");
		realContent.add(cKeyboardPanel, "wrap");
		realContent.add(cOptionsPanel, "wrap");
		realContent.add(cprogressPanel, "wrap");

		getContentPane().add(realContent);
	}

	public void keyPressed(Character c) {
		if (started) {
			keyboardPanel.disableKey(c);
			game.guess(c);
		}
	}

	@Override
	public void lost(String missedWord) {
		JOptionPane.showMessageDialog(ui, i18n.getLostMessage(missedWord));
		game.newGame();
		keyboardPanel.enableAllKeys();
		pack();
	}

	protected void newGame(String wordlist) {
		game.newGame(wordlist);
	}

	@Override
	public void newGameStarted(String guessWord) {
		wordPanel.setText(guessWord);
		started = true;
		progressImagePanel.showImage(0);
	}

	@Override
	public void won(String foundWord) {
		wordPanel.setText(foundWord);
		JOptionPane.showMessageDialog(ui, i18n.getWonMessage(foundWord));
		game.newGame();
		pack();
		keyboardPanel.enableAllKeys();
	}

	@Override
	public void wrongGuess(char c, int attempt, String guessWord) {
		wordPanel.setText(guessWord);
		progressImagePanel.showImage(attempt);
	}

	public SoundPlayer getSoundPlayer(){
		return game.getSoundPlayer();
	}
	
	public I18N getI18N() {
		return i18n;
	}
}