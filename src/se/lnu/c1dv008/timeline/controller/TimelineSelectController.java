package se.lnu.c1dv008.timeline.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.model.Timeline;
import se.lnu.c1dv008.timeline.view.CalendarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by otto on 2015-05-12.
 */
public class TimelineSelectController {

    @FXML
    private VBox vboxForSelectingTimelines;

    @FXML
    private HBox hboxForSelectingTimelinesTitle;

    @FXML
    private Label selectingTimelinesTitle;

    @FXML
    private Button showSelectedTimelinesBtn;

    @FXML
    private Button newTimelineBtn;

    //TreeSet for adding selected timelines to show, treeset used since we dont want doubles
    private static TreeSet<Timeline> timelinesSelected = new TreeSet<>();

    private List<Timeline> timelines = new ArrayList<>();


    public static TimelineSelectController timelineSelectController;


    @FXML
    void initialize() {
        // Draw the list of radio buttons and set hover colors for the buttons on initialize
        drawTimelineList();
        timelineSelectController = this;
        showSelectedTimelinesBtn.setOnMouseEntered(event -> showSelectedTimelinesBtn.setStyle("-fx-background-color: #606060;"));
        showSelectedTimelinesBtn.setOnMouseExited(event -> showSelectedTimelinesBtn.setStyle("-fx-background-color: #404040;"));

        newTimelineBtn.setOnMouseEntered(event -> newTimelineBtn.setStyle("-fx-background-color: #606060;"));
        newTimelineBtn.setOnMouseExited(event -> newTimelineBtn.setStyle("-fx-background-color: #404040;"));
    }


    public void drawTimelineList() {

        // Clear the children of the vbox and if timelines list is not empty clear it
        vboxForSelectingTimelines.getChildren().clear();

        if (!timelines.isEmpty()) {
            timelines.clear();
        }

        // Get the timelines from the database and create radio button for each timeline
        timelines = DB.timelines().findAll();

        for (Timeline time : timelines) {
                RadioButton btn = new RadioButton(time.getTitle());
                btn.setFont(Font.font("Ubuntu", 16));
                btn.setUserData(time);
                btn.setPadding(new Insets(15));
                btn.setWrapText(true);

                // Add listener to add it to treeset if selected, remove it if unselected
                btn.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        Timeline t = (Timeline) btn.getUserData();
                        timelinesSelected.add(t);
                    } else {
                        Timeline t = (Timeline) btn.getUserData();
                        timelinesSelected.remove(t);
                    }
                });

                // Set the drawn timelines radio buttons to be selected if redrawn
                if (timelinesSelected.contains(time)) {
                    btn.setSelected(true);
                }
                // Add button to the vbox
                vboxForSelectingTimelines.getChildren().add(btn);
            }
        }


    // Method to retrieve treeset
    public static TreeSet<Timeline> getTimelinesSelected() {
        return timelinesSelected;
    }

    @FXML
    private void onUpdateClicked() {

        // Method to draw the timelines
        TimelineController.timeLineController.draw();
    }

    @FXML
    void newTimelineCreate() {

        // Load fxml and set initial values and then show it in a new stage
        FXMLLoader fxmlLoader = new FXMLLoader(CalendarView.class.getResource("NewTimeline.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
            NewTimelineController newTimelineController = fxmlLoader.getController();
            newTimelineController.setErrorTextVisible(false);
            newTimelineController.getTimelineChoiceBox().setValue("Days");
            Stage stage = new Stage();
            newTimelineController.newTimelineStage = stage;
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Create new timeline");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Add timeline to treeset
    public void addTimelineToTimelinesSelected(Timeline timeline) {
        timelinesSelected.add(timeline);
        drawTimelineList();
    }

    // Remove timeline from treeset
    public void removeFromTimelinesSelected(Timeline timeline) {
        timelinesSelected.remove(timeline);
        drawTimelineList();
    }


}
