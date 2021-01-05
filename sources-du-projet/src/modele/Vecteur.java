package modele;

public class Vecteur {

	private double x;
	private double y;
	private double z;
	
	
	public Vecteur(double anX, double anY, double aZ) {
		this.x = anX;
		this.y = anY;
		this.z = aZ;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * 
	 * @param v2 Le second vecteur
	 * @return Le produit vectoriel des deux vecteurs
	 */
	public Vecteur produitVectoriel(Vecteur v2) {
		double newX = this.y * v2.getZ() - this.z * v2.getY();
		double newY = this.z * v2.getX() - this.x * v2.getZ();
		double newZ = this.x * v2.getY() - this.y * v2.getX();
		return new Vecteur(newX, newY, newZ);
	}
	
	/**
	 * 
	 * @param v2 Le second vecteur
	 * @return Le produit scalaire des deux vecteurs
	 */
	public double produitScalaire(Vecteur v2) {
		return this.x * v2.x + this.y * v2.y + this.z * v2.z;
	}
	
	/**
	 * Calcule la norme pour éviter les erreurs d'affichages
	 * @return la norme d'un vecteur
	 */
	private double Norme() {
		return (Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
	}
	
	/**
	 * Normalise un vecteur
	 * @return Le vecteur normalisé
	 */
	public Vecteur Normalisation() {
		double norme = Norme();
		this.x = this.x / norme;
		this.y = this.y / norme;
		this.z = this.z / norme;
		return this;
	}
}
