package se.lnu.c1dv008.timeline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.MasterDetailPane;
import se.lnu.c1dv008.timeline.controller.TimelineController;
import se.lnu.c1dv008.timeline.controller.TimelineToolbarController;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.view.CalendarView;


public class Main extends Application {

    public static MasterDetailPane masterDetailPane;

	@Override
	public void start(Stage primaryStage) {

		Parent root;
		Parent mainDisplay;
        masterDetailPane = new MasterDetailPane();
		masterDetailPane.maxHeight(Double.MAX_VALUE);
		masterDetailPane.maxWidth(Double.MAX_VALUE);
        //masterDetailPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE);
		FXMLLoader loader = new FXMLLoader(CalendarView.class.getResource("Timeline.fxml"));
		FXMLLoader loader2 = new FXMLLoader(CalendarView.class.getResource("TimelineToolBar.fxml"));
		//FXMLLoader loaderHiddenPane = new FXMLLoader(CalendarView.class.getResource("TimelineSelectView.fxml"));
		try {

			root = loader2.load();
			mainDisplay = loader.load();

            mainDisplay.maxHeight(Double.MAX_VALUE);
            mainDisplay.maxWidth(Double.MAX_VALUE);
            masterDetailPane.setMasterNode(mainDisplay);
            masterDetailPane.getMasterNode().maxHeight(Double.MAX_VALUE);
            masterDetailPane.getMasterNode().maxWidth(Double.MAX_VALUE);
			TimelineToolbarController timelineToolbarController = loader2.getController();
			timelineToolbarController.getAnchorPaneForMainWindow().getChildren().add(masterDetailPane);
			timelineToolbarController.setMainStage(primaryStage);
			//TimelineSelectController timelineSelectController = loaderHiddenPane.getController();
			//timelineSelectController.pane = hiddenSidesPane;
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			TimelineController timelineController = loader.getController();
			timelineController.mainStage = primaryStage;
			//Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			//root.maxHeight(visualBounds.getHeight());
			//root.maxWidth(visualBounds.getWidth());
			primaryStage.setTitle("TimeLine Manager");
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public void stop() {
        DB.closeSessionFactory();
    }


	
	public static void main(String[] args) {
		launch(args);
	}
}
