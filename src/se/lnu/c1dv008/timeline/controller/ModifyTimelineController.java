package se.lnu.c1dv008.timeline.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.model.Event;
import se.lnu.c1dv008.timeline.model.EventWithoutDuration;
import se.lnu.c1dv008.timeline.model.Timeline;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by otto on 2015-05-01.
 */
public class ModifyTimelineController {

    @FXML
    public TextField modifyTimelineTitle;

    @FXML
    public DatePicker modifyTimelineStartDate;

    @FXML
    public DatePicker modifyTimelineEndDate;

    @FXML
    public Button modifyTimelineUpdateBtn;

    @FXML
    public Button modifyTimelineDeleteBtn;

    @FXML
    private ChoiceBox<String> modifyTimelineChoiceBox;

    public Timeline time;

    public PopOver popOver;

    // Add choice box for selecting days months or years for drawing the timeline

    @FXML
    void initialize() {
        modifyTimelineChoiceBox.getItems().addAll("Days", "Months", "Years");
    }



    @FXML
    private void updateTimeline() {

        // Get the info and update the event in the database and redraw the timelines
        if (!modifyTimelineTitle.getText().isEmpty() || modifyTimelineStartDate.getValue() != null ||
                modifyTimelineEndDate.getValue() != null) {
            time.setTimeBounds(modifyTimelineStartDate.getValue().toString(),
                    modifyTimelineEndDate.getValue().toString());
            time.setTitle(modifyTimelineTitle.getText());
            time.setShowVal(modifyTimelineChoiceBox.getSelectionModel().getSelectedItem());
            DB.timelines().update(time);
            TimelineController.timeLineController.draw();
            popOver.hide();
        }
    }
    @FXML
    private void deleteTimeline() {

        // Remove events inside the timeline and delete them from the database, then delete timeline from database
        // Redraw the timelines
        List<Event> events = DB.events().findAll();
        List<EventWithoutDuration> eventWithoutDurations = DB.eventsWithoutDuration().findAll();
        for (Event e : events) {
            if (e.getTimelineId() == time.getId()) {
                DB.events().delete(e);
            }
        }
        for (EventWithoutDuration eventWithoutDuration : eventWithoutDurations) {
            if (eventWithoutDuration.getTimelineId() == time.getId()) {
                DB.eventsWithoutDuration().delete(eventWithoutDuration);
            }
        }
        DB.timelines().delete(time);
        TimelineSelectController.timelineSelectController.removeFromTimelinesSelected(time);
        TimelineController.timeLineController.draw();
        popOver.hide();
    }

    public ChoiceBox getModifyTimelineChoiceBox() {
        return modifyTimelineChoiceBox;
    }

    public void setModifyTimelineChoiceBox(ChoiceBox<String> modifyTimelineChoiceBox) {
        this.modifyTimelineChoiceBox = modifyTimelineChoiceBox;
    }

    // Disable days that are invalid
    final Callback<DatePicker, DateCell> dayCellFactory =
            new Callback<DatePicker, DateCell>() {
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    modifyTimelineStartDate.getValue().plusDays(1))
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
                                    modifyTimelineEndDate.getValue())
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
        modifyTimelineEndDate.setDayCellFactory(dayCellFactory);

    }

    public void setStartdate() throws IOException {
        modifyTimelineEndDate.setDayCellFactory(dayCellFactory);

    }

    @FXML
    private void setEnddate(ActionEvent event) throws IOException {
        modifyTimelineStartDate.setDayCellFactory(dayCellFactory2);

    }

    public void setEnddate() throws IOException {
        modifyTimelineStartDate.setDayCellFactory(dayCellFactory2);
    }
}