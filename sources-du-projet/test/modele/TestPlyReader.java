package modele;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class TestPlyReader {
	
	private PlyReader aPlyReader;
	/**
	 * Teste si la lecture de fichier se fait bien
	 */
	@Test(expected = java.io.FileNotFoundException.class)
	public void testReadNotOK() throws FileNotFoundException {
		PlyReader aPlyReader = new PlyReader("~/");
		aPlyReader.initPly();
	}
	
	@Test
	public void testReadOk() throws FileNotFoundException {
		PlyReader aPlyReader2 = new PlyReader("sources-du-projet/exemples/cow.ply");
		aPlyReader2.initPly();
		}
	
	@Before
	public void init() throws FileNotFoundException {
		aPlyReader = new PlyReader("sources-du-projet/exemples/cow.ply");
		aPlyReader.initPly();
	}
	/**
	 * Teste si les nombres de face et de point du fichier ply sont bien affectés
	 */
	@Test
	public void testNbFaceNbPoint() {
		assertEquals(5804,aPlyReader.getNbFace());
		assertEquals(2903,aPlyReader.getNbPoint());
		aPlyReader.readPly();
		assertEquals(5804,aPlyReader.getListFace().size());
		assertEquals(2903,aPlyReader.getListPoint().size());
	}
	
	@Test
	public void testCreationPointFonction() {
		String pointTest = "0.605538 0.183122 -0.472278 ";
		assertTrue(aPlyReader.creationPoint(pointTest));
	}
	
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationPoint() {
		aPlyReader.readPly();
		Point test = new Point(0.605538, 0.183122, -0.472278 );
		assertEquals(test, aPlyReader.getListPoint().get(0));	
	}
	
	@Test
	public void testCreationFaceFonction() {
		aPlyReader.readPly();
		String testFace = "3 0 1 2 ";
		assertTrue(aPlyReader.creationFace(testFace));
	}
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationFace() {
		aPlyReader.readPly();
		Face test = new Face(new Point(0.605538,0.183122,-0.472278), new Point(0.649223,0.1297,-0.494875), new Point(0.601082,0.105512,-0.533343));
		assertEquals(test, aPlyReader.getListFace().get(0));
	}
}
