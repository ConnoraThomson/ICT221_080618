package au.edu.usc.bict_explorer.controllers;

import au.edu.usc.bict_explorer.rules.Option;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;


public class OptionRowController {

    private Option option;

    @FXML private Label  textLabel1;
    @FXML private Label  textLabel2;
    @FXML private Label  textLabel3;
    @FXML private Label  textLabel4;
    @FXML private CheckBox  optionCheckBox;
    @FXML private GridPane rowContainer;

    @FXML
    protected void initialize() {

    }

    /**
     * Return GUI items for use modifcation and event handling
     * @return
     */

    public Label getTextLabel1() {
        return textLabel1;
    }

    public Label getTextLabel2() {
        return textLabel2;
    }

    public Label getTextLabel3() {
        return textLabel3;
    }

    public Label getTextLabel4() {
        return textLabel4;
    }

    public CheckBox getOptionCheckBox() {
        return optionCheckBox;
    }

    public GridPane getRowContainer() {
        return rowContainer;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}


//}
