package de.dkweb.lotto.domain;

import java.time.LocalDate;

public record Drawing(LocalDate drawingDate, int number1, int number2, int number3, int number4, int number5, int number6, int superNumber,
                      double prize1, double prize2, double prize3, double prize4, double prize5, double prize6,
                      double prize7, double prize8, double prize9) {

    public int[] getNumbersAsArray() {
        return new int[] { number1, number2, number3, number4, number5, number6 };
    }

    public double[] getPricesAsArray() {
        return new double[] { prize1, prize2, prize3, prize4, prize5, prize6, prize7, prize8, prize9 };
    }
}
