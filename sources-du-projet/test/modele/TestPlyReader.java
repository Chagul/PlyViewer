package modele;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class TestPlyReader {
	
	/**
	 * Teste si la lecture de fichier se fait bien
	 */
	@Test(expected = java.io.FileNotFoundException.class)
	public void testReadNotOK() throws FileNotFoundException {
		PlyReader aPlyReader = new PlyReader("~/");
		aPlyReader.initPly();
	}
	@Test
	public void testReadOk() {
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
		Point test = new Point(0.605538, 0.183122, -0.472278 );
		assertEquals(test, aPlyReader.getListPoint().get(0));
	}
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationFace() {
		PlyReader aPlyReader = new PlyReader("sources-du-projet/exemples/cow.ply");
		aPlyReader.initPly();
		Face test = new Face(aPlyReader.getListPoint().get(0), aPlyReader.getListPoint().get(1), aPlyReader.getListPoint().get(2));
		assertEquals(test, aPlyReader.getListFace().get(0));
	}

}
