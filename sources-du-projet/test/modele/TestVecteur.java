package modele;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * 
 * @author hodari
 *
 */

public class TestVecteur {
	
	Vecteur v1, v2, v3, v4, v5, v6, v7, v8, v9;
	
	@Before
	public void initialization() {
		v1 = new Vecteur(1.0, 0.0, 2.0);
		v2 = new Vecteur(1.0, 2.0, 3.0);
		v3 = new Vecteur(-4.0, 1.0, 2.0);
		v4 = new Vecteur(3.0, 3.0, 3.0);
		v5 = new Vecteur(2.0, -2.0, 2.0);
		v6 = new Vecteur(12.0, 0.0, -12.0);
		v7 = new Vecteur(3.0, 2.0, -1.0);
		v8 = new Vecteur(0.0, 0.0, 4.0);
		v9 = new Vecteur(0.0, 0.0, 1.0);
	}
	
	@Test
	public void testEquals() {
		assertTrue(v1.equals(v1));
		assertTrue(v2.equals(v2));
		assertFalse(v1.equals(v2));
	}
	
	@Test
	public void testProduitVectoriel() {
		assertEquals(v1.produitVectoriel(v2),new Vecteur(-4, -1, 2));
		assertEquals(v4.produitVectoriel(v5),v6);
		assertNotEquals(v1.produitVectoriel(v2),v4);
	}
	
	@Test
	public void testProduitScalaire() {
		assertEquals(v1.produitScalaire(v2), 7.0, 0.0001);
		assertEquals(v3.produitScalaire(v4), -3.0, 0.0001);
		assertNotEquals(v3.produitScalaire(v4), 12.0);
	
	}
	
	@Test
	public void testNormalisation() {
		assertEquals(v8.Normalisation(), new Vecteur(0.0, 0.0, 1.0));
		assertNotEquals(v8.Normalisation(),v1);
	}
}
