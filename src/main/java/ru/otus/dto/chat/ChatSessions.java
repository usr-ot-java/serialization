package ru.otus.dto.chat;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class ChatSessions {
    @JsonProperty("chat_sessions")
    private List<ChatSession> chatSessions;
}
