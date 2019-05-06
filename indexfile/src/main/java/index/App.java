package index;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {

	public static void main(String[] args) {

		Options options = new Options();
		
		options.addOption(
				Option.builder("f")
				.required()
				.longOpt("file")
				.hasArg()
				.argName("name")
				.desc("File to be indexed")
				.build()
		);

		options.addOption(
				Option.builder("l")
				.longOpt("locale")
				.hasArg()
				.argName("language")
				.desc("Locale to be used for sorting")
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
		String cmdLineSyntax = "indexfile -f <file name> [-l <locale>]";

		
		try {
			CommandLine cmd = parser.parse( options, args);
			IndexHandler handler;
			
			if (cmd.hasOption("h")) {
				formatter.printHelp(String.format("%s%n", cmdLineSyntax), options);
				return;
			}
			
			String language = null;
			if (cmd.hasOption("l")) {
				language = cmd.getOptionValue("l");
			}
			handler = new IndexHandler(cmd.getOptionValue("f"), language);
			handler.indexAFile();
			
		} catch (ParseException e) {
			formatter.printHelp(String.format("%s%n%s", e.getMessage(), cmdLineSyntax), options);
		} catch (IOException e) {
			System.err.format("IO problem: %s%n", e.getMessage());
		}
		
	}

}
