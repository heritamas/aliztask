package folder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import folder.model.TreeItem;

public class App {
	
	public static void main(String[] args) {
		
		
		Options options = new Options();
		
		options.addOption(
				Option.builder("r")
				.longOpt("readables")
				.required()
				.hasArg()
				.argName("name")
				.desc("File name for readable folders")
				.build()
		);

		options.addOption(
				Option.builder("w")
				.longOpt("writables")
				.required()
				.hasArg()
				.argName("name")
				.desc("File name for writable folders")
				.build()
		);

		options.addOption(
				Option.builder("h")
				.longOpt("help")
				.desc("Prints help")
				.build()
		);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		String cmdLineSyntax = "folderstructure -r <file name> -w <file name>";
		
		try  {
			CommandLine cmd = parser.parse( options, args);
			
			if (cmd.hasOption("h")) {
				formatter.printHelp(String.format("%s%n", cmdLineSyntax), options);
				return;
			}
			
			FolderHandler handler = new FolderHandler("");

			List<String> readables = Helper.readStringListFromPath(Paths.get(cmd.getOptionValue("r")));
			handler.buildTreeStructureFromList(readables, TreeItem::setReadable, true);
			
			List<String> writables = Helper.readStringListFromPath(Paths.get(cmd.getOptionValue("w")));
			handler.buildTreeStructureFromList(writables, TreeItem::setWritable, true);
			
			TreeItem result = handler.getWritablesSimple();
			System.out.format("writable folders: %s%n", result.getWritablePaths(null, false));
			
		} catch (IOException e) {
			System.err.format("IO problem: %s%n", e.getMessage());
		} catch (CloneNotSupportedException e) {
			// should not happen
			e.printStackTrace();
		} catch (ParseException e) {
			formatter.printHelp(String.format("%s%n%s", e.getMessage(), cmdLineSyntax), options);
		}
		
	}

}
