package carprov.phone;

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
public class PhoneApp implements App {
	
	@Inject
	private volatile BundleContext bundleContext;
	
	@Override
	public Node getDashboardIcon() {
		return DashboardHelper.getImage(bundleContext, "phone");
	}

	@Override
	public Node getMainApp() {
		Text text = new Text("NAVIGATION!!");
		text.setFill(Color.AQUAMARINE);
		text.setFont(new Font(20));
		return text;
	}

}
