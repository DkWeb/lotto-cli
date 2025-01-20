package de.dkweb.lotto.domain;

public record Tip(int number1, int number2, int number3, int number4, int number5, int number6, int superNumber) {
    public int[] getNumbersAsArray() {
        return new int[] { number1, number2, number3, number4, number5, number6 };
    }
}
