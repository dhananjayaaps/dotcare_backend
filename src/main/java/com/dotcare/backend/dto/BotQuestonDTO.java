package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotQuestonDTO {
    private String nic;
    private String question;
    private String answer;

    public BotQuestonDTO(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public BotQuestonDTO() {

    }
}
