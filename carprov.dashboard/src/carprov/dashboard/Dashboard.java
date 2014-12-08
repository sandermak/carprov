package carprov.dashboard;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.osgi.framework.BundleContext;

@Component
public class Dashboard {

	private Stage stage;
	
	@Inject
	private volatile BundleContext bundleContext;
	
	@Start
	public void start() {
		// Workaround to trigger toolkit init because
		// we are not using JavaFX's Application class
		System.out.println(new JFXPanel());
		Platform.setImplicitExit(false);
		System.out.println("Dashboard started");
		Platform.runLater(() -> createUI());
	}

	@Stop
	public void stop() {
		Platform.runLater(() -> destroyUI());
		System.out.println("Dashboard stopped");
	}

	private void createUI() {
		if (stage == null) {
			stage = new Stage();
		}
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400);
		root.setStyle("-fx-background-color: #444444;");
		Text text = new Text("My first dashboard");
		text.setFill(Color.AZURE);
		root.setTop(text);
		root.setCenter(getDashboard());
		root.setPadding(new Insets(20));
		stage.setScene(scene);
		stage.show();
	}
	
	private void destroyUI() {
		
	}
	
	private Node getDashboard() {
		FlowPane pane = new FlowPane();
		pane.setOrientation(Orientation.HORIZONTAL);
		pane.getChildren().add(getIcon("radio"));
		pane.getChildren().add(getIcon("maps"));
		pane.getChildren().add(getIcon("games"));
		pane.getChildren().add(getIcon("phone"));
		pane.getChildren().add(getIcon("music"));
		pane.getChildren().add(getIcon("config"));
		pane.setPadding(new Insets(20));
		return pane;
	}

	private Node getIcon(String name) {
		URL entry = bundleContext.getBundle().getEntry(name + ".png");
		try {
			Image image = new Image(entry.openStream());
			ImageView view = new ImageView(image);
			view.setPreserveRatio(true);
			return view;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
