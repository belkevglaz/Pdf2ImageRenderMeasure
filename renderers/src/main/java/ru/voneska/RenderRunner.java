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

		options.addOption(renderOption)
				.addOption(dpiOption)
				.addOption(fileOption)
				.addOption(saveOption);

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
			defaultDpi = ((Number) cmd.getParsedOptionValue("dpi")).intValue();
		}

		if (cmd.hasOption("save-images")) {
			defaultSave = true;
		}

		if (!cmd.hasOption("render") || (!cmd.hasOption("file"))) {
			formatter.printHelp("RenderRun", options);
		} else {

			System.out.println("Process Id : " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
			Scanner userInput = new Scanner(System.in);

			String input;
			do {
				System.out.println("Press any key to start...");

				input = userInput.nextLine();

			} while (input == null);
		}

		@SuppressWarnings("SuspiciousMethodCalls")
		Class rc = renders.get(cmd.getParsedOptionValue("render"));
		RendererRunnable run = (RendererRunnable) rc.newInstance();

		File file = new File((String) cmd.getParsedOptionValue("file"));
		run.setDpi(defaultDpi);
		run.setFile(file);
		run.setSaveToFile(defaultSave);

		Measurer.measureTimeUsage(run);

	}


}