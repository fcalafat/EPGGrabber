package fr.vred.epg.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigReader {

	private static Logger log = Logger.getLogger(ConfigReader.class);

	private static String CONFIG_PATH = "config.properties";
	private static Properties config = new Properties();

	private ConfigReader() {

	}

	public static void readConfig() {
		try {
			config.load(new FileInputStream(CONFIG_PATH));

			// Retreive fields
			for (Config conf : Config.values()) {
				if (config.get(conf.name()) != null) {
					String newValue = String.valueOf(config.get(conf.name()));
					if (!newValue.equals(conf.getValue())) {
						conf.setValue(newValue);
						log.info(conf.name() + " value is now: " + newValue);
					}

				} else {
					if (conf.isRequired())
						log.error("Required parameter [" + conf.name()
								+ "] not found in file " + CONFIG_PATH);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Can't find " + CONFIG_PATH + " file");
		} catch (IOException e) {
			log.error("Can't read " + CONFIG_PATH + " file");
		}
	}
}
