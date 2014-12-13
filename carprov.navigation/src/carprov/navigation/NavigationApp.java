package carprov.navigation;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.osgi.framework.BundleContext;

import carprov.dashboard.api.App;
import carprov.dashboard.api.DashboardHelper;

@Component
public class NavigationApp implements App {
	
	@Inject
	private volatile BundleContext bundleContext;
	
	@Override
	public String getAppName() {
		return "Navigation";
	}
	
	@Override
	public int getPreferredPosition() {
		return 10;
	}
	
	@Override
	public Node getDashboardIcon() {
		return DashboardHelper.getImage(bundleContext, "maps");
	}

	@Override
	public Node getMainApp() {
		Text text = new Text("NAVIGATION!!");
		text.setFill(Color.AQUAMARINE);
		text.setFont(new Font(20));
		return text;
	}

}
