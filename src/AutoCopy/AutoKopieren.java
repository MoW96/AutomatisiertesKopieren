package AutoCopy;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class AutoKopieren extends Thread {

	Scanner scan = new Scanner(System.in);
	String checkArt;
	boolean changed = false, copy = false, versionierung = false, fertig = false;
	String version = new String();
	public Starter start;
	long totalLength, currentLength, totalNumber, currentNumber;

	@Override
	public void run() {
		boolean vers = start.getVersion();
		String check = start.getCheckArt();
		String pathBase = start.getPathBase();
		String pathBackup = start.getPathBackup();
		try {
			startCopy(vers, check, pathBase, pathBackup);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		fertig = true;
		start.setProgress(100, 100, 100, 100, "");
	}

	private void startCopy(boolean version, String checkArt, String pathBase, String pathBackup) throws IOException {
		File UrsprungOrdner = new File(pathBase); // "C:/Users/mwegn/OneDrive/Desktop/BaseDirectory/"
		File BackupOrdner = new File(pathBackup); // "C:/Users/mwegn/OneDrive/Desktop/BackupDirectory/"

		this.checkArt = checkArt;
		this.versionierung = version;

		countData(UrsprungOrdner, BackupOrdner);
		copyFolder(UrsprungOrdner, BackupOrdner, false);
		System.out.println("Base-Dir-Tree: \n ");
		listOrdner(UrsprungOrdner, "");
		System.out.println("\n Target-Dir-Tree: \n ");
		listOrdner(BackupOrdner, "");
	}

	// in Log-File schreiben mit Versionierung
	private void listOrdner(File Base, String t) {
		System.out.println(t + Base.getAbsolutePath());
		if (Base.isDirectory()) {
			for (File sub : Base.listFiles()) {
				listOrdner(sub, t + "\t");
			}
		}
	}

	// Datum anhängen zur Versionierung
	private void versioning() {

		LocalDateTime datum = LocalDateTime.now();
		DateTimeFormatter datumFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy-kkmm");

		version = datum.format(datumFormat).toString();

	}

	private void copyFolder(File Base, File Target, boolean copyItem) {

		boolean append = false;

		if (this.isInterrupted()) {
			return;
		}

		if (copyItem == true) {
			try {
				System.out.print("Copy-Process: \n" + Base + "  -->  " + Target + "\n \n ");

				currentNumber++;

				try (InputStream input = new FileInputStream(Base);
						OutputStream output = new FileOutputStream(Target, append)) {
					byte[] buffer = new byte[65536];
					for (int count = input.read(buffer); count >= 0; count = input.read(buffer)) {
						currentLength += count;
						output.write(buffer, 0, count);
						start.setProgress(totalLength, currentLength, totalNumber, currentNumber, Base.getName());
					}
				}
			} catch (IOException e) {
				System.out.println("Fehler: " + e + " Zeile: 70");
			}
		}

		if (Base.isDirectory()) {

			for (File sub : Base.listFiles()) {
				switch (checkArt) {
				case "Datum":
					copy = checkChangeLastModified(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()));
					break;
				case "Size":
					copy = checkChangeSize(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()));
					break;
				case "MD5":
					try {
						copy = checkChangeMD5(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

				// Versionierung
				if (versionierung) {
					if (new File(Target.getAbsolutePath() + "/" + sub.getName()).exists()) {
						versioning();
						copyFolder(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()), copy);

						if (!new File(Target.getAbsolutePath() + "/" + "Versions").exists()) {
							new File(Target.getAbsolutePath() + "/" + "Versions").mkdir();
							System.out.println(Target.getAbsolutePath() + "\\" + "Versions wurde angelegt");
							try {
								Files.setAttribute(Paths.get(Target.getAbsolutePath() + "/" + "Versions"), "dos:hidden",
										true);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						copyFolder(sub,
								new File(Target.getAbsolutePath() + "/" + "Versions" + "/"
										+ sub.getName().substring(0, sub.getName().indexOf('.')) + "_" + version
										+ sub.getName().substring(sub.getName().indexOf('.'))),
								copy);
					} else {
						copyFolder(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()), copy);
					}
				} else {
					copyFolder(sub, new File(Target.getAbsolutePath() + "/" + sub.getName()), copy);
				}
			}
		}
	}

	private void countData(File Base, File Backup) {

		if (Base.isDirectory()) {
			for (File sub : Base.listFiles()) {
				countData(sub, new File(Backup.getAbsolutePath() + "/" + sub.getName()));
			}
		} else {
			totalNumber++;
			switch (checkArt) {
			case "Datum":
				if (checkChangeLastModified(Base, Backup)) {
					totalLength += Base.length();
				}
				break;
			case "Size":
				if (checkChangeSize(Base, Backup)) {
					totalLength += Base.length();
				}
				break;
			case "MD5":
				try {
					if (checkChangeMD5(Base, Backup)) {
						totalLength += Base.length();
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	private boolean checkChangeLastModified(File itemBase, File itemBackup) {
		changed = false;
		if (!itemBase.isDirectory()) {
			if (itemBase.lastModified() > itemBackup.lastModified()) {
				changed = true;
			}
		} else if (!itemBackup.exists()) {
			changed = true;
		}
		return changed;
	}

	private boolean checkChangeSize(File itemBase, File itemBackup) {
		changed = false;
		if (!itemBase.isDirectory()) {
			if (itemBase.length() != itemBackup.length()) {
				changed = true;
			}
		} else if (!itemBackup.exists()) {
			changed = true;
		}

		return changed;
	}

	private boolean checkChangeMD5(File itemBase, File itemBackup) throws NoSuchAlgorithmException {
		changed = false;
		String checksumBase = new String(), checksumBackup = new String();
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		if (!itemBase.isDirectory()) {
			try {
				checksumBase = getFileChecksum(md5, itemBase);
			} catch (IOException io) {
				io.printStackTrace();
			}
		}

		if (itemBackup.exists()) {
			if (!itemBackup.isDirectory()) {
				try {
					checksumBackup = getFileChecksum(md5, itemBackup);
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		}

		if (!itemBase.isDirectory() && !itemBackup.isDirectory()) {
			if (!checksumBase.equals(checksumBackup)) {
				changed = true;
			}
		} else if (!itemBackup.exists()) {
			changed = true;
		}
		return changed;
	}

	private String getFileChecksum(MessageDigest md5, File file) throws IOException {
		try (InputStream fis = new FileInputStream(file)) {
			byte[] buffer = new byte[65536];
			int nread;
			while ((nread = fis.read(buffer)) != -1) {
				md5.update(buffer, 0, nread);
			}
		}

		// bytes to hex
		StringBuilder result = new StringBuilder();
		for (byte b : md5.digest()) {
			result.append(String.format("%02x", b));
		}
		return result.toString();

	}

}
