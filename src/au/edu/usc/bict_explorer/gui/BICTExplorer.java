package au.edu.usc.bict_explorer.gui;

import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.helpers.ViewHelper;
import au.edu.usc.bict_explorer.helpers.DegreeHelper;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;

import java.io.File;
import javafx.scene.Parent;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;


public class BICTExplorer extends Application {

    //Constant reference here to allow easy changing of default values. Could
    //also store this in a .ini or .config file to allow user controlled defaults.
    public static final int DEFAULT_APP_WIDTH = 800;
    public static final int DEFAULT_APP_HEIGHT = 600;

    //There should only ever be one BICTExplorer class object, so
    //use static access is used for simplicity and to provide
    //app wide access. These are immediately initialised in start()
    // to prevent NullPointerException
    private static Degree degree;
    private static Stage primaryStage;
    private static ViewHelper viewHelper;

    @Override
    /**
     * This function defines the what files are used and the classes also used. This also sets primary stage
     */
    public void start(Stage stage) throws IOException,ParseException {

        //need to fix these up and get them loading from resources folder...
        ClassLoader classLoader = getClass().getClassLoader();
        File careers = new File(classLoader.getResource("careers.options").getFile());
        File minors = new File(classLoader.getResource("minors.options").getFile());
        File courses = new File(classLoader.getResource("courses.options").getFile());

        //load our data
        degree = new Degree(careers, minors, courses);

        //sort our data
        //we do this here at the start to improve performance.
        //alernative would be to maintain a separate sorted list or sort in the GUI on the fly.
        degree.setCourses(DegreeHelper.getAlphabeticalSortedMap(degree.courses()));
        degree.setMinors(DegreeHelper.getAlphabeticalSortedMap(degree.minors()));
        degree.setCareers(DegreeHelper.getAlphabeticalSortedMap(degree.careers()));

        primaryStage = stage;
        primaryStage.setTitle("BICT Explorer");
        viewHelper.showCareerLayout();

        JOptionPane.showMessageDialog(null,
                "Please select a career to begin.",
                "Welcome!",
                JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Set a refrence to the viewHelper
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        setViewHelper(new ViewHelper());
        launch(args);
    }

    public static Degree getDegree() {
        return degree;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void updateScene(Parent root) {

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root, DEFAULT_APP_WIDTH, DEFAULT_APP_HEIGHT));
        }
        primaryStage.getScene().setRoot(root);
        primaryStage.show();

    }

    public static ViewHelper getViewHelper() {
        return viewHelper;
    }

    public static void setViewHelper(ViewHelper viewHelper) {
        BICTExplorer.viewHelper = viewHelper;
    }

}