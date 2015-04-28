package se.lnu.c1dv008.timeline.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * Created by otto on 2015-04-25.
 */
public class EventController {

    private PopOver popOver;

    @FXML
    private VBox eventBox;

    @FXML
    private Label eventName;

    @FXML
    public void popupOnRightClick() {

            VBox box = new VBox();
            popOver = new PopOver(box);
            popOver.setPrefSize(200, 200);
            popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
            popOver.show(eventBox);
            popOver.getContentNode().setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    popOver.hide();
                }
            });
        /*
            popOver.focusedProperty().addListener((ov, t, t1) -> {

                if (t1 == false) {
                    popOver.hide();
                }
            });
            */

    }

    public Label getName() {
        return eventName;
    }


}
