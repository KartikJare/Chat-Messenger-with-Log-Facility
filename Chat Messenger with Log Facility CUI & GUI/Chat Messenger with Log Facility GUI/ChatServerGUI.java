// ////////////////////////////////////////////////////////////////////////////////////////////////
// File Name   : ChatServerGUI.java
// Description : GUI-based Chat Server that communicates with a client using sockets. It logs
//               chat messages with timestamps and displays them in a Swing GUI.
// Author      : Kartik Ganesh Jare
// Date        : 19/09/2025
// ////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ////////////////////////////////////////////////////////////////////////////////////////////////
// Class Name  : ChatServerGUI
// Description : Implements a chat server GUI that can send/receive messages with a client.
// ////////////////////////////////////////////////////////////////////////////////////////////////

class ChatServerGUI implements ActionListener
{
    private JFrame fobj;
    private JTextArea chatArea;
    private JButton sendButton;
    private JTextField inputArea;

    private PrintStream outStream;
    private BufferedReader inStream;
    private FileWriter logWriter;
    private DateTimeFormatter dtf;

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor : ChatServerGUI
    // Description : Sets up GUI components, initializes socket I/O streams, logging, and starts
    //               the message receiving thread.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public ChatServerGUI(String title, int width, int height, Socket clientSocket) throws Exception
    {
        fobj = new JFrame();
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JScrollPane scrollpane = new JScrollPane(chatArea);
        scrollpane.setBounds(20, 20, 450, 250);

        inputArea = new JTextField();
        inputArea.setBounds(20, 290, 340, 30);

        sendButton = new JButton("Send");
        sendButton.setBounds(370, 290, 100, 30);

        sendButton.addActionListener(this);
        inputArea.addActionListener(this);

        outStream = new PrintStream(clientSocket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        DateTimeFormatter filenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String fileTimestamp = filenameFormat.format(LocalDateTime.now());
        String filename = "ChatLog-Server-" + fileTimestamp + ".txt";
        logWriter = new FileWriter(filename, true);

        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        new Thread(this::receiveMessage).start();

        fobj.add(scrollpane);
        fobj.add(inputArea);
        fobj.add(sendButton);
        fobj.setLayout(null);
        fobj.setTitle(title);
        fobj.setSize(width, height);
        fobj.setVisible(true);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Function Name : receiveMessage
    // Description   : Continuously listens for incoming client messages and appends them to GUI.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    private void receiveMessage()
    {
        try
        {
            String msg;
            while ((msg = inStream.readLine()) != null)
            {
                String timestamp = dtf.format(LocalDateTime.now());
                chatArea.append("Client: " + msg + "\n");
                logWriter.write("[" + timestamp + "] Client says: " + msg + "\n");
                logWriter.flush();

                if (msg.equalsIgnoreCase("end"))
                {
                    chatArea.append("Client ended the chat.\n");
                    endChat();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            chatArea.append("Connection closed.\n");
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Function Name : actionPerformed
    // Description   : Sends a message typed by the server to the client and logs it with timestamp.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent aobj)
    {
        String msg = inputArea.getText().trim();
        if (!msg.isEmpty())
        {
            outStream.println(msg);
            String timestamp = dtf.format(LocalDateTime.now());
            chatArea.append("Server: " + msg + "\n");
            try
            {
                logWriter.write("[" + timestamp + "] Server says: " + msg + "\n");
                logWriter.flush();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            inputArea.setText("");
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Function Name : endChat
    // Description   : Ends the chat session, closes the log file, and disables input controls.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    private void endChat()
    {
        try
        {
            String endTime = dtf.format(LocalDateTime.now());
            logWriter.write("---------------------------------------------\n");
            logWriter.write("Chat session ended at: " + endTime + "\n");
            logWriter.write("---------------------------------------------\n\n");
            logWriter.flush();
            logWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        sendButton.setEnabled(false);
        inputArea.setEditable(false);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Function Name : main
    // Description   : Entry point. Waits for client connection and launches the server GUI.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception
    {
        ServerSocket ssobj = new ServerSocket(5100);
        System.out.println("Server waiting for client...");
        Socket clientSocket = ssobj.accept();
        new ChatServerGUI("Server", 500, 400, clientSocket);
    }
}
