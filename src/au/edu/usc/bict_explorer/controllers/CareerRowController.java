package au.edu.usc.bict_explorer.controllers;

import au.edu.usc.bict_explorer.rules.Option;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CareerRowController {

    private Option career;

    @FXML private Label  careerTitle;
    //@FXML private ImageView careerImage;
    @FXML private Label  minorInformation;
    @FXML private Label  careerDescription;
    @FXML private Button selectButton;

    @FXML private GridPane rowContainer;

    @FXML
    protected void initialize() {

    }

    /**
     * get set for  careers
     * @return
     */

    public Option getCareer() {
        return career;
    }

    public void setCareer(Option career) {
        this.career = career;
    }

    public Label getCareerLabel(){

        return careerTitle;
    }

    public Label getMinorLabel() {

        return minorInformation;

    }

    public Label getDescriptionLabel(){

        return careerDescription;

    }

    public Button getSelectButton() {
        return selectButton;
    }


    public void setSelectButton(Button selectButton) {
        this.selectButton = selectButton;
    }

    public GridPane getRowContainer() {
        return rowContainer;
    }

    public void setRowContainer(GridPane rowContainer) {
        this.rowContainer = rowContainer;
    }
}

