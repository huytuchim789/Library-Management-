package Publisher;

import Database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class publisherAddController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField id;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXButton cancel;
    private DatabaseHandler databaseHandler;
    @FXML
    void addPublisher(ActionEvent event) throws SQLException {
        databaseHandler=DatabaseHandler.getInstance();
        String name=this.name.getText();
        String id=this.id.getText();
        String phone=this.phone.getText();
        String email=this.email.getText();
        String address=this.address.getText();
        if(name.isEmpty() || id.isEmpty() || phone.isEmpty() || email.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all blank");
            alert.showAndWait();
            return;
        }
        String query="INSERT INTO LMS_PUBLISHER VALUES("
                +"'"+id+"',"
                +"'"+name+"',"
                +"'"+address+"',"
                +"'"+phone+"'"+","
                +"'"+email+"'"+
                ")";
        if(databaseHandler.execAction(query)){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }
        else {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
