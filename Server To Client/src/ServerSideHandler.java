import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

/**
 * @author Abhijeet Biswas
 * This the code for Handler thread which receives message from one client's ObjectOutputStream and send it on the other's ObjectInputStream.
 */
public class ServerSideHandler implements Runnable{

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     *
     * @param inputStream The Handler keeps on listening to this input stream for incoming message.
     * @param outputStream The Handler sends the received message on this stream.
     */
    public ServerSideHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                //Read Incoming Message
                Object obj = inputStream.readObject();
                //Send the received message
                outputStream.writeObject(obj);
                outputStream.flush();
            }
            catch (EOFException exp)
            {
                break;
            }
            catch (IOException exp)
            {
                exp.printStackTrace();
                break;
            }
            catch (ClassNotFoundException exp)
            {
                exp.printStackTrace();
                break;
            }
        }
    }
}
