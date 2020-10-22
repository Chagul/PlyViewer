package modele;

public class MainA {

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
				
		MatriceBonne m1 = new MatriceBonne(tab1);
		MatriceBonne m6 = new MatriceBonne(tab6);
		
		int k = 2;
		
		System.out.println(m6);
		System.out.println();
		
		MatriceBonne res = m1.rotation(Rotation.X, degre);
		System.out.println(res);

	}

}
