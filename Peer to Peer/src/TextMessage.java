import java.sql.Timestamp;

/**
 * @date 31 Jan 2020
 * This class encapsulates the TextMessage sent between the clients
 */
public class TextMessage extends Message  {
    private String text;    // The text message

    public TextMessage(Timestamp timestamp, String sender, String receiver, String text) {
        super(timestamp, sender, receiver);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
