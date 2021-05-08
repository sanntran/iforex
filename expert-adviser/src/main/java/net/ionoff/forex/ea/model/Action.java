package net.ionoff.forex.ea.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

	public enum ACTION {
		NOACTION(0), OPEN_ORDER(1), CLOSE_ORDER(2), MODIFY_ORDER(3) ;
		private int code;
		ACTION(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	public static Action noOrder() {
		return Action.builder().code(ACTION.OPEN_ORDER.code).build();
	}
	public static ActionBuilder openOrder()  {
		return Action.builder().code(ACTION.OPEN_ORDER.code);
	}
	public static ActionBuilder closeOrder() {
		return Action.builder().code(ACTION.CLOSE_ORDER.code);
	}
	public static ActionBuilder modifyOrder() {
		return Action.builder().code(ACTION.MODIFY_ORDER.code);
	}

	private int code;
	private Order order;
}
