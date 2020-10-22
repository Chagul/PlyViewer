package modele;

<<<<<<< HEAD
public class Main {

	public static void main(String[] args) {
		double degre = 59.6;
		double[][] tab1 = new double[][]{
			    {0, 0, 0, 0, 4, 4, 4, 4}, 
				{0, 0, 4, 4, 0, 0, 4, 4},
				{0, 4, 0, 4, 0, 4, 0, 4},
				{1, 1, 1, 1, 1, 1, 1, 1}};
				
		double[][] tab6 = new double[][]{
			{1, 0, 0, 0},
			{0, Math.cos(degre), -Math.sin(degre), 0}, 
			{0, Math.sin(degre), Math.cos(degre), 0}, 
			{0, 0, 0, 1}};
				
		Matrice m1 = new Matrice(tab1);
		Matrice m6 = new Matrice(tab6);
		
		int k = 2;
		
		System.out.println(m6);
		System.out.println();
		
		Matrice res = m1.rotation(Rotation.X, degre);
		System.out.println(res);

	}

=======
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../vue/view.fxml"));
        primaryStage.setTitle("3D Viewer");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> branch 'master' of https://gitlab.univ-lille.fr/aurelien.plancke.etu/projetmode-alt3.git
}
