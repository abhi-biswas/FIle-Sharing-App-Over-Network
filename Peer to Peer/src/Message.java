import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Base class for the FileMessage and TextMessage
 * Provides single interface for transfer both types of messages
 * For future extendability of message types
 */
abstract public class Message implements Serializable {
    Timestamp timestamp;           // Timestamp for the message
    String sender,receiver;        // name of sender and receiver

    public Message(Timestamp timestamp, String sender, String receiver) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
