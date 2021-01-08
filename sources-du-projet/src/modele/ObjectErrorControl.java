package modele;

public class ObjectErrorControl {

	private int cptPoint;
	private int cptFace;
	private int cptLine;
	private int cptPointManquant;
	private int cptFaceManquante;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double minZ;
	private double maxZ;
	private boolean dansPoint;

	public ObjectErrorControl() {
		cptPoint = 0;
		cptFace = 0;
		cptLine = 0;
		cptPointManquant = 0;
		cptFaceManquante = 0;
		minX = 0.0;
		maxX = 0.0;
		minY = 0.0;
		maxY = 0.0;
		minZ = 0.0;
		maxZ = 0.0;
		dansPoint = true;
	}
	
	public void incrCptPointDeUn() {
		cptPoint++;
	}
	
	public void incrCptFaceDeUn() {
		cptFace++;
	}
	
	public void incrCptLineDeUn() {
		cptLine++;
	}
	
	public void incrCptPointManquantDeUn() {
		cptPointManquant++;
	}
	
	public void incrCptFaceManquanteDeUn() {
		cptFaceManquante++;
	}

	public boolean isDansPoint() {
		return dansPoint;
	}

	public void setDansPoint(boolean dansPoint) {
		this.dansPoint = dansPoint;
	}

	public int getCptPoint() {
		return cptPoint;
	}

	public int getCptFace() {
		return cptFace;
	}

	public int getCptLine() {
		return cptLine;
	}

	public int getCptPointManquant() {
		return cptPointManquant;
	}

	public int getCptFaceManquante() {
		return cptFaceManquante;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	
	public double getMinZ() {
		return minZ;
	}

	public void setMinZ(double minZ) {
		this.minZ = minZ;
	}

	public double getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(double maxZ) {
		this.maxZ = maxZ;
	}
}
