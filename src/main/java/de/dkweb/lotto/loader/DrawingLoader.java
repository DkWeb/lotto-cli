package de.dkweb.lotto.loader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import de.dkweb.lotto.domain.Drawing;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DrawingLoader {
    private static final Logger logger = LoggerFactory.getLogger(DrawingLoader.class);

    private DrawingLoader() {
    }

    public static List<Drawing> loadDrawings(Reader drawingsAsCSV) throws IOException {
        List<Drawing> drawings = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreLeadingWhiteSpace(true).build();
        try (CSVReader reader = new CSVReaderBuilder(drawingsAsCSV).withCSVParser(parser).withSkipLines(1).build()) {
            for (String[] line : reader.readAll()) {
                drawings.add(createDrawing(line));
            }
        } catch (CsvException csvException) {
            throw new IllegalArgumentException("Error during parsing input (" + IOUtils.toString(drawingsAsCSV) + ")", csvException);
        }
        return drawings;
    }

    private static Drawing createDrawing(String[] line) {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
            LocalDate drawingDate = LocalDate.parse(line[0], DateTimeFormatter.ofPattern("dd.MM.yy[yy]"));
            Double[] prizes = new Double[] { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
            for (int i = 8; i <=16; i++) {
                // Sometimes there was no winner -> prizes is empty / has some unparseable number in it
                // -> set it to zero than and print out some warning message
                try {
                    prizes[i - 8] = numberFormat.parse(line[i]).doubleValue();
                } catch (Exception e) {
                    logger.warn("Prize " + line[i] + " of drawing " + drawingDate + " seems not to be set. Assume 0€ instead");
                }
            }

            return new Drawing(drawingDate, Integer.parseInt(line[1]), Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]), Integer.parseInt(line[4]),
                    Integer.parseInt(line[5]), Integer.parseInt(line[6]),
                    Integer.parseInt(line[7]), prizes[0],
                    prizes[1], prizes[2],
                    prizes[3], prizes[4],
                    prizes[5], prizes[6],
                    prizes[7], prizes[8]);
        } catch(Exception e) {
            throw new IllegalArgumentException("Unable to parse line " + String.join(";", line), e);
        }
    }
}
