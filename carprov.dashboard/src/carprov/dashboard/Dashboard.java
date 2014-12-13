package carprov.dashboard;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

	private static final String DASHBOARD_TITLE = "Ace Car Entertainment";
	
	@Inject
	private volatile BundleContext bundleContext;
	
	private final Map<ServiceReference, App> apps = new ConcurrentHashMap<>();
	private final Map<App, Node> addedDashboardIcons = new ConcurrentHashMap<>();
	
	private Text titleText;
	private Stage stage;
	private BorderPane mainView;
	private FlowPane dashboardIcons;

	@Start
	void start() {
		// Workaround to trigger toolkit init because
		// we are not using JavaFX's Application class
		System.out.println(new JFXPanel());
		Platform.setImplicitExit(false);
		System.out.println("Dashboard started");
		Platform.runLater(() -> createUI());
	}

	@Stop
	void stop() {
		Platform.runLater(() -> destroyUI());
		System.out.println("Dashboard stopped");
	}

	@ServiceDependency(removed = "removeApp", required = false)
	void addApp(ServiceReference sr, App app) {
		System.out.println("added " + sr);
		renderApp(app);
		apps.put(sr, app);
	}

	void removeApp(ServiceReference sr) {
		System.out.println("removed " + sr);
		App removedApp = apps.remove(sr);
		Platform.runLater(() -> {
			Node removedDashboardApp = addedDashboardIcons.remove(removedApp);
			dashboardIcons.getChildren().remove(removedDashboardApp);
		});
	}

	private void renderApp(App app) {
		if (dashboardIcons != null) {
			Platform.runLater(() -> {
				Node dashboardApp = app.getDashboardIcon();
				dashboardApp.setUserData(app.getPreferredPosition());
				addedDashboardIcons.put(app, dashboardApp);
				dashboardApp.setOnMouseClicked(event -> startApp(app));
				
				// Add to children while respecting the preferred order
				List<Node> children = dashboardIcons.getChildren();
				if(children.size() == 0) {
					children.add(dashboardApp);
				} else {
					for(int index = 0; index < children.size(); index++) {
						if((int) children.get(index).getUserData() >= app.getPreferredPosition()) {
							children.add(index, dashboardApp);
							break;
						}
					}		
				}
			});
		}
	}

	private void startApp(App app) {
		titleText.setText(DASHBOARD_TITLE + " > " + app.getAppName()); 
		Node mainApp = app.getMainApp();
		mainView.setCenter(mainApp);
	}
	
	private void startDashboard() {
		titleText.setText(DASHBOARD_TITLE);
		mainView.setCenter(dashboardIcons);
	}
	
	private void createUI() {
		if (stage == null) {
			stage = new Stage();
		}
		mainView = new BorderPane();
		mainView.setPadding(new Insets(20));
		Scene scene = new Scene(mainView, 600, 400);
		mainView.setStyle("-fx-background-color: #444444;");
		mainView.setTop(getTopBar());

		dashboardIcons = new FlowPane();
		dashboardIcons.setPadding(new Insets(20));
		dashboardIcons.setOrientation(Orientation.HORIZONTAL);
		apps.values().forEach(this::renderApp);
		startDashboard();
		
		stage.setScene(scene);
		stage.show();
	}

	private Node getTopBar() {
		ImageView homeImg = DashboardHelper.getImage(bundleContext, "home");
		homeImg.setFitHeight(40);
		homeImg.setOnMouseClicked(event -> startDashboard());
		
		titleText = new Text(DASHBOARD_TITLE);
		titleText.setFont(new Font("Open Sans", 22));
		titleText.setFill(Color.AZURE);

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		pane.setStyle("-fx-background-color: #222222;");
		pane.setLeft(titleText);
		pane.setRight(homeImg);
		return pane;
	}

	private void destroyUI() {
		Platform.runLater(() -> {
			stage.hide();
			stage = null;
			dashboardIcons = null;
			mainView = null;
		});
	}

}
