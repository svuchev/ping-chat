package com.stoyanvuchev.pingchat.domain.model;

public class Message {

    private String content;
    private boolean isSent;

    public Message(String content, boolean isSent) {
        this.content = content;
        this.isSent = isSent;
    }

    public String getContent() {
        return content;
    }

    public boolean isSent() {
        return isSent;
    }

}
