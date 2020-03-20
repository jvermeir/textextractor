package nl.vermeir.extractor;

public class ComExtractor extends TextExtractor {
    public ComExtractor(Story story) { super(story); }

    @Override
    public TextExtractor removeHeader() {
        int startOfTitle = story.text.indexOf("    Original Fiction") + "    Original Fiction\n".length();
        story.text = story.text.substring(startOfTitle).stripLeading();
        if (story.text.startsWith("Wild Cards on Tor.com")) {
            story.text = story.text.substring("Wild Cards on Tor.com".length()).stripLeading();
        }
        return this;
    }

    @Override
    public TextExtractor setTitleAndAuthor() {
        String[] lines = story.text.split("\n");
        story.title = lines[0];
        story.author = lines[1];
        int startOfAuthor = story.text.indexOf(story.author) + story.author.length()+1;
        story.text = story.text.substring(startOfAuthor);
        return this;
    }

    @Override
    public TextExtractor removeSecondHeader() {
        return this;
    }

    @Override
    public TextExtractor removeEndOfFooter() {
        int positionOfShareText = story.text.lastIndexOf("Share:");
        story.text = story.text.substring(0, positionOfShareText);
        int positionOfAuthorCopyRight = story.text.lastIndexOf(story.author);
        story.text = story.text.substring(0, positionOfAuthorCopyRight + story.author.length());
        return this;
    }
}
