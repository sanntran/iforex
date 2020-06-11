package net.ionoff.forex.ea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDto {
    private int code;
    private Object body;
}
