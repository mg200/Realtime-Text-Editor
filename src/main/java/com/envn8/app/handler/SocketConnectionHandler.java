package com.envn8.app.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.envn8.app.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketConnectionHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> documentRooms = new ArrayList<WebSocketSession>();
    private Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();
    private DocumentService documentService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established");
        super.afterConnectionEstablished(session);
        documentRooms.add(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed");

        super.afterConnectionClosed(session, status);
        if (!documentRooms.isEmpty() && documentRooms.contains(session)) {
            System.out.println("HENAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            documentRooms.remove(session);
            removeSessionFromRooms(session);
            System.out.println("   B3d    HENAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
    }

    private void removeSessionFromRooms(WebSocketSession session) {
        for (List<WebSocketSession> sessions : roomSessions.values()) {
            sessions.remove(session);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(message.getPayload(), Map.class);

        String documentId = (String) data.get("documentId");
        Map<String, Object> operation = (Map<String, Object>) data.get("operation");

        if (!roomSessions.containsKey(documentId)) {
            roomSessions.put(documentId, new ArrayList<>());
        }
        roomSessions.get(documentId).add(session);

        if (operation.get("type").equals("insertCharacter")) {
            int position = (int) operation.get("position");
            char character = (char) operation.get("character");
            String beforeId = (String) operation.get("beforeId");
            String afterId = (String) operation.get("afterId");
            // Call the insertCharacter method in the DocumentService
            documentService.insertCharacter(documentId, position, beforeId, afterId, character);
        } else if (operation.get("type").equals("deleteCharacter")) {
            String characterId = (String) operation.get("characterId");
            // Call the deleteCharacter method in the DocumentService
            documentService.deleteCharacter(documentId, characterId);
        }
        // other types of operations...

        // Get the updated document content
        String newContent = documentService.getContent(documentId);
        // Create a new message with the updated content and the operation
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("content", newContent);
        newMessage.put("operation", operation);

        // Convert the new message to a JSON string
        String newMessageJson = mapper.writeValueAsString(newMessage);

        // Send the new message to all sessions in the room
        sendMessage(documentId, new TextMessage(newMessageJson));
    }

    private void sendMessage(String roomId, TextMessage message) {
        if (roomSessions.containsKey(roomId)) {
            List<WebSocketSession> sessions = new ArrayList<>(roomSessions.get(roomId)); // added array here to avoid
                                                                                         // ConcurrentModificationException
            System.out.println(" Now Server sending response " + sessions);
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    } else {
                        roomSessions.remove(roomId, session);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getDocumentId(TextMessage message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(message.getPayload(), Map.class);
        System.out.println("Document ID: " + map.get("documentId"));
        return map.get("documentId");
    }

}