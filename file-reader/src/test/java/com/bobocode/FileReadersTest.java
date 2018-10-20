package com.bobocode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class FileReadersTest {

    @Test
    public void testReadWholeFileOnEmptyFile() {
        String fileContent = FileReaders.readWholeFile("empty.txt");

        assertEquals("", fileContent);

    }

    @Test
    public void testReadWholeFileOnFileWithEmptyLines() {
        String fileContent = FileReaders.readWholeFile("lines.txt");

        assertEquals("Hey!\n" +
                "\n" +
                "What's up?\n" +
                "\n" +
                "Hi!", fileContent);
    }

    @Test
    public void testReadWholeFile() {
        String fileContent = FileReaders.readWholeFile("simple.txt");

        assertEquals("Hello!\n" + "It's a test file.", fileContent);
    }

    @Test
    public void testReadNotExistedFile() {
        try {
            FileReaders.readWholeFile("fake.file");
            fail();
        } catch (Exception e) {
            assertEquals("Wrong resource file URI", e.getMessage());
            assertEquals("fake.file - given resource file does not exist", e.getCause().getMessage());
        }
    }

    @Test
    public void testReadNullFilename() {
        try {
            FileReaders.readWholeFile(null);
            fail();
        } catch (Exception e) {
            assertEquals("Wrong resource file URI", e.getMessage());
            assertEquals("Resources filename must not be null", e.getCause().getMessage());
        }
    }
}
