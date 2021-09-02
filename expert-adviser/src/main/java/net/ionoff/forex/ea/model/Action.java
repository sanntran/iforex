package net.ionoff.forex.ea.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

	public enum Code {
		NONE(0),
		OPEN(1),
		CLOSE(2),
		MODIFY(3);

		private int value;
		Code(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}

	private int code;
	private Order order;

	public static Action none() {
		return Action.builder().code(Code.NONE.value).build();
	}

	public static Action open(Order order)  {
		return Action.builder()
				.code(Code.OPEN.value)
				.order(order).build();
	}

	public static Action close(Order order) {
		return Action.builder()
				.code(Code.CLOSE.value)
				.order(order).build();
	}
	public static Action modify(Order order) {
		return Action.builder()
				.code(Code.MODIFY.value)
				.order(order).build();
	}

	@JsonIgnore
	public boolean isNone() {
		return Code.NONE.value == code;
	}

	@JsonIgnore
	public boolean isClose() {
		return Code.CLOSE.value == code;
	}

	@JsonIgnore
	public boolean isOpen() {
		return Code.OPEN.value == code;
	}
}
