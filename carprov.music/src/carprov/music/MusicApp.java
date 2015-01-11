package carprov.music;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.osgi.framework.BundleContext;

import carprov.dashboard.api.App;
import carprov.dashboard.api.DashboardHelper;

@Component
public class MusicApp implements App {

    @Inject
    private BundleContext bundleContext;
    
    @Override
    public String getAppName() {
        return "Music";
    }

    @Override
    public int getPreferredPosition() {
        return 5;
    }

    @Override
    public Node getDashboardIcon() {
        return DashboardHelper.getImage(bundleContext, "music");
    }

    @Override
    public Node getMainApp() {
        ImageView background = DashboardHelper.getImageByFullname(bundleContext, "wave.gif");
        ColorInput blackout = new ColorInput();
        blackout.setPaint(Color.BLACK);
        background.setEffect(blackout);
        StackPane stackPane = new StackPane(background);
        
        ImageView playButton = DashboardHelper.getImage(bundleContext, "play");
        playButton.setOnMouseClicked((evt) -> background.setEffect(null));
        ImageView stopButton = DashboardHelper.getImage(bundleContext, "stop");
        stopButton.setOnMouseClicked((evt) -> background.setEffect(blackout));
        HBox buttons = new HBox(
                DashboardHelper.getImage(bundleContext, "prev"),
                playButton,
                stopButton,
                DashboardHelper.getImage(bundleContext, "forward")
                );
        StackPane.setMargin(buttons, new Insets(30, 0, 0, 70));
        StackPane.setAlignment(buttons, Pos.TOP_CENTER);
        stackPane.getChildren().add(buttons);
        return stackPane;
    }

}
