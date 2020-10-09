package modele;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Test;

public class TestMatrice {
	int k;
	int degre;
	Rotation x, y, z;
	public Matrice m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;	
	
	@Before
	public void initialization() {

		k = 2;
		degre = 90;
		x = Rotation.X; y = Rotation.Y; z = Rotation.Z;
		double[][] tab1 = new double[][]{{0, 0, 0, 0, 4, 4, 4, 4}, 
	               						{0, 0, 4, 4, 0, 0, 4, 4},
	               						{0, 4, 0, 4, 0, 4, 0, 4}};
		double[][] tab2 = new double[][]{{0, 0, 0, 0, 2, 2, 2, 2}, 
           				   				{0, 0, 2, 2, 0, 0, 2, 2},
           				   				{0, 2, 0, 2, 0, 2, 0, 2}};
		double[][] tab3 = new double[][]{{0, 0, 0, 0, 8, 8, 8, 8}, 
				   						{0, 0, 8, 8, 0, 0, 8, 8},
				   						{0, 8, 0, 8, 0, 8, 0, 8}};
		double[][] tab5 = new double[][]{{0, 0, 0, 0, 8, 8, 8, 8}, 
				   						{0, 0, 8, 8, 0, 0, 8, 8},
				   						{0, 4, 0, 4, 0, 4, 0, 4}};
		double[][] tab6 = new double[][]{{2, 2, 2, 2, 6, 6, 6, 6}, 
				   						{2, 2, 6, 6, 2, 2, 6, 6},
				   						{2, 6, 2, 6, 2, 6, 2, 6}};
		double[][] tab7 = new double[][]{{0, 0, 0, 0, 2, 2, 2, 2}, 
	   									{0, -2, 0, -2, 0, -2, 0, -2},
	   									{0, 0, 2, 2, 0, 0, 2, 2}};
		double[][] tab8 = new double[][]{{0, 2, 0, 2, 0, 2, 0, 2}, 
				   						{0, 0, 2, 2, 0, 0, 2, 2},
				   						{0, 0, 0, 0, -2, -2, -2, -2}};
		double[][] tab9 = new double[][]{{0, 0, -2, -2, 0, 0, -2, -2}, 
				   						{0, 0, 0, 0, 2, 2, 2, 2},
				   						{0, 2, 0, 2, 0, 2, 0, 2}};
		double[][] tab10 = new double[][]{{0, 0, 0, 0, -2, -2, -2, -2}, 
   										{0, 0, -2, -2, 0, 0, -2, -2},
   										{0, -2, 0, -2, 0, -2, 0, -2}};
		m1 = new Matrice(tab1);
		m4 = new Matrice(tab1);
		m2 = new Matrice(tab2);
		m3 = new Matrice(tab3);
		m5 = new Matrice(tab5);
		m6 = new Matrice(tab6);
		m7 = new Matrice(tab7);
		m8 = new Matrice(tab8);
		m9 = new Matrice(tab9);
		m10 = new Matrice(tab10);
	}
	@Test
	public void testEquals() {
		assertFalse(m1.equals(null));
		assertTrue(m1.equals(m1));
		assertTrue(m1.equals(m4));
		assertFalse(m1.equals(m8));
	}
	
	@Test
	public void testAddition() {
		assertFalse(m1.addition(m1).equals(null));
		assertTrue(m1.addition(m1).equals(m3));
		assertTrue(m1.addition(m10).equals(m2));	
		assertFalse(m1.addition(m1).equals(m5));
	}
	
	@Test
	public void testMultiplication(){
		assertFalse(m1.multiplication(m1).equals(null));
		assertTrue(m1.multiplication(1).equals(m1));
		assertTrue(m1.multiplication(k).equals(m3));
		assertTrue(m1.multiplication(0.5).equals(m2));
		assertFalse(m1.multiplication(0.5).equals(m3));
		// TO DO test entre 2 matrices
	}

	@Test
	public void testHomothétie(){
		assertFalse(m1.homothétie(k).equals(null));
		assertTrue(m1.homothétie(k).equals(m5));
		assertFalse(m1.homothétie(k).equals(m6));
	}
	
	@Test
	public void testTranslation(){
		assertFalse(m1.translation(k,k,k).equals(null));
		assertTrue(m1.translation(k,k,k).equals(m6));
		assertFalse(m1.translation(k,k,k).equals(m5));
	}
	
	@Test
	public void testRotation(){
		assertFalse(m1.rotation(x, degre).equals(null));
		assertTrue(m1.rotation(x, degre).equals(m7));
		assertTrue(m1.rotation(x, 0).equals(m1));
		//assertTrue(m1.rotation(x, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertFalse(m1.rotation(x, degre).equals(m8));
		assertFalse(m1.rotation(y, degre).equals(null));
		assertTrue(m1.rotation(y, degre).equals(m8));
		assertTrue(m1.rotation(y, 0).equals(m1));
		//assertTrue(m1.rotation(y, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertFalse(m1.rotation(y, degre).equals(m7));
		assertFalse(m1.rotation(z, degre).equals(null));
		assertTrue(m1.rotation(z, degre).equals(m9));
		assertTrue(m1.rotation(z, 0).equals(m1));
		//assertTrue(m1.rotation(z, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertFalse(m1.rotation(z, degre).equals(m8));		
	}
}
