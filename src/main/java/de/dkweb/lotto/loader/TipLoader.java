package de.dkweb.lotto.loader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import de.dkweb.lotto.domain.Tip;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class TipLoader {
    private TipLoader() {
    }

    public static List<Tip> loadTips(Reader tipsAsCSV) throws IOException {
        List<Tip> tips = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreLeadingWhiteSpace(true).build();
        try (CSVReader reader = new CSVReaderBuilder(tipsAsCSV).withCSVParser(parser).build()) {
            for (String[] line : reader.readAll()) {
                tips.add(createTip(line));
            }
        } catch (CsvException csvException) {
            throw new IllegalArgumentException("Error during parsing input (" + IOUtils.toString(tipsAsCSV) + ")", csvException);
        }
        return tips;
    }

    private static Tip createTip(String[] line) {
        try {
            return new Tip(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                    Integer.parseInt(line[2]), Integer.parseInt(line[3]),
                    Integer.parseInt(line[4]), Integer.parseInt(line[5]),
                    Integer.parseInt(line[6]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse line " + String.join(";", line));
        }
    }
}
