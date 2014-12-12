package carprov.phone;

import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.apache.felix.dm.annotation.api.Start;
import org.osgi.framework.BundleContext;

import carprov.dashboard.api.App;
import carprov.dashboard.api.DashboardHelper;

@Component
public class PhoneApp implements App {
	
	@Inject
	private volatile BundleContext bundleContext;
	
	private AtomicReference<Node> mainApp = new AtomicReference<>();
	
	@Start
	void start() {
		mainApp.set(getMainAppNode());
	}
	
	@Override
	public String getAppName() {
		return "Phone";
	}
	
	@Override
	public Node getDashboardIcon() {
		return DashboardHelper.getImage(bundleContext, "phone");
	}

	@Override
	public Node getMainApp() {
		return mainApp.get();
	}
	
	public Node getMainAppNode() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(20));
		ImageView numberPadImg = DashboardHelper.getImage(bundleContext, "number-pad");
		numberPadImg.setFitHeight(100);
		BorderPane.setAlignment(numberPadImg, Pos.CENTER);
		pane.setLeft(numberPadImg);
		TextField phone = new TextField();
		phone.setFont(new Font(20));
		phone.setMaxWidth(200);
		pane.setCenter(phone);
		return pane;
	}

}
