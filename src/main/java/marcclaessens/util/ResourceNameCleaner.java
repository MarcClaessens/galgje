package marcclaessens.util;

public class ResourceNameCleaner {

	private ResourceNameCleaner() {
    }

	public static String resourceToClass(String resourceName) {
		return resourceName.replace("/", ".").replace(".class", "");
	}
	
	public static String clean(String name, String ... patterns) {
		String x = name;
		if (x.indexOf("\\") > -1) {
			x = x.substring(x.lastIndexOf("\\") + 1);
		}
		if (x.indexOf("/") > -1) {
			x = x.substring(x.lastIndexOf("/") + 1);
		}
		if (x.indexOf(".") > -1) {
			x = x.substring(0, x.lastIndexOf("."));
		}
		
		for (String pattern : patterns) {
			if (x.endsWith(pattern)) {
				x = x.substring(0, x.length() - pattern.length());
			} else {
				x = x.replace(pattern, "");
			}
		}
		
		return x;
	}
}
