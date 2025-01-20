package de.dkweb.lotto.analyze;

import de.dkweb.lotto.domain.Drawing;
import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;

import java.util.ArrayList;
import java.util.List;

public final class DrawingAnalyzer {
    private DrawingAnalyzer() {
    }

    /**
     * For a hit you need either 2 correct normal numbers + super number or 3 or more correct normal numbers
     */
    public static List<Hit> analyze(Drawing drawing, List<Tip> tips) {
        List<Hit> hits = new ArrayList<>();
        int[] drawingNumbers = drawing.getNumbersAsArray();

        for (Tip tip : tips) {
            boolean correctSuperNumber = drawing.superNumber() == tip.superNumber();
            int correctNormalNumbers = 0;
            int[] tipNumbers = tip.getNumbersAsArray();
            for (int i = 0; i < drawingNumbers.length; i++) {
                for (int j = 0; j < tipNumbers.length; j++) {
                    if (tipNumbers[j] == drawingNumbers[i]) {
                        correctNormalNumbers++;
                    }
                }
            }
            int prizeClass = 14 - (correctNormalNumbers * 2) - (correctSuperNumber ? 1 : 0);
            if (prizeClass <= 9) {
                hits.add(new Hit(drawing, tip, prizeClass));
            }
        }
        return hits;
    }
}
