package com.bobocode;

import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * {@link FileStats} provides an API that allow to get character statistic based on text file. All whitespace characters
 * are ignored.
 */
public class FileStats {

    private static IntPredicate isAlphabetic = c -> ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');

    private Map<Character, Long> charCountersMap;
    private char mostPopularChar;

    public FileStats(Map<Character, Long> charCountersMap, char mostPopularChar) {
        this.charCountersMap = charCountersMap;
        this.mostPopularChar = mostPopularChar;
    }

    /**
     * Creates a new immutable {@link FileStats} objects using data from text file received as a parameter.
     *
     * @param fileName input text file name
     * @return new FileStats object created from text file
     */
    public static FileStats from(String fileName) {
        try (Stream<String> linesStream = FileReaders.getLinesStream(fileName)) {
            List<String> lines = linesStream.collect(Collectors.toList());
            Map<Character, Long> charCountersMap = getCharCountersMap(lines);
            char mostPopularChar = getMostPopularCharacter(charCountersMap);

            return new FileStats(charCountersMap, mostPopularChar);
        } catch (Exception e) {
            throw new FileStatsException(e.getMessage(), e.getCause());
        }
    }

    private static Map<Character, Long> getCharCountersMap(List<String> lines) {
        return lines.stream()
                .flatMap(line -> line
                        .chars()
                        .filter(isAlphabetic)
                        .mapToObj(c -> (char) c))
                .collect(groupingBy(identity(), counting()));
    }

    private static char getMostPopularCharacter(Map<Character, Long> charCountersMap) {
        return charCountersMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    /**
     * Returns a number of occurrences of the particular character.
     *
     * @param character a specific character
     * @return a number that shows how many times this character appeared in a text file
     */
    public int getCharCount(char character) {
        return charCountersMap.getOrDefault(character, 0L).intValue();
    }

    /**
     * Returns a character that appeared most often in the text.
     *
     * @return the most frequently appeared character
     */
    public char getMostPopularCharacter() {
        return mostPopularChar;
    }

    /**
     * Returns {@code true} if this character has appeared in the text, and {@code false} otherwise
     *
     * @param character a specific character to check
     * @return {@code true} if this character has appeared in the text, and {@code false} otherwise
     */
    public boolean containsCharacter(char character) {
        return charCountersMap.containsKey(character);
    }
}
