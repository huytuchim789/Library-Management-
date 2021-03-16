package Main;

import Database.DatabaseHandler;
import Listbook.ListBookController;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainConTroller implements Initializable {
    @FXML
    private HBox bookInfo;
    @FXML
    private HBox memberInfo;
    @FXML
    private TextField idBookInput;
    @FXML
    private TextField idBookInput2;
    @FXML
    private TextField idMemberInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private Text memberName;
    @FXML
    private Text memberPhone;
    @FXML
    private ListView issueDataList;
    Boolean readyForSubmission = false;
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JFXDepthManager.setDepth(bookInfo, 2);
        JFXDepthManager.setDepth(memberInfo, 2);
        try {
            databaseHandler = DatabaseHandler.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    private void loadAddMember() {
        loadWindow("/Member/addMember.fxml", "Add MemBer");
    }

    @FXML
    private void loadAddBook() {
        loadWindow("/AddBook/addbook.fxml", "Add Book");
    }

    @FXML
    private void loadViewMember() {
        loadWindow("/Listmember/listMember.fxml", "View Member");

    }

    @FXML
    private void loadViewBook() {
        loadWindow("/Listbook/listbook.fxml", "View Book");

    }

    @FXML
    private void loadSetting() {

    }

    void loadWindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMemberInfo() {
        String id = idMemberInput.getText();
        String query = "SELECT * FROM LMS_MEMBERS WHERE MEMBER_ID ='" + id + "'";
        ResultSet resultSet = databaseHandler.execQuery(query);
        Boolean check = false;
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("member_name");
                String phone = resultSet.getString("member_phone");
                memberName.setText(name);
                memberPhone.setText(phone);
                check = true;
            }
        } catch (SQLException e) {
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE, null, e);

        }
        if (!check) {
            memberName.setText("No such a member");
            memberPhone.setText("");

        }
    }

    @FXML
    private void loadBookInfo() {
        String id = idBookInput.getText();
        String query = "SELECT * FROM LMS_BOOK WHERE BOOK_CODE ='" + id + "'";
        ResultSet resultSet = databaseHandler.execQuery(query);
        Boolean check = false;
        try {
            while (resultSet.next()) {
                String title = resultSet.getString("book_title");
                String author = resultSet.getString("author");
                Boolean isavail = resultSet.getBoolean("isavail");
                bookName.setText(title);
                bookAuthor.setText(author);
                String status = (isavail) ? "Available" : "Not Available";
                bookStatus.setText(status);
                check = true;
            }
        } catch (SQLException e) {
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE, null, e);

        }
        if (!check) {
            bookName.setText("No such a book");
            bookAuthor.setText("");
            bookStatus.setText("");
        }
    }

    private void clearBookCahe() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");

    }

    private void clearMemberCache() {
        memberName.setText("");
        memberPhone.setText("");
    }

    @FXML
    private void loadIssue() throws SQLException {
        String memberID = this.idMemberInput.getText();
        String bookID = this.idBookInput.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm issue operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to issue this book" + bookName.getText() + "\n to " + memberName.getText() + "?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String query = "INSERT INTO LMS_ISSUE(book_code,member_id) VALUES ("
                    + "'" + bookID + "',"
                    + "'" + memberID + "'"
                    + ")";
            System.out.println(query);
            String query2 = "UPDATE LMS_BOOK SET isavail=false WHERE book_code=" + "'" + bookID + "'";
            if (databaseHandler.execAction(query) && databaseHandler.execAction(query2)) {
                String query3="SELECT * from LMS_ISSUE WHERE book_code ="+"'"+bookID+"'",query4=null;
                ResultSet resultSet=databaseHandler.execQuery(query3);
                while (resultSet.next()){
                    String mbookID = bookID;
                    String member_ID = resultSet.getString("member_ID");
                    Timestamp mIssueTime = resultSet.getTimestamp("issueTime");
                    int mrenew_count = resultSet.getInt("renew_count");
                    Timestamp mReturnTime = resultSet.getTimestamp("return_date");
                    query4 ="INSERT INTO LMS_HISTORY(member_id,book_code,issuetime,return_date,type) VALUES("
                            +"'" + member_ID +"',"
                            +"'" + mbookID +"',"
                            +"'" + mIssueTime +"',"
                            +"'" + mReturnTime +"',"
                            +       true +")";
                    System.out.println(query4);
                    if(databaseHandler.execAction(query4)){
                        System.out.println("succe");
                    }
                }
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Issue Complete");
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Issue Failed");
                alert1.showAndWait();
            }
        }
    }

    @FXML
    private void loadBookInfo2() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();// create an array list to display in the viewlist to submit
        String bookID = idBookInput2.getText();
        String query = "SELECT * FROM LMS_ISSUE WHERE BOOK_CODE='" + bookID + "'";
        ResultSet resultSet = databaseHandler.execQuery(query);
        Boolean check = false;
        if (!resultSet.isBeforeFirst()) {
            System.out.println("run in");
            list.add("Book isn't issued");
            issueDataList.getItems().setAll(list);
            return;
        }
        try {
            while (resultSet.next()) {

                String mbookID = bookID;
                String memberID = resultSet.getString("member_ID");
                Timestamp mIssueTime = resultSet.getTimestamp("issueTime");
                int mrenew_count = resultSet.getInt("renew_count");

                list.add("  Issue date and time: " + mIssueTime.toGMTString()); //print to viewlist
                list.add("  Renew Count: " + mrenew_count);//print to viewlist
                list.add("Book Information:");//print to viewlist

                query = "SELECT * FROM LMS_BOOK WHERE book_code='" + bookID + "'";       //query from book table
                ResultSet resultSet1 = databaseHandler.execQuery(query);
                while (resultSet1.next()) {
                    list.add("  Book ID: " + resultSet1.getString("book_code"));
                    list.add("  Book Name: " + resultSet1.getString("book_title"));
                    list.add("  Book Author: " + resultSet1.getString("author"));
                    list.add("  Book Publisher: " + resultSet1.getString("publisher_id"));

                }

                list.add("Member Information:");//print to viewlist
                query = "SELECT * FROM LMS_MEMBERS WHERE member_id='" + memberID + "'";       //query from member table
                ResultSet resultSet2 = databaseHandler.execQuery(query);
                while (resultSet2.next()) {
                    list.add("  Member ID: " + resultSet2.getString("member_id"));
                    list.add("  Member Name: " + resultSet2.getString("member_name"));
                    list.add("  Member Address: " + resultSet2.getString("member_address"));
                    list.add("  Member Phone: " + resultSet2.getString("member_phone"));
                    list.add("  Member Email: " + resultSet2.getString("member_email"));
                }
                readyForSubmission = true;
                issueDataList.getItems().setAll(list);
                issueDataList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() { //change color specified row in listview
                    @Override
                    public ListCell call(ListView listView) {
                        return new ListCell<String>() {
                            @Override
                            protected void updateItem(String cell, boolean empty) {
                                super.updateItem(cell, empty);
                                if (cell == null || empty) {
                                    setText(null);
                                }
                                else {
                                    setText(cell);
                                    if (cell.equals("Book Information:") || cell.equals("Member Information:")) {

                                        setStyle("-fx-font-size: 150%;"+"-fx-font-family: 'Britannic Bold';");

                                    }
                                }
                            }
                        };
                    }
                });


            }
        }catch(SQLException e)
    {
        Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE, null, e);
    }
    }

    @FXML
    private void loadSubmission() throws SQLException { //this function to help user for returning book
        if (!readyForSubmission) {            //submit failed if you didnt issue book
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setContentText("PLEASE SELECT A BOOK TO SUBMIT");
            alert.showAndWait();
            return;
        }
        Alert confAlert=new Alert(Alert.AlertType.CONFIRMATION);
        confAlert.setTitle("CONFIRMATION");
        confAlert.setContentText("Are you sure want to confirm");
        Optional<ButtonType>  response = confAlert.showAndWait();
        if(response.get()==ButtonType.OK) {
            String id = idBookInput2.getText();
            String query3="SELECT * from LMS_ISSUE WHERE book_code ="+"'"+id+"'",query4=null;
            System.out.println(query3);
            ResultSet resultSet=databaseHandler.execQuery(query3);
            while (resultSet.next()){
                String mbookID = id;
                String member_ID = resultSet.getString("member_ID");
                Timestamp mIssueTime = resultSet.getTimestamp("issueTime");
                int mrenew_count = resultSet.getInt("renew_count");
                Timestamp mReturnTime = resultSet.getTimestamp("return_date");
                Timestamp mdateReturn=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                 query4 ="INSERT INTO LMS_HISTORY(member_id,book_code,issuetime,return_date,date_return,type) VALUES("
                        +"'" + member_ID +"',"
                        +"'" + mbookID +"',"
                        +"'" + mIssueTime +"',"
                        +"'" + mReturnTime +"',"
                        +"'" + mdateReturn +"',"
                        +       false +")";
                System.out.println(query4);
                 if(databaseHandler.execAction(query4)){
                     System.out.println("succee");
                 }
            }
            String query1 = "DELETE FROM LMS_ISSUE WHERE BOOK_CODE='" + id + "'";
            String query2 = "UPDATE LMS_BOOK SET isavail = TRUE WHERE book_code='" + id + "'";

            try {
                if (databaseHandler.execAction(query1) && databaseHandler.execAction(query2)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Submit Successfully");
                    alert.setTitle("Success");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Submission has failed");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE, null, e);
            }
        }else if(response.get()==ButtonType.CANCEL){
            confAlert.close();
        }

    }
    @FXML
    private void loadRenew() throws SQLException { //extend book lending's time
        String bookID=idBookInput2.getText();
        if (!readyForSubmission) {            //submit failed if you didnt issue book
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setContentText("PLEASE SELECT A BOOK TO SUBMIT");
            alert.showAndWait();
            return;
        }
        Alert confAlert=new Alert(Alert.AlertType.CONFIRMATION);
        confAlert.setTitle("CONFIRMATION");
        confAlert.setContentText("Are you sure want to renew");
        Optional<ButtonType>  response = confAlert.showAndWait();
        if(response.get()== ButtonType.OK){
            String query="UPDATE LMS_ISSUE SET issueTime =CURRENT_TIMESTAMP,renew_count=renew_count+1 WHERE BOOK_CODE='"+bookID+"'";

            System.out.println(query);
            if(databaseHandler.execAction(query)){
                String query3="SELECT * from LMS_ISSUE WHERE book_code ="+"'"+bookID+"'",query4=null;
                ResultSet resultSet=databaseHandler.execQuery(query3);
                while (resultSet.next()) {
                    String mbookID = bookID;
                    String member_ID = resultSet.getString("member_ID");
                    Timestamp mIssueTime = resultSet.getTimestamp("issueTime");
                    int mrenew_count = resultSet.getInt("renew_count");
                    Timestamp mReturnTime = resultSet.getTimestamp("return_date");
                    query4 = "INSERT INTO LMS_HISTORY(member_id,book_code,issuetime,return_date,type) VALUES("
                            + "'" + member_ID + "',"
                            + "'" + mbookID + "',"
                            + "'" + mIssueTime + "',"
                            + "'" + mReturnTime + "',"
                            + true + ")";
                    if(databaseHandler.execAction(query4)){
                        System.out.println("succe");
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Submit Successfully");
                alert.setTitle("Success");
                alert.showAndWait();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Submission has failed");
                alert.showAndWait();

            }
        }
        else{
            confAlert.close();
        }
    }
    @FXML
    private void addPublisher(){
    loadWindow("/Publisher/addPublisher.fxml","Add Publisher");
    }
    @FXML
    private void addAuthor(){

    }
    @FXML
    private void viewPublisher(){
        loadWindow("/ListPublisher/ListPublisher.fxml","View Publisher");
    }
    @FXML
    private void viewAuthor(){

    }
}
