package edu.cnm.deepdive.craps.view;

import edu.cnm.deepdive.craps.model.Game.Roll;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * This class defines the way the dice rolls appear on the screen,
 * and imports resources for that display.
 */
public class RollCell extends ListCell<Roll> {

  private static final String RESOURCE_PATH = "res";
  private static final String ROLL_PATH_RESOURCE = "/roll.fxml";
  private ResourceBundle bundle;

  /**
   * RollCell method that takes resource bundle as a parameter.
   * @param bundle
   */
  public RollCell(ResourceBundle bundle) {
    this.bundle = bundle;
  }


  /**
   * Updates dice rolls and imports it's resources from roll.fxml
   * @param item
   * @param empty
   */
  @Override
  protected void updateItem(Roll item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    if (empty) {
      setGraphic(null);
    } else {
      try {
        ClassLoader classLoader = getClass().getClassLoader();
        FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource(RESOURCE_PATH + ROLL_PATH_RESOURCE), bundle);
        Controller controller = new Controller();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        controller.setRoll(item);
        setGraphic(root);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private static class Controller {

    private static final String FACE_STRING_PATH = "/face_%d.png";
    private static final String DIE_FACE_FORMAT = RESOURCE_PATH + FACE_STRING_PATH;

    private static Image[] faces;

    static {
      ClassLoader loader = Controller.class.getClassLoader();
      faces = new Image[6];
      for (int i = 0; i < faces.length; i++) {
        faces[i] = new Image(loader.getResourceAsStream(String.format(DIE_FACE_FORMAT, i + 1)));
      }
    }

    private String totalFormat;
    @FXML
    private ImageView die0;
    @FXML
    private ImageView die1;
    @FXML
    private Text total;

    @FXML
    private void initialize() {
      totalFormat = total.getText();
    }

    private void setRoll(Roll roll) {
      int[] dice = roll.getDice();
      die0.setImage(faces[dice[0] - 1]);
      die1.setImage(faces[dice[1] - 1]);
      total.setText(String.format(totalFormat, dice[0] + dice[1]));
    }
  }
}
