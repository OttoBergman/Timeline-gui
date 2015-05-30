package se.lnu.c1dv008.timeline.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.model.Timeline;

import java.io.IOException;
import java.time.LocalDate;


/**
 * Created by otto on 2015-04-26.
 */
public class NewTimelineController {

    @FXML
    private Button createTimelineBtn;

    @FXML
    private Button cancelCreateTimelineBtn;

    @FXML
    private TextField timelineTitle;

    @FXML
    private DatePicker timelineStartDate;

    @FXML
    private DatePicker timelineEndDate;

    @FXML
    private Label errorText;

    @FXML
    private ChoiceBox<String> timelineChoiceBox;

    private double X;

    private double Y;

    public Stage newTimelineStage;


    // Add choice box for selecting to display timeline in days months or years
    @FXML
    private void initialize() {
        timelineChoiceBox.getItems().addAll("Days", "Months", "Years");
    }


    @FXML
    private void onCreateClick() {
        // Get the values, create a timeline from it and store it in the database, if values are null display error text
        if (!timelineTitle.getText().equals("") && timelineStartDate.getValue() != null &&
                timelineEndDate.getValue() != null) {
            setErrorTextVisible(false);
            Timeline timeline = new Timeline(timelineTitle.getText());
            timeline.setTimeBounds(timelineStartDate.getValue().toString(), timelineEndDate.getValue().toString());
            timeline.setShowVal(timelineChoiceBox.getSelectionModel().getSelectedItem());
            DB.timelines().save(timeline);
            TimelineSelectController.timelineSelectController.addTimelineToTimelinesSelected(timeline);
            TimelineController.timeLineController.draw();

            Stage stage = (Stage) createTimelineBtn.getScene().getWindow();
            stage.close();



        }
        else {
            setErrorTextVisible(true);

        }
    }

    @FXML
    private void onCancelClick() {

        // Closes stage
        Stage stage = (Stage) cancelCreateTimelineBtn.getScene().getWindow();
        stage.close();
    }

    // Set invalid dates as disabled
    final Callback<DatePicker, DateCell> dayCellFactory =
            new Callback<DatePicker, DateCell>() {
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    timelineStartDate.getValue().plusDays(1))
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #767676;");
                            }
                        }
                    };
                }
            };

    final Callback<DatePicker, DateCell> dayCellFactory2 =
            new Callback<DatePicker, DateCell>() {
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isAfter(
                                    timelineEndDate.getValue())
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #767676;");
                            }
                        }
                    };
                }
            };

    @FXML
    private void setStartdate(ActionEvent event) throws IOException {
        timelineEndDate.setDayCellFactory(dayCellFactory);

    }

    @FXML
    private void setEnddate(ActionEvent event) throws IOException {
        timelineStartDate.setDayCellFactory(dayCellFactory2);

    }

    public void setErrorTextVisible(boolean visible) {
        errorText.setVisible(visible);
    }

    public ChoiceBox getTimelineChoiceBox() {
        return this.timelineChoiceBox;
    }


    // Methods for dragging the stage around
    @FXML
    protected void onPressed(MouseEvent event) {
        X = newTimelineStage.getX() - event.getScreenX();
        Y = newTimelineStage.getY() - event.getScreenY();
    }


    @FXML
    protected void onDragged(MouseEvent event) {
        newTimelineStage.setX(event.getScreenX() + X);
        newTimelineStage.setY(event.getScreenY() + Y);
    }

}
