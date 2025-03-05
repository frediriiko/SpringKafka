import { useState, useEffect, useRef } from "react";
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Card,
  CardContent,
  Box,
} from "@mui/material";

const ChatApp = () => {
  const ws = useRef<WebSocket | null>(null);
  const [messages, setMessages] = useState<
    { author: string; content: string; timestamp: string }[]
  >([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (ws.current) return;

    ws.current = new WebSocket("ws://localhost:8080/ws");

    ws.current.onopen = () => {
      console.log("✅ Connected to WebSocket");
    };

    ws.current.onmessage = (event) => {
      const receivedMessage = JSON.parse(event.data);
      setMessages((prev) => [...prev, receivedMessage]);
    };

    ws.current.onclose = (event) => {
      console.warn(`❌ WebSocket closed: Code ${event.code}, Reason: ${event.reason}`);
      setTimeout(() => {
        console.log("🔄 Attempting to reconnect...");
        ws.current = new WebSocket("ws://localhost:8080/ws");
      }, 5000);
    };

    ws.current.onerror = (error) => {
      console.error("⚠️ WebSocket Error: ", error);
    };

    return () => {
      if (ws.current && ws.current.readyState === WebSocket.OPEN) {
        console.log("Closing WebSocket connection...");
        ws.current.close();
      }
    };
  }, []);

  const sendMessage = () => {
    if (ws.current && ws.current.readyState === WebSocket.OPEN && message.trim()) {
      const messageObject = {
        author: "John",
        content: message,
        timestamp: new Date().toISOString(),
      };

      ws.current.send(JSON.stringify(messageObject));
      setMessage("");
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
        <Typography variant="h5" gutterBottom align="center">
          Chat App
        </Typography>
        <Box
          sx={{
            border: "1px solid #ccc",
            borderRadius: 2,
            p: 2,
            height: 300,
            overflowY: "auto",
            display: "flex",
            flexDirection: "column",
            gap: 1,
          }}
        >
          {messages.map((msg, index) => (
            <Card key={index} sx={{ backgroundColor: "#f5f5f5", p: 1 }}>
              <CardContent>
                <Typography variant="subtitle2" color="primary">
                  {msg.author} - {new Date(msg.timestamp).toLocaleTimeString()}
                </Typography>
                <Typography>{msg.content}</Typography>
              </CardContent>
            </Card>
          ))}
        </Box>
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
      </Paper>
    </Container>
  );
};

export default ChatApp;
