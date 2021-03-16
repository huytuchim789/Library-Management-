package Listbook;

import AddBook.AddBookController;
import Database.DatabaseHandler;
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
import javafx.scene.layout.AnchorPane;
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

public class ListBookController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    initCol();
        try {
            loadData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    private AnchorPane pane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book,String> titleCol;
    @FXML
    private TableColumn<Book,String> idCol;
    @FXML
    private TableColumn<Book, String> categoryCol;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String>  pubCol;

    @FXML
    private TableColumn<Book, String> priceCol;
    @FXML
    private TableColumn<Book, Integer> editionCol;
    @FXML
    private TableColumn<Book, String> dateCol;


    @FXML
    private TableColumn<Book, String>  isAvailCol;
    ObservableList<Book> list= FXCollections.observableArrayList();
    private void initCol(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isAvailCol.setCellValueFactory(new PropertyValueFactory<>("isavail"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
    private void loadData() throws SQLException {
        list.clear();
        DatabaseHandler databaseHandler= DatabaseHandler.getInstance();
        String id,title,author,publisher,category;
        String date;
        int edition;
        float price;
        Boolean isavil;
        String query="SELECT * FROM LMS_BOOK;";
        ResultSet resultSet=databaseHandler.execQuery(query);
        try{
        while (resultSet.next()){
            id=resultSet.getString("book_code");
            title=resultSet.getString("book_title");
            author=resultSet.getString("author");
            publisher=resultSet.getString("publisher_id");
            isavil=resultSet.getBoolean("isavail");
            category=resultSet.getString("category");
            price=resultSet.getFloat("price");
            edition=resultSet.getInt("book_edition");
            date=resultSet.getDate("publish_date").toString();
            System.out.println(id+" "+" "+title+" "+author+" "+isavil);
            list.add(new Book(id,title,category,author,publisher,date,edition,price,isavil));
        }
        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);

        }
        tableView.setItems(list);
    }
    @FXML
    private void deleteBook() throws SQLException {
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        Alert alert;
        Book selectedForDeletion=tableView.getSelectionModel().getSelectedItem();
        if(selectedForDeletion==null){
            alert= new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nothing to delete");
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }
        alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("Are you sure to delete the book: "+selectedForDeletion.getTitle()+" ?");
        Optional<ButtonType> answer=alert.showAndWait();
        if(answer.get()==ButtonType.OK){
        Boolean result=databaseHandler.deleteBook(selectedForDeletion);
        if(result){
            Alert alert1= new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Successfully deleted the book: "+selectedForDeletion.getTitle());
            alert1.setTitle("SUCCESS");
            alert1.showAndWait();
            list.remove(selectedForDeletion); //remove from book viewlist

        }
        else if(databaseHandler.isBookIssued(selectedForDeletion)) {
            Alert alert1= new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Failed to delete the book: "+selectedForDeletion.getTitle()+" because the book are being issued");
            alert1.setTitle("FAILED");
            alert1.showAndWait();
        }
        }else {
            alert.close();
            return;
        }
    }
    @FXML
    private void editBook() throws SQLException {
        DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
        Alert alert;
        Book selectedForEdit=tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit==null){
            alert= new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nothing to delete");
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }
        try {
           FXMLLoader loader= new FXMLLoader(getClass().getResource("/AddBook/addbook.fxml"));
            Parent parent = loader.load();//show editbook stage
            AddBookController addBookController=(AddBookController)loader.getController();
            addBookController.inflateUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
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
    private void refreshData() throws SQLException {
    loadData();
    }
}

