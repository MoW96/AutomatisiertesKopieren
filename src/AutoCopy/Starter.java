package AutoCopy;

import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Starter extends JFrame implements ActionListener, MouseListener {

	// AutoKopieren
	AutoKopieren test;

	// Überschrift
	JPanel Header = new JPanel();
	JLabel heading = new JLabel("<html><u>AutoCopy</u></html>");

	// Eingabeteil
	JPanel Content = new JPanel();
	JLabel LabelBase = new JLabel("Base-Directory");
	JTextField inputBase = new JTextField(950);
	JLabel LabelBackup = new JLabel("Backup-Directory");
	JTextField inputBackup = new JTextField(950);
	JLabel LabeldirPath1 = new JLabel("Geben Sie einen Ordner-Pfad an.");
	JLabel LabeldirPath2 = new JLabel("Geben Sie einen Ordner-Pfad an.");
	JLabel LabelVersionierung = new JLabel("Weitere Einstellungen:");

	// Radio-Buttons
	ButtonGroup groupRadio = new ButtonGroup();
	JLabel selectRadio = new JLabel("Kriterium für den Kopiervorgang:");
	JRadioButton radioDatum = new JRadioButton("Datum", true);
	JRadioButton radioSize = new JRadioButton("Größe der Datei", false);
	JRadioButton radioMD5 = new JRadioButton("MD5", false);

	// Checkboxen
	JCheckBox Versionierung = new JCheckBox("Versionierung");

	// Buttons
	JButton buttonBase = new JButton("Browse Explorer");
	JButton ButtonBackup = new JButton("Browse Explorer");
	JButton buttonGo = new JButton("Ausführen");
	ImageIcon clear = new ImageIcon("C:\\Users\\mwegn\\OneDrive\\Bilder\\Eigene Aufnahmen\\trashcan.jpg");

	// FileChooser
	JFileChooser chooser = new JFileChooser("C:\\");

	// Separator
	JSeparator Linie1 = new JSeparator(SwingConstants.HORIZONTAL);
	JSeparator Linie2 = new JSeparator(SwingConstants.HORIZONTAL);
	JSeparator Linie3 = new JSeparator(SwingConstants.HORIZONTAL);
	JSeparator Linie4 = new JSeparator(SwingConstants.HORIZONTAL);

	// ProgressBar
	JDialog progressbarDialog = new JDialog();
	JLabel progressLabel = new JLabel("Fortschritt...");
	JLabel progressBarSizeLabel = new JLabel("Dateigröße:");
	JProgressBar progressBarSize = new JProgressBar(0, 100);
	JLabel progressBarNumberLabel = new JLabel("Dateien:");
	JProgressBar progressBarNumber = new JProgressBar(0, 100);
	JButton closeButton = new JButton("Cancel");
	JLabel messageLabel = new JLabel();

	// Durchlaufzähler
	int durchlauf = 0;

	// Autokopieren-Paramter
	boolean version = false;
	String checkArt, pfadBase, pfadBackup;

	Starter(String title) {
		super(title);

		// Clear Button
		Image image = clear.getImage();
		Image newimg = image.getScaledInstance(30, 40, java.awt.Image.SCALE_SMOOTH);
		clear = new ImageIcon(newimg);
		JButton clearButton = new JButton(clear);

		// Layoutmanager
		setLayout(new BorderLayout());
		Content.setLayout(null);

		// Eventhandler
		buttonGo.addActionListener(this);
		buttonBase.addActionListener(this);
		ButtonBackup.addActionListener(this);
		clearButton.addActionListener(this);
		buttonGo.setActionCommand("Go");
		buttonBase.setActionCommand("Base");
		ButtonBackup.setActionCommand("Backup");
		clearButton.setActionCommand("clear");

		// Layout einrichten & ausrichten
		heading.setHorizontalAlignment(JLabel.CENTER);
		Header.add(heading);
		this.add(Header, BorderLayout.NORTH);
		Content.add(Linie1);
		Content.add(Linie2);
		Content.add(Linie3);
		Content.add(Linie4);
		Content.add(clearButton);
		Content.add(LabelBase);
		Content.add(inputBase);
		Content.add(LabelBackup);
		Content.add(inputBackup);
		Content.add(selectRadio);
		Content.add(LabeldirPath1);
		Content.add(ButtonBackup);
		Content.add(buttonBase);
		Content.add(LabeldirPath2);
		// Content.add(progressBar);
		groupRadio.add(radioDatum);
		groupRadio.add(radioSize);
		groupRadio.add(radioMD5);
		Content.add(radioDatum);
		Content.add(radioSize);
		Content.add(radioMD5);
		Content.add(LabelVersionierung);
		Content.add(Versionierung);
		this.add(Content, BorderLayout.CENTER);
		this.add(buttonGo, BorderLayout.SOUTH);

		// Tool-Tipps
		Versionierung.setToolTipText(
				"<html>Ausgewählt -> neue Version wird erstellt<br>Nicht Ausgewählt -> Hauptversion wird überschrieben</html>");
		clearButton.setToolTipText("Klicken, zum Löschen der Eingaben");
		radioMD5.setToolTipText("<html>Sehr sicher, aber deutlich<br>langsamer als 'Datum' & 'Größe der Datei'</html>");

		// Schriftgrößen einstellen
		heading.setFont(heading.getFont().deriveFont(25f));
		LabelBase.setFont(LabelBase.getFont().deriveFont(15f));
		LabelBackup.setFont(LabelBackup.getFont().deriveFont(15f));
		selectRadio.setFont(selectRadio.getFont().deriveFont(15f));
		LabeldirPath1.setFont(LabeldirPath1.getFont().deriveFont(10f));
		LabeldirPath2.setFont(LabeldirPath2.getFont().deriveFont(10f));
		LabelVersionierung.setFont(LabeldirPath2.getFont().deriveFont(15f));

		// Farben
		Linie1.setForeground(Color.black);
		Linie2.setForeground(Color.black);
		Linie3.setForeground(Color.black);
		Linie4.setForeground(Color.black);

		// Content-Panel "einrichten"
		Linie1.setBounds(5, 3, 975, 5);
		clearButton.setBounds(10, 10, 30, 40);
		Linie2.setBounds(5, 56, 975, 5);
		LabelBase.setBounds(10, 50, 500, 40);
		inputBase.setBounds(10, 80, 950, 22);
		LabeldirPath1.setBounds(800, 103, 500, 10);
		buttonBase.setBounds(10, 103, 150, 20);
		LabelBackup.setBounds(10, 150, 500, 40);
		inputBackup.setBounds(10, 180, 950, 22);
		LabeldirPath2.setBounds(800, 203, 500, 10);
		ButtonBackup.setBounds(10, 203, 150, 20);
		Linie3.setBounds(5, 245, 975, 5);
		selectRadio.setBounds(10, 250, 500, 40);
		radioDatum.setBounds(10, 290, 500, 20);
		radioSize.setBounds(10, 310, 500, 20);
		radioMD5.setBounds(10, 330, 500, 20);
		Linie4.setBounds(5, 370, 975, 5);
		LabelVersionierung.setBounds(10, 370, 180, 40);
		Versionierung.setBounds(10, 410, 250, 20);
		// progressBar.setBounds(270, 410, 300, 20);

		closeButton.setEnabled(true);

		// Checkbox
		Versionierung.setSelected(false);

		// Exit über Close-Button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// MouseEvents zum Fortschrittsbalken schließen
	@Override
	public void mouseClicked(MouseEvent e) {
		if (test.fertig == false) {
			test.interrupt();
		}
		progressbarDialog.dispose();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	private static void neuerDurchlauf() {
		for (int i = 1; i < 10; i++) {
			System.out.print("\n");
		}
	}

	public void actionPerformed(ActionEvent evt) {

		test = new AutoKopieren();
		closeButton.setText("Cancel");

		if (durchlauf > 0) {
			neuerDurchlauf();
		}

		test.start = this;
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int rueckgabeWert;

		if (evt.getActionCommand().equals("clear")) {
			inputBase.setText("");
			inputBackup.setText("");
		}

		if (evt.getActionCommand().equals("Base")) {
			rueckgabeWert = chooser.showDialog(null, "Auswählen");
			if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
				inputBase.setText(chooser.getSelectedFile().getAbsolutePath());
			}
			rueckgabeWert = 0;
		}

		if (evt.getActionCommand().equals("Backup")) {
			rueckgabeWert = chooser.showDialog(null, "Auswählen");
			if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
				inputBackup.setText(chooser.getSelectedFile().getAbsolutePath());
			}
			rueckgabeWert = 0;
		}

		if (evt.getActionCommand().equals("Go")) {
			String pathBase = inputBase.getText();
			String pathBackup = inputBackup.getText();

			if (new File(pathBase).exists() && new File(pathBackup).exists()) {
				if (!new File(pathBase).equals(new File(pathBackup))) {

					if (Versionierung.isSelected()) {
						if (radioDatum.isSelected()) {

							this.version = true;
							this.checkArt = "Datum";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}

						} else if (radioSize.isSelected()) {

							this.version = true;
							this.checkArt = "Size";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}
						} else if (radioMD5.isSelected()) {

							this.version = true;
							this.checkArt = "MD5";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}
						}
					} else {
						if (radioDatum.isSelected()) {

							this.version = false;
							this.checkArt = "Datum";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}

						} else if (radioSize.isSelected()) {

							this.version = false;
							this.checkArt = "Size";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}
						} else if (radioMD5.isSelected()) {

							this.version = false;
							this.checkArt = "MD5";
							this.pfadBase = pathBase;
							this.pfadBackup = pathBackup;

							try {
								test.start();
							} catch (Exception e) {
								System.out.println("Fehler: " + e);
							}
						}
					}

					// Progressbar
					makeProgressbar();

					try {
						test.join();
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(inputBackup, "Base- und Backup-Directory besitzen den selben Pfad",
							"Warnung!", JOptionPane.WARNING_MESSAGE);
				}

				inputBase.setText("");
				inputBackup.setText("");
			} else {
				if (!new File(pathBase).exists() && !new File(pathBackup).exists()) {
					JOptionPane.showMessageDialog(inputBackup,
							"Base- und Backup-Directory:  Ungültiger Pfad! - Datei/Ordner existiert nicht", "Warnung!",
							JOptionPane.WARNING_MESSAGE);
					inputBase.setText("");
					inputBackup.setText("");
				} else {
					if (!new File(pathBase).exists()) {
						JOptionPane.showMessageDialog(inputBase,
								"Base-Directory:  Ungültiger Pfad! - Datei/Ordner existiert nicht", "Warnung!",
								JOptionPane.WARNING_MESSAGE);
						inputBase.setText("");
					}

					if (!new File(pathBackup).exists()) {
						JOptionPane.showMessageDialog(inputBackup,
								"Backup-Directory:  Ungültiger Pfad! - Datei/Ordner existiert nicht", "Warnung!",
								JOptionPane.WARNING_MESSAGE);
						inputBackup.setText("");
					}
				}
			}
			durchlauf++;
		}
	}

	private void makeProgressbar() {
		
		progressbarDialog.setTitle("Fortschritt...");
		progressbarDialog.setSize(400, 250);
		progressbarDialog.setLocation(800, 385);
		progressbarDialog.setResizable(false);
		progressbarDialog.setLayout(null);
		progressbarDialog.setModal(true);
		progressbarDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		progressBarSize.setBorderPainted(true);
		progressBarNumber.setBorderPainted(true);
		progressBarSize.setForeground(Color.darkGray);
		progressBarNumber.setForeground(Color.darkGray);
		progressBarSize.setStringPainted(true);
		progressBarNumber.setStringPainted(true);
		progressbarDialog.add(progressLabel);
		progressbarDialog.add(progressBarSizeLabel);
		progressbarDialog.add(progressBarSize);
		progressbarDialog.add(progressBarNumberLabel);
		progressbarDialog.add(progressBarNumber);
		progressbarDialog.add(closeButton);
		progressbarDialog.add(messageLabel);
		messageLabel.setFont(selectRadio.getFont().deriveFont(10f));
		progressLabel.setBounds(10, 5, 150, 20);
		progressBarNumberLabel.setBounds(10, 35, 150, 20);
		progressBarNumber.setBounds(10, 55, 360, 30);
		progressBarSizeLabel.setBounds(10, 90, 150, 30);
		progressBarSize.setBounds(10, 115, 360, 30);
		messageLabel.setBounds(10, 143, 360, 30);
		closeButton.setBounds(285, 175, 80, 25);
		closeButton.addMouseListener(this);
		progressbarDialog.setVisible(true);
		
	}
	
	public boolean getVersion() {
		return this.version;
	}

	public String getCheckArt() {
		return this.checkArt;
	}

	public String getPathBase() {
		return this.pfadBase;
	}

	public String getPathBackup() {
		return this.pfadBackup;
	}

	// ProgressBar
	public void setProgress(long maxValLength, long fortschrittLength, long maxValNumber, long fortschrittNumber,
			String message) {
		progressBarSize.setMaximum(100);
		progressBarNumber.setMaximum(100);

		fortschrittLength = fortschrittLength * 100 / maxValLength;
		fortschrittNumber = fortschrittNumber * 100 / maxValNumber;
		
		progressBarNumber.setValue((int) fortschrittNumber);
		progressBarNumber.setString(fortschrittNumber + "%");
		
		progressBarSize.setValue((int) fortschrittLength);
		progressBarSize.setString(fortschrittLength + "%");

		messageLabel.setText("Datei: " + message + " wird kopiert.");

		if (fortschrittLength == 100 || fortschrittNumber == 100) {
			messageLabel.setText("Kopiervorgang abgeschlossen.");
			closeButton.setText("Finish");
		}
	}

	public static void main(String[] args) {

		Starter copy = new Starter("AutoCopy");

		copy.setSize(1000, 550);
		copy.setLocation(500, 250);
		copy.setResizable(false);
		copy.setVisible(true);
	}

}