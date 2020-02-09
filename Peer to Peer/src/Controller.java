import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Controller class for the application.
 * Handles the various GUI events of the application.
 */
public class Controller {

    private ObjectInputStream ois;      //  input stream from the TCP connection (Injected by main method of Client)
    private ObjectOutputStream oos;     //  output stream from the TCP connection (Injected by main method of Client)
    private Task receiver;              //  thread to receive incoming messages
    private Thread t1,t2;               // Thread t1 -> Holder thread for receiver, t2 -> Background thread that uses Runlater calls to the Main apllication thread.
    private Sender sender;              // Sender Object to send the message. ( Single Responsibility Principle )
    private double prevY = 0;           // Y co-ordinate for the new message container
    String senderName,receiverName;     // Stores the current-client name and the friend's name
    ArrayList<Message> incomingList;    // Queue to buffer the incoming messages

    @FXML
    TextField messageBox;               // User enters the message here
    @FXML
    Button sendFile,sendMessage;
    @FXML
    ScrollPane scrollView;
    @FXML
    AnchorPane chatView;               // Container to show the chat history

    /**
     * Handler method -> invoked when user clicks on send button
     */
    @FXML
    public void sendMessageClicked()
    {
        String text = messageBox.getText(); // get message
        TextMessage message = new TextMessage(new Timestamp(System.currentTimeMillis()),senderName,receiverName,text);
        messageBox.clear();
        sender.sendMessage(message); //send the message
        processTextMessage(message); // display the message on screen
    }

    /**
     * Handler method -> invoked when user clicks on send File button
     * Invokes the FileChooser, the user chooses a file to share , the handler creates the corresponding FileMassage Object and
     * sends it using the sender object
     */
    @FXML
    public void sendFileClicked()
    {
        //invoke file chooser

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(sendFile.getParent().getScene().getWindow());
        // The user chooses a file
        if(file==null) // If no file is chosen
            return;
        try {
            // This code generates the FileMessage Object for the file
            FileInputStream stream = new FileInputStream(file);
            ArrayList<Pair<byte[],Integer>> list = new ArrayList<>();
            byte bytes[] = new byte[(int)file.length()];
            int count;
            while((count = stream.read(bytes)) > 0)
            {
                list.add(new Pair<>(bytes,count));
            }
            Message message = new FileMessage(new Timestamp(System.currentTimeMillis()),senderName,receiverName,file.getName(),list);
            sender.sendMessage(message); //send the message
            processFileMessage((FileMessage) message); // display the message on screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter method for Input stream
     */
    public void setObjectInput(ObjectInputStream ois) {
        this.ois = ois;
    }
    /**
     * Setter method for Output stream
     */
    public void setObjectOutput(ObjectOutputStream oos) {
        this.oos = oos;
    }


    /**
     * Does the necessary pre-processing
     * Invoked by the Main Application Thread
     */
    public void instantiate()
    {
        instantiateSender();
        instantiateReceiver();
        incomingList = new ArrayList<>();
    }

    /**
     * Creates the Sender Object with the Output Stream
     */
    public void instantiateSender()
    {
        sender = new Sender(oos);
    }

    /**
     * Creates the Receiver threads
     */
    public void instantiateReceiver()
    {
        /*
            This Thread receives the incoming messages and stores them into the IncomingList Queue Buffer
         */
        receiver = new Task<Void>()
        {
            int a =1,b=1;
            @Override
            public Void call()
            {
                while(a==b)
                {
                    try {
                        Message message = (Message) ois.readObject();
                        synchronized (incomingList)
                        {
                            incomingList.add(message);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        t1 = new Thread(receiver);
        t1.start();
        /*
            This thread uses RunLater method which causes the main Application thread to extract the messages and display on the screen
            from the queue buffer periodically after 100ms
         */
        t2 = new Thread()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Process the messages in the buffer
                                synchronized (incomingList)
                                {
                                    for(Message message : incomingList )
                                        if(message instanceof TextMessage )
                                        {
                                            processTextMessage((TextMessage) message);
                                        }
                                        else
                                        {
                                            extractAndSaveFile((FileMessage) message); // extract and save the file
                                            processFileMessage((FileMessage) message); // display on the screen
                                        }
                                    incomingList.clear();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t2.start();

    }

    /**
     * Generates the TextField for messages to be displayed on the screen
     */
    private TextField generateTextField(String message)
    {
        TextField resp = new TextField(message);
        resp.setPrefWidth(601);
        resp.setPrefHeight(25);
        resp.setLayoutY(prevY);
        resp.setEditable(false);
        prevY += 25.05;
        return resp;
    }

    /**
     * Displays the TextMessage on screen
     */
    private void processTextMessage(TextMessage message)
    {
        chatView.getChildren().add( generateTextField(message.sender + ": " + message.getText()) );
    }
    /**
     * Displays the FileMessage on screen
     */
    private void processFileMessage(FileMessage message)
    {
        chatView.getChildren().add( generateTextField(message.sender + ": " + message.getFileName()) );
    }


    /**
     * This method extracts the file from FileMessage Object and saves it into present working directory
     */
    private void extractAndSaveFile(FileMessage message)
    {
        try {
            File file = new File(message.getFileName());
            FileOutputStream fout = new FileOutputStream(file);
            for(Pair<byte[],Integer> u : message.getData())
                fout.write(u.getKey(),0,u.getValue());
            fout.flush();
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Setter method
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Setter method
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

}
