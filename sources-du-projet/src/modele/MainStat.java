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
		MatriceBonne m1 = new MatriceBonne(m);
		MatriceBonne m2 = new MatriceBonne(m23);
		System.out.println(m1);
		m1.multiplication(m2);
		System.out.println(m1);
		MatriceBonne m3 = new MatriceBonne(m);
		m3 = m3.multiplication(m1,m2);
		System.out.println(m3);


	}

}
