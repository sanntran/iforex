package net.xapxinh.forex.server.entity.wave;

import net.xapxinh.forex.server.entity.Wave;

public class EurUsdH1Wave extends Wave {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Wave newInstance() {
		return new EurUsdH1Wave();
	}
}
