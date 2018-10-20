package com.bobocode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link FileReaders} privides an API that allow to read whole file into a {@link String} by file name.
 */
public class FileReaders {

    /**
     * Returns a {@link String} that contains whole text from the file specified by name.
     *
     * @param fileName a name of a text file
     * @return string that holds whole file content
     */
    public static String readWholeFile(String fileName) {
        try(Stream<String> stream = getLinesStream(fileName)) {
            return stream.collect(Collectors.joining("\n"));
        }
    }

    private static Stream<String> getLinesStream(String fileName) {
        try {
            return Files.lines(getResourcePath(fileName));
        } catch (IOException e) {
            throw new FileReaderException("Can't read resource file", e);
        }
    }

    private static Path getResourcePath(String fileName) {
        try {
            Objects.requireNonNull(fileName, "Resources filename must not be null");
            URL resourceUrl = Objects.requireNonNull(
                    FileReaders.class.getClassLoader().getResource(fileName),
                    fileName + " - given resource file does not exist");
            return Paths.get(resourceUrl.toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new FileReaderException("Wrong resource file URI", e);
        }
    }
}
