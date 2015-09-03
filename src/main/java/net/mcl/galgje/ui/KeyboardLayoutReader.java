package net.mcl.galgje.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import net.mcl.util.ResourceList;


public class KeyboardLayoutReader {
	private final Map<String, KeyboardLayout> keyboardLayouts = new HashMap<>(8);
	
	public KeyboardLayoutReader() {
		Pattern kbPattern = Pattern.compile(".*_kb\\.properties");
		Collection <String> keyboardFiles = ResourceList.getResources(kbPattern);
		for (String s: keyboardFiles)
		{
			parse(s);
		}
	}

	private void parse(String s) {
		try {
			Properties props = new Properties();
			props.load(new InputStreamReader(new FileInputStream(new File(s))));
			parseProps(s, props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseProps(String resourceName, Properties props) {
		Map<String, String> names = new HashMap<>(10);
		List<char[]> keys = new ArrayList<>(10);
		int fontSize = 18;
		
		String sep = " ";
		
		for (String key: props.stringPropertyNames()) {
			if (key.startsWith("#")) {
				continue;
			}
			if (key.startsWith("name.")) {
				String lang = key.substring(key.indexOf(".")+1);
				names.put(lang, props.getProperty(key));
			}
			if (key.equals("separator")) {
				sep = props.getProperty(key);
			}
			if (key.equals("fontsize") || key.equals("fontSize")) {
				fontSize = Integer.parseInt(props.getProperty(key));
			}

		}
		for (int i=0; i<20; i++) {
			String line = props.getProperty("line." + i);
			if (line != null) {
				keys.add(parseKeys(line, sep));
			}
		}
		
		KeyboardLayout bkl = new KeyboardLayout(resourceName, names, keys, fontSize);
		keyboardLayouts.put(bkl.getName(), bkl);
	}
	

	private char[] parseKeys(String line, String sep) {
		String split [] = line.split(sep);
		char [] c = new char[split.length];
		for (int i=0; i<split.length; i++) {
			c[i] = split[i].charAt(0);			
		}
		return c;
	}

	public List<String> getKeyboardLayoutNames() {
		List<String> l = new ArrayList<>(keyboardLayouts.keySet());
		Collections.sort(l);
		return l;
	}
	
	public KeyboardLayout getKeyboardLayout(String name) {
		return keyboardLayouts.get(name);
	}
	
	public static void main(String[] args) {
		System.out.println(new KeyboardLayoutReader().keyboardLayouts);
	}

}
