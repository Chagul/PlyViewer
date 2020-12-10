package vue;


import controller.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/view.fxml"));
		Parent root = loader.load();

		/*
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/vue/view2.fxml"));
		Canvas root2 = (Canvas) loader2.load();

		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/vue/view2.fxml"));

		Canvas newCanvas = (Canvas) loader3.load();
		*/

		MainWindow controller = loader.getController();
		primaryStage.setResizable(false);
		controller.setStage(primaryStage);
		primaryStage.setTitle("3D Viewer");
		primaryStage.setScene(new Scene(root, 1450, 700));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

