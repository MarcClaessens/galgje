package marcclaessens.galgje.ui;

import marcclaessens.util.ResourceNameCleaner;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * immutable keyboard layout, read from file.
 */
public class KeyboardLayout {
	private final String internalName;
	private final Map<String, String> names;
	private final List<char[]> keys;
	
	public KeyboardLayout(String internalName, Map<String, String> names, List<char[]> keys, int fontSize) {
		this.internalName = internalName;
		this.names = names;
		this.keys = Collections.unmodifiableList(keys);
	}
	
	public String getName() {
		String lang = Locale.getDefault().getLanguage();
		if (lang != null && names.keySet().contains(lang)) {
			return names.get(lang);
		}
		// if your own language is not available, use English
		if (names.keySet().contains("en")) {
			return names.get("en");
		}
		// last option
		return ResourceNameCleaner.clean(internalName, "_kb");
	}

	public List<char[]> getRows () {
		return Collections.unmodifiableList(keys);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
