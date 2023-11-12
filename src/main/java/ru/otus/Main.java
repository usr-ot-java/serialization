package ru.otus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import ru.otus.dto.chat.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        xmlMapper.registerModule(new JavaTimeModule());
    }

    private static final String INPUT_FILE_PATH = "sms.json";
    private static final String OUTPUT_FILE_PATH = "sms.xml";

    private static final TypeReference<ChatSessions> CHAT_SESSIONS_TYPE_REF = new TypeReference<>() {};
    private static final TypeReference<List<ChatSummaryResult>> LIST_CHAT_SUMMARY_RESULT_TYPE_REF =
            new TypeReference<>() {};

    public static void main(String[] args) throws IOException {
        byte[] file = FileUtils.readFileToByteArray(new File(INPUT_FILE_PATH));
        ChatSessions chatSessions = objectMapper.readValue(file, CHAT_SESSIONS_TYPE_REF);

        List<ChatSummaryResult> chatSummaryResults = new ArrayList<>();
        for (ChatSession chatSession : chatSessions.getChatSessions()) {
            String chatIdentifier = chatSession.getChatIdentifier();
            List<String> membersLast =
                    chatSession.getMembers().stream().map(ChatMember::getLast).collect(Collectors.toList());

            Map<String, ChatMessage> groupedChatMessages = new HashMap<>();
            for (ChatMessage chatMessage : chatSession.getMessages()) {
                String belongNumber = chatMessage.getBelongNumber();
                if (!groupedChatMessages.containsKey(belongNumber)) {
                    groupedChatMessages.put(belongNumber, chatMessage);
                }
            }

            List<ChatMessage> messages = groupedChatMessages.entrySet().stream()
                    .sorted(Comparator.comparing(o -> o.getValue().getSendDate()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            chatSummaryResults.add(new ChatSummaryResult(chatIdentifier, membersLast, messages));
        }

        System.out.println("Serializing data to XML");
        xmlMapper.writeValue(new File(OUTPUT_FILE_PATH), chatSummaryResults);

        List<ChatSummaryResult> chatSummaryResult =
                xmlMapper.readValue(new File(OUTPUT_FILE_PATH), LIST_CHAT_SUMMARY_RESULT_TYPE_REF);
        System.out.println("Deserialized data from XML:");
        System.out.println(chatSummaryResult);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(chatSummaryResult);
        }
        System.out.println("Serialized data:");
        System.out.println(byteArrayOutputStream);
    }
}
