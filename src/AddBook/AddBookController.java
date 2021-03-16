package AddBook;

import Database.DatabaseHandler;
import Listbook.Book;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {


    @FXML
    private JFXTextField bookTitle;
    @FXML
    private JFXTextField bookAuthor;
    @FXML
    private JFXTextField bookId;
    @FXML
    private JFXTextField category;

    @FXML
    private JFXTextField publishDate;

    @FXML
    private JFXTextField bookEdition;

    @FXML
    private JFXTextField price;

    @FXML
    private JFXTextField publisherID;

    @FXML
    private JFXButton addBook;
    @FXML
    private JFXButton cancel;
    private DatabaseHandler databaseHandler;
    private Boolean isInEditMode=false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    @FXML
    private void addBook() throws SQLException, ParseException {

        databaseHandler=DatabaseHandler.getInstance();


        String id=bookId.getText();
        String author=bookAuthor.getText();
        String publisherID=this.publisherID.getText();
        String category=this.category.getText();
        String publishDate=this.publishDate.getText();

        int  edition = Integer.parseInt(this.bookEdition.getText());
        float price = Float.parseFloat(this.price.getText());

        String title=bookTitle.getText();
        if(id.isEmpty() || author.isEmpty() || publisherID.isEmpty() || title.isEmpty() || category.isEmpty()  ){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all blank");
            alert.showAndWait();
            return;

        }
        if(isInEditMode){
            editBookOperation();
            return;
        }


           String query  = "INSERT INTO LMS_BOOK(book_code,book_title,category,author,publish_date,book_edition,price,publisher_ID,isavail) VALUES" + "(" +
                    "'" + id + "'," +
                    "'" + title + "'," +
                    "'" + category + "'," +
                    "'" + author + "'," +
                   "'" + publishDate+ "'," +
                   "" + edition+ "," +
                   "" + price+ "," +
                   "'" + publisherID+ "'," +
                    true +
                    ")";

        System.out.println(query);
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
    private void editBookOperation() throws SQLException {
    Book book=new Book(bookId.getText(),bookTitle.getText(),category.getText(),bookAuthor.getText(),publisherID.getText(),publishDate.getText(),Integer.parseInt(bookEdition.getText()),Float.parseFloat(price.getText()),true);
        Alert alert;
    if(databaseHandler.updateBook(book))
        {
            alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Book is  updated successfully");
            alert.showAndWait();
        }
        else{
        alert=new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("FAILED");
        alert.setContentText("Failed to update book");
        alert.showAndWait();

        }

    }

    @FXML
    public void cancel(){
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }
    public void inflateUI(Book book){
        bookAuthor.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());
        bookId.setText(book.getId());
        category.setText(book.getCategory());
        publisherID.setText(book.getPublisher());
        price.setText(String.valueOf(book.getPrice()));
        publishDate.setText(String.valueOf(book.getDate()));
        bookEdition.setText(String.valueOf(book.getEdition()));
        isInEditMode=true;
        bookId.setEditable(false);
    }

}
