package se.lnu.c1dv008.timeline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.MasterDetailPane;
import se.lnu.c1dv008.timeline.controller.TimelineController;
import se.lnu.c1dv008.timeline.controller.TimelineToolbarController;
import se.lnu.c1dv008.timeline.dao.DB;
import se.lnu.c1dv008.timeline.view.CalendarView;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Main extends Application {

    public static MasterDetailPane masterDetailPane;

	@Override
	public void init() {

		// Check if database exists next to jar file, if not extract it from jar
		try {
			Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            if (path.endsWith("1DV008-ProjectCourse-master.jar")) {
				File dataBaseFilePath = new File(path.getParent().toString() + "/timeline.db");

				if (!dataBaseFilePath.isFile()) {
					extractMyDBromJAR(".");
					System.out.println("Database copied successfully!");
				} else {
					System.out.println("Database already exists!");
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {

		// Set fullscreen hint to nothing and set up layout for main stage
		primaryStage.setFullScreenExitHint("");

        CalendarView.setMainStage(primaryStage);
		AnchorPane root;
		Parent mainDisplay;
        masterDetailPane = new MasterDetailPane();
		masterDetailPane.maxHeight(Double.MAX_VALUE);
		masterDetailPane.maxWidth(Double.MAX_VALUE);
		FXMLLoader loader = new FXMLLoader(CalendarView.class.getResource("Timeline.fxml"));
		FXMLLoader loader2 = new FXMLLoader(CalendarView.class.getResource("TimelineToolBar.fxml"));

		try {

			// Load in the fxml files and add them to the scene and push needed values into the controllers
			root = loader2.load();
			mainDisplay = loader.load();
            mainDisplay.maxHeight(Double.MAX_VALUE);
            mainDisplay.maxWidth(Double.MAX_VALUE);
            masterDetailPane.setMasterNode(mainDisplay);
			TimelineToolbarController timelineToolbarController = loader2.getController();
			timelineToolbarController.getAnchorPaneForMainWindow().getChildren().add(masterDetailPane);
			AnchorPane.setRightAnchor(masterDetailPane, 0.0);
			AnchorPane.setLeftAnchor(masterDetailPane, 0.0);
			AnchorPane.setTopAnchor(masterDetailPane, 0.0);
			AnchorPane.setBottomAnchor(masterDetailPane, 0.0);
			timelineToolbarController.setMainStage(primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			TimelineController timelineController = loader.getController();
			timelineController.mainStage = primaryStage;

			// Set scene to be a percentage of the screens size so it looks good on all resolutions
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			root.setPrefSize(visualBounds.getWidth() * 0.75, visualBounds.getHeight() * 0.9);
			primaryStage.setTitle("Timeline Manager");

			// If operating system is mac do not set it to undecorated stage because it messed it up,
			// if it is windows or linux then set stage style to undecorated
			if (!System.getProperties().getProperty("os.name").toLowerCase().equals("mac os x")) {
				primaryStage.initStyle(StageStyle.UNDECORATED);
			}
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public void stop() {

		// When program closes close the sessionfactory before terminating
		DB.closeSessionFactory();
    }

	public void extractMyDBromJAR(String dest){
		try {

			// Get path to jar and load it
			File home = new File(Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString());

			JarFile jar = new JarFile(home);

			// Get the database from the jar file and create it as a file to be read
            JarEntry entry = (JarEntry) jar.getEntry("timeline.db");
            File efile = new File(dest, entry.getName());

			/*
			Read the file and save it to the destination specified by the argument
			 */
			InputStream in =
					new BufferedInputStream(jar.getInputStream(entry));
			OutputStream out =
					new BufferedOutputStream(new FileOutputStream(efile));
			byte[] buffer = new byte[2048];
			for (;;)  {
				int nBytes = in.read(buffer);
				if (nBytes <= 0) break;
				out.write(buffer, 0, nBytes);
			}
			// Flush and close the streams
			out.flush();
			out.close();
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
