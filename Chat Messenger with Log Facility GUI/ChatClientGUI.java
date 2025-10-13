// ////////////////////////////////////////////////////////////////////////////////////////////////
// File Name   : ChatClientGUI.java
// Description : A simple GUI-based chat client that connects to a server using sockets.
//               It allows sending and receiving messages with a graphical interface,
//               and logs all messages with timestamps.
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
// Class Name  : ChatClientGUI
// Description : Implements a GUI-based chat client that sends and receives messages from server.
// ////////////////////////////////////////////////////////////////////////////////////////////////

class ChatClientGUI implements ActionListener
{
    // GUI components
    private JFrame fobj;
    private JTextArea chatArea;
    private JButton sendButton;
    private JTextField inputArea;

    // Networking and logging
    private PrintStream outStream;
    private BufferedReader inStream;
    private FileWriter logWriter;
    private DateTimeFormatter dtf;

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor : ChatClientGUI
    // Description : Sets up GUI, initializes socket streams, and starts background receive thread.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public ChatClientGUI(String title, int width, int height, Socket serverSocket) throws Exception
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

        // Connect to server and set up I/O streams
        outStream = new PrintStream(serverSocket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        // Create timestamped log file
        DateTimeFormatter filenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String fileTimestamp = filenameFormat.format(LocalDateTime.now());
        String filename = "ChatLog-Client-" + fileTimestamp + ".txt";
        logWriter = new FileWriter(filename, true);

        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Start background thread to receive messages
        new Thread(this::receiveMessage).start();

        // Finalize GUI
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
    // Description   : Continuously receives messages from the server and appends them to the GUI.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    private void receiveMessage()
    {
        try
        {
            String msg;
            while ((msg = inStream.readLine()) != null)
            {
                String timestamp = dtf.format(LocalDateTime.now());
                chatArea.append("Server: " + msg + "\n");
                logWriter.write("[" + timestamp + "] Server says: " + msg + "\n");
                logWriter.flush();

                if (msg.equalsIgnoreCase("end"))
                {
                    chatArea.append("Server ended the chat.\n");
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
    // Description   : Sends a message typed by the client to the server and logs it.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent aobj)
    {
        String msg = inputArea.getText().trim();
        if (!msg.isEmpty())
        {
            outStream.println(msg);
            String timestamp = dtf.format(LocalDateTime.now());
            chatArea.append("Client: " + msg + "\n");
            try
            {
                logWriter.write("[" + timestamp + "] Client says: " + msg + "\n");
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
    // Description   : Ends the chat session, closes the log file, and disables input.
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
    // Description   : Entry point. Connects to the server and launches the client GUI.
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception
    {
        Socket serverSocket = new Socket("localhost", 5100);
        new ChatClientGUI("Client", 500, 400, serverSocket);
    }
}
