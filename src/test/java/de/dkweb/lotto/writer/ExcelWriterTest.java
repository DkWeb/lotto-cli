package de.dkweb.lotto.writer;

import de.dkweb.lotto.de.dkweb.lotto.i18n.GermanTranslator;
import de.dkweb.lotto.domain.Drawing;
import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class ExcelWriterTest {
    private Drawing oneDrawing;

    @BeforeEach
    void setUp() {
        oneDrawing = new Drawing(LocalDate.of(2024, 1, 1), 1, 2, 33, 10, 11, 12, 8,
                500000, 250000, 10000, 1000, 500, 200, 50, 10, 6);
    }

    @Test
    public void writeFromUntilDate() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelWriter excelWriter = new ExcelWriter(outputStream, new GermanTranslator());
        excelWriter.writeResult(LocalDate.of(2024,1, 1), LocalDate.of(2024, 12, 31),
                List.of(), List.of());
        String cell = getExcelCell(outputStream, 0, 0);
        assertThat(cell, is("Auswertung von 01.01.2024 bis 31.12.2024"));
    }

    @Test
    public void writeTips() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelWriter excelWriter = new ExcelWriter(outputStream, new GermanTranslator());
        excelWriter.writeResult(LocalDate.of(2024,1, 1), LocalDate.of(2024, 12, 31),
                List.of(new Tip(1, 2, 3, 4, 5, 6, 7),
                        new Tip(8, 9, 10, 11, 12, 13, 8)), List.of());
        assertThat(getExcelCell(outputStream, 3, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 3, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 3, 3), is("3.0"));
        assertThat(getExcelCell(outputStream, 3, 4), is("4.0"));
        assertThat(getExcelCell(outputStream, 3, 5), is("5.0"));
        assertThat(getExcelCell(outputStream, 3, 6), is("6.0"));
        assertThat(getExcelCell(outputStream, 3, 7), is("7.0"));

        assertThat(getExcelCell(outputStream, 4, 1), is("8.0"));
        assertThat(getExcelCell(outputStream, 4, 2), is("9.0"));
        assertThat(getExcelCell(outputStream, 4, 3), is("10.0"));
        assertThat(getExcelCell(outputStream, 4, 4), is("11.0"));
        assertThat(getExcelCell(outputStream, 4, 5), is("12.0"));
        assertThat(getExcelCell(outputStream, 4, 6), is("13.0"));
        assertThat(getExcelCell(outputStream, 4, 7), is("8.0"));
    }

    @Test
    public void writeDrawingWithOneHitTip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelWriter excelWriter = new ExcelWriter(outputStream, new GermanTranslator());
        excelWriter.writeResult(LocalDate.of(2024,1, 1), LocalDate.of(2024, 12, 31),
                List.of(new Tip(1, 2, 3, 4, 5, 6, 7),
                        new Tip(8, 9, 10, 11, 12, 13, 8)),
                List.of(new Hit(oneDrawing, new Tip(1, 2, 3, 4, 5, 6, 7), 8)));

        assertThat(getExcelCell(outputStream, 6, 0), is("Ziehung vom 01.01.2024"));
        assertThat(getExcelCell(outputStream, 6, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 6, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 6, 3), is("33.0"));
        assertThat(getExcelCell(outputStream, 6, 4), is("10.0"));
        assertThat(getExcelCell(outputStream, 6, 5), is("11.0"));
        assertThat(getExcelCell(outputStream, 6, 6), is("12.0"));
        assertThat(getExcelCell(outputStream, 6, 7), is("8.0"));

        assertThat(getExcelCell(outputStream, 7, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 7, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 7, 3), is("3.0"));
        assertThat(getExcelCell(outputStream, 7, 4), is("4.0"));
        assertThat(getExcelCell(outputStream, 7, 5), is("5.0"));
        assertThat(getExcelCell(outputStream, 7, 6), is("6.0"));
        assertThat(getExcelCell(outputStream, 7, 7), is("7.0"));

        assertThat(getExcelCell(outputStream, 7, 8), is("8.0"));
        assertThat(getExcelCell(outputStream, 7, 9), is("10.0"));
    }

    @Test
    public void writeDrawingWithTwoHitTips() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelWriter excelWriter = new ExcelWriter(outputStream, new GermanTranslator());
        excelWriter.writeResult(LocalDate.of(2024,1, 1), LocalDate.of(2024, 12, 31),
                List.of(new Tip(1, 2, 3, 4, 5, 6, 7),
                        new Tip(8, 9, 10, 11, 12, 13, 8)),
                List.of(new Hit(oneDrawing, new Tip(1, 2, 3, 4, 5, 6, 7), 8),
                        new Hit(oneDrawing, new Tip(1, 2, 33, 10, 5, 6, 7), 6)));

        assertThat(getExcelCell(outputStream, 6, 0), is("Ziehung vom 01.01.2024"));
        assertThat(getExcelCell(outputStream, 6, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 6, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 6, 3), is("33.0"));
        assertThat(getExcelCell(outputStream, 6, 4), is("10.0"));
        assertThat(getExcelCell(outputStream, 6, 5), is("11.0"));
        assertThat(getExcelCell(outputStream, 6, 6), is("12.0"));
        assertThat(getExcelCell(outputStream, 6, 7), is("8.0"));

        assertThat(getExcelCell(outputStream, 7, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 7, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 7, 3), is("3.0"));
        assertThat(getExcelCell(outputStream, 7, 4), is("4.0"));
        assertThat(getExcelCell(outputStream, 7, 5), is("5.0"));
        assertThat(getExcelCell(outputStream, 7, 6), is("6.0"));
        assertThat(getExcelCell(outputStream, 7, 7), is("7.0"));

        assertThat(getExcelCell(outputStream, 7, 8), is("8.0"));
        assertThat(getExcelCell(outputStream, 7, 9), is("10.0"));

        assertThat(getExcelCell(outputStream, 8, 1), is("1.0"));
        assertThat(getExcelCell(outputStream, 8, 2), is("2.0"));
        assertThat(getExcelCell(outputStream, 8, 3), is("33.0"));
        assertThat(getExcelCell(outputStream, 8, 4), is("10.0"));
        assertThat(getExcelCell(outputStream, 8, 5), is("5.0"));
        assertThat(getExcelCell(outputStream, 8, 6), is("6.0"));
        assertThat(getExcelCell(outputStream, 8, 7), is("7.0"));

        assertThat(getExcelCell(outputStream, 8, 8), is("6.0"));
        assertThat(getExcelCell(outputStream, 8, 9), is("200.0"));
    }

    private static String getExcelCell(ByteArrayOutputStream result, int row, int cell) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(result.toByteArray())) {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            return sheet.getRow(row).getCell(cell).toString();
        }
    }

}