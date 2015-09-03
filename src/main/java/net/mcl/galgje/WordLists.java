package net.mcl.galgje;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.mcl.util.ResourceList;
import net.mcl.util.ResourceNameCleaner;

import org.apache.commons.io.IOUtils;

public class WordLists {
	
	private final Pattern listPattern = Pattern.compile(".*\\.list");
	private final List<String> files;
	private final Map<String, String> wordResources= new HashMap<>(4);

	public WordLists() {
		files = ResourceList.getResources(listPattern);
		for (String file : files) {
			wordResources.put(ResourceNameCleaner.clean(file), file);
		}
	}
	
	public List<String> getWordListNames() {
		List<String> l = new ArrayList<String>(wordResources.keySet());
		Collections.sort(l);
		return l;
	}

	public static List<String> readList(String resource) {
		List<String> words = new ArrayList<>(30000);
		
		InputStream is = null;
		try {
			is = ClassLoader.getSystemResourceAsStream(resource);
			if (is == null) {
				is = new FileInputStream(new File(resource));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("failed to find file " + resource);
		}
		try {
			List<String> list = IOUtils.readLines(is, "UTF-8");
			words = list.stream().filter(line -> line.length() > 6 && line.charAt(0) != '#').collect(Collectors.toList());;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("failed to read file " + resource);
		}
		
		return words;
	}

	public String getResourceName(String wordListName) {
		return wordResources.get(wordListName);
	}
	
}
