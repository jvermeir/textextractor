package nl.vermeir.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        addToCalibre(text);
    }

    private static void addToCalibre(TextExtractor text) {
        String txtFileName = text.story.title + " - " + text.story.author + ".txt";

        makeSureCalibreIsntRunning();

        String calibreConvert = "/Applications/calibre.app/Contents/MacOS/ebook-convert";
        String epubName = txtFileName.replace(".txt", ".epub");
        String[] convertCommand = {calibreConvert, txtFileName, epubName };
        System.out.println(String.join(" ", convertCommand));
        run(convertCommand);

        String calibredb = "/Applications/calibre.app/Contents/MacOS/calibredb";
        String author = text.story.author;
        String title = text.story.title;
        String[] addCommand = {calibredb, "add", "-a", author, "-t", title, epubName};
        System.out.println(String.join(" ", addCommand));
        run(addCommand);
    }

    private static void run(String[] command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void makeSureCalibreIsntRunning() {
        // First check if the process is running
        if (isProcessRunning("calibre")) {
            System.out.println("Killing process: " + "calibre");
            String[] command = {"killall", "calibre"};
            run(command);

            // Wait for the process to terminate
            int maxAttempts = 10;
            int attempts = 0;
            while (isProcessRunning("calibre") && attempts < maxAttempts) {
                System.out.println("Waiting for " + "calibre" + " to terminate...");
                try {
                    Thread.sleep(500); // Wait 500ms between checks
                    attempts++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Force kill if normal kill didn't work
            if (isProcessRunning("calibre")) {
                System.out.println("Force killing process: " + "calibre");
                String[] forceCommand = {"killall", "-9", "calibre"};
                run(forceCommand);
            }
        }
    }

    private static boolean isProcessRunning(String processName) {
        try {
            String[] command = {"pgrep", processName};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();

            int exitCode = process.waitFor();
            return exitCode == 0; // Exit code 0 means process was found
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
