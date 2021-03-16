package Member;

import Database.DatabaseHandler;
import ListMember.Member;
import Listbook.Book;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MemberAddController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private JFXButton cancel;
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
    private DatabaseHandler databaseHandler;
    private Boolean isInEditMode=false;
    @FXML
    private void addMember() throws SQLException {
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
        if(isInEditMode){
            editMemberOperation();
            return;

        }
        String query="INSERT INTO LMS_MEMBERS VALUES("
                +"'"+id+"',"
                +"'"+name+"',"
                +"'"+address+"',"
                +"'"+phone+"',"
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
    private void editMemberOperation() throws SQLException {
        Member member=new Member(id.getText(),name.getText(),address.getText(),email.getText(),phone.getText());
        Alert alert;
        if(databaseHandler.updateMember(member))
        {
            alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Member is  updated successfully");
            alert.showAndWait();
        }
        else{
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("FAILED");
            alert.setContentText("Failed to update Member");
            alert.showAndWait();

        }

    }
    @FXML
    private void cancel(){
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Member member){
        id.setText(member.getId());
        id.setEditable(false);
        name.setText(member.getName());
        phone.setText(member.getPhone());
        email.setText(member.getEmail());
        address.setText(member.getAddress());
        isInEditMode=true;
    }
}
