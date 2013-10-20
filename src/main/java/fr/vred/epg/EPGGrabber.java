package fr.vred.epg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import fr.vred.epg.config.ConfigReader;

@SuppressWarnings("serial")
public class EPGGrabber extends JFrame {

	private static Logger log = Logger.getLogger(EPGGrabber.class);
	private static EPGGrabber instance;

	public static String LAST_REFRESH = "Last refresh: ";

	private JLabel label;
	private Date lastRefresh;

	private EPGGrabber() {
		setTitle("EPGGrabber");
		setSize(400, 125);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JButton quitButton = new JButton("Quit");
		quitButton.setBounds(50, 60, 80, 30);

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(10, 10));

		label = new JLabel(LAST_REFRESH);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setForeground(new Color(50, 50, 25));

		panel.add(label, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(quitButton, BorderLayout.SOUTH);
		add(panel);
	}

	public static EPGGrabber getInstance() {
		if (instance == null) {
			instance = new EPGGrabber();
		}
		return instance;
	}

	public void setLabel() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		lastRefresh = Calendar.getInstance().getTime();
		this.label.setText(LAST_REFRESH + sdf.format(lastRefresh));
	}

	public Date getLastRefresh() {
		return lastRefresh;
	}

	public static void main(String[] args) {

		log.info("EPGGrabber started");
		ConfigReader.readConfig();
		log.info("Config loaded");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EPGGrabber ex = EPGGrabber.getInstance();
				ex.setVisible(true);
			}
		});

		GrabThread thread = new GrabThread();
		thread.start();

	}
}
