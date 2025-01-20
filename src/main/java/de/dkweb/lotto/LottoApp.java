package de.dkweb.lotto;

import de.dkweb.lotto.analyze.DrawingAnalyzer;
import de.dkweb.lotto.de.dkweb.lotto.i18n.GermanTranslator;
import de.dkweb.lotto.de.dkweb.lotto.i18n.Translator;
import de.dkweb.lotto.domain.Drawing;
import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;
import de.dkweb.lotto.loader.DrawingLoader;
import de.dkweb.lotto.loader.TipLoader;
import de.dkweb.lotto.writer.ExcelWriter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class LottoApp {
    private static final Logger logger = LoggerFactory.getLogger(LottoApp.class);

    private static final Translator TRANSLATOR = new GermanTranslator();

    private static final Option START_DATE = Option.builder("s")
            .longOpt("start")
            .hasArg()
            .argName("start")
            .desc(TRANSLATOR.i18n("optionStartDate"))
            .build();

    private static final Option END_DATE = Option.builder("e")
            .longOpt("end")
            .hasArg()
            .argName("end")
            .desc(TRANSLATOR.i18n("optionEndDate"))
            .build();

    private static final Option TIP_FILE = Option.builder("t")
            .longOpt("tips")
            .hasArg()
            .argName("tips")
            .required()
            .desc(TRANSLATOR.i18n("optionTipFile"))
            .build();

    private static final Option DRAWING_FILE = Option.builder("d")
            .longOpt("drawings")
            .hasArg()
            .argName("drawings")
            .desc(TRANSLATOR.i18n("optionDrawingFile"))
            .build();

    private static final Option OUTPUT_FILE = Option.builder("o")
            .longOpt("output")
            .hasArg()
            .argName("output")
            .desc(TRANSLATOR.i18n("optionOutputFile"))
            .build();

    private static final Option ONLY_SATURDAY = Option.builder("satOnly")
            .longOpt("only-saturday")
            .desc(TRANSLATOR.i18n("optionOnlySaturday"))
            .build();

    private static final Option ONLY_WEDNESDAY = Option.builder("wedOnly")
            .longOpt("only-wednesday")
            .desc(TRANSLATOR.i18n("optionOnlyWednesday"))
            .build();

    private static final Option HELP = Option.builder("h")
            .longOpt("help")
            .desc(TRANSLATOR.i18n("optionHelp"))
            .build();

    public static void main(String[] args) throws ParseException, IOException {

        if (args.length == 0
                || Arrays.stream(args).anyMatch(s -> s.equalsIgnoreCase("--" + HELP.getLongOpt()))
                || Arrays.stream(args).anyMatch(s -> s.equalsIgnoreCase("-" + HELP.getOpt()))) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar <lotto-cli.jar> -t <tip-file>", createFullOptions());
        } else {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(createFullOptions(), args);

            LocalDate startDate = getStartDate(line);
            LocalDate endDate = getEndDate(line);
            List<Tip> tips = TipLoader.loadTips(createReader(line, TIP_FILE));
            List<Drawing> drawings = DrawingLoader.loadDrawings(getDrawings(line));
            Path outputFile = getOutputFile(line);

            List<Hit> allHits = new ArrayList<>();
            Predicate<Drawing> drawingPredicate = getDrawingPredicate(line);
            List<Drawing> suitableDrawings = drawings.stream()
                    .filter(drawingPredicate)
                    .collect(Collectors.toList());

            for (Drawing drawing : suitableDrawings) {
                allHits.addAll(DrawingAnalyzer.analyze(drawing, tips));
            }
            var writer = new ExcelWriter(new FileOutputStream(outputFile.toFile()), TRANSLATOR);
            writer.writeResult(startDate,endDate, tips, allHits);
        }
    }

    private static LocalDate getStartDate(CommandLine line) {
        if (!line.hasOption(START_DATE)) {
            logger.info(TRANSLATOR.i18n("infoNoStartDate"));
            return LocalDate.parse("1900-01-01");
        }
        return LocalDate.parse(line.getOptionValue(START_DATE));
    }

    private static LocalDate getEndDate(CommandLine line) {
        if (!line.hasOption(END_DATE)) {
            logger.info(TRANSLATOR.i18n("infoNoEndDate", LocalDate.now()));
            return LocalDate.now();
        }
        return LocalDate.parse(line.getOptionValue(END_DATE));
    }

    private static InputStreamReader createReader(CommandLine line, Option option) throws IOException {
        Path path = Path.of(line.getOptionValue(option));
        return new FileReader(path.toFile(), StandardCharsets.UTF_8);
    }

    private static InputStreamReader getDrawings(CommandLine line) throws IOException {
        if (!line.hasOption(DRAWING_FILE)) {
            logger.info(TRANSLATOR.i18n("infoNoDrawingFile"));
            return new InputStreamReader(LottoApp.class.getClassLoader().getResourceAsStream("lotto_2024.csv"));
        }
        return createReader(line, DRAWING_FILE);
    }

    private static Path getOutputFile(CommandLine line) {
        if (!line.hasOption(OUTPUT_FILE)) {
            logger.info(TRANSLATOR.i18n("infoNoOutputFile"));
            return Path.of("auswertung_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".xlsx");
        }
        return Path.of(line.getOptionValue(OUTPUT_FILE));
    }

    private static Predicate<Drawing> getDrawingPredicate(CommandLine line) {
        LocalDate startDate = getStartDate(line);
        LocalDate endDate = getEndDate(line);
        // With this little trick we make sure that start and end date are inclusive
        Predicate<Drawing> datePredicate = d -> d.drawingDate().isAfter(startDate.minusDays(1)) && d.drawingDate().isBefore(endDate.plusDays(1));
        if (line.hasOption(ONLY_SATURDAY)) {
            return d -> d.drawingDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) && datePredicate.test(d);
        } else if (line.hasOption(ONLY_WEDNESDAY)) {
            return d -> d.drawingDate().getDayOfWeek().equals(DayOfWeek.WEDNESDAY) && datePredicate.test(d);
        }
        return datePredicate;
    }

    private static Options createFullOptions() {
        Options options = new Options();
        options.addOption(START_DATE);
        options.addOption(END_DATE);
        options.addOption(TIP_FILE);
        options.addOption(DRAWING_FILE);
        options.addOption(OUTPUT_FILE);
        options.addOption(HELP);
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(ONLY_SATURDAY);
        optionGroup.addOption(ONLY_WEDNESDAY);
        options.addOptionGroup(optionGroup);
        return options;
    }
}
