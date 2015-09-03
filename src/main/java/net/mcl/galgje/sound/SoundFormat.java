package net.mcl.galgje.sound;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public enum SoundFormat {
	WAV, MP3, AU;
	
	static final FileFilter FILTER = new FileNameExtensionFilter("Audio files", "mp3", "wav", "au");
	
	static SoundFormat fromExtension(String extension) {
		switch (extension.toLowerCase()) {
		case "mp3" : return MP3;
		case "wav" : return WAV;
		case "au" : return AU;
		default : throw new IllegalArgumentException("Unsupported file extension " + extension + ". Expected wav, mp3 or au file.");
		}
	}
	
	public static FileFilter getFileFilter() {
		return FILTER;
	}
}
