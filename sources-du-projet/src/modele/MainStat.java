package modele;

public class MainStat {

	public static void main(String[] args) {
		double m[][] = new double[][]{
				{0,1,2}
				,{3,4,5}
				,{6,7,8}
				};
		Matrice m1 = new Matrice(m);
		System.out.println(m1);

	}

}
