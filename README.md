### File Sharing App over the network


This is a JavaFx based project written using Intellij Idea IDE.
The Jar files for both Peer to Peer and, Server Client application are present in the **Jar folder**  along with the required instructions.
The Source codes are present in **Source Code** Folder.<br>
**Both Server To Client and Peer To Peer applications were thoroughly tested on both localhost as well as Remote Hosts.**


 #### PEER TO PEER
 **Goto ./Jar/Peer To Peer**
 **Instructions to use the Jar File:** <br>
 `1. Open the Terminal or CMD `<br>
 `2. Type the command :` <br>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java -jar Peer_to_Peer.jar ip-address_of_other_client yourname friendname<br>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example:<br>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java -jar Peer_to_Peer.jar 127.0.0.1 abhijeet ash<br>
 `Do step 1 and 2` for both clients<br>
 Now you can send and receive messages and files using the GUI<br>
 ![](./images/Peer_To_Peer/01.png)<hr>
 ![](./images/Peer_To_Peer/02.png)<hr>
 ![](./images/Peer_To_Peer/03.png)<hr>
 ![](./images/Peer_To_Peer/04.png)<hr>
 ![](./images/Peer_To_Peer/05.png)<hr>
 ![](./images/Peer_To_Peer/06.png)<hr>
 <hr>
 
#### Server To client
**Goto the location : ./Jar/Server To Client/** <br>

**Instructions to use the Jar Files:**<br>
`First create the server: `<br>
`1. Open the Terminal or CMD `<br>
`2. Type the command :` <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java -jar Server.jar<br>

![](./images/Server_To_Client/01.png)<hr><br><br>

`For clients: ` <br>
`1. Open the Terminal or CMD `<br>
`2. Type the command :` <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java -jar Client.jar serverIp myname friendname<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java -jar Client.jar 127.0.0.1 abhijeet ash<br>
`Do step 1 and 2` for both clients<br>
![](./images/Server_To_Client/06.png)<hr>
![](./images/Server_To_Client/07.png)<hr>
Now you can send and receive messages and files using the GUI<br>
![](./images/Server_To_Client/02.png)<hr>
![](./images/Server_To_Client/03.png)<hr>
![](./images/Server_To_Client/04.png)<hr>
![](./images/Server_To_Client/05.png)<hr>
<hr>

**In order to use between Remote Hosts just change the localhost IP with corresponding IPV4 address of Hosts/Server**
