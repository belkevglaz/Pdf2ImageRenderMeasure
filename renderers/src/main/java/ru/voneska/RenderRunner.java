package ru.voneska;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import ru.voneska.runnable.Ghost4JRendererRun;
import ru.voneska.runnable.PdfBoxRendererRun;
import ru.voneska.runnable.RendererRunnable;

/**
 *
 */
public class RenderRunner {

	private static int defaultDpi = 150;
	private static boolean defaultSave = false;

	private static Map<String, Class> renders = ImmutableMap.<String, Class>builder()
			.put("ghost4j", Ghost4JRendererRun.class)
			.put("pdfbox", PdfBoxRendererRun.class)
			.build();

	public static void main(String[] args) throws ParseException, IllegalAccessException, InstantiationException {

		// tod avoid : WARN No appenders could be found for logger (org.ghost4j.Ghostscript)
		BasicConfigurator.configure();

		Options options = new Options();

		Option renderOption = Option.builder("r")
				.longOpt("render")
				.hasArg(true)
				.desc("Set up renderer : [ghostj4] or [pdfbox]")
				.required(true)
				.build();

		Option dpiOption = Option.builder("d")
				.longOpt("dpi")
				.hasArg(true)
				.desc("Set up expected image DPI")
				.required(false)
				.build();

		Option fileOption = Option.builder("f")
				.longOpt("file")
				.hasArg(true)
				.desc("Select PDF file to rendering")
				.required(true)
				.build();

		Option saveOption = Option.builder("s")
				.longOpt("save-images")
				.hasArg(false)
				.desc("This flag indicate to need to save rendered images")
				.required(false)
				.build();

		Option waitsOption = Option.builder("w")
				.longOpt("wait")
				.hasArg(false)
				.desc("This flag indicate that to need wait any input to attach profilers to PID")
				.required(false)
				.build();

		Option outputFile = Option.builder("o")
				.longOpt("output")
				.hasArg(true)
				.desc("Path to csv file with results")
				.required(false)
				.build();

		Option type = Option.builder("t")
				.longOpt("type")
				.hasArg(true)
				.desc("Type of parseable PDF files. [print] or [raster]")
				.required(true)
				.build();

		options.addOption(renderOption)
				.addOption(dpiOption)
				.addOption(fileOption)
				.addOption(saveOption)
				.addOption(waitsOption)
				.addOption(outputFile)
				.addOption(type);

		HelpFormatter formatter = new HelpFormatter();

		CommandLineParser parser = new BasicParser();

		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace(System.err);
			formatter.printHelp("RenderRun", options);
			return;
		}

		if (!cmd.hasOption("d")) {
			System.out.println("No dpi set. Will use default = 150");
		} else {
			defaultDpi = Integer.parseInt((String) cmd.getParsedOptionValue("dpi"));
		}

		if (cmd.hasOption("save-images")) {
			defaultSave = true;
		}

		if (!cmd.hasOption("type")) {
			throw new IllegalArgumentException("No type argument. Set in \'print\' or \'raster\'");
		}

		if (!cmd.hasOption("render") || (!cmd.hasOption("file"))) {
			formatter.printHelp("RenderRun", options);
		} else {

			System.out.println("Process Id : " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

			// if need waits to attach to JVM
			if (cmd.hasOption("wait")) {
				Scanner userInput = new Scanner(System.in);
				String input;
				do {
					System.out.println("Press any key to start...");

					input = userInput.nextLine();

				} while (input == null);
			}
		}

		@SuppressWarnings("SuspiciousMethodCalls")
		Class rc = renders.get(cmd.getParsedOptionValue("render"));
		RendererRunnable run = (RendererRunnable) rc.newInstance();

		File file = new File((String) cmd.getParsedOptionValue("file"));
		run.setDpi(defaultDpi);
		run.setFile(file);
		run.setSaveToFile(defaultSave);

		Measurer m = new Measurer();
		m.setResultFile(cmd.hasOption("output") ? cmd.getParsedOptionValue("output").toString() : "result.csv");
		m.setType(cmd.getParsedOptionValue("type").toString());
		m.measureTimeUsage(run);

	}


}