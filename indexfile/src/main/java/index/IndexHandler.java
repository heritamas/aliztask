package index;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexHandler 
{
	Path path;
	SortedMap<String, SortedSet<Integer>> index;
	
	public IndexHandler(String fileName, String language) {
		super();
		this.path = Paths.get(fileName);
		initIndex(language);
	}

	public IndexHandler(Path path) {
		super();
		this.path = path; 
		initIndex(null);
	}

	private void initIndex (String language) {
		
		final Locale locale; 
		if (language != null) {
			locale = Locale.forLanguageTag(language);
		} else {
			locale = Locale.getDefault();
		}
		
		index = new TreeMap<String, SortedSet<Integer>>(new Comparator<String>() {
			
			Collator coll =  Collator.getInstance(locale);

			@Override
			public int compare(String o1, String o2) {
				return coll.compare(o1, o2);
			}
		});
	}
	
	public void indexAFile() throws IOException {
		
		List<String> lines = Helper.readStringListFromPath(path);

		IntStream.range(0, lines.size())
		.forEach( i -> tokenizeLine(i+1, lines.get(i)));
		
		writeIndexFile();
	}
	
	private void tokenizeLine(Integer idx, String line) {
		
		// split on whitespace and punctuation
		String[] tokens = line.split("[\\s\\p{Punct}]+");
		
		// save to index map
		for (String token: tokens) {
			SortedSet<Integer> set = new TreeSet<Integer>();
			set.add(idx);
			index.merge(token, set, (l1, l2) -> { l1.addAll(l2); return l1; });
		}
	}
	
	private void writeIndexFile() throws IOException {
		
		Path indexPath = path.resolveSibling(
				path.getFileName().toString().replaceFirst("\\.txt$", "-index.txt"));

		Helper.writeStringsToFile(
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
