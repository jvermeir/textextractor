package nl.vermeir.extractor;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextExtractorTest {
    private final String DE_FILE_NAME = "src/test/resources/tor.de";
    private final String COM_FILE_NAME = "src/test/resources/tor.com";

    @Test
    public void testDERemoveHeader() throws Exception {
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader();
        String text = result.story.text;
        assertTrue(text.startsWith("TitleGoesHere"));
    }

    @Test
    public void testDETitle() throws Exception {
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader().setTitleAndAuthor();
        assertEquals("TitleGoesHere", result.story.title);
        assertEquals("AuthorGoesHere", result.story.author);
    }

    @Test
    public void testDETitleSecondForm() throws Exception {
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader().setTitleAndAuthor();
        assertEquals("TitleGoesHere", result.story.title);
        assertEquals("AuthorGoesHere", result.story.author);
    }

    @Test
    public void testDESecondHeaderIsRemoved() throws Exception {
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader().setTitleAndAuthor().removeSecondHeader();
        assertTrue(result.story.text.startsWith("Lorem ipsum dolor sit amet, consectetur adipis"));
    }

    @Test
    public void testDEFooterIsRemoved() throws Exception {
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader().setTitleAndAuthor().removeSecondHeader().removeEndOfFooter();
        assertTrue(result.story.text.endsWith("© 2019 by AuthorGoesHere."));
    }

    @Test
    public void testCOMRemoveHeader() throws Exception {
        TextExtractor result = TextExtractor.loadText(COM_FILE_NAME, TextSource.COM);
        result.removeHeader();
        String text = result.story.text;
        assertTrue(text.startsWith("TitleGoesHere"));
    }

    @Test
    public void testCOMTitle() throws Exception {
        TextExtractor result = TextExtractor.loadText(COM_FILE_NAME, TextSource.COM);
        result.removeHeader().setTitleAndAuthor();
        assertEquals("TitleGoesHere", result.story.title);
        assertEquals("AuthorGoesHere", result.story.author);
        assertTrue(result.story.text.startsWith("Illustrated by illustratorGoesHere"));
    }

    @Test
    public void testCOMSecondHeaderIsRemoved() throws Exception {
        TextExtractor result = TextExtractor.loadText(COM_FILE_NAME, TextSource.COM);
        result.removeHeader().setTitleAndAuthor().removeSecondHeader();
        assertTrue(result.story.text.startsWith("Illustrated by illustratorGoesHere"));
    }

    @Test
    public void testCOMFooterIsRemoved() throws Exception {
        TextExtractor result = TextExtractor.loadText(COM_FILE_NAME, TextSource.COM);
        result.removeHeader().setTitleAndAuthor().removeSecondHeader().removeEndOfFooter();
        assertTrue(result.story.text.endsWith("by AuthorGoesHere"));
    }

    @Test
    public void testDataIsWrittenToFile() throws Exception {
        Path targetFile = Paths.get("TitleGoesHere - AuthorGoesHere.txt");
        Files.deleteIfExists(targetFile);
        TextExtractor result = TextExtractor.loadText(DE_FILE_NAME, TextSource.DE);
        result.removeHeader().setTitleAndAuthor().removeSecondHeader().removeEndOfFooter();
        result.writeToFile();
        assertTrue(Files.exists(targetFile));
    }

    @Test
    public void testCOMWildCardsVersionRemoveHeader() throws Exception {
        TextExtractor result = TextExtractor.loadText("src/test/resources/wildCardsTest/tor.com", TextSource.COM);
        result.removeHeader();
        String text = result.story.text;
        assertTrue(text.startsWith("TitleGoesHere"));
    }
}