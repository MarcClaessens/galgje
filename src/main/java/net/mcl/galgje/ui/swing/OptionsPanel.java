package net.mcl.galgje.ui.swing;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import net.mcl.galgje.WordLists;
import net.mcl.galgje.sound.FileSound;
import net.mcl.galgje.sound.ResourceSound;
import net.mcl.galgje.sound.Sound;
import net.mcl.galgje.sound.SoundFormat;
import net.mcl.galgje.sound.SoundType;
import net.mcl.galgje.ui.KeyboardLayout;
import net.mcl.galgje.ui.KeyboardLayoutReader;
import net.miginfocom.swing.MigLayout;

public class OptionsPanel extends JPanel {

	private static enum SoundValue {
		OFF, DEFAULT, CUSTOM;
	}
	
	private static final String PREFS_WORDLIST = "prefs.wordlist";
	private static final String PREFS_LAF = "prefs.laf";
	private static final String PREFS_FONTSIZE = "prefs.fontsize";
	private static final String PREFS_KEYBOARD = "prefs.keyboard";
	
	private static final String PREFS_SOUND_GUESS = "prefs.sound.guess";
	private static final String PREFS_SOUND_WON = "prefs.sound.won";
	private static final String PREFS_SOUND_LOST = "prefs.sound.lost";
	
	private static final long serialVersionUID = 1L;

	private final WordLists wordLists = new WordLists();
	private final KeyboardLayoutReader reader = new KeyboardLayoutReader();
	private final LafScanner lafScanner = new LafScanner();
	
	private final JComboBox<String> wordList = new JComboBox<>();
	private final JComboBox<String> keyboardNameList = new JComboBox<>();
	private final JComboBox<Integer> fontSizeList = new JComboBox<>();
	private final JComboBox<String> lafList = new JComboBox<>();
	
	private final JPanel contentSound; 
	
	private final JRadioButton sndClickOff = new JRadioButton("Off");
	private final JRadioButton sndClickDefault = new JRadioButton("Default", true);
	private final JRadioButton sndClickCustom = new JRadioButton("Custom");
	private final JButton sndClickCustomBrowse = new JButton("...");

	private final JRadioButton sndWonOff = new JRadioButton("Off");
	private final JRadioButton sndWonDefault = new JRadioButton("Default", true);
	private final JRadioButton sndWonCustom = new JRadioButton("Custom");
	private final JButton sndWonCustomBrowse = new JButton("...");
	
	private final JRadioButton sndLostOff = new JRadioButton("Off");
	private final JRadioButton sndLostDefault = new JRadioButton("Default", true);
	private final JRadioButton sndLostCustom = new JRadioButton("Custom");
	private final JButton sndLostCustomBrowse = new JButton("...");
	
	private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
	private JFileChooser soundChooser = null;
	
	private final GameUI ui;
	
	public OptionsPanel(GameUI ui) {
		this.ui = ui;
		setName("OptionsPanel");
		setLayout(new MigLayout("align center", "[grow, fill]", "[]"));
		
		final JPanel content1 = new JPanel(new MigLayout("align center", "[grow, fill]", "[]"));
		contentSound = new JPanel(new MigLayout("align center", "[grow, fill]", "[]"));
		
		initWordList();
        initKeyboardNameList();
        initFontSizeList();
        initLafList();
		initSoundPanel();
		
        content1.add(wordList);
        content1.add(keyboardNameList);
        content1.add(fontSizeList);
        content1.add(lafList);

        add(content1, "wrap");
        add(contentSound);

	}

	private void initSoundPanel() {
		
		ButtonGroup clickGroup = new ButtonGroup();
		ButtonGroup wonGroup = new ButtonGroup();
		ButtonGroup lostGroup = new ButtonGroup();
		
		sndClickCustomBrowse.addActionListener(e -> selectCustomSound(sndClickCustom));
		sndWonCustomBrowse.addActionListener(e -> selectCustomSound(sndWonCustom));
		sndLostCustomBrowse.addActionListener(e -> selectCustomSound(sndLostCustom));
		
		initSoundGroup(clickGroup, sndClickOff, sndClickDefault, sndClickCustom, SoundType.GUESS);
		initSoundGroup(wonGroup, sndWonOff, sndWonDefault, sndWonCustom, SoundType.WON);
		initSoundGroup(lostGroup, sndLostOff, sndLostDefault, sndLostCustom, SoundType.LOST);
		
		final JPanel clickSound = new JPanel();
		final JPanel wonSound = new JPanel();
		final JPanel lostSound = new JPanel();

		clickSound.setBorder(new TitledBorder("Click sound"));
		addSoundButtons(clickSound, sndClickOff, sndClickDefault, sndClickCustom, sndClickCustomBrowse);
		wonSound.setBorder(new TitledBorder("Won sound"));
		addSoundButtons(wonSound, sndWonOff, sndWonDefault, sndWonCustom, sndWonCustomBrowse);
		lostSound.setBorder(new TitledBorder("Lost sound"));
		addSoundButtons(lostSound, sndLostOff, sndLostDefault, sndLostCustom, sndLostCustomBrowse);
		
		contentSound.add(clickSound, "wrap");
		contentSound.add(wonSound, "wrap");
		contentSound.add(lostSound, "wrap");
        
		add(contentSound);		
	}

	private void selectCustomSound(JRadioButton sndCustom) {
		JFileChooser chooser = getSoundChooser();
		 int returnVal = chooser.showOpenDialog(ui);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File f = chooser.getSelectedFile();
	    	sndCustom.putClientProperty("file", f);
	    	sndCustom.setSelected(false);
	    	sndCustom.setSelected(true);	    	
	    }		
	}

	private void addSoundButtons(JPanel panel, JRadioButton b1, JRadioButton b2, JRadioButton b3, JButton b4) {
		panel.add(b1);
		panel.add(b2);
		panel.add(b3);
		panel.add(b4);
	}

	private void initSoundGroup(ButtonGroup group, JRadioButton sndOff, JRadioButton sndDefault, JRadioButton sndCustom, SoundType type) {
		group.add(sndOff);
		group.add(sndDefault);
		group.add(sndCustom);
		
		sndOff.addChangeListener( e -> soundChanged(e, type, SoundValue.OFF));
		sndDefault.addChangeListener( e -> soundChanged(e, type, SoundValue.DEFAULT));
		sndCustom.addChangeListener( e -> soundChanged(e, type, SoundValue.CUSTOM));
	}

	private void soundChanged(ChangeEvent e, SoundType type, SoundValue value ) {
		switch (value) {
		case CUSTOM :
			JRadioButton btn = (JRadioButton) e.getSource();
			File src = (File) btn.getClientProperty("file");
			if (src != null && src.exists()) {
				changeSound(type, src.getAbsolutePath(), new FileSound(src));
			}
			break;
		case OFF:
			changeSound(type, value.toString(), null);
			break;
		case DEFAULT:
			Sound sound = null;
			switch(type) {
			case GUESS : 
				sound = ResourceSound.GUESS_DEFAULT; 
				break;
			case WON : 
				sound = ResourceSound.WON_DEFAULT; 
				break;
			case LOST : 
				sound = ResourceSound.LOST_DEFAULT; 
				break;
			}
			changeSound(type, value.toString(), sound);

		}
	}

	
	public void changeSound(SoundType type, String prefsValue, Sound sound) {
		switch(type) {
		case GUESS : 
			prefs.put(PREFS_SOUND_GUESS, prefsValue);
			break;
		case WON : 
			prefs.put(PREFS_SOUND_WON, prefsValue);
			break;
		case LOST : 
			prefs.put(PREFS_SOUND_LOST, prefsValue);
			break;
		}
		ui.getSoundPlayer().setSound(type, sound);
	}
	
	public void loadPrefs() {
		String prefWord = prefs.get(PREFS_WORDLIST, null);
		if (prefWord != null) {
			wordList.setSelectedItem(prefWord);
			wordList.actionPerformed(new ActionEvent(wordList, 0, null));
		} else {
			wordList.setSelectedIndex(0);
			wordList.actionPerformed(new ActionEvent(wordList, 0, null));
		}
		String prefLaf = prefs.get(PREFS_LAF, null);
		if (prefLaf != null) {
			lafList.setSelectedItem(prefLaf);
			lafList.actionPerformed(new ActionEvent(lafList, 0, null));
		} else {
			lafList.setSelectedIndex(0);
			lafList.actionPerformed(new ActionEvent(lafList, 0, null));
		}
		String prefKeyboard= prefs.get(PREFS_KEYBOARD, null);
		if (prefKeyboard != null) {
			keyboardNameList.setSelectedItem(prefKeyboard);
			keyboardNameList.actionPerformed(new ActionEvent(keyboardNameList, 0, null));
		} else {
			keyboardNameList.setSelectedIndex(0);
			keyboardNameList.actionPerformed(new ActionEvent(keyboardNameList, 0, null));
		}
		int prefFontsize = prefs.getInt(PREFS_FONTSIZE, 18);
		fontSizeList.setSelectedItem(prefFontsize);
		fontSizeList.actionPerformed(new ActionEvent(fontSizeList, 0, null));
		
		String guessSound = prefs.get(PREFS_SOUND_GUESS, SoundValue.DEFAULT.toString());
		String wonSound = prefs.get(PREFS_SOUND_WON, SoundValue.DEFAULT.toString());
		String lostSound = prefs.get(PREFS_SOUND_LOST, SoundValue.DEFAULT.toString());

		loadPrefSound(guessSound, sndClickOff, sndClickDefault, sndClickCustom);
		loadPrefSound(wonSound, sndWonOff, sndWonDefault, sndWonCustom);
		loadPrefSound(lostSound, sndLostOff, sndLostDefault, sndLostCustom);
	}

	private void loadPrefSound(String guessSound, JRadioButton sndOff, JRadioButton sndDefault, JRadioButton sndCustom) {
		if (guessSound.equals(SoundValue.OFF.toString())) {
			sndOff.setSelected(false);
			sndOff.setSelected(true);
		} else if (! guessSound.equals(SoundValue.DEFAULT.toString()) && new File(guessSound).exists()) {
			sndCustom.putClientProperty("file", new File(guessSound));
			sndCustom.setSelected(false);
			sndCustom.setSelected(true);
		} else {
			sndDefault.setSelected(false);
			sndDefault.setSelected(true);
		}
	}

	private void initWordList() {
		for (String s: wordLists.getWordListNames()) {
			wordList.addItem(s);
		}
		wordList.addActionListener(e -> actionPerformedOnWordList(e));
	}

	private void actionPerformedOnWordList(ActionEvent e) {
		String wordListName = ((String) wordList.getSelectedItem());
		String resource = wordLists.getResourceName(wordListName);
        ui.newGame(resource);
        prefs.put(PREFS_WORDLIST, wordListName);
	}
	
	private void initLafList() {
		for (String s: lafScanner.getLafNames()) {
			lafList.addItem(s);
		}
		
		lafList.addActionListener(e -> actionPerformedOnLafList(e));
	}
	
	private void actionPerformedOnLafList(ActionEvent e) {
		String value = ((String) lafList.getSelectedItem()); 
        new LafChanger().change(lafScanner.getLafClassname(value), ui);
        ui.repaint();
        prefs.put(PREFS_LAF, value);
	}

	private void initFontSizeList() {
        final int [] sizes = new int [] {8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 24, 26, 28, 32, 36, 40, 48, 56, 64, 72};
        for (int s: sizes) {
        	fontSizeList.addItem(s);
        }        
        fontSizeList.addActionListener(e -> actionPerformedOnFontSizeList(e));
	}
	
	private void actionPerformedOnFontSizeList(ActionEvent e) {
		int value = ((Integer) fontSizeList.getSelectedItem()).intValue(); 
        ui.applyFontSize(value);
        prefs.putInt(PREFS_FONTSIZE, value);
	}

	private void initKeyboardNameList() {
        for (String kbn: reader.getKeyboardLayoutNames()) {
        	keyboardNameList.addItem(kbn);
        }
        keyboardNameList.addActionListener(e -> actionPerformedOnKeyboardNameList(e));
	}	
	
	private void actionPerformedOnKeyboardNameList(ActionEvent e) {
		String value = (String) keyboardNameList.getSelectedItem();
		KeyboardLayout kbl = reader.getKeyboardLayout(value);
        ui.applyKeyboardLayout(kbl);
        prefs.put(PREFS_KEYBOARD, value);
	}

	public JFileChooser getSoundChooser() {
		if (soundChooser == null) {
			soundChooser = new JFileChooser();
			soundChooser.setAcceptAllFileFilterUsed(false);
			soundChooser.setFileFilter(SoundFormat.getFileFilter());
			soundChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return soundChooser;
	}

}
