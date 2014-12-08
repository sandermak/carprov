package carprov.dashboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.osgi.framework.ServiceReference;

import carprov.dashboard.api.App;

@Component
public class Dashboard {

	private final Map<ServiceReference, App> apps = new ConcurrentHashMap<>();
	private Stage stage;
	private FlowPane pane;
	private BorderPane root;

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

	@ServiceDependency(removed = "removeApp")
	public void addApp(ServiceReference sr, App app) {
		System.out.println("added " + sr);
		renderApp(app);
		apps.put(sr, app);
	}

	public void removeApp(ServiceReference sr) {
		// todo
	}

	private void renderApp(App app) {
		if (pane != null) {
			Platform.runLater(() -> {
				Node dashboardApp = app.getDashboardIcon();
				dashboardApp.setOnMouseClicked(event -> root.setCenter(app
						.getMainApp()));
				pane.getChildren().add(dashboardApp);
			});
		}
	}

	private void createUI() {
		if (stage == null) {
			stage = new Stage();
		}
		root = new BorderPane();
		Scene scene = new Scene(root, 600, 400);
		root.setStyle("-fx-background-color: #444444;");
		Text text = new Text("My first dashboard");
		text.setFill(Color.AZURE);
		root.setTop(text);
		pane = new FlowPane();
		pane.setPadding(new Insets(20));
		apps.values().forEach(this::renderApp);
		pane.setOrientation(Orientation.HORIZONTAL);
		root.setCenter(pane);
		root.setPadding(new Insets(20));
		stage.setScene(scene);
		stage.show();
	}

	private void destroyUI() {
		Platform.runLater(() -> {
			stage.hide();
			stage = null;
			pane = null;
			root = null;
		});
	}

}
