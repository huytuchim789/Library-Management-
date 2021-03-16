package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class login extends Application {
    private StackPane root = new StackPane();
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("Login");
        Label user=new Label("Your Username");
        Label pass=new Label("Your Password");
        TextField user1=new TextField();
        PasswordField pass1 =new PasswordField();
        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.getChildren().addAll(
                user,
                user1,
                pass,
                pass1,
                button);
        root.getChildren().addAll(vBox);

        button.setOnAction(actionEvent-> {
           if(user1.getText().equals("admin") && pass1.getText().equals("admin"))
           {
               Parent root = null;
               try {
                   root = FXMLLoader.load(getClass().getResource("/Main/MainWindow.fxml"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
               primaryStage.setTitle("Library Management");
               primaryStage.setScene(new Scene(root));
               primaryStage.show();
           }
           else {
               Alert alert=new Alert(Alert.AlertType.ERROR);
               alert.setContentText("Wrong user or password");
               alert.setTitle("login failed");
               alert.showAndWait();
           }
        });
        Scene scene = new Scene(root,200,200);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Login");
    }
}
