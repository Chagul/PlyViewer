package modele;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;

/**
 * Deprecated
 * @author aurelien
 *
 */
public class AutoTurn implements Runnable{

	private volatile boolean running = true;
	private Model3D modele = null;
	private Canvas selected = null;
	
	public AutoTurn(Model3D model3d, Canvas canvasSelected) {
		this.modele = model3d;
		this.selected = canvasSelected;
	}
		
	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		Timeline rotation;
		KeyFrame beggining = new KeyFrame(Duration.seconds(0));
		KeyFrame end = new KeyFrame(Duration.millis(500), event ->{
			this.modele.setMatricePoint(this.modele.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
			this.modele.setMatricePoint(this.modele.getMatricePoint().rotation(Rotation.X, 1));
			this.modele.setMatricePoint(this.modele.getMatricePoint().rotation(Rotation.Y, 1));
			this.modele.setMatricePoint(this.modele.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
		});
		rotation = new Timeline(beggining, end);
		rotation.setCycleCount(Timeline.INDEFINITE);
		
		/*while(running) {
			this.modele.setMatricePoint(this.modele.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
			this.modele.setMatricePoint(this.modele.getMatricePoint().rotation(Rotation.X, 1));
			this.modele.setMatricePoint(this.modele.getMatricePoint().rotation(Rotation.Y, 1));
			this.modele.setMatricePoint(this.modele.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		
	}
}
