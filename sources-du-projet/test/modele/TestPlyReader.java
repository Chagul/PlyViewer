package modele;
import static org.junit.Assert.*;

import org.junit.Test;

import modele.PlyReader;
import modele.Point;

public class TestPlyReader {
	
	/**
	 * Teste si la lecture de fichier se fait bien
	 */
	@Test
	public void testRead() {
		PlyReader aPlyReader = new PlyReader("~/");
		assertFalse(aPlyReader.initPly());
		PlyReader aPlyReader2 = new PlyReader("sources-du-projet/exemples/cow.ply");
		assertTrue(aPlyReader2.initPly());
		}
	
	/**
	 * Teste si les nombres de face et de point du fichier ply sont bien affectés
	 */
	@Test
	public void testNbFaceNbPoint() {
		PlyReader aPlyReader = new PlyReader("sources-du-projet/exemples/cow.ply");
		aPlyReader.initPly();
		assertEquals(5804,aPlyReader.getNbFace());
		assertEquals(2903,aPlyReader.getNbPoint());
	}
	
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationPoint() {
		PlyReader aPlyReader = new PlyReader("sources-du-projet/exemples/cow.ply");
		aPlyReader.initPly();
		assertEquals(new Point(0.605538, 0.183122, -0.472278 ),aPlyReader.getPoint(0));
	}

}
