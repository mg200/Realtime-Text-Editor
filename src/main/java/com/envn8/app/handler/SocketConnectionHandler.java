package com.envn8.app.handler;

import java.util.ArrayList; 
import java.util.Collections; 
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage; 
import org.springframework.web.socket.WebSocketSession; 
import org.springframework.web.socket.handler.TextWebSocketHandler; 

public class SocketConnectionHandler extends TextWebSocketHandler {

    private static final Map<String, List<WebSocketSession>> documentRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String documentId = getDocumentId(session);
		System.out.println("sssssssssssssssssssssssssssssssss"+documentId);
        if (documentId != null) {
            documentRooms.computeIfAbsent(documentId, key -> new ArrayList<>()).add(session);
            System.out.println(session.getId() + " joined the room for document: " + documentId);
        } else {
            System.out.println("Invalid document ID");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String documentId = getDocumentId(session);
        if (documentId != null) {
            documentRooms.getOrDefault(documentId, Collections.emptyList()).remove(session);
            System.out.println(session.getId() + " left the room for document: " + documentId);
        } 
    }

    @Override
	@SendTo("/topic/{documentId}")
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String documentId = getDocumentId(session);
        if (documentId != null) {
            List<WebSocketSession> sessions = documentRooms.getOrDefault(documentId, Collections.emptyList());
            for (WebSocketSession webSocketSession : sessions) {
                if (!session.getId().equals(webSocketSession.getId())) {
                    webSocketSession.sendMessage(message);
                }
            }
        }
    }

    private String getDocumentId(WebSocketSession session) {
        String documentId = session.getUri().toString();
        int index = documentId.lastIndexOf('/');
        if (index != -1 && index + 1 < documentId.length()) {
            return documentId.substring(index + 1);
        }
        return null;
    }
}