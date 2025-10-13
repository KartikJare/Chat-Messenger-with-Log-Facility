//////////////////////////////////////////////////////////////////////////////////////////////////
//
// File Name   : ChatClientCUI.java
// Description : A simple console-based chat client that connects to a server using sockets.
//               It sends and receives messages and logs all chat messages with timestamps.
// Author      : Kartik Ganesh Jare
// Date        : 19/09/2025
// 
//////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ChatClientCUI
{
    public static void main(String Arg[]) throws Exception
    {
        // Inform user about connection attempt
        System.out.println("Client is ready to connect with server");

        // Create socket and connect to server on localhost port 5100
        Socket sobj = new Socket("localhost",5100);
        System.out.println("Marvellous client is successfully connected with server");

        // Output stream to send messages to server
        PrintStream pobj = new PrintStream(sobj.getOutputStream());

        // Input stream to receive messages from server
        BufferedReader bobj1 = new BufferedReader(new InputStreamReader(sobj.getInputStream()));

        // Input stream to take input from client user (keyboard)
        BufferedReader bobj2 = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("------------------------------------------");
        System.out.println("Marvellous Chat Messenger is ready to use");
        System.out.println("------------------------------------------");

        // Variables to hold messages
        String str1 = null, str2 = null;

        // Create a timestamped log file for chat history
        DateTimeFormatter filenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String fileTimestamp = filenameFormat.format(LocalDateTime.now());
        String filename = "ChatLog-Client-" + fileTimestamp + ".txt";

        FileWriter fwobj = new FileWriter(filename, true);

        // Formatter for timestamps in log file
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Chat loop: runs until user types "end"
        while(!(str1 = bobj2.readLine()).equals("end"))
        {
            // Send message to server
            pobj.println(str1);

            // Receive reply from server
            str2 = bobj1.readLine();
            System.out.println("Server says :"+str2);

            // Log both client and server messages
            fwobj.write("Client sent:"+str1);
            fwobj.write("Server says :"+str2);
            fwobj.write(System.lineSeparator());
            fwobj.flush();

            System.out.println("Enter message for server :");

            // Log messages with timestamps
            String timestamp = dtf.format(LocalDateTime.now());
            fwobj.write("[" + timestamp + "] Client sent: " + str1 + "\n");
            timestamp = dtf.format(LocalDateTime.now());
            fwobj.write("[" + timestamp + "] Server says: " + str2 + "\n");
        }

        // Close log file when chat ends
        fwobj.close();
    }
}