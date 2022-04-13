package nl.vermeir.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TextExtractor {
    public abstract TextExtractor removeHeader();

    public abstract TextExtractor setTitleAndAuthor();

    public abstract TextExtractor removeSecondHeader();

    public abstract TextExtractor removeEndOfFooter();

    final Story story;

    public TextExtractor(Story story) {
        this.story = story;
    }

    public void writeToFile() throws IOException {
        String filename = story.title + " - " + story.author + ".txt";
        Files.write(Paths.get(filename), story.text.getBytes());
        System.out.println("Output in " + filename);
    }

    static TextExtractor loadText(String filename, TextSource source) throws Exception {

        Path path = Paths.get(filename);

        String text = new String(Files.readAllBytes(path));
        Story story = new Story(text);
        switch (source) {
            case DE:
                return new DeExtractor(story);
            case COM:
                return new ComExtractor(story);
            default:
                throw new IllegalArgumentException("Unknown source: " + source);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java nl.vermeir.extractor.TorComExtractor <filename>\nNote:filenames should end in .de or .com");
            System.exit(1);
        }
        String filename = args[0];

        TextExtractor text = TextExtractor.loadText(filename, TextSource.whatTypeIsThis(filename))
                .removeHeader()
                .setTitleAndAuthor()
                .removeSecondHeader()
                .removeEndOfFooter();
        text.writeToFile();

        poorMansCalibreSolution(text);
    }

    private static void poorMansCalibreSolution(TextExtractor text) {
        // Note: calling calibredb commands using Java's Process class doesn't seem to work.
        // When called through Process, calibredb can't find files.

        String txtFileName = text.story.title + " - " + text.story.author + ".txt";
        txtFileName = txtFileName.replaceAll(" ", "\\\\ ");

        String calibreConvert = "/Applications/calibre.app/Contents/MacOS/ebook-convert";
        String epubName = txtFileName.replace(".txt", ".epub");
        String[] convertCommand = {calibreConvert, txtFileName,epubName };
        System.out.println(String.join(" ", convertCommand));

        String calibredb = "/Applications/calibre.app/Contents/MacOS/calibredb";
        String author = "\"" + text.story.author + "\"";
        String title = "\"" + text.story.title + "\"";
        String[] addCommand = {calibredb, "add", "-a", author, "-t", title, epubName};
        System.out.println(String.join(" ", addCommand));
    }
}
