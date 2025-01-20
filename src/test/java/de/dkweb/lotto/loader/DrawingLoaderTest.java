package de.dkweb.lotto.loader;

import de.dkweb.lotto.domain.Drawing;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

class DrawingLoaderTest {

    private static final String HEADER_ROW = "Datum;Zahl1;Zahl2;Zahl3;Zahl4;Zahl5;Zahl6;S;Quote Kl. 1;Quote Kl. 2;Quote Kl. 3;Quote Kl. 4;Quote Kl. 5;Quote Kl. 6;Quote Kl. 7;Quote Kl. 8;Quote Kl. 9";

    @Test
    void loadEmptyDrawingList() throws IOException {
        assertThat(DrawingLoader.loadDrawings(new StringReader("")), is(empty()));
        assertThat(DrawingLoader.loadDrawings(new StringReader(HEADER_ROW)), is(empty()));
    }

    @Test
    void loadDrawingListSkipHeaderRow() throws IOException {
        String validRow = "06.01.24;1;2;3;4;5;6;7;1000000,10;100000;10000,4;4500,7;300,6;50,2;16,3;10,8;6";
        assertThat(DrawingLoader.loadDrawings(new StringReader(HEADER_ROW + "\n" + validRow)),
                is(List.of(new Drawing(LocalDate.of(2024,1,6), 1,2,3,4,5,6,7,
                        1000000.10d, 100000d, 10000.4d, 4500.7, 300.6, 50.2, 16.3, 10.8, 6))));
    }

    @Test
    void loadDrawingListWithEmptyPrize() throws IOException {
        String validRow = "06.01.24;1;2;3;4;5;6;7;;100000;10000,4;4500,7;300,6;50,2;16,3;10,8;6";
        assertThat(DrawingLoader.loadDrawings(new StringReader(HEADER_ROW + "\n" + validRow)),
                is(List.of(new Drawing(LocalDate.of(2024,1,6), 1,2,3,4,5,6,7,
                        0d, 100000d, 10000.4d, 4500.7, 300.6, 50.2, 16.3, 10.8, 6))));
    }

    @Test
    void loadDrawingListWithSomeUnrecognizedCharAsPrize() throws IOException {
        String validRow = "06.01.24;1;2;3;4;5;6;7;-;100000;10000,4;4500,7;300,6;50,2;16,3;10,8;6";
        assertThat(DrawingLoader.loadDrawings(new StringReader(HEADER_ROW + "\n" + validRow)),
                is(List.of(new Drawing(LocalDate.of(2024,1,6), 1,2,3,4,5,6,7,
                        0d, 100000d, 10000.4d, 4500.7, 300.6, 50.2, 16.3, 10.8, 6))));
    }

    @Test
    void loadMultiDrawingList() throws IOException {
        String validRow1 = "06.01.24;1;2;3;4;5;6;7;1000000,10;100000;10000,4;4500,7;300,6;50,2;16,3;10,8;6";
        String validRow2 = "13.01.24;8;9;10;11;12;13;8;1000001,10;100001;10001,4;4501,7;301,6;51,2;17,3;11,8;7";
        assertThat(DrawingLoader.loadDrawings(new StringReader(HEADER_ROW + "\n" + validRow1 + "\n" + validRow2)),
                is(List.of(new Drawing(LocalDate.of(2024,1,6), 1,2,3,4,5,6,7,
                        1000000.10d, 100000d, 10000.4d, 4500.7, 300.6, 50.2, 16.3, 10.8, 6),
                        new Drawing(LocalDate.of(2024,1,13), 8,9,10,11,12,13,8,
                                1000001.10d, 100001d, 10001.4d, 4501.7, 301.6, 51.2, 17.3, 11.8, 7))));
    }

    @Test()
    void loadMalformattedDrawingList() throws IOException{
        try {
            DrawingLoader.loadDrawings(new StringReader(HEADER_ROW + "\n" + "1;2;3;4;5;6"));
            fail("Exception expected, but nothing thrown");
        } catch(IllegalArgumentException iae) {
            assertThat(iae.getMessage(), is("Unable to parse line 1;2;3;4;5;6"));
        }
    }
}