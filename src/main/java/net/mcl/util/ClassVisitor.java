package net.mcl.util;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * FileVisitor that looks for .class files.
 * @author marc
 */
class ClassVisitor implements FileVisitor<Path> {
		private final FileFilter filter;
		private final List<Path> classes = new ArrayList<Path>();

		/**
		 * Constructor.
		 * @param filter - the filter to apply (not null)
		 */
		public ClassVisitor(final FileFilter filter) {
			this.filter = filter;
		}


		/**
		 * Gets the files that could not be accessed.
		 * @return Iterator of Paths.
		 */
		public List<Path> getClasses() {
			return classes;
		}

	
		public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}


	
		public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
			if (filter == null || (file.toString().endsWith(".class") && filter.accept(file.toFile()))) {
				classes.add(file);
			}
			return FileVisitResult.CONTINUE;
		}

	
		public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
			if (!(exc instanceof AccessDeniedException)) {
				exc.printStackTrace();
				return FileVisitResult.TERMINATE;
			} else {
				return FileVisitResult.CONTINUE;
			}

		}

	
		public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
