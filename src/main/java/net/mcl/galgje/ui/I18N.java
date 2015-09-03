package net.mcl.galgje.ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.mcl.util.UTF8Control;

public class I18N {

	private final static String FILENAME = "messages";
	private final ResourceBundle bundle;
	
	private final MessageFormat wonGame;
	private final MessageFormat lostGame;
	private final String newGame;
	private final String hideKeyboard;
	private final String showKeyboard;
	private final String hideOptions;
	private final String showOptions;
	private final String hideProgress;
	private final String showProgress;
	
	
	
	public I18N() {
		ResourceBundle b;
		try {
			b = ResourceBundle.getBundle(FILENAME, new UTF8Control());
		} catch (MissingResourceException mre) {
			b = ResourceBundle.getBundle(FILENAME, Locale.ENGLISH ,new UTF8Control());
		}
		bundle = b;
		wonGame = new MessageFormat(bundle.getString("game.won"));
		lostGame = new MessageFormat(bundle.getString("game.lost"));
		
		newGame = bundle.getString("game.new");
		hideKeyboard = bundle.getString("mnu.keyboard.hide");
		showKeyboard = bundle.getString("mnu.keyboard.show");

		hideOptions = bundle.getString("mnu.options.hide");
		showOptions= bundle.getString("mnu.options.show");
		hideProgress = bundle.getString("mnu.progress.hide");
		showProgress =  bundle.getString("mnu.progress.show");
	}

	public String getWonMessage(String foundWord) {
		return wonGame.format(new String[] {foundWord});
	}
	
	public String getLostMessage(String missedWord) {
		return lostGame.format(new String[] {missedWord});
	}

	public String getNewGame() {
		return newGame;
	}

	public String getHideKeyboard() {
		return hideKeyboard;
	}

	public String getShowKeyboard() {
		return showKeyboard;
	}

	public String getHideOptions() {
		return hideOptions;
	}

	public String getShowOptions() {
		return showOptions;
	}

	public String getShowProgress() {
		return showProgress;
	}
	
	public String getHideProgress() {
		return hideProgress;
	}	
}
