import { useState, useEffect, useRef } from "react";
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Select,
  MenuItem,
  List,
} from "@mui/material";
import axios from "axios"; // Import Axios
import ChatMessage from "./components/ChatMessage";


const ChatApp = () => {
  const ws = useRef<WebSocket | null>(null);
  const [messages, setMessages] = useState<
    { author: string; content: string; timestamp: string }[]
  >([]);
  const [message, setMessage] = useState("");
  const [username, setUsername] = useState("");
  const [roomId, setRoomId] = useState(""); 
  const [isConnected, setIsConnected] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  const rooms = ["room-1", "room-2", "room-3"];

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);
  

  /**
   * Fetch chat history from REST API
   */
  const fetchChatHistory = async (roomId: string) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/messages/${roomId}`);
      setMessages(response.data);
    } catch (error) {
      console.error("Error fetching chat history:", error);
    }
  };

  /**
   * Connects to the WebSocket server with the selected room & username.
   */
  const connectWebSocket = async () => {
    if (!username.trim() || !roomId.trim()) {
      alert("Please enter a username and select a chat room.");
      return;
    }

    if (ws.current) {
      ws.current.close();
    }

    setMessages([]); // Clear messages before fetching history

    // Fetch chat history before connecting to WebSocket
    await fetchChatHistory(roomId);

    ws.current = new WebSocket(
      `ws://localhost:8080/ws?roomId=${roomId}&username=${username}`
    );

    ws.current.onopen = () => {
      console.log(`✅ Connected to WebSocket (Room: ${roomId})`);
      setIsConnected(true);
    };

    ws.current.onmessage = (event) => {
      const receivedMessage = JSON.parse(event.data);
      setMessages((prev) => [...prev, receivedMessage]);
    };

    ws.current.onclose = (event) => {
      console.warn(
        `❌ WebSocket closed: Code ${event.code}, Reason: ${event.reason}`
      );
      setIsConnected(false);
    };

    ws.current.onerror = (error) => {
      console.error("WebSocket Error: ", error);
    };
  };

  const sendMessage = () => {
    if (ws.current && ws.current.readyState === WebSocket.OPEN && message.trim()) {
      const messageObject = {
        author: username,
        content: message,
        timestamp: new Date().toISOString(),
        roomId: roomId,
      };

      ws.current.send(JSON.stringify(messageObject));
      setMessage("");
    }
  };

  const leaveRoom = () => {
    if (ws.current) {
      ws.current.close();
    }
    setIsConnected(false);
    setMessages([]); // Clear messages on leave
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
        <Typography variant="h5" gutterBottom align="center">
          Chat App
        </Typography>

        {!isConnected ? (
          <>
            <TextField
              fullWidth
              label="Username"
              variant="outlined"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              sx={{ mb: 2 }}
            />
            <Select
              fullWidth
              value={roomId}
              onChange={(e) => setRoomId(e.target.value)}
              displayEmpty
              sx={{ mb: 2 }}
            >
              <MenuItem value="" disabled>
                Select a chat room
              </MenuItem>
              {rooms.map((room) => (
                <MenuItem key={room} value={room}>
                  {room}
                </MenuItem>
              ))}
            </Select>
            <Button variant="contained" color="primary" onClick={connectWebSocket}>
              Join Room
            </Button>
          </>
        ) : (
          <>
            <Typography variant="subtitle1" gutterBottom>
              Connected as: <b>{username}</b> in <b>{roomId}</b>
            </Typography>

            {/* Message List */}
            <Box
            component={Paper}
            elevation={2}
            sx={{
              borderRadius: 2,
              height: 300, // Fixed height for scrollability
              overflowY: "auto", // Enables vertical scrolling
              p: 2,
              display: "flex",
              flexDirection: "column",
            }}
          >
            <List sx={{ width: "100%", padding: 0 }}>
              {messages.map((msg, index) => (
                <ChatMessage
                  key={index}
                  author={msg.author}
                  content={msg.content}
                  timestamp={msg.timestamp}
                  isCurrentUser={msg.author === username}
                />
              ))}
              <div ref={messagesEndRef} />
            </List>
          </Box>


            {/* Message Input */}
            <Box sx={{ display: "flex", mt: 2, gap: 1 }}>
              <TextField
                fullWidth
                variant="outlined"
                placeholder="Type a message..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              />
              <Button variant="contained" color="primary" onClick={sendMessage}>
                Send
              </Button>
            </Box>

            {/* Leave Room Button */}
            <Button
              variant="contained"
              color="secondary"
              sx={{ mt: 2 }}
              onClick={leaveRoom}
            >
              Leave Room
            </Button>
          </>
        )}
      </Paper>
    </Container>
  );
};

export default ChatApp;
