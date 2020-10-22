package modele;

public class MainStat {

	public static void main(String[] args) {
		double m[][] = new double[][]{
			{0,1,2}
			,{3,4,5}
			,{6,7,8}
		};
		double m23[][] = new double[][]{
			{0,1,2}
			,{3,4,5}
			,{6,7,8}
		};
		Matrice m1 = new Matrice(m);
		Matrice m2 = new Matrice(m23);
		System.out.println(m1);
		m1.multiplication(m2);
		System.out.println(m1);
		Matrice m3 = new Matrice(m);
		m3 = m3.multiplication(m1,m2);
		System.out.println(m3);


	}

}
