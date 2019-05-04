package index.helper;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestHelper {
	
	public static Path getPathToResource(String fname) throws URISyntaxException {
		Path pathToResource = Paths.get(TestHelper.class.getResource(fname).toURI());
		
		return pathToResource;
	}

}
