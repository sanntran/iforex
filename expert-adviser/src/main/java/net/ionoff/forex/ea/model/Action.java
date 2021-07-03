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
		NO_ORDER(0),
		OPEN_ORDER(1),
		CLOSE_ORDER(2),
		MODIFY_ORDER(3),
		CLOSE_AND_OPEN_ORDER(4);

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

	public static Action noOrder() {
		return Action.builder().code(Code.NO_ORDER.value).build();
	}

	public static Action openOrder(Order order)  {
		return Action.builder()
				.code(Code.OPEN_ORDER.value)
				.order(order).build();
	}
	public static Action closeOrder(Order order) {
		return Action.builder()
				.code(Code.CLOSE_ORDER.value)
				.order(order).build();
	}
	public static Action modifyOrder(Order order) {
		return Action.builder()
				.code(Code.MODIFY_ORDER.value)
				.order(order).build();
	}
	public static Action closeAndOpenOrder(Order order) {
		return Action.builder()
				.code(Code.CLOSE_AND_OPEN_ORDER.value)
				.order(order).build();
	}

	@JsonIgnore
	public boolean isNoOrder() {
		return Code.NO_ORDER.value == code;
	}

	@JsonIgnore
	public boolean isCloseOrder() {
		return Code.CLOSE_ORDER.value == code;
	}

	@JsonIgnore
	public boolean isOpenOrder() {
		return Code.OPEN_ORDER.value == code;
	}
}
