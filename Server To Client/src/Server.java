import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the driver code for the server.
 * It acts as an intermediate between the two client
 */
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9080);
        // Waiting for the two clients
        ObjectOutputStream out[] = new ObjectOutputStream[2];
        ObjectInputStream in[] = new ObjectInputStream[2];
        for(int i = 0; i < 2; i++)
        {
            Socket socket = serverSocket.accept();
            //get the input stream for the ith client
            in[i] = new ObjectInputStream(socket.getInputStream());
            //get the output stream for the ith client
            out[i] = new ObjectOutputStream(socket.getOutputStream());
        }

        // Handler1 thread -> receives messages from first client and send the received message to the second client
        ServerSideHandler handler1 = new ServerSideHandler(in[0],out[1]);
        // Handler2 thread -> receives messages from second client and send the received message to the first client
        ServerSideHandler handler2 = new ServerSideHandler(in[1],out[0]);

        // starting the handler threads
        Thread t1 = new Thread(handler1);
        Thread t2 = new Thread(handler2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
