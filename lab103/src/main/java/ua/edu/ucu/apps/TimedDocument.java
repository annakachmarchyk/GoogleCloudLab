package ua.edu.ucu.apps;

public class TimedDocument extends SmartDocument {

    public TimedDocument(String gcsPath) {
        super(gcsPath);
    }

    @Override
    public String parse() {
        long startTime = System.nanoTime();
        String text = super.parse();
        long endTime = System.nanoTime();

        System.out.println("Parsing time: " + (endTime - startTime) / 1000000 + " ms");
        return text;
    }
}