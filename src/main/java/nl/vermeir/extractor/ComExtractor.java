package nl.vermeir.extractor;

import java.util.Arrays;

public class ComExtractor extends TextExtractor {

    public static final String ORIGINAL_FICTION_SF = "Original Fiction Science Fiction";
    public static final String ORIGINAL_FICTION = "Original Fiction";
    public static final String DARK_FICTION  = "Original Fiction Dark Fantasy";
    private static final String AUTHOR = "By ";
    private static final String PUBLISHED = "Published on ";
    public static final String ART_COPYRIGHT = "Art copyright";

    public ComExtractor(Story story) { super(story); }

    @Override
    public TextExtractor removeHeader() {
        int startOfOriginalFiction = story.text.indexOf(ORIGINAL_FICTION);
        int startOfTitle = story.text.indexOf("\n", startOfOriginalFiction);
        story.text = story.text.substring(startOfTitle).stripLeading();
        return this;
    }

    @Override
    public TextExtractor setTitleAndAuthor() {
        String[] lines = story.text.split("\n");
        story.title = lines[0];

        int startOfAuthor = story.text.indexOf(AUTHOR) + AUTHOR.length();
        story.text = story.text.substring(startOfAuthor);
        lines = story.text.split("\n");
        story.author = lines[0];

        int startOfPublished = story.text.indexOf(PUBLISHED);
        story.text = story.text.substring(startOfPublished);
        lines = story.text.split(System.lineSeparator());
        story.text = String.join(System.lineSeparator(), Arrays.copyOfRange(lines, 1, lines.length));

        return this;
    }

    @Override
    public TextExtractor removeSecondHeader() {
        return this;
    }

    @Override
    public TextExtractor removeEndOfFooter() {
        int positionOfArtCopyrightText = story.text.lastIndexOf(ART_COPYRIGHT);
        story.text = story.text.substring(0, positionOfArtCopyrightText);
        return this;
    }
}
