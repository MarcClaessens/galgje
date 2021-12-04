package marcclaessens.galgje.ui.swing;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Reset {

	public static void main(String[] args) throws BackingStoreException {
		Preferences prefs = Preferences.userRoot().node(
				OptionsPanel.class.getName());
		prefs.removeNode();
		System.out.println("Done");
	}

}
