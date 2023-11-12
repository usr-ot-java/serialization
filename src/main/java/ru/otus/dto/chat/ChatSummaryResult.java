package ru.otus.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSummaryResult implements Serializable {
    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("membersLast")
    private List<String> membersLast;

    @JsonProperty("messages")
    private List<ChatMessage> messages;
}
