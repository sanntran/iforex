package net.ionoff.forex.ea.entity;

import java.io.Serializable;

public interface Entity extends Serializable {

	long DEFAULT_ID = 0L;

	long getId();
	void setId(long id);

	default boolean isNew() {
		return getId() == DEFAULT_ID;
	}
}
