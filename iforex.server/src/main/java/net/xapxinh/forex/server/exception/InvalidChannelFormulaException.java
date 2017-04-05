package net.xapxinh.forex.server.exception;

public class InvalidChannelFormulaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static final String CANNOT_CANCULATE = "Cannot calculate";
	public static final String INVALID_CHAR_AT = "Invalid character at ";
	public static final String INVALID_CHAR_AFTER = "Invalid character after ";
	public static final String USES_ITSELF = "Formula uses itself";
	public static final String USES_MANY_DEVICES = "Formula uses many devices";
	public static final String INVALID_STATION_ID = "Invalid station id ";
	public static final String INVALID_DEVICE_ID = "Invalid device id ";
	public static final String INVALID_CHANNEL_IDX = "Invalid channel id ";
	public static final String INVALID_SENSOR_IDX = "Invalid sensor id ";
	
	public InvalidChannelFormulaException(String message) {
		super(message);
	}
}
