package ListMember;
import Member.MemberAddController;
import Database.DatabaseHandler;
import Listbook.Book;
import Listbook.ListBookController;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListMemberController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    init();
    try{
        loadData();
    }catch (SQLException e){
        e.printStackTrace();
    }
    }
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member,String> idCol;
    @FXML
    private TableColumn<Member,String> nameCol;
    @FXML
    private TableColumn<Member,String> emailCol;
    @FXML
    private TableColumn<Member,String> phoneCol;
    @FXML
    private TableColumn<?, ?> addressCol;


    ObservableList<Member> list= FXCollections.observableArrayList();

    private void init(){
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    private void  loadData() throws SQLException {
        list.clear();
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        String id,name,phone,email,address;
        String query="SELECT * FROM LMS_MEMBERS";
        ResultSet resultSet=databaseHandler.execQuery(query);
        try{
            while (resultSet.next()){
                id=resultSet.getString("member_id");
                name=resultSet.getString("member_name");
                phone=resultSet.getString("member_phone");
                email=resultSet.getString("member_email");
                address=resultSet.getString("member_address");
                list.add(new Member(id,name,address,email,phone));
            }

        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);

        }
        tableView.setItems(list);

    }
    @FXML
    private void deleteMember() throws SQLException {
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        Alert alert;
        Member selectedForDeletion=tableView.getSelectionModel().getSelectedItem();
        if(selectedForDeletion==null){
            alert= new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nothing to delete");
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }
        alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("Are you sure to delete this member: "+selectedForDeletion.getName()+" ?");
        Optional<ButtonType> answer=alert.showAndWait();
        if(answer.get()==ButtonType.OK){
            Boolean result=databaseHandler.deleteMember(selectedForDeletion);
            if(result){
                Alert alert1= new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("Successfully deleted the member: "+selectedForDeletion.getName());
                alert1.setTitle("SUCCESS");
                alert1.showAndWait();
                list.remove(selectedForDeletion); //remove from book viewlist

            }
            else  {
                Alert alert1= new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Failed to delete this member: "+selectedForDeletion.getName());
                alert1.setTitle("FAILED");
                alert1.showAndWait();
            }
        }else {
            alert.close();
            return;
        }
    }
    @FXML
    private void editMember() throws SQLException {
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        Alert alert;
        Member selectedForEdit=tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit==null){
            alert= new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nothing to delete");
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/Member/addMember.fxml"));
            Parent parent = loader.load();//show editbook stage
            MemberAddController memberAddController=(MemberAddController)loader.getController();
            memberAddController.inflateUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(parent));
            stage.show();
            stage.setOnCloseRequest(windowEvent-> {
                try {
                    loadData();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void refresh() throws SQLException {
    loadData();
    }

}
