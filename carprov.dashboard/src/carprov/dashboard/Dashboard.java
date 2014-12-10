package carprov.dashboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import carprov.dashboard.api.App;
import carprov.dashboard.api.DashboardHelper;

@Component
public class Dashboard {

	@Inject
	private volatile BundleContext bundleContext;
	
	private final Map<ServiceReference, App> apps = new ConcurrentHashMap<>();
	private final Map<App, Node> addedDashboardIcons = new ConcurrentHashMap<>();
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

	@ServiceDependency(removed = "removeApp", required = false)
	public void addApp(ServiceReference sr, App app) {
		System.out.println("added " + sr);
		renderApp(app);
		apps.put(sr, app);
	}

	public void removeApp(ServiceReference sr) {
		System.out.println("removed " + sr);
		App removedApp = apps.remove(sr);
		Platform.runLater(() -> {
			Node removedDashboardApp = addedDashboardIcons.remove(removedApp);
			pane.getChildren().remove(removedDashboardApp);
		});
	}

	private void renderApp(App app) {
		if (pane != null) {
			Platform.runLater(() -> {
				Node dashboardApp = app.getDashboardIcon();
				addedDashboardIcons.put(app, dashboardApp);
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
		root.setTop(getTopBar());
		pane = new FlowPane();
		pane.setPadding(new Insets(20));
		pane.setOrientation(Orientation.HORIZONTAL);
		apps.values().forEach(this::renderApp);
		root.setCenter(pane);
		root.setPadding(new Insets(20));
		stage.setScene(scene);
		stage.show();
	}

	private Node getTopBar() {
		ImageView homeImg = DashboardHelper.getImage(bundleContext, "home");
		homeImg.setFitHeight(40);
		homeImg.setOnMouseClicked(event -> {
			root.setCenter(pane);
		});
		
		Text text = new Text("Ace Car Entertainment");
		text.setFont(new Font("Open Sans", 22));
		text.setFill(Color.AZURE);

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		pane.setStyle("-fx-background-color: #222222;");
		pane.setLeft(text);
		pane.setRight(homeImg);
		return pane;
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
