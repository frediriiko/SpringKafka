import { ListItem, Paper, Typography } from "@mui/material";

interface ChatMessageProps {
  author: string;
  content: string;
  timestamp: string;
  isCurrentUser: boolean;
}

const ChatMessage = ({ author, content, timestamp, isCurrentUser }: ChatMessageProps) => {
  return (
    <ListItem
      sx={{
        alignItems: "flex-start",
        justifyContent: isCurrentUser ? "flex-end" : "flex-start",
        padding: "4px 16px",
      }}
    >
      <Paper
        elevation={1}
        sx={{
          maxWidth: "70%",
          padding: "8px 16px",
          backgroundColor: isCurrentUser ? "#e3f2fd" : "#f5f5f5",
          borderRadius: "12px",
        }}
      >
        <Typography
          variant="subtitle2"
          sx={{
            fontWeight: "bold",
            color: isCurrentUser ? "#1976d2" : "#424242",
          }}
        >
          {author}
        </Typography>
        <Typography variant="body1" sx={{ wordBreak: "break-word" }}>
          {content}
        </Typography>
        <Typography variant="caption" sx={{ color: "text.secondary" }}>
          {new Date(timestamp).toLocaleTimeString()}
        </Typography>
      </Paper>
    </ListItem>
  );
};

export default ChatMessage; 