package vue;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import modele.CreationFaceException;
import modele.CreationPointException;
import modele.Face;
import modele.PlyReader;

public class Main extends Application {
	@FXML ResizableCanvas canvas;
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("../vue/view.fxml"));

		/*
        Point2D p1 = new Point2D(15,15);

        canvas.getGraphicsContext2D().fillRect(p1.getX(), p1.getY(), 150, 150);
		 */
		primaryStage.setTitle("3D Viewer");
		primaryStage.setScene(new Scene(root, 1280, 800));
		primaryStage.show();
	}

	public void initialize() throws FileNotFoundException, CreationPointException, CreationFaceException {
		// Point2D p1 = new Point2D(15,15);
		//canvas.getGraphicsContext2D().fillRect(p1.getX(), p1.getY(), 150, 150);
		/*double ratioX = canvas.getWidth() / (canvas.getWidth()+1280);
		double ratioY = canvas.getHeight() / (canvas.getHeight() + 800);*/
		PlyReader aPlyReader = new PlyReader("sources-du-projet/exemples/dodecahedron.ply");
		aPlyReader.initPly();
		aPlyReader.readPly();
		ArrayList<Face> listFace = new ArrayList<Face>();
		listFace.addAll(aPlyReader.getListFace());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.beginPath();
		gc.translate(600, 400);
		for (Face face : listFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				if(i < face.getListPoint().size() - 1)
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(i+1).getX()*100, face.getListPoint().get(i+1).getY()*100);
				else
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(0).getX()*100, face.getListPoint().get(0).getY()*100);
			}
		}
		gc.closePath();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
