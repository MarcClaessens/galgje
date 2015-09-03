package net.mcl.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * list resources available from the classpath Credit to / Source :
 * http://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
 * (modified for auto-closable ZipFile + remove a FindBugs warning)
 */
public class ResourceList {

	/**
	 * for all elements of java.class.path get a Collection of resources Pattern
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param pattern
	 *            the pattern to match
	 * @return the resources in the order they are found
	 */
	public static List<String> getResources(final Pattern pattern) {
		final List<String> retval = new ArrayList<String>();
		final String classPath = System.getProperty("java.class.path", ".");
		final String[] classPathElements = classPath.split(":");
		for (final String element : classPathElements) {
			retval.addAll(getResources(element, pattern));
		}
		return retval;
	}

	private static Collection<String> getResources(final String element,
			final Pattern pattern) {
		final ArrayList<String> retval = new ArrayList<String>();
		final File file = new File(element);
		if (file.isDirectory()) {
			retval.addAll(getResourcesFromDirectory(file, pattern));
		} else {
			retval.addAll(getResourcesFromJarFile(file, pattern));
		}
		return retval;
	}

	private static Collection<String> getResourcesFromJarFile(final File file,
			final Pattern pattern) {
		final ArrayList<String> retval = new ArrayList<String>();
		
		try (ZipFile zf = new ZipFile(file)) {
			final Enumeration<? extends ZipEntry> e = zf.entries();
			while (e.hasMoreElements()) {
				final ZipEntry ze = (ZipEntry) e.nextElement();
				final String fileName = ze.getName();
				final boolean accept = pattern.matcher(fileName).matches();
				if (accept) {
					retval.add(fileName);
				}
			}
		} catch (final IOException e1) {
			throw new Error(e1);
		}
		return retval;
	}

	private static Collection<String> getResourcesFromDirectory(
			final File directory, final Pattern pattern) {
		final ArrayList<String> retval = new ArrayList<String>();
		final File[] fileList = directory.listFiles();
		if (fileList != null) {
			for (final File file : fileList) {
				if (file.isDirectory()) {
					retval.addAll(getResourcesFromDirectory(file, pattern));
				} else {
					try {
						final String fileName = file.getCanonicalPath();
						final boolean accept = pattern.matcher(fileName).matches();
						if (accept) {
							retval.add(fileName);
						}
					} catch (final IOException e) {
						throw new Error(e);
					}
				}
			}
		}
		return retval;
	}
}
