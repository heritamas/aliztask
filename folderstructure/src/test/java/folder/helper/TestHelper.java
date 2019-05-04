package folder.helper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestHelper {
	
	public static List<String> readStringListFromResource(String fname) throws URISyntaxException, IOException {
		Path pathToResource = getPathToResource(fname);
		List<String> listOfFolders = Files.lines(pathToResource, StandardCharsets.UTF_8).collect(Collectors.toList());
		
		return listOfFolders;
	}
	
	private static Path getPathToResource(String fname) throws URISyntaxException {
		Path pathToResource = Paths.get(TestHelper.class.getResource(fname).toURI());
		
		return pathToResource;
	}

}
