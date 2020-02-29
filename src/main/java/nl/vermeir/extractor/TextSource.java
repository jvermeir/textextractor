package nl.vermeir.extractor;

public enum TextSource {
    COM, DE;
    public static TextSource whatTypeIsThis(String filename) {
        if (filename.indexOf(".de")>0) {
            return DE;
        } else if (filename.indexOf(".com")>0) {
            return COM;
        } else {
            throw new IllegalArgumentException("Unable to find source for file: " + filename);
        }
    }
}
