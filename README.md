# 💬 Chat Messenger with Log Facility (Java Project)

## 📌 Description

The **Chat Messenger with Log Facility** is a real-time communication application developed in Java that allows users to exchange messages over a network while maintaining a persistent log of conversations.

The project is divided into two modules:

* 🖥️ **CUI (Console User Interface)** – Lightweight terminal-based chat
* 🪟 **GUI (Graphical User Interface)** – User-friendly interface using Java GUI framework

---

## 🚀 Features

* 💬 Real-time messaging between multiple clients
* 🌐 Client-server architecture using sockets
* 🧵 Concurrent communication (multi-client handling)
* 📝 Chat logging (stores messages in file)
* 🖥️ Dual interface:

  * CUI (Terminal-based)
  * GUI (Visual interface)
* 📤 Send & receive messages instantly

---

## 🛠️ Technologies Used

* Language: **Java**
* Concepts:

  * Socket Programming (`Socket`, `ServerSocket`)
  * Multithreading
  * File Handling (Logging)
  * Client-Server Architecture
* GUI:

  * Java Swing / AWT (based on your implementation)

---

## 📂 Project Structure

```bash
Chat-Messenger-with-Log-Facility/
│── CUI/
│   ├── Server.java
│   ├── Client.java
│
│── GUI/
│   ├── ServerGUI.java
│   ├── ClientGUI.java
│
│── README.md
```

---

## ⚙️ How It Works

### 🔹 Server

* Starts a server using `ServerSocket`
* Accepts multiple client connections
* Handles each client using **threads**
* Broadcasts messages to all connected clients
* Saves chat messages into a log file

### 🔹 Client

* Connects to server using IP + port
* Sends messages to server
* Receives messages from other clients in real-time

---

## 🧑‍💻 How to Run

### 🔧 Compile (CUI)

```bash
javac Server.java
javac Client.java
```

### ▶️ Run Server (CUI)

```bash
java Server
```

### ▶️ Run Client (CUI)

```bash
java Client
```

---

### 🪟 Run GUI Version

```bash
javac ServerGUI.java
javac ClientGUI.java

java ServerGUI
java ClientGUI
```
---

## 🔒 Requirements

* Java JDK 8 or above
* Any OS (Windows/Linux/Mac)
* Basic networking setup (localhost or LAN)

---

## 📈 Future Improvements

* 🔐 User authentication (login system)
* 👥 Private messaging
* 🏠 Chat rooms (group chat)
* 🔒 Encryption for secure communication
* 🌐 Deploy over internet
* 📱 Mobile app integration

---

## 💡 Learning Outcomes

* Real-time communication systems
* Java socket programming
* Multithreading in Java
* GUI development
* File logging and persistence

---

## 👤 Author

**Kartik Ganesh Jare**
---

## ⭐ Why This Project Matters

This project combines:

* Networking
* Concurrency
* GUI + CUI design

It demonstrates the ability to build **complete communication systems**, which is valuable for:

* Backend Development
* Full Stack Systems
* Distributed Applications

---
