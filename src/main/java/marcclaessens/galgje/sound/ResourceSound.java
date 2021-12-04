package marcclaessens.galgje.sound;

import java.io.InputStream;

public class ResourceSound implements Sound { 
	private final String resource;
	private final SoundFormat type;
	
	public static ResourceSound GUESS_DEFAULT = new ResourceSound("/sounds/keyclick.mp3");
	public static ResourceSound WON_DEFAULT = new ResourceSound("/sounds/applause.mp3");
	public static ResourceSound LOST_DEFAULT = new ResourceSound("/sounds/scream.mp3");
	
	private ResourceSound(String resource) {
		this.resource = resource;
		String ext = resource.substring(resource.lastIndexOf('.') +1);
		type = SoundFormat.fromExtension(ext);
	}

	@Override
	public InputStream getSoundStream() {
		return getClass().getResourceAsStream(resource);
	}

	@Override
	public SoundFormat getType() {
		return type;
	}
}
