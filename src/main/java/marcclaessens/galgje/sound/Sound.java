package marcclaessens.galgje.sound;

import java.io.InputStream;

public interface Sound {
	InputStream getSoundStream();
	SoundFormat getType();
}
