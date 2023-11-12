package ru.otus.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class ChatSession {
    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("members")
    private List<ChatMember> members;

    @JsonProperty("messages")
    private List<ChatMessage> messages;
}
