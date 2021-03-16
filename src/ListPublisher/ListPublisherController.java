package ListPublisher;

import Database.DatabaseHandler;
import ListMember.Member;
import Listbook.ListBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListPublisherController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Publisher> tableView;

    @FXML
    private TableColumn<Publisher,String> idCol;

    @FXML
    private TableColumn<Publisher,String> nameCol;

    @FXML
    private TableColumn<Publisher,String> addressCol;

    @FXML
    private TableColumn<Publisher,String> phoneCol;

    @FXML
    private TableColumn<Publisher,String> emailCol;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        try{
            loadData();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    ObservableList<Publisher> list= FXCollections.observableArrayList();

    private void init(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

    }
    private void  loadData() throws SQLException {
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        String id,name,phone,email,address;
        String query="SELECT * FROM LMS_PUBLISHER";
        ResultSet resultSet=databaseHandler.execQuery(query);
        try{
            while (resultSet.next()){
                id=resultSet.getString("publisher_id");
                name=resultSet.getString("publisher_name");
                phone=resultSet.getString("phone");
                email=resultSet.getString("email");
                address=resultSet.getString("address");
                list.add(new Publisher(id,name,email,phone,address));
            }

        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);

        }
        tableView.getItems().setAll(list);

    }


}
