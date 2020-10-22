package modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestMatrice {
	double k;
	double degre;
	Rotation x, y, z;
	public Matrice m1, m2, m3, m3bis, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13;
	
	@Before
	public void initialization() {

		k = 2.0;
		degre = 90;
		x = Rotation.X; y = Rotation.Y; z = Rotation.Z;
		double[][] tab1 = new double[][]{{0, 0, 0, 0, 4, 4, 4, 4}, 
	               						{0, 0, 4, 4, 0, 0, 4, 4},
	               						{0, 4, 0, 4, 0, 4, 0, 4},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab2 = new double[][]{{0, 0, 0, 0, 2, 2, 2, 2}, 
           				   				{0, 0, 2, 2, 0, 0, 2, 2},
           				   				{0, 2, 0, 2, 0, 2, 0, 2},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab3 = new double[][]{{0, 0, 0, 0, 8, 8, 8, 8}, 
				   						{0, 0, 8, 8, 0, 0, 8, 8},
				   						{0, 8, 0, 8, 0, 8, 0, 8},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
	    double[][] tab3bis = new double[][]{{0, 0, 0, 0, 8, 8, 8, 8}, 
					   					{0, 0, 8, 8, 0, 0, 8, 8},
					   					{0, 8, 0, 8, 0, 8, 0, 8},
		               					{2, 2, 2, 2, 2, 2, 2, 2}};
		double[][] tab5 = new double[][]{{0, 0, 0, 0, 8, 8, 8, 8}, 
				   						{0, 0, 8, 8, 0, 0, 8, 8},
				   						{0, 4, 0, 4, 0, 4, 0, 4},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab6 = new double[][]{{2, 2, 2, 2, 6, 6, 6, 6}, 
				   						{2, 2, 6, 6, 2, 2, 6, 6},
				   						{2, 6, 2, 6, 2, 6, 2, 6},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab7 = new double[][]{{0, 0, 0, 0, 4, 4, 4, 4}, 
	   									{0, -4, 0, -4, 0, -4, 0, -4},
	   									{0, 0, 4, 4, 0, 0, 4, 4},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab8 = new double[][]{{0, 4, 0, 4, 0, 4, 0, 4}, 
				   						{0, 0, 4, 4, 0, 0, 4, 4},
				   						{0, 0, 0, 0, -4, -4, -4, -4},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab9 = new double[][]{{0, 0, -4, -4, 0, 0, -4, -4}, 
				   						{0, 0, 0, 0, 4, 4,4, 4},
				   						{0, 4, 0, 4, 0, 4, 0, 4},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
		double[][] tab10 = new double[][]{{0, 0, 0, 0, -2, -2, -2, -2}, 
   										{0, 0, -2, -2, 0, 0, -2, -2},
   										{0, -2, 0, -2, 0, -2, 0, -2},
	               						{1, 1, 1, 1, 1, 1, 1, 1}};
   		double[][] tab11 = new double[][]{{1, 2, 3}, 
   	   									{4, 5, 6},
   	   									{7, 8, 9}};
   	   	double[][] tab12 = new double[][]{{1, 0, 0}, 
   	   									{0, 1, 0},
   	   									{0, 0, 1}};
   	   	double[][] tab13 = new double[][]{{30, 36, 42}, 
   	   									{66, 81, 96},
   	   									{102, 126, 150}};								
   	   									
		m1 = new Matrice(tab1);
		m4 = new Matrice(tab1);
		m2 = new Matrice(tab2);
		m3 = new Matrice(tab3);
		m3bis = new Matrice(tab3bis);
		m5 = new Matrice(tab5);
		m6 = new Matrice(tab6);
		m7 = new Matrice(tab7);
		m8 = new Matrice(tab8);
		m9 = new Matrice(tab9);
		m10 = new Matrice(tab10);
		m11 = new Matrice(tab11);
		m12 = new Matrice(tab12);
		m13 = new Matrice(tab13);
	}
	@Test
	public void testEquals() {
		//assertFalse(m1.equals(null));
		assertTrue(m1.equals(m1));
		assertTrue(m1.equals(m4));
		assertFalse(m1.equals(m8));
	}
	
	@Test
	public void testAddition() {
		assertTrue(m1.addition(m1).equals(m3bis));
		//assertTrue(m1.addition(m10).equals(m2));	
		assertFalse(m1.addition(m1).equals(m5));
	}
	
	@Test
	public void testMultiplication(){
		//multiplications scalaires
		assertEquals(m1.multiplication(1),m1);
		assertEquals(m1.multiplication(k),m3bis);
		//assertEquals(m1.multiplication(0.5),m2);
		assertNotEquals(m1.multiplication(0.5),m3);
		// multiplications matricielles
		assertNotEquals(m11.multiplication(m11),m12);
		assertEquals(m11.multiplication(m12),m11);
		assertEquals(m11.multiplication(m11),m13);
	}

	@Test
	public void testHomothétie(){
		assertEquals(m1.homothétie(k),m5);
		assertNotEquals(m1.homothétie(k),m6);
	}
	
	@Test
	public void testTranslation(){
		assertEquals(m1.translation(k,k,k),m6);
		assertNotEquals(m1.translation(k,k,k),m5);
	}
	
	@Test
	public void testRotation(){
		//assertFalse(m1.rotation(x, degre).equals(null));
		assertEquals(m1.rotation(x, degre),m7);
		assertEquals(m1.rotation(x, 0),m1);
		//assertTrue(m1.rotation(x, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertNotEquals(m1.rotation(x, degre),m8);
		//assertFalse(m1.rotation(y, degre).equals(null));
		assertEquals(m1.rotation(y, degre),m8);
		assertEquals(m1.rotation(y, 0),m1);
		//assertTrue(m1.rotation(y, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertNotEquals(m1.rotation(y, degre),m7);
		//assertFalse(m1.rotation(z, degre).equals(null));
		assertEquals(m1.rotation(z, degre),m9);
		assertEquals(m1.rotation(z, 0),m1);
		//assertTrue(m1.rotation(z, 360).equals(m1));
		//Pas de Modulo donc pas possible mais serait intéressant à programmer
		assertNotEquals(m1.rotation(z, degre),m8);		
	}
}
