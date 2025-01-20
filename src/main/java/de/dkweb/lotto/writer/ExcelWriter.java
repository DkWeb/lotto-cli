package de.dkweb.lotto.writer;

import de.dkweb.lotto.de.dkweb.lotto.i18n.Translator;
import de.dkweb.lotto.domain.Drawing;
import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ExcelWriter implements ResultWriter {
    private final OutputStream output;

    private final Translator translator;

    public ExcelWriter(OutputStream output, Translator translator) {
        this.output = output;
        this.translator = translator;
    }

    @Override
    public void writeResult(LocalDate start, LocalDate end, List<Tip> allTips, List<Hit> hits) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(translator.i18n("sheetName"));
            Row sheetHeadline = sheet.createRow(0);
            Cell cell = sheetHeadline.createCell(0);
            cell.setCellValue(translator.i18n("analyzeHeadline", start, end));
            int currentRow = tipSection(start, end, allTips, sheet);
            currentRow++;
            currentRow = drawingHitsSections(hits, sheet, currentRow);
            currentRow++;
            winTotalSection(sheet, currentRow);
            workbook.write(output);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write Excel data");
        }
    }

    private void winTotalSection(Sheet sheet, int currentRow) {
        Row winTotalHeadline = sheet.createRow(currentRow);
        Cell cell = winTotalHeadline.createCell(0);
        cell.setCellValue(translator.i18n("prizesTotal"));
        cell = winTotalHeadline.createCell(9);
        cell.setCellFormula("SUM(J1:J" + (currentRow - 1) + ")");
    }

    private int drawingHitsSections(List<Hit> hits, Sheet sheet, int currentRow) {
        var hitsByDate = hits.stream().collect(Collectors.groupingBy(hit -> hit.drawing().drawingDate()));
        var sortedHitsByDate = new TreeMap<>(hitsByDate);
        for (var hitsForOneDate : sortedHitsByDate.entrySet()) {
            Drawing drawing = hitsForOneDate.getValue().iterator().next().drawing();
            Row drawingLine = sheet.createRow(currentRow);
            Cell cell = drawingLine.createCell(0);
            cell.setCellValue(translator.i18n("drawingHeadline", hitsForOneDate.getKey()));
            for (int i = 0; i < 6; i++) {
                cell = drawingLine.createCell(i + 1);
                cell.setCellValue(drawing.getNumbersAsArray()[i]);
            }
            cell = drawingLine.createCell(7);
            cell.setCellValue(drawing.superNumber());

            cell = drawingLine.createCell(8);
            cell.setCellValue(translator.i18n("prizeClass"));

            cell = drawingLine.createCell(9);
            cell.setCellValue(translator.i18n("prize"));
            currentRow++;

            int hitTip = 1;
            for (Hit hit : hitsForOneDate.getValue()) {
                Row hitRow = sheet.createRow(currentRow);
                cell = hitRow.createCell(0);
                cell.setCellValue(translator.i18n("winTip", hitTip));
                for (int i = 0; i < hit.hitTip().getNumbersAsArray().length; i++) {
                    cell = hitRow.createCell(i + 1);
                    CellStyle style = createIntCellStyle(sheet.getWorkbook());
                    cell.setCellStyle(style);
                    cell.setCellValue(hit.hitTip().getNumbersAsArray()[i]);
                }
                // Supernumber
                cell = hitRow.createCell(7);
                cell.setCellValue(hit.hitTip().superNumber());

                cell = hitRow.createCell(8);
                cell.setCellValue(hit.prizeClass());

                cell = hitRow.createCell(9);
                cell.setCellValue(hit.drawing().getPricesAsArray()[hit.prizeClass() - 1]);

                hitTip++;
                currentRow++;
            }
            currentRow++;
        }
        return currentRow;
    }

    private int tipSection(LocalDate start, LocalDate end, List<Tip> allTips, Sheet sheet) {
        Row tipsHeadline = sheet.createRow(2);
        Cell cell = tipsHeadline.createCell(0);
        cell.setCellValue(translator.i18n("tipsHeadline", start, end));
        for (int i = 1; i <= 6; i++) {
            cell = tipsHeadline.createCell(i);
            cell.setCellValue(translator.i18n("number" + i));
        }
        cell = tipsHeadline.createCell(7);
        cell.setCellValue(translator.i18n("superNumber"));

        int currentRow = 3;
        int tipNumber = 1;
        for (Tip tip : allTips) {
            Row tipRow = sheet.createRow(currentRow);
            cell = tipRow.createCell(0);
            cell.setCellValue(translator.i18n("tipNumber", tipNumber));
            for (int i = 0; i < tip.getNumbersAsArray().length; i++) {
                cell = tipRow.createCell(i + 1);
                CellStyle style = createIntCellStyle(sheet.getWorkbook());
                cell.setCellStyle(style);
                cell.setCellValue(tip.getNumbersAsArray()[i]);
            }
            // Supernumber
            cell = tipRow.createCell(7);
            cell.setCellValue(tip.superNumber());
            currentRow++;
            tipNumber++;
        }
        return currentRow;
    }

    private static CellStyle createIntCellStyle(Workbook workbook) {
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("0"));
        return numberStyle;
    }
}
