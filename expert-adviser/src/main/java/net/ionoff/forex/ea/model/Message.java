package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int code;
    private Object data;

    public Message(int code) {
        this.code = code;
    }
}
