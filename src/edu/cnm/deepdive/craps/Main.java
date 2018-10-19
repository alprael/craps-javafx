package edu.cnm.deepdive.craps;

import edu.cnm.deepdive.craps.controller.Controller;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class used a loader for resources for the game and sets up the stage for the game to begin.
 */
public class Main extends Application {

  private static final String RESOURCE_PATH = "res";
  private static final String ICON_PNG = RESOURCE_PATH + "/icon.png";
  private static final String UI_PATH_BUNDLE = RESOURCE_PATH + "/ui";
  private static final String WINDOW_TITLE_KEY = "window_title";
  private static final String MAIN_PATH_STRING = RESOURCE_PATH + "/main.fxml";
  private ClassLoader classLoader;
  private ResourceBundle bundle;
  private FXMLLoader fxmlLoader;
  private Controller controller;

  /**
   * Main method that loads resources from the resources folder such as images and layout files.
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   *Starts game.
   * @param stage
   * @throws Exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    setupLoaders();
    setupStage(stage, loadLayout());
  }

  /**
   * Pauses game.
   * @throws Exception
   */
  @Override
  public void stop() throws Exception {
    controller.stop();
    super.stop();
  }

  private void setupLoaders() {
    classLoader = getClass().getClassLoader();
    bundle = ResourceBundle.getBundle(UI_PATH_BUNDLE);
    fxmlLoader = new FXMLLoader(classLoader.getResource(MAIN_PATH_STRING), bundle);
  }

  private Parent loadLayout() throws IOException {
    Parent root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    return root;
  }

  private void setupStage(Stage stage, Parent root) {
    Scene scene = new Scene(root);
    stage.setTitle(bundle.getString(WINDOW_TITLE_KEY));
    stage.getIcons().add(new Image(classLoader.getResourceAsStream(ICON_PNG)));
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }
}
