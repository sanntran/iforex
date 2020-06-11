package net.ionoff.forex.ea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@Builder
public class TickDto {
	String time;
	Double bid;
	Double ask;
}
