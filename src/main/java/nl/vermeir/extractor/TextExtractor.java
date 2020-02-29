package nl.vermeir.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    }
}
