package carprov.navigation;

import javafx.scene.Node;

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
	public Node getDashboardIcon() {
		return DashboardHelper.getIcon(bundleContext, "maps");
	}

}
