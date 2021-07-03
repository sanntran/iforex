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

    public static Message candleUpdated() {
        return new Message(0, "candle_updated");
    }

    public static Message candleClosed() {
        return new Message(1, "candle_closed");
    }
}
