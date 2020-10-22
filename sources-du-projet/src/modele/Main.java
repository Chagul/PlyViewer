package modele;

public class Main {

	public static void main(String[] args) {
		double degre = 59.6;
		double[][] tab1 = new double[][]{
			    {0, 0, 0, 0, 4, 4, 4, 4}, 
				{0, 0, 4, 4, 0, 0, 4, 4},
				{0, 4, 0, 4, 0, 4, 0, 4},
				{1, 1, 1, 1, 1, 1, 1, 1}};
				
		double[][] tab6 = new double[][]{
			{1, 0, 0, 0},
			{0, Math.cos(degre), -Math.sin(degre), 0}, 
			{0, Math.sin(degre), Math.cos(degre), 0}, 
			{0, 0, 0, 1}};
				
		Matrice m1 = new Matrice(tab1);
		Matrice m6 = new Matrice(tab6);
		
		int k = 2;
		
		System.out.println(m6);
		System.out.println();
		
		Matrice res = m1.rotation(Rotation.X, degre);
		System.out.println(res);
		
		Matrice restranslation = m1.translation(tab1[0][7], tab1[0][7], tab1[0][7]);

	}
}


