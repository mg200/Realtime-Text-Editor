package com.envn8.app.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        documentRooms.add(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if (documentRooms != null && documentRooms.contains(session)) {
            documentRooms.remove(session);
        }
    }

    // @Override
    // protected void handleTextMessage(WebSocketSession session, TextMessage
    // message) throws Exception {
    // super.handleTextMessage(session, message);
    // String roomId = getDocumentId(message); // Assuming you have a method to get
    // the room ID

    // }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String roomId = getDocumentId(message); // You need to implement this method
        
        // Add the session to the room's list
        if (!roomSessions.containsKey(roomId)) {
            roomSessions.put(roomId, new ArrayList<>());
        }
        roomSessions.get(roomId).add(session);
        sendMessage(roomId, message);

    }

    private void sendMessage(String roomId, TextMessage message) {
        if (roomSessions.containsKey(roomId)) {
            for (WebSocketSession session : roomSessions.get(roomId)) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // private String getDocumentId(TextMessage message) throws IOException {
    //     // message.getPayload().documentId;
    //     System.out.println("Document ID: " + message.getPayload();
    //     return message.getPayload();
    // }

    private String getDocumentId(TextMessage message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(message.getPayload(), Map.class);
        System.out.println("Document ID: " + map.get("documentId"));
        return map.get("documentId");
    }
}