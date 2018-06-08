package au.edu.usc.bict_explorer.helpers;

import au.edu.usc.bict_explorer.gui.BICTExplorer;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * decides what scene should be shown
 */
public class ViewHelper {

    public void showCareerLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CareerLayout.fxml"));
        VBox root = loader.load();
        BICTExplorer.updateScene(root);
    }

    /**
     * show the layout of the GUI. Refreshes the view, if ir updates.
     * @throws IOException
     */

    public void showCoreLayout()throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CoreLayout.fxml"));
        Parent root = loader.load();
        BICTExplorer.updateScene(root);
    }


}
