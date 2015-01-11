package carprov.dashboard.api;

import java.io.IOException;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.osgi.framework.BundleContext;

public class DashboardHelper {
	
	public static ImageView getImageByFullname(BundleContext bundleContext, String name) {
		URL entry = bundleContext.getBundle().getEntry(name);
		try {
			Image image = new Image(entry.openStream());
			ImageView view = new ImageView(image);
			view.setPreserveRatio(true);
			return view;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ImageView getImage(BundleContext bundleContext, String name) {
	    return getImageByFullname(bundleContext, name + ".png");
	}
}
