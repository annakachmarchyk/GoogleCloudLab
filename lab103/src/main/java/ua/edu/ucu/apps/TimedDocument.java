package ua.edu.ucu.apps;

public class TimedDocument implements Document {

    private SmartDocument decoratedDocument;

    public TimedDocument(Document document) {
        this.decoratedDocument = (SmartDocument) document;
    }

    @Override
    public String parse() {
        long startTime = System.nanoTime();
        String text = decoratedDocument.parse();
        long endTime = System.nanoTime();

        System.out.println("Parsing time: " + (endTime - startTime) / 1000000 + " ms");
        return text;
    }

    public String getGcsPath() {
        return decoratedDocument.getGcsPath();
    }
}
