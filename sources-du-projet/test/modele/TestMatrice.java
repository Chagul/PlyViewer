package modele;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Test;

public class TestMatrice {
	
	public Matrice m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;
	
	@Before
	public void initialization() {

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
		Matrice test = m1.addition(m1);
		assertFalse(test.equals(null));
		
		test = m1.addition(m1);
		assertTrue(test.equals(m3));
		test = m1.addition(m10);
		assertTrue(test.equals(m2));	
		
	}

}
