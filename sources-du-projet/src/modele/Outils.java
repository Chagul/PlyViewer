package modele;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class contenant des méthodes utils au développement de cette application.
 * @author kharmacm
 * @version 11/11/2020
 */
public class Outils {
	/**
	 * Methode prenent en paramètre un lien et retournant l'image.
	 * @param path
	 * @return
	 * @throws MalformedURLException 
	 */
	public static ImageView fileToImg(String path) throws MalformedURLException {
		File f = new File(path);
		Image i = new Image(f.toURI().toURL().toString());
		ImageView iv = new ImageView(i);
		iv.setFitHeight(15);
		iv.setFitWidth(15);
		
		return iv;
	}
}
