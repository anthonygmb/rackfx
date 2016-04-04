package utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import controller.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class FileUtils {

	private static ResourceBundle Lang_bundle;

	@SuppressWarnings("static-access")
	@PostConstruct
	private void initialize() {
		this.Lang_bundle = MainApp.getInstance().Lang_bundle;
	}

	/**
	 * Convert file to image.
	 *
	 * @param file
	 *            the file
	 * @return the image
	 */
	public static Image convertFileToImage(File file) {
		Image image = new Image("file:src/img/cd_music.png");
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			image = SwingFXUtils.toFXImage(bufferedImage, null);
		} catch (Exception e) {
			Validateur.showPopup(AlertType.ERROR, Lang_bundle.getString("Erreur"),
					Lang_bundle.getString("Erreur.de.format"),
					Lang_bundle.getString("Le.format.de.ce.fichier.n'est.pas.reconnu")).showAndWait();
		}
		return image;
	}

	/**
	 * Methode de conversion de fichier en tableau de byte.
	 *
	 * @param file
	 *            the file
	 * @return the byte[]
	 */
	public static byte[] convertFileToByte(File file) {
		byte[] bFile = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bFile;
	}

	/**
	 * Convert byte to image.
	 *
	 * @param tByte
	 *            the t byte
	 * @return the image
	 */
	public static Image convertByteToImage(byte[] tByte) {
		Image image = null;
		try {
			FileOutputStream fos = new FileOutputStream("C:\\test.gif");
			fos.write(tByte);
			fos.close();
			InputStream in = new ByteArrayInputStream(tByte);
			image = new Image(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
