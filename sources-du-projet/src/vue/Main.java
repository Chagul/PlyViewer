package vue;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {
	@FXML Canvas canvas;
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

    public void initialize() {
    	// Point2D p1 = new Point2D(15,15);
         //canvas.getGraphicsContext2D().fillRect(p1.getX(), p1.getY(), 150, 150);
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.beginPath();
    	gc.lineTo(100, 100);
    	gc.lineTo(50, 50);
    	gc.lineTo(150, 200);
    	gc.closePath();
    	gc.fill();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
