import { useState, useEffect, useRef } from "react";

const ChatApp = () => {
  const ws = useRef<WebSocket | null>(null);
  const [messages, setMessages] = useState<string[]>([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (ws.current) return; // Prevent duplicate connections

    ws.current = new WebSocket("ws://localhost:8080/ws");

    ws.current.onopen = () => {
      console.log("✅ Connected to WebSocket");
    };

    ws.current.onmessage = (event) => {
      setMessages((prev) => [...prev, event.data]);
    };

    ws.current.onclose = (event) => {
      console.warn(`❌ WebSocket closed: Code ${event.code}, Reason: ${event.reason}`);
      setTimeout(() => {
        console.log("🔄 Attempting to reconnect...");
        ws.current = new WebSocket("ws://localhost:8080/ws");
      }, 5000); // Reconnect after 5 seconds
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
  }, []); // Run only on mount

  const sendMessage = () => {
    if (ws.current && ws.current.readyState === WebSocket.OPEN && message.trim()) {
      ws.current.send(message);
      setMessage("");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "500px", margin: "auto" }}>
      <h2>Chat App</h2>
      <div style={{ border: "1px solid black", padding: "10px", height: "300px", overflowY: "auto" }}>
        {messages.map((msg, index) => (
          <div key={index}>{msg}</div>
        ))}
      </div>
      <input
        type="text"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        style={{ width: "80%", padding: "5px" }}
      />
      <button onClick={sendMessage} style={{ padding: "5px 10px" }}>
        Send
      </button>
    </div>
  );
};

export default ChatApp;