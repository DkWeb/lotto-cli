package de.dkweb.lotto.loader;

import de.dkweb.lotto.domain.Tip;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

class TipLoaderTest {
    @Test
    void loadEmptyTipList() throws IOException {
        assertThat(TipLoader.loadTips(new StringReader("")), is(empty()));
    }

    @Test
    void loadTipListOfOne() throws IOException {
        assertThat(TipLoader.loadTips(new StringReader("1;2;3;4;5;6;7")),
                is(List.of(new Tip(1,2,3,4,5,6,7))));
    }

    @Test
    void loadMultiTipList() throws IOException {
        assertThat(TipLoader.loadTips(new StringReader("1;2;3;4;5;6;7\n8;9;10;11;12;13;8")),
                is(List.of(new Tip(1,2,3,4,5,6,7),
                            new Tip(8,9,10,11,12,13,8))));
    }

    @Test()

    void loadMalformattedTipList() throws IOException{
        try {
            TipLoader.loadTips(new StringReader("1;2;3;4;5;6"));
            fail("Exception expected, but nothing thrown");
        } catch(IllegalArgumentException iae) {
            assertThat(iae.getMessage(), is("Unable to parse line 1;2;3;4;5;6"));
        }
    }
}