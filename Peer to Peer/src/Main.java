import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.Thread.sleep;


/**
 * @author Abhijeet Biswas
 * @date 31 Jan 2020
 * This is the entry-point for the JavaFx application.
 * This class establishes the connection to other client and initializes the corresponding controller object.
 */
public class Main extends Application {
    private static boolean master;
    private static ObjectInputStream is;
    private static ObjectOutputStream os;
    private static boolean done;
    private static String senderName,receiverName;

    /**
     * This method overrides the start method of Application class and, loads and initializes the controller.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.<Controller>getController();
        controller.setObjectInput(is);
        controller.setObjectOutput(os);
        controller.setReceiverName(receiverName);
        controller.setSenderName(senderName);
        controller.instantiate();
        primaryStage.setTitle(senderName);
        primaryStage.setScene(new Scene(root, 601, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Entry point for the application.
     * It creates the TCP connection between the clients.
     * @param args Expects Three arguments : ip-address_of_other_client your-name friend-name
     *             ip-address_of_other_client -> It is the IP address of the other client
     *             your-name -> Name to be shown when you send a message.
     *             friend-name -> name to be associated with the other client.
     */
    public static void main(String[] args) throws Exception {
        if(args.length!=3)
        {
            throw new Exception("Usage: ip-address_of_the_server your-name friend-name");
        }
        /*
            This code establishes the connection.
            First this clients tries to connect as a TCP client.
            If the first attempt fails the client becomes the TCP server and waits for the other client.
            This way the client which is started first becomes the TCP server and the latter becomes the TCP client.
         */
       Thread t1 = new Thread(){
            @Override
            public void run()
            {

                try {
                    ServerSocket serverSocket = new ServerSocket(9080);
                    Socket socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress().getHostAddress());
                    is = new ObjectInputStream(socket.getInputStream());
                    os = new ObjectOutputStream(socket.getOutputStream());
                    synchronized(Main.class) {
                        done = true;
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run()
            {

                try {

                    Socket socket = new Socket(args[0],9080);

                    os = new ObjectOutputStream(socket.getOutputStream());
                    is = new ObjectInputStream(socket.getInputStream());
                    synchronized(Main.class) {
                        done = true;
                    }

                }
                catch (Exception e)
                {

                }

            }
        };
        /*
            Thread t2 -> try as TCP client
            Thread t1 -> Become TCP server
         */
        t2.start();
        t2.join();
        if(!done)
            t1.start();
        while (true)
        {
            synchronized (Main.class)
            {
                if(done)
                    break;
            }
            sleep(300);
        }
        senderName = args[1];
        receiverName = args[2];
        launch(args);
    }
    @Override
    public void stop()
    {
        System.out.println("Stage is closing");
    }
}
