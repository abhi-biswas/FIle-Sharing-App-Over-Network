import javafx.util.Pair;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @date 31 Jan 2020
 * This class encapsulates the File sent between the clients
 */
public class FileMessage extends Message {
    String FileName;                  // stores the filename
    ArrayList<Pair<byte[],Integer>> data;  // stores the data of file as a List of bytes

    public FileMessage(Timestamp timestamp, String sender, String receiver, String FileName, ArrayList<Pair<byte[],Integer>> data) {
        super(timestamp, sender, receiver);
        this.FileName = FileName;
        this.data = data;
    }

    public String getFileName() {
        return FileName;
    }

    public ArrayList<Pair<byte[],Integer>>  getData() {
        return data;
    }
}
