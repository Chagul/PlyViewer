package modele;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author planckea
 *
 */
public class TestPlyReader {
	
	private PlyReader aPlyReader;
	private Model3D vache;
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
				e.printStackTrace();
		}
	}
	/**
	 * Teste si les nombres de face et de point du fichier ply sont bien affectés
	 */
	@Test
	public void testNbFaceNbPoint() {
		assertEquals(5804,vache.getArrayListFace().size());
		assertEquals(2903,vache.getTabPoint().length);
	}
	/**
	 * Teste si la fonction création Point renvoies bien vrai quand la création d'un point s'est bien passée et faux sinon
	 */
	@Test
	public void testCreationPointFonction() {
		String pointTest = "0.605538 0.183122 -0.472278 ";
		Point[] hashMapTest = new Point[1];
		assertTrue(aPlyReader.creationPoint(pointTest, hashMapTest));
		assertFalse(aPlyReader.creationPoint("", hashMapTest));
	}
	
	/**
	 * Teste si les points créés correspondent bien à ce qui est lu dans le ply
	 */
	@Test
	public void testCreationPoint() {
		Point test = new Point(0.605538, 0.183122, -0.472278 );
		assertEquals(test, vache.getTabPoint()[0]);	
	}
	/**
	 * Teste si la fonction creation face renvoies bien l'exception CreationFormatFaceException.
	 * @throws CreationFormatFaceException si il y a un problème dans la face
	 */
	@Test(expected = CreationFormatFaceException.class)
	public void testCreationFaceFonctionException() throws CreationFormatFaceException  {
		Point[] hashMapTest = new Point[1];
		ArrayList<Face> arrayListFaceTest = new ArrayList<Face>();
		aPlyReader.creationFace("", arrayListFaceTest, hashMapTest);
		aPlyReader.creationFace("1 5 7", arrayListFaceTest, hashMapTest);
	}
	/**
	 * Teste si la fonction creation face renvoies bien vrai quand une face valide est lue et créée.
	 * @throws CreationFormatFaceException
	 */
	@Test
	public void testCreationFaceFonction() throws CreationFormatFaceException{
		Point[] hashMapTest = new Point[1];
		ArrayList<Face> arrayListFaceTest = new ArrayList<Face>();
		String testFace = "3 0 1 2 ";
		String testFace2 = "4 52 63 89 85";
		assertTrue(aPlyReader.creationFace(testFace, arrayListFaceTest, hashMapTest));
		assertTrue(aPlyReader.creationFace(testFace2, arrayListFaceTest, hashMapTest));
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