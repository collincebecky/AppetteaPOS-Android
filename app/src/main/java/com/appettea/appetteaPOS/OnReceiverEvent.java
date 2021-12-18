package com.appettea.appetteaPOS;

public class OnReceiverEvent {
    private MessageParser data;
    public OnReceiverEvent(MessageParser messageParser){

        this.data = messageParser;
    }

    public MessageParser getSender() {
        return data;
    }
}
