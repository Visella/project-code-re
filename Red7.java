import javafx.application.Application;
import javafx.stage.Stage;

public class Red7 extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Red 7");
        GameController controller = new GameController(primaryStage);
        controller.startGame();
    }

} 
