package fr.vred.epg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import fr.vred.epg.config.Config;
import fr.vred.epg.config.ConfigReader;
import fr.vred.epg.utils.UnZip;

public class GrabThread extends Thread {
	private Logger log = Logger.getLogger(getClass());
	boolean isRunning;

	private final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	private final String TEMP_FILE = "tvguide.zip";

	public GrabThread() {
		isRunning = true;
	}

	public void run() {
		log.info("Refresh started");
		while (isRunning) {
			try {
				Long sleepTime = Long.valueOf(Integer.toString(Integer
						.parseInt(Config.REFRESH_TIME.getValue()) * 60 * 1000));
				log.info("Check will be performed every "
						+ Config.REFRESH_TIME.getValue() + " minutes");
				log.info("File will be downloaded only if previous file is older than "
						+ Config.REFRESH_PERIOD.getValue() + " days");
				getFile();
				sleep(sleepTime);
				// Reload the config
				ConfigReader.readConfig();
			} catch (InterruptedException e) {
				log.error("Failed refreshing XML");
			}

		}
	}

	private void getFile() {
		// First check if is necessary to retreive file regarding REFRESH_PERIOD
		// parameter
		long lastRefresh = EPGGrabber.getInstance().getLastRefresh() == null ? 0L
				: EPGGrabber.getInstance().getLastRefresh().getTime();
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long timeDifference = currentTime - lastRefresh;

		int nbDays = Integer.valueOf(Config.REFRESH_PERIOD.getValue()) * 24
				* 60 * 60 * 1000;

		if (lastRefresh == 0L
				|| Integer.valueOf(Long.toString(timeDifference)) > nbDays) {
			try {
				URL input = new URL(Config.INPUT_URL.getValue());
				File output = new File(TEMP_DIR + File.separator + TEMP_FILE);
				if (output.exists()) {
					log.info("Remove previously downloaded tvguide.zip");
					output.delete();
				}
				FileUtils.copyURLToFile(input, output);
				log.info("File tvguide.zip correctly downloaded");

				UnZip.unzip(output.getPath(), Config.OUTPUT_PATH.getValue());
				log.info("File tvguide.zip correctly extracted to "
						+ Config.OUTPUT_PATH.getValue());

				log.info("Next refresh in " + Config.REFRESH_TIME.getValue()
						+ " minutes");
			} catch (IOException e) {
				log.error("Failed to retreive new tvguide.xml file");
			}
		}

		EPGGrabber.getInstance().setLabel();
	}
}
