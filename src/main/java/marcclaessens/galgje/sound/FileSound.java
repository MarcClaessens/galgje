package marcclaessens.galgje.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileSound implements Sound {
	private final File file;
	private final SoundFormat type;
	
	public FileSound(File file) {
		this.file = file;
		String resource = file.getName();
		String ext = resource.substring(resource.lastIndexOf('.') +1);
		type = SoundFormat.fromExtension(ext);

	}

	@Override
	public InputStream getSoundStream() {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SoundFormat getType() {
		return type;
	}
}
