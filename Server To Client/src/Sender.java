import java.io.ObjectOutputStream;


/**
 * This class is used by the Main controller to send the messages
 */
public class Sender {
    ObjectOutputStream oos;       // TCP output stream

    public Sender(ObjectOutputStream oos) {

        this.oos = oos;
    }

    /**
     * Writes the message on output stream
     */
    public void sendMessage(Message message)
    {
        try {
            oos.writeObject(message);
            oos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
