package index;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexHandler 
{
	public static List<String> readStringListFromPath(Path path) throws IOException  {
		List<String> listOfLines = Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.toList());

		return listOfLines;
	}
	
	public static void writeStringsToFile(Path path, Iterable<String> lines) throws IOException {
		
		Files.write(
				path,
				lines,
				StandardCharsets.UTF_8, 
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);	
	}

	Path path;
	SortedMap<String, List<Integer>> index = new TreeMap<>();
	
	
	public IndexHandler(String fileName) {
		super();
		this.path = Paths.get(fileName); 
	}

	public IndexHandler(Path path) {
		super();
		this.path = path; 
	}

	public void indexAFile() throws IOException {
		
		List<String> lines = readStringListFromPath(path);

		IntStream.range(0, lines.size())
		.forEach( i -> tokenizeLine(i+1, lines.get(i)));
		
		writeIndexFile();
	}
	
	private void tokenizeLine(Integer idx, String line) {
		
		// split on whitespace and punctuation
		String[] tokens = line.split("[\\s\\p{Punct}]+");
		
		// save to index map
		for (String token: tokens) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(idx);
			index.merge(token, list, (l1, l2) -> { l1.addAll(l2); return l1; });
		}
	}
	
	private void writeIndexFile() throws IOException {
		
		Path indexPath = path.resolveSibling(
				path.getFileName().toString().replaceFirst("\\.txt$", "-index.txt"));
		
		//String indexFileName = fileName.replaceFirst("\\.txt", "-index.txt");

		writeStringsToFile(
				indexPath,
				index.entrySet()
				.stream()
				.map( entry -> String.format("%s: %s", 
							entry.getKey(), 
							entry.getValue()
								.stream()
								.map( i-> Integer.toString(i))
								.collect(Collectors.joining(","))))
				.collect(Collectors.toList())
				);
	}
}
