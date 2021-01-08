package modele;

import java.util.ArrayList;

public class ErrorList {

	private ArrayList<String> errorPoint;
	private ArrayList<String> errorFace;
	
	public ErrorList(ArrayList<String> anErrorPointList, ArrayList<String> anErrorFaceList) {
		this.errorPoint = new ArrayList<String>();
		this.errorPoint.addAll(anErrorPointList);
		this.errorFace = new ArrayList<String>();
		this.errorFace.addAll(anErrorFaceList);
	}

	public boolean isEmpty() {
		return this.errorFace.isEmpty() && this.errorFace.isEmpty();
	}

	public ArrayList<String> getListPointErreur() {
		return this.errorPoint;
	}
	
	public ArrayList<String> getListFaceErreur() {
		return this.errorFace;
	}
	
	@Override
	public String toString() {
		String stringBuilder = "";
		for(String s : errorPoint)
			stringBuilder += s + "\n";
		stringBuilder += "\n";
		for(String s : errorFace)
			stringBuilder += s + "\n";
		return stringBuilder;
	}
}
