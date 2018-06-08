package au.edu.usc.bict_explorer.controllers;

import au.edu.usc.bict_explorer.gui.BICTExplorer;
import au.edu.usc.bict_explorer.helpers.DegreeHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import au.edu.usc.bict_explorer.rules.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javax.swing.JOptionPane;

public class CareerController {

    @FXML private VBox rowContainer;

    @FXML
    /**
     *  clears all selected for the degrees and adds the children to the career rows
     */
    protected void initialize() throws IOException {

        //Career page is meant to be a 'splash' page. Deselect all options when this page loads.
        DegreeHelper.clearAllSelections();

        rowContainer.getChildren().add(createCareerRows());


    }

    public VBox createCareerRows() throws IOException {

        Map<String,Option> map = BICTExplorer.getDegree().careers();

        VBox root = new VBox();

        for (String key : map.keySet()) {

            Option career = map.get(key);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CareerRow.fxml"));
            GridPane careerRow = loader.load();
            CareerRowController c = loader.getController();
            c.setCareer(career);
            //Note: Option to place this logic inside the CareerRowController however felt it was easiest
            //to keep code confined to one class. This ensures Career view logic is generally in one place.

            c.getCareerLabel().setText(career.getCode() + " : " + career.getName());

            Set<Option> minors = career.getDownstream();

            String stringBuilder = "";
            for (Option m : minors) {
                stringBuilder += m.getCode() + " : " + m.getName() + '\n';
            }

            c.getMinorLabel().setText(stringBuilder);
            c.getDescriptionLabel().setText(career.getDescription());

            c.getSelectButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {

                    c.getCareer().setChosen(true);
                    c.getCareer().refresh();
                    try {
                        BICTExplorer.getViewHelper().showCoreLayout();
                    }catch(IOException ie) {
                        JOptionPane.showMessageDialog(null, ie.getMessage());
                    }


                }
            });

            //resizing
            c.getRowContainer().prefWidthProperty().bind(root.prefWidthProperty());
            c.getRowContainer().maxWidthProperty().bind(root.prefWidthProperty());

            root.getChildren().add(careerRow);
        }

        return root;
    }

}