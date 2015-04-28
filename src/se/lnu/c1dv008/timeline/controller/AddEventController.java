package se.lnu.c1dv008.timeline.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.model.Event;
import se.lnu.c1dv008.timeline.model.Timeline;


/**
 * Created by otto on 2015-04-26.
 */
public class AddEventController {

    @FXML
    private TextArea addEventDescription;

    @FXML
    private DatePicker addEventStartDate;

    @FXML
    private DatePicker addEventEndDate;

    @FXML
    private Button addEventCreate;

    @FXML
    private Button addEventCancel;

    @FXML
    private TextField addEventTitle;

    @FXML
    private ColorPicker addEventColorPicker;

    public Timeline timeline;


    @FXML
    private void onCreate() {


        if (!addEventTitle.getText().isEmpty() || addEventStartDate.getValue() != null ||
                addEventEndDate.getValue() != null || addEventDescription.getText().isEmpty()) {
            Event event = new Event(addEventTitle.getText(), addEventDescription.getText(),
                    addEventStartDate.getValue().toString(), addEventEndDate.getValue().toString(),
                    addEventColorPicker.getCustomColors().toString(), timeline.getId());

            DB.events().save(event);
            Stage stage = (Stage) addEventCreate.getScene().getWindow();
            stage.close();
            TimelineController timelineController = new TimelineController();
            timelineController.draw();


        }
    }

    public void setTimeline(Timeline time) { this.timeline = time; }
}
