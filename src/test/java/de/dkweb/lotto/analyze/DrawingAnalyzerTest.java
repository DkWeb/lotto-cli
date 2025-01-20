package de.dkweb.lotto.analyze;

import de.dkweb.lotto.domain.Drawing;
import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

class DrawingAnalyzerTest {
    private Drawing oneDrawing;

    @BeforeEach
    void setUp() {
        oneDrawing = new Drawing(LocalDate.of(2024, 1, 1), 1, 2, 3, 4, 5, 6, 7,
                500000, 250000, 10000, 1000, 500, 200, 50, 10, 6);
    }

    @Test
    void zeroHitsShouldNotWinAPrize() {
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(new Tip(7,8,9,10,11,12,0))), is(empty()));
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(new Tip(7,8,9,10,11,12,7))), is(empty()));
    }

    @Test
    void oneHitShouldNotWinAPrize() {
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(new Tip(1,8,9,10,11,12,0))), is(empty()));
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(new Tip(1,8,9,10,11,12,7))), is(empty()));
    }

    @Test
    void twoHitsWithoutSuperNumberShouldNotWinAPrize() {
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(new Tip(1,2,9,10,11,12,0))), is(empty()));
    }

    @Test
    void twoHitsWithSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,9,10,11,12,7);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 9))));
    }

    @Test
    void threeHitsWithoutSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,10,11,12,8);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 8))));
    }

    @Test
    void threeHitsWithSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,10,11,12,7);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 7))));
    }

    @Test
    void fourHitsWithoutSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,11,12,8);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 6))));
    }

    @Test
    void fourHitsWithSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,11,12,7);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 5))));
    }


    @Test
    void fiveHitsWithoutSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,5,12,8);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 4))));
    }

    @Test
    void fiveHitsWithSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,5,12,7);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 3))));
    }

    @Test
    void sixHitsWithoutSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,5,6,8);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 2))));
    }

    @Test
    void sixHitsWithSuperNumberShouldWinAPrize() {
        Tip tip = new Tip(1,2,3,4,5,6,7);
        assertThat(DrawingAnalyzer.analyze(oneDrawing, List.of(tip)), is(List.of(new Hit(oneDrawing, tip, 1))));
    }

}