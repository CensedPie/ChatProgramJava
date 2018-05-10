package message;

import java.io.Serializable;

public class Message implements Serializable {
    String text;
    
    public Message(String text) {
        this.text = text;
    }
    public String getMsg() {
        return text;
    }
}
