package it.univr.wordautomata;

import java.io.File;
import java.util.List;
import it.univr.wordautomata.utils.Methods;

public class Globals {

    /**
     * A list of test files.
     */
    public static final List<File> testFiles = List.of(
            Methods.getResource(PathTest.class, "tests", "deterministic.automata"),
            Methods.getResource(PathTest.class, "tests", "alternative.automata"),
            Methods.getResource(PathTest.class, "tests", "stats.automata"),
            Methods.getResource(PathTest.class, "tests", "noexit.automata"),
            Methods.getResource(PathTest.class, "tests", "notfinal.automata"));

    /**
     * A list of test words.
     */
    public static final List<String> testWords = List.of(
            "xxxy",
            "xy",
            "yxy",
            "z",
            "x");

    /**
     * A list of test results.
     */
    public static final List<Boolean> testResults = List.of(
            true,
            true,
            true,
            false,
            false);

    /**
     * A list of paths to test.
     */
    public static final List<List<String>> testPaths = List.of(
            List.of("xxx", "y"),
            List.of("x", "y"),
            List.of("yxy"),
            List.of(),
            List.of());

    /**
     * An instance of the WordAutomata class.
     */
    public static final WordAutomata instance = new WordAutomata();
}
