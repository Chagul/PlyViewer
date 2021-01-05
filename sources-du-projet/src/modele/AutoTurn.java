package modele;

import javafx.scene.canvas.Canvas;

public class AutoTurn implements Runnable{

	private boolean running = true;
	private Model3D modele = null;
	private Canvas selected = null;
	
	public void start(Model3D aModel, Canvas aCanvas) {
		this.modele = aModel;
		this.selected = aCanvas;
		run();
	}
	@Override
	public void run() {
		while(running) {
			this.modele.setMatricePoint(this.modele.getMatricePoint().rotation(Rotation.X, 1));
		}try {
			Thread.sleep(500);
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		
	}

	public void stop() {
		this.running = false;
		this.modele = null;
		this.selected = null;
	}
}
