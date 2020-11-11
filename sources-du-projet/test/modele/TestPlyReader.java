package modele;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author planckea
 *
 */
public class TestPlyReader {
	
	private PlyReader aPlyReader;
	private PlyFile vache;
	/**
	 * Teste si la lecture d'un fichier non possible renvoie une FIleNOtFOundException
	 */
	@Test(expected = java.io.FileNotFoundException.class)
	public void testReadNotOK() throws FileNotFoundException {
		PlyReader aPlyReader = new PlyReader();
		aPlyReader.initPly("/~");
	}
	/**
	 * Teste si l'ouverture d'un fichier correcte fonctionne
	 * @throws FileNotFoundException
	 */
	@Test
	public void testReadOk() throws FileNotFoundException {
		PlyReader aPlyReader2 = new PlyReader();
		aPlyReader2.initPly("sources-du-projet/exemples/cow.ply");
		}
	
	@Before
	public void init() throws FileNotFoundException {
		aPlyReader = new PlyReader();
		aPlyReader.initPly("sources-du-projet/exemples/cow.ply");
		try {
		vache = aPlyReader.getPly("sources-du-projet/exemples/cow.ply");
			}catch(Exception e) {
		}
	}
	/**
	 * Teste si les nombres de face et de point du fichier ply sont bien affectés
	 */
	@Test
	public void testNbFaceNbPoint() {
		assertEquals(5804,vache.getArrayListFace().size());
		assertEquals(2903,vache.getHashMapPoint().size());
	}
	/**
	 * Teste si la fonction création Point renvoies bien vrai quand la création d'un point s'est bien passée et faux sinon
	 */
	@Test
	public void testCreationPointFonction() {
		String pointTest = "0.605538 0.183122 -0.472278 ";
		HashMap<Integer, Point> hashMapTest = new HashMap<Integer,Point>();
		assertTrue(aPlyReader.creationPoint(pointTest, hashMapTest));
		assertFalse(aPlyReader.creationPoint("", hashMapTest));
	}
	
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationPoint() {
		Point test = new Point(0.605538, 0.183122, -0.472278 );
		assertEquals(test, vache.getHashMapPoint().get(0));	
	}
	/**
	 * Teste si la fonction creation face renvoies bien vrai pour la creation d'une quelconque face et faux sinon
	 */
	@Test
	public void testCreationFaceFonction()  {
		HashMap<Integer, Point> hashMapTest = new HashMap<Integer,Point>();
		ArrayList<Face> arrayListFaceTest = new ArrayList<Face>();
		String testFace = "3 0 1 2 ";
		String testFace2 = "4 52 63 89 85";
		assertTrue(aPlyReader.creationFace(testFace, arrayListFaceTest, hashMapTest));
		assertTrue(aPlyReader.creationFace(testFace2, arrayListFaceTest, hashMapTest));
		assertFalse(aPlyReader.creationFace("", arrayListFaceTest, hashMapTest));
		assertFalse(aPlyReader.creationFace("1 5 7", arrayListFaceTest, hashMapTest));
	}
	
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationFace(){
		Face test = new Face();
		test.addPoint(new Point(0.605538,0.183122,-0.472278));
		test.addPoint(new Point(0.649223,0.1297,-0.494875));
		test.addPoint(new Point(0.601082,0.105512,-0.533343));
		assertEquals(test, vache.getArrayListFace().get(0));
	}
}