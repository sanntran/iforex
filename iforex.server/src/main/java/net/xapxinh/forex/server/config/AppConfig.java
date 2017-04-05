package net.xapxinh.forex.server.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class AppConfig {
	private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());

	private static AppConfig instance = null;
	
	public AppConfig() {

		Properties prop = new Properties();
		try {
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
			prop.load(isr);
			
			
		}
		catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static AppConfig getInstance() {
		if (instance == null) {
			instance = new AppConfig();
		}
		return instance;
	}
	
	public static void mkDir(File dir) {
		File parent = dir.getParentFile();
		if (parent.exists()) {
			dir.mkdir();
		}
		else {
			try {
				mkDir(parent);
			}
			catch (Exception e) {
				// ignore
			}
			mkDir(dir);
		}
	}

	public static boolean isWinPlatform() {
		String os = System.getProperty("os.name");
		if (os.contains("Win")) {
			return true;
		}
		else
			return false;
	}

	public static boolean isLinuxPlatform() {
		String os = System.getProperty("os.name");
		if (os.contains("nux")) {
			return true;
		}
		else
			return false;
	}
}
