public class TranslationRequest {
    private int requestID;
    private String text;
    private String targetLanguage;

    public TranslationRequest(int requestID, String text, String targetLanguage) {
        this.requestID = requestID;
        this.text = text;
        this.targetLanguage = targetLanguage;
    }

    public int getRequestID() {
        return requestID;
    }

    public String getText() {
        return text;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }
}
