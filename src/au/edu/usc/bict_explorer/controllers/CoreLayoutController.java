package au.edu.usc.bict_explorer.controllers;

import au.edu.usc.bict_explorer.gui.BICTExplorer;
import au.edu.usc.bict_explorer.helpers.DegreeHelper;
import au.edu.usc.bict_explorer.helpers.ReportHelper;
import au.edu.usc.bict_explorer.rules.Option;
import au.edu.usc.bict_explorer.rules.Course;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.control.Label;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.beans.value.ChangeListener;
import java.util.HashMap;
import java.util.Map;

import java.io.File;


import java.awt.Desktop;
import java.net.URI;

import javax.swing.JOptionPane;


public class CoreLayoutController {

    private class OptionCheckBox {

        private Option option;
        private CheckBox checkbox;

        public OptionCheckBox(Option option) {
            this.option = option;
            this.checkbox = new CheckBox(option.getCode());

            this.checkbox.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e)  {

                    boolean newSelection = checkbox.isSelected();
                    if (newSelection) {
                        newSelection = checkPrereqs(option);
                    }
                    checkbox.setSelected(newSelection);
                    propagateOptionChange(option, newSelection);
                    refreshGUI();


                }
            });


        }

        public Option getOption() {
            return option;
        }

        public void setOption(Option option) {
            this.option = option;
        }

        public CheckBox getCheckbox() {
            return checkbox;
        }

        public void setCheckbox(CheckBox checkbox) {
            this.checkbox = checkbox;
        }
    }

    private int currentTab = 0;

    private HashMap<Option,OptionCheckBox> careerCheckboxMap;
    private HashMap<Option,OptionCheckBox> courseCheckboxMap;
    private HashMap<Option,OptionCheckBox> minorCheckboxMap;

    @FXML private VBox careerVBox;
    @FXML private VBox minorVBox;
    @FXML private VBox courseVBox;
    @FXML private TabPane tabPane;
    @FXML private ScrollPane careerPane;
    @FXML private ScrollPane minorPane;
    @FXML private ScrollPane coursePane;
    @FXML private MenuItem menuItemClose;
    @FXML private MenuItem menuItemExport;
    @FXML private MenuItem menuItemClearAll;
    @FXML private MenuItem menuItemAbout;

    @FXML private Menu menuFile;
    @FXML private Label tabLabel;


    /**
     * manages chack boxes and checks if they are checked or not
     */
    @FXML
    protected void initialize() {

        //DegreeHelper.clearAllSelections();

        careerCheckboxMap = new HashMap<Option,OptionCheckBox>();
        courseCheckboxMap = new HashMap<Option,OptionCheckBox>();
        minorCheckboxMap = new HashMap<Option,OptionCheckBox>();

        //load our checkbox lists:

        Map<String,Option> map = BICTExplorer.getDegree().careers();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            OptionCheckBox oc = new OptionCheckBox(o);
            oc.getCheckbox().setSelected(o.isChosen());
            careerVBox.getChildren().add(oc.getCheckbox());
            //allows us to refer back to the checkbox at any time
            careerCheckboxMap.put(o,oc);
        }

        map = BICTExplorer.getDegree().minors();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            OptionCheckBox oc = new OptionCheckBox(o);
            oc.getCheckbox().setSelected(o.isChosen());
            minorVBox.getChildren().add(oc.getCheckbox());
            //allows us to refer back to the checkbox at any time
            minorCheckboxMap.put(o,oc);

        }

        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            OptionCheckBox oc = new OptionCheckBox(o);
            oc.getCheckbox().setSelected(o.isChosen());
            courseVBox.getChildren().add(oc.getCheckbox());
            //allows us to refer back to the checkbox at any time
            courseCheckboxMap.put(o,oc);
        }

        //event listener for tab change
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        refreshTabs();
                    }
                }
        );

        menuItemClearAll.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                DegreeHelper.clearAllSelections();
                refreshGUI();
            }


        });



        menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });


        menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI("https://www.usc.edu.au"));
                    }
                }catch(Exception e) {

                }


            }
        });

        menuItemExport.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * this class allows for the creation of the report
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {
                // GOES into this method on click of the menu item 'My Item'
                try {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Report");
                    fileChooser.getExtensionFilters().addAll(
                            new ExtensionFilter("HTML", "*.html"));
                    File selectedFile = fileChooser.showSaveDialog(BICTExplorer.getStage());
                    if (selectedFile != null) {
                        ReportHelper.WriteReport(selectedFile);
                    }

                }catch(Exception e) {
                    //TODO
                    //Popup window showing erorr.
                    System.out.println(e.getMessage());

                }
            }
        });

        refreshGUI();

    }

    private void refreshGUI() {
        refreshCheckBoxes();
        refreshTabs();
    }


    private boolean checkPrereqs(Option o) {

        //if it isn't a course, just return true;
        if(BICTExplorer.getDegree().courses().containsKey(o.getCode())){
            //Option is a course.
            Course course = (Course) o;
            String required = course.getPreReqs().toString();


            if(!course.isSatisfied(DegreeHelper.getSelectedCourses())) {
                JOptionPane.showMessageDialog(null,
                        "You have not met the required prerequisites. The following are required: " + required,
                        "Prerequisites Not Met",
                        JOptionPane.INFORMATION_MESSAGE);

                return false;
            }
        }
        return true;


    }


    private void propagateOptionChange(Option option, Boolean isSelected) {

        option.setChosen(isSelected);

        //call refresh as we may have ticked a minor, impacting the downstream choices.
        option.refresh();

        for (Option upstream : option.getUpstream()) {
            //call refresh on all upstream values to propagate upstream changes
            upstream.refresh();

            //keep that refresh cascading up
            for(Option upstreamsUpstream : upstream.getUpstream()) {
                upstreamsUpstream.refresh();
            }

        }
    }
    private void refreshCheckBoxes() {

        Map<String,Option> map = BICTExplorer.getDegree().careers();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            careerCheckboxMap.get(o).getCheckbox().setSelected(o.isChosen());
        }

        map = BICTExplorer.getDegree().minors();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            minorCheckboxMap.get(o).getCheckbox().setSelected(o.isChosen());
        }

        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {
            Option o = map.get(key);
            courseCheckboxMap.get(o).getCheckbox().setSelected(o.isChosen());
        }

    }

    private static final int TAB_CAREER = 0;
    private static final int TAB_MINOR = 1;
    private static final int TAB_COURSE = 2;

    /**
     *  refeshes the tab after a pane is rendered
     */
    private void refreshTabs() {

        tabLabel.setText("BICT Explorer (You have " + DegreeHelper.getSelectedCourseCount() + " courses selected.)");

        //TODO - implement option tracking to avoid rebuilding the entire tab

        int currentTab = tabPane.getSelectionModel().getSelectedIndex();
        ScrollPane paneToRender;
        Map<String,Option> optionsToRender;

        switch (currentTab) {

            case TAB_MINOR:
                paneToRender = minorPane;
                optionsToRender = BICTExplorer.getDegree().minors();
                break;

            case TAB_COURSE:
                paneToRender = coursePane;
                optionsToRender = BICTExplorer.getDegree().courses();
                break;

            //Catch all other values and default to career layout
            default:
                paneToRender = careerPane;
                optionsToRender = BICTExplorer.getDegree().careers();
                break;

        }

        VBox vbox = new VBox();

        try{


            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/OptionRow.fxml"));
            Parent optionRow = loader.load();
            OptionRowController c = loader.getController();

            //COLUMN HEADERS
            if (currentTab==TAB_COURSE) {
                c.getTextLabel1().setText("COURSE");
                c.getTextLabel2().setText("DESCRIPTION");
                c.getTextLabel3().setText("PREREQUISITES");
                c.getTextLabel4().setText("SEMESTERS OFFERED");
             }else{
                c.getTextLabel1().setText("CODE");
                c.getTextLabel2().setText("NAME");
                c.getTextLabel3().setText("DESCRIPTION");
                c.getTextLabel4().setText("");
            }


            c.getTextLabel1().setFont(Font.font ("Arial", 14));
            c.getTextLabel2().setFont(Font.font ("Arial", 14));
            c.getTextLabel3().setFont(Font.font ("Arial", 14));
            c.getTextLabel4().setFont(Font.font ("Arial", 14));

            c.getOptionCheckBox().setVisible(false);

            vbox.getChildren().add(optionRow);
            c.getRowContainer().prefWidthProperty().bind(vbox.prefWidthProperty());
            c.getRowContainer().maxWidthProperty().bind(vbox.prefWidthProperty());


          for (String key : optionsToRender.keySet()) {
            Option o = optionsToRender.get(key);

            //if (o.isChosen()) {

                loader = new FXMLLoader(getClass().getResource("../views/OptionRow.fxml"));
                optionRow = loader.load();
                c = loader.getController();
                c.setOption(o);

                if (currentTab==TAB_COURSE) {
                    Course course = (Course) o;
                    c.getTextLabel1().setText(o.getCode() + " - " + o.getName());
                    c.getTextLabel2().setText(o.getDescription());
                    c.getTextLabel3().setText(course.getPreReqs().toString());
                    c.getTextLabel4().setText(course.getSemesters());

                }else{
                    c.getTextLabel1().setText(o.getCode());
                    c.getTextLabel2().setText(o.getName());
                    c.getTextLabel3().setText(o.getDescription());
                    c.getTextLabel4().setText("");
                }

                c.getOptionCheckBox().setSelected(o.isChosen());

                c.getOptionCheckBox().setOnAction(new EventHandler<ActionEvent>() {

                    @Override public void handle(ActionEvent e)  {

                        boolean newSelection = !o.isChosen();

                        if (newSelection) {
                            newSelection = checkPrereqs(o);
                        }

                        propagateOptionChange(o, newSelection);
                        refreshGUI();
                    }


                });


                vbox.getChildren().add(optionRow);

                //set the child container to grow with parent container
                c.getRowContainer().prefWidthProperty().bind(vbox.prefWidthProperty());
                c.getRowContainer().maxWidthProperty().bind(vbox.prefWidthProperty());


            //}
        }
        vbox.prefWidthProperty().bind(paneToRender.widthProperty().multiply(0.80));
          //vbox.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            paneToRender.setContent(vbox);



        }catch(Exception e) {

                System.out.println( e.getMessage());
        }
    }

}
