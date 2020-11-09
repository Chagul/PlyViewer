package modele;

import java.util.Arrays;

/**
 * <b>Matrice est la classe représentant une matrice algébrique.</b>
 * <p>
 * 	Une matrice est caractérisée par les informations suivantes :
 * 	<ul>
 * 	<li>Le nombre de lignes constant, 
 * 		puisqu'on crée les matrices à partir d'un Point de coordonnées x, y et z. </li>
 * 	<li>Le nombre de colonnes constants.</li>
 * 	<li>Un tableau à deux dimensions.</li>
 * </ul>
 * </p>
 * @author kharmacm
 * @version 1.0
 */
public class Matrice {
	
	/**
	 * Nombre de Lignes de la matrice.
	 */
	private int nb_Lignes;
	
	/**
	 * Nombre de colonnes de la matrice.
	 */
	private int nb_Col;
	
	/**
	 * Tableau de deux dimensions, representant la matrice.
	 */
	private double M[][];
	
	/**
	 * Constructeur Matrice à partir d'un point donné.
	 * <p>
	 * A la construction de la matrice à partir du point donné en paramètre,
	 * les valeurs x, y et z du Point sont stockés dans le tableau M dans cet ordre.
	 * </p>
	 * @param p
	 * 			Le point à partir duquel la matrice est crée.
	 */
	public Matrice(Point p) {
		this.nb_Col = 3; //x, y et z donc 3.
		this.nb_Lignes = 1; //Matrice ligne donc 1.
		
		this.M = new double[nb_Lignes][nb_Col];
		this.M[0][0] = p.getX();
		this.M[0][1] = p.getY();
		this.M[0][2] = p.getZ();
	}
	
	/**
	 * Constructeur Matrice à partir d'un tableau de valeur.
	 * <p>
	 * A la construction de la matrice à partir d'un tableau de valeur,
	 * après vérification du tableau donné en paramètre on le stock dans le tableau en attribut.
	 * </p>
	 * @param M 
	 * 			Tableau de valeurs.
	 * @exception IllegalArgumentException Toutes les colonnes doivent avoir la même longueur.
	 */
	public Matrice(double M[][]) {
		this.nb_Lignes = M.length;
		this.nb_Col = M[0].length;
		
		for (int i = 0; i < this.nb_Lignes; i++) {
         if (M[i].length != this.nb_Col) {
            throw new IllegalArgumentException("Toutes les colonnes doivent avoir la même longueur.");
         }
		}
		
		this.M = M;
	}
	
	
	/**
	 * Getter du nombre de lignes de la matrice THIS.
	 * @return nb_Lignes
	 * 			Nombre de lignes.
	 */
	public int getNb_Lignes() {
		return this.nb_Lignes;
	}

	/**
	 * Getter du nombre de colonnes de la matrice THIS.
	 * @return nb_Col
	 * 			Nombre de colonnes.
	 */
	public int getNb_Col() {
		return this.nb_Col;
	}

	/**
	 * Getter du tableau de valeurs de la matrice THIS.
	 * @return M
	 * 			Tableau de valeurs.
	 */
	public double[][] getM() {
		return this.M;
	}

	/**
	 * Addition de deux matrices, celle donnée en paramètre et THIS.
	 * @param m
	 * 			Matrice à additioner au THIS.
	 * @return
	 * 			La matrice résultat en conservant la matrice THIS.
	 * @exception IllegalArgumentException vérification des dimentions
	 */
	public Matrice addition(Matrice m) {
		double vals[][];
		int l1 = this.nb_Lignes;
		int c1 = this.nb_Col;
		
		int l2 = m.getNb_Lignes();
		int c2 = m.getNb_Col();
		
		if(l1 != l2 || c1 != c2) {
			throw new IllegalArgumentException("Addition impossible : Problème de dimensions.");
		} else {
			vals = new double[l1][c1];
		}
		
		for (int i = 0; i < l1; i++) {
			for(int j = 0; j<c1; j++) {
				vals[i][j] = this.M[i][j] + m.getM()[i][j];
			}
		}
		
		Matrice res = new Matrice(vals);
		return res;
	}
	
	/**
	 * Addition de deux matrices dans l'une est contruite à partir d'un point.
	 * @param p
	 * 			Point à partir duquel on construit la matrice.
	 * @return
	 * 			Le résultat de l'addition.
	 */
	public Matrice addition(Point p) {
		Matrice m = new Matrice(p);
		return this.addition(m);
	}
	
	
	/**
	 * Multiplication de deux Matrices, This et celle donnée en paramètre.
	 * @param m
	 * 			Matrice other.
	 * @return
	 * 			La matrice résultat en conservant la matrice this.
	 * @exception IllegalArgumentException vérification des dimensions.
	 */
	public Matrice multiplication(Matrice m) {
		return this.multiplication(this, m);
	}
	
	/**
	 * Multiplication de deux matrices dans l'une est contruite à partir d'un point.
	 * @param p
	 * 			Point à partir duquel on construit la matrice.
	 * @return
	 * 			Le produit.
	 */
	public Matrice multiplication(Point p) {
		Matrice m = new Matrice(p);
		return this.multiplication(m);
	}
	
	/**
	 * Multiplication de deux matrices.
	 * @param m1
	 * 			Première Matrice.
	 * @param m2
	 * 			Deuxième Matrice.
	 * @return
	 * 			Le produit.
	 */
	public Matrice multiplication(Matrice m1, Matrice m2) {
		double vals[][];
		int l1 = m1.getNb_Lignes();
		int c1 = m1.getNb_Col();
		
		int l2 = m2.getNb_Lignes();
		int c2 = m2.getNb_Col();
		if(l1 == c2) {
			vals = new double [l2][c1];
		} else {
			throw new IllegalArgumentException("Multiplication impossible : Problème de dimensions.");
		}
		
		for(int i = 0; i<l2; i++) {
			for(int j = 0; j<c1; j++) {
				vals[i][j] = 0;
				for(int k = 0; k<c2; k++) {
					vals[i][j] = vals[i][j] + m2.getM()[i][k] * m1.getM()[k][j];
				}
			}
		}
		
		Matrice res = new Matrice(vals);
		
		return res;
	}
	
	/**
	 * Multiplication d'une matrice par un nombre scalaire.
	 * @param scalaire
	 * 					Nombre par lequel on multiplie la matrice.
	 * @return
	 * 			La Matrice résultat.
	 */
	public Matrice multiplication(Matrice m, double scalaire) {
		double resTab[][] = new double[m.getNb_Lignes()][m.getNb_Col()];
		for(int i = 0; i < resTab.length; i++) {
			for(int j = 0; j < resTab[0].length; j++) {
				if(i == resTab.length-1) {
					resTab[i][j] = m.getM()[i][j];
				} else {
					resTab[i][j] = m.getM()[i][j]*scalaire;
				}
				
			}
		}
		return new Matrice(resTab);
	}
	
	/**
	 * Multiplication de THIS par un nombre scalaire.
	 * @param scalaire
	 * 			Nombre par lequel on multiplie la matrice.
	 * @return
	 * 			La Matrice résultat.
	 */
	public Matrice multiplication(double scalaire) {
		return this.multiplication(this, scalaire);
	}
	
	/**
	 * Homothétie de rapport k autour de l'origine.
	 * <p>
	 * La matrice THIS est conservée, après homothétie la fonction retourne une nouvelle matrice résultat.
	 * </p>
	 * @param rapportK 
	 * 					Rapport k.
	 * @return
	 * 			La matrice résultat de l'homothétie sur THIS. En gardant THIS intacte.
	 */
	public Matrice homothétie(double rapportK) {
		double vals[][] = new double[][] { 
			{rapportK, 0, 0, 0}, 
			{0, rapportK, 0, 0}, 
			{0, 0, rapportK, 0},
			{0, 0, 0, 1}
		};
		
		Matrice m = new Matrice(vals);
		
		return this.multiplication(m);	
	}
	
	/**
	 * Translation de vecteur (t1, t2, t3).
	 * @param t1 
	 * @param t2
	 * @param t3
	 * @return
	 * 			Résultat de la translation.
	 */
	public Matrice translation(double t1, double t2, double t3) {
		double vals[][] = new double[][] { 
			{1, 0, 0, t1}, 
			{0, 1, 0, t2},
			{0, 0, 1, t3},
			{0, 0, 0, 1}
		};
		
		Matrice m1 = new Matrice(vals);
		
		return multiplication(this, m1);
		
	}
	
	/**
	 * Rotation autour de l'origine d'angle degre.
	 * @param degre
	 * 				l'angle de l'origine
	 * @return
	 * 			Le résultat de la rotation.
	 * @exception Vérification de la nature de la rotation.
	 */
	public Matrice rotation(Rotation r, double degre) {
		double vals[][] = new double[this.nb_Lignes][this.nb_Col];
		if(r.equals(Rotation.X)) {
			vals = new double[][] { 
				{1, 0, 0, 0},
				{0, Math.cos(Math.toRadians(degre)), -Math.sin(Math.toRadians(degre)), 0}, 
				{0, Math.sin(Math.toRadians(degre)), Math.cos(Math.toRadians(degre)), 0}, 
				{0, 0, 0, 1},
			};
		} else if(r.equals(Rotation.Y)) {
			vals = new double[][] { 
				{Math.cos(Math.toRadians(degre)), 0, Math.sin(Math.toRadians(degre)), 0},
				{0, 1, 0, 0}, 
				{-Math.sin(Math.toRadians(degre)), 0, Math.cos(Math.toRadians(degre)), 0}, 
				{0, 0, 0, 1},
			};
		} else if(r.equals(Rotation.Z)) {
			vals = new double[][] { 
				{Math.cos(Math.toRadians(degre)), -Math.sin(Math.toRadians(degre)), 0, 0},
				{Math.sin(Math.toRadians(degre)), Math.cos(Math.toRadians(degre)), 0, 0}, 
				{0, 0, 1, 0}, 				
				{0, 0, 0, 1},
			};
		} else {
			throw new IllegalArgumentException("Le type de rotation n'est pas valable.");
		}
		
		
		Matrice m = new Matrice(vals);
		//System.out.println("cos(90) = " + Math.cos(Math.toRadians(90.0)) + " sin(90) = " + Math.sin(Math.toRadians(degre)));
		return this.multiplication(m);	
	}
	
	/**
	 * Fonction d'affichage d'une Matrice.
	 */
	public String toString() {
		String res = "";
		for(int i = 0; i< this.nb_Lignes; i++) {
			for(int j = 0; j< this.nb_Col; j++) {
				res+=this.M[i][j] + " ";
			}
			res+="\n";
		}
		return res;
	}

	
	/**
	 * Override de equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrice other = (Matrice) obj;
		if (!Arrays.deepEquals(Arrays.copyOfRange(M, 0, 3), Arrays.copyOfRange(other.M, 0, 3)))
			return false;
		if (nb_Col != other.nb_Col)
			return false;
		if (nb_Lignes != other.nb_Lignes)
			return false;
		return true;
	}
}
