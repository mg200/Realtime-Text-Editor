package com.envn8.app.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.envn8.app.models.CHAR;
import com.envn8.app.models.CharacterSequence;
import com.envn8.app.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Service
public class SocketConnectionHandler extends TextWebSocketHandler implements ApplicationContextAware {
    private static final List<WebSocketSession> documentRooms = new ArrayList<WebSocketSession>();
    private Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();
    private Map<String, CharacterSequence> documentSequences =new HashMap<>();
    private DocumentService documentService;
    private ApplicationContext applicationContext;
    public SocketConnectionHandler(DocumentService documentService) {
        this.documentService = documentService;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.documentService = applicationContext.getBean(DocumentService.class);
    }
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
        // super.handleTextMessage(session, message);
        String documentId = getDocumentId(message);
        CharacterSequence sequences = documentSequences.computeIfAbsent(documentId, k -> new CharacterSequence());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> messageMap = mapper.readValue(message.getPayload(), Map.class);
        Map<String, Object> operation = (Map<String, Object>) messageMap.get("operation");
        
        if (!roomSessions.containsKey(documentId)) {
            roomSessions.put(documentId, new ArrayList<>());
        }
      
        roomSessions.get(documentId).add(session);
        if(operation!=null){
            String operationType= (String)operation.get("operationType");
            System.out.print("Ana geeet hena y3mna "+operationType+" docID"+ documentId);
        if (operationType.equals("insertCharacter")) {
    
            String id = (String) operation.get("id");
            int index = (int) operation.get("indexStart");
            String charValue = (String) operation.get("charValue");
            Object attributes = operation.get("attributes");

            List<CHAR> relatives=sequences.getRelativeIndex(index);
            double startIndex=relatives.get(0).getIndex();
            double endIndex=relatives.get(1).getIndex();
            System.out.println("END ELEMENT"+relatives.get(1).getChar());
            if(startIndex==-1 &&endIndex==-1){
                startIndex=0;
                endIndex=0;
            }
            CHAR insertedChar = sequences.insert(startIndex, endIndex, charValue, attributes, id);
            System.out.println("Ana geeet Tany hena y3mna "+index+"  "+startIndex+" "+endIndex+" "+charValue+" "+id+" "+insertedChar);
            String content = "content"+sequences.getSequence() + "index" + insertedChar.getIndex();
            TextMessage updatedSequenceMessage = new TextMessage(sequences.getSequence());
            System.out.println("final string is "+updatedSequenceMessage);
            sendMessage(documentId, updatedSequenceMessage);
        }else if(operationType.equals("deleteCharacter")){
            int index = (int) operation.get("indexStart");
            String id = (String) operation.get("id");
            List<CHAR> relatives=sequences.getRelativeIndex(index);
            double startIndex=relatives.get(0).getIndex();
            sequences.delete(id);



            

        }
        }
    }

    private void sendMessage(String roomId, TextMessage message) {
        if (roomSessions.containsKey(roomId)) {
            List<WebSocketSession> sessions = roomSessions.get(roomId); 
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