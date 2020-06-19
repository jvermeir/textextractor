package nl.vermeir.extractor;

public class DeExtractor extends TextExtractor {

    public DeExtractor(Story story) {
        super(story);
    }

    @Override
    public TextExtractor removeHeader() {
        int startOfFictionFridayText = story.text.indexOf("FICTION FRIDAY");
        int startOfFictionText = story.text.indexOf("FICTION");
        int startOfText = startOfFictionText + "FICTION\n".length();
        if (startOfFictionFridayText == startOfFictionText) {
            startOfText = startOfFictionText + "FICTION FRIDAY\n".length();
        }
        story.text = story.text.substring(startOfText);
        return this;
    }

    @Override
    public TextExtractor setTitleAndAuthor() {
        String[] lines = story.text.split("\n");
        String titleLine = lines[0];
        int openingParenthesis = titleLine.indexOf('(');
        int closingParenthesis = titleLine.indexOf(')');
        if (openingParenthesis>0) {
            story.title = story.text.substring(0, openingParenthesis - 1).trim();
            story.author = story.text.substring(openingParenthesis + 1, closingParenthesis).trim();
        } else {
            titleLine = titleLine.replace('|', '–');
            String[] titleParts = titleLine.split("–");
            story.title =  titleParts[0].trim();
            story.author = lines[1];
        }
        return this;
    }

    @Override
    public TextExtractor removeSecondHeader() {
        int merken = story.text.indexOf("Merken") + "Merken\n".length();
        story.text = story.text.substring(merken).replaceFirst("^\\s+", "");
        return this;
    }

    @Override
    public TextExtractor removeEndOfFooter() {
        int startOfFooter = story.text.indexOf("Alle Rechte vorbehalten");
        int endOfCopyRightText = 0;
        if (startOfFooter<0) {
            startOfFooter = story.text.indexOf("by "+story.author);
            endOfCopyRightText = startOfFooter + ("by "+story.author).length();
        }
        story.text = story.text.substring(0, Math.max(startOfFooter, endOfCopyRightText)).replaceFirst("\\s+$", "");;
        return this;
    }
}
