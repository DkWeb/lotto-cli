package de.dkweb.lotto.writer;

import de.dkweb.lotto.domain.Hit;
import de.dkweb.lotto.domain.Tip;

import java.time.LocalDate;
import java.util.List;

public interface ResultWriter {
    void writeResult(LocalDate start, LocalDate end, List<Tip> allTips, List<Hit> hits);
}
