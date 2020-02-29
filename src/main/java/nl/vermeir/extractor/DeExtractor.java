package nl.vermeir.extractor;

public class DeExtractor extends TextExtractor {

    public DeExtractor(Story story) {
        super(story);
    }

    @Override
    public TextExtractor removeHeader() {
        int startOfTitle = story.text.indexOf("FICTION FRIDAY") + "FICTION FRIDAY\n".length();
        story.text = story.text.substring(startOfTitle);
        return this;
    }

    @Override
    public TextExtractor setTitleAndAuthor() {
        int openingParenthesis = story.text.indexOf('(');
        int closingParenthesis = story.text.indexOf(')');
        story.title = story.text.substring(0, openingParenthesis-1).trim();
        story.author = story.text.substring(openingParenthesis+1, closingParenthesis).trim();
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
        int alleRechteVorbehalten = story.text.indexOf("Alle Rechte vorbehalten.");
        story.text = story.text.substring(0, alleRechteVorbehalten).replaceFirst("\\s+$", "");;
        return this;
    }
}
