package modele.outils;

public interface Observable {

	public void ajouterObservateur(Observateur o);
	public void supprimerObservateur(Observateur o);
	public void notifierObservateurs(Observateur o);
}
