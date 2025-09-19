//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// File Name     : ChatServerCUI.java
// Description   : A simple console-based chat server that communicates with a client using sockets.
//                 It logs all messages with timestamps into a text file.
// Author         : Kartik Ganesh Jare
// Date           : 19/09/2025
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ChatServerCUI
{
    public static void main(String Arg[]) throws Exception
    {
        /// Create a server socket to listen on port 5100
        ServerSocket ssobj = new ServerSocket(5100);
        System.out.println("Marvellous Server is waiting at port number 5100");

        /// Accept the client connection request
        Socket sobj = ssobj.accept();
        System.out.println("Marvellous Server successfully connected with client");

        /// Create output stream to send messages to client
        PrintStream pobj = new PrintStream(sobj.getOutputStream());

        /// Create input stream to receive messages from client
        BufferedReader bobj1 = new BufferedReader(new InputStreamReader(sobj.getInputStream()));

        /// Create input stream to take input from server user (keyboard)
        BufferedReader bobj2 = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("------------------------------------------");
        System.out.println("Marvellous Chat Messenger is ready to use");
        System.out.println("------------------------------------------");

        /// Variables to hold messages from client and server
        String str1 = null, str2 = null;

        /// Create a timestamped log file for chat history
        DateTimeFormatter filenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String fileTimestamp = filenameFormat.format(LocalDateTime.now());
        String filename = "ChatLog-Server-" + fileTimestamp + ".txt";

        FileWriter fwobj = new FileWriter(filename, true);

        /// Formatter for message timestamps
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /// Loop to continuously exchange messages until client disconnects
        while((str1 = bobj1.readLine()) != null)
        {
            /// Display and log the client message
            System.out.println("Client says :"+str1);
            fwobj.write("Client says:"+str1);
            fwobj.write(System.lineSeparator());

            /// Prompt server user to send reply
            System.out.println("Enter the message for client :");
            str2 = bobj2.readLine();
            pobj.println(str2);
            fwobj.write("Server says: "+str2);
            fwobj.flush();

            /// Log both messages with proper timestamps
            String timestamp = dtf.format(LocalDateTime.now());
            fwobj.write("[" + timestamp + "] Client says: " + str1 + "\n");

            System.out.print("Enter the message for client: ");
            str2 = bobj2.readLine();

            timestamp = dtf.format(LocalDateTime.now());
            fwobj.write("[" + timestamp + "] Server says: " + str2 + "\n");
        }

        /// Close the log file after chat ends
        fwobj.close();
    }
}