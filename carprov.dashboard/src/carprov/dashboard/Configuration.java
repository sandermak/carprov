package carprov.dashboard;

import java.sql.Date;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import carprov.dashboard.api.App;
import carprov.dashboard.api.DashboardHelper;

@Component
public class Configuration implements App {

	@Inject
	private BundleContext bundleContext;
	
	@Override
	public String getAppName() {
		return "Config";
	}
	
	@Override
	public int getPreferredPosition() {
		return 100;
	}

	@Override
	public Node getDashboardIcon() {
		return DashboardHelper.getImage(bundleContext, "config");
	}

	@Override
	public Node getMainApp() {
		FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
		flowPane.setAlignment(Pos.CENTER);
		
		for(Bundle b: bundleContext.getBundles()) {
			if(b.getSymbolicName().startsWith("carprov"))
				flowPane.getChildren().add(getBundleVersion(b));
		}
		
		return flowPane;
	}

	private Node getBundleVersion(Bundle b) {
		Text text = new Text(b.getSymbolicName() + ": " + b.getHeaders().get("Bundle-Version") + " (" + new Date(b.getLastModified())+ ")");
		text.setFont(new Font("Open Sans", 18));
		text.setFill(Color.AZURE);
		return text;
	}

}
