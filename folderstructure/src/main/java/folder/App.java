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
	
	public static void main(String[] args) throws ParseException {
		
		
		Options options = new Options();
		
		options.addOption(
				Option.builder("r")
				.longOpt("readables")
				.hasArg()
				.argName("name")
				.desc("File name for readable folders")
				.build()
		);

		options.addOption(
				Option.builder("w")
				.longOpt("writables")
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
		CommandLine cmd = parser.parse( options, args);
		
		FolderHandler handler = new FolderHandler("");

		try  {
			if (cmd.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("folderstructure -r <file name> -w <filename>", options);
				return;
			}
			
			if (cmd.hasOption("r") && cmd.getOptionValue("r") != null) {
				List<String> readables = Helper.readStringListFromPath(Paths.get(cmd.getOptionValue("r")));
				handler.buildTreeStructureFromList(readables, TreeItem::setReadable, true);
			}
			
			if (cmd.hasOption("w") && cmd.getOptionValue("w") != null) {
				List<String> writables = Helper.readStringListFromPath(Paths.get(cmd.getOptionValue("w")));
				handler.buildTreeStructureFromList(writables, TreeItem::setWritable, true);
			}
			
			TreeItem result = handler.getWritablesSimple();
			System.out.format("writable folders: %s%n", result.getWritablePaths(null, false));
			
		} catch (IOException e) {
			System.err.format("IO problem: %s%n", e.getMessage());
		} catch (CloneNotSupportedException e) {
			// should not happen
			e.printStackTrace();
		}
		
	}

}
