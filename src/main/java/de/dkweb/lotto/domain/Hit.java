package de.dkweb.lotto.domain;

import java.time.LocalDate;

public record Hit(Drawing drawing, Tip hitTip, int prizeClass) {
}
