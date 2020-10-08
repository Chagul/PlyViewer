package modele;

public enum Rotation {
	X("axeX"), Y("axeZ"), Z("axeZ");
	
	private String s;
	
	private Rotation(String s) {
		this.s = s;
	}
	
	public String getRotation() {
		return this.s;
	}
}
