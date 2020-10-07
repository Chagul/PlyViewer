package modele;

public class Point {
	private int x;
	private int y;
	private int z;
	
	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	
}
