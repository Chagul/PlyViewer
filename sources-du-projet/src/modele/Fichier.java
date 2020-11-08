package modele;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Class permettant la manipulation de fichier
 * @author kharmacm
 * @version 09/11/2020
 */
public class Fichier {
	/**
	 * Deplace un fichier de la source à la destination.
	 * @param source
	 * 				Path source du fichier à deplacer.
	 * @param destination
	 * 				Path destination.
	 * @throws IOException
	 */
	public static void move(String source, String destination) throws IOException {
		File fSource=new File(source);
	    File fDestination=new File(destination);  
	    StandardCopyOption remplacerSiExiste=StandardCopyOption.REPLACE_EXISTING;
	    
	    Files.copy(fSource.toPath(), fDestination.toPath(),remplacerSiExiste);
	    
	}
	
}
