package modele.Math;

public enum Rotation {
	X("axeX"), Y("axeY"), Z("axeZ");
	
	private String s;
	
	private Rotation(String s) {
		this.s = s;
	}
	
	public String getRotation() {
		return this.s;
	}
}
