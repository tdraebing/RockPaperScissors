package org.driven_by_data.rockpaperscissors.rockpaperscissors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by tdraebing on 3/16/2017.
 */

public enum Figure {
    ROCK ("Rock"),
    PAPER ("Paper"),
    SCISSORS ("Scissors");

    private final String name;

    Figure(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

    private static final List<Figure> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Figure randomFigure()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}