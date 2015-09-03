package net.mcl.galgje.ui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.UIManager;

import net.mcl.util.ResourceList;
import net.mcl.util.ResourceNameCleaner;

/**
 * Returns a list of available Look And Feel implementations.
 * @author marc
 */
public final class LafScanner {
	
	private final Pattern lafPattern = Pattern.compile(".*LookAndFeel\\.class");
	private final Map<String, String> map = new HashMap<String, String>();

	
	
	public LafScanner() {
		map.put("System default", UIManager.getSystemLookAndFeelClassName());
		map.put("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");

		List<String> l = ResourceList.getResources(lafPattern);
		for (String laf : l) {
			String key = ResourceNameCleaner.clean(laf, "LookAndFeel");
			String clazzName = ResourceNameCleaner.resourceToClass(laf);
			map.put(key, clazzName);
		}
	}
	
	/**
	 * Returns a list of available Look And Feel names.
	 * @return list with names 
	 */
	public List<String> getLafNames() {
		List<String> l = new ArrayList<> (map.keySet());
		Collections.sort(l);
		return l;
	}
	
	public String getLafClassname(String refName) {
		return map.get(refName);
	}
}
