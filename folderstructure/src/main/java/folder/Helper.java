package folder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {

	public static List<String> readStringListFromPath(Path path) throws IOException  {
		List<String> listOfLines = Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.toList());

		return listOfLines;
	}
	
}
