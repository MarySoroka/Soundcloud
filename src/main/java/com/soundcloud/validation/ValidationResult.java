package com.soundcloud.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private final List<ValidationMessage> messageList = new ArrayList<>();

    public void addMessage(ValidationMessage validationMessage) {
        messageList.add(validationMessage);
    }

    public List<ValidationMessage> getMessageList() {
        return messageList;
    }

    public boolean isValid() {
        return this.messageList.isEmpty();
    }

}
