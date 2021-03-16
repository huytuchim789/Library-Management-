package Database;

import ListMember.Member;
import Listbook.Book;
import Listbook.ListBookController;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private final String url = "jdbc:postgresql://localhost/lms_db";
    private final String user = "postgres";
    private final String password = "123456";
    private static Connection conn = null;
    private static Statement stmt;
    private static DatabaseHandler databaseHandler=null;
    private DatabaseHandler() throws SQLException {
        this.connect();
       // this.setUpBookTable();
       // this.setUpMemberTable();
        //this.setUpIssueTable();
        //this.checkDataMember();
       // this.setUpPublisherTable();
    }
    public static DatabaseHandler getInstance() throws SQLException {
        if(databaseHandler==null)
            databaseHandler=new DatabaseHandler();
        return databaseHandler;
    }
    private Connection connect() {

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
    private void setUpBookTable() throws SQLException {
        String TABLE_NAME="BOOK";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            ResultSet tables=databaseMetaData.getTables(null,"public",TABLE_NAME.toLowerCase(),null);// Table in postgre has to be lowercased
            if(tables.next()){
                System.out.println("TABLE"+TABLE_NAME+"Already exist");
            }
            else {
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        +   "id varchar(200) primary key ,\n"
                        +   "title varchar (200)  ,\n"
                        +   "author varchar (100)  ,\n"
                        +   "publisher varchar (100),\n"
                        +   "isavil boolean default true"
                        +   ")");
            }
        }catch (SQLException e){
                e.printStackTrace();
        }
    }
    private void setUpMemberTable() throws SQLException {
        String TABLE_NAME="MEMBER";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            ResultSet tables=databaseMetaData.getTables(null,"public",TABLE_NAME.toLowerCase(),null);// Table in postgre has to be lowercased
            if(tables.next()){
                System.out.println("TABLE"+TABLE_NAME+"Already exist");
            }
            else {
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        +   "id varchar(200) primary key ,\n"
                        +   "name varchar (200)  ,\n"
                        +   "phone varchar (100)  ,\n"
                        +   "email varchar (100)"
                        +   ")");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public  boolean execAction(String query) throws SQLException { //check SQL statement
        ResultSet resultSet;
        try{
            stmt=conn.createStatement();
            stmt.execute(query);
            return  true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error"+e.getMessage(),"Error Occured",JOptionPane.ERROR_MESSAGE);
            System.out.println( e.getLocalizedMessage());
            return false;

        }
    }
    public   ResultSet execQuery(String query){   //execute SQL
        ResultSet resultSet;
        try{
            stmt=conn.createStatement();
            resultSet=stmt.executeQuery(query);

        }catch (SQLException e){
            System.out.println("Exception at:"+e.getLocalizedMessage());
            return null;

        }
        return resultSet;

    }
    private void checkDataBook() throws SQLException {
        String id,title,author;
         boolean isavil;
        String query="SELECT * FROM BOOK;";
        ResultSet resultSet=this.execQuery(query);
        while (resultSet.next()){
             id=resultSet.getString("id");
             title=resultSet.getString("title");
             author=resultSet.getString("author");
             isavil=resultSet.getBoolean("isavil");
            System.out.println(id+" "+" "+title+" "+author+" "+isavil);
        }
    }
    private void checkDataMember() throws SQLException {
        String id,name,phone,email;

        String query="SELECT * FROM MEMBER;";
        ResultSet resultSet=this.execQuery(query);
        while (resultSet.next()){
            id=resultSet.getString("id");
            name=resultSet.getString("name");
            phone=resultSet.getString("phone");
            email=resultSet.getString("email");
            System.out.println(id+" "+name +" "+phone+" "+" "+ email);
        }
    }
    private void setUpIssueTable(){
        String TABLE_NAME="ISSUE";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            ResultSet tables=databaseMetaData.getTables(null,"public",TABLE_NAME.toLowerCase(),null);// Table in postgre has to be lowercased
            if(tables.next()){
                System.out.println("TABLE"+TABLE_NAME+"Already exist");
            }
            else {
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        +   "bookID varchar(200) primary key ,\n"
                        +   "memberID varchar (200)  ,\n"
                        +   "issueTime timestamp default CURRENT_TIMESTAMP ,\n"
                        +   "renew_count integer default 0,\n"
                        +   "FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        +   "FOREIGN KEY (memberID) REFERENCES MEMBER(id)\n"
                        +   ")");
            }
        }catch (SQLException e){
            System.out.println("Cant creat table");
            e.printStackTrace();
        }

    }
    private void setUpPublisherTable(){
        String TABLE_NAME="PUBLISHER";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            ResultSet tables=databaseMetaData.getTables(null,"public",TABLE_NAME.toLowerCase(),null);// Table in postgre has to be lowercased
            if(tables.next()){
                System.out.println("TABLE"+TABLE_NAME+"Already exist");
            }
            else {
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        +   "publisher_id varchar(200) primary key ,\n"
                        +   "publisher_name varchar (200)  ,\n"
                        +   "publisher_address varchar (200)  ,\n"
                        +   "publisher_phone varchar (100)  ,\n"
                        +   "publisher_email varchar (100)"
                        +   ")");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public Boolean deleteBook(Book book) throws SQLException {
        String table_name="LMS_BOOK";
        String query="DELETE FROM LMS_BOOK WHERE BOOK_CODE = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, book.getId()); //1 là ? thứ 1
            int res=preparedStatement.executeUpdate();
            if(res==1)
            return true;
        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);
        }
    return false;
    }
    public Boolean deleteMember(Member member){
        String table_name="LMS_MEMBER";
        String query="DELETE FROM LMS_MEMBERS WHERE MEMBER_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, member.getId()); //1 là ? thứ 1
            int res=preparedStatement.executeUpdate();
            if(res==1)
                return true;
        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);
        }
        return false;

    }
    public boolean isBookIssued(Book book){ //check the book whether it is issued or not

        String query="SELECT COUNT(*) FROM LMS_ISSUE WHERE BOOK_CODE = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, book.getId()); //1 là ? thứ 1
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                int count=resultSet.getInt(1);
                return (count>0);
            }
        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);
        }
        return false;

    }
    public boolean updateBook(Book book) throws SQLException {
        String query="UPDATE LMS_BOOK SET book_code = ?,book_title = ?,category = ?,author = ?,publish_date =?,book_edition =? ,publisher_id = ?,price= ? WHERE book_code =?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getCategory());
            preparedStatement.setString(4, book.getAuthor());
            preparedStatement.setDate(5, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(book.getDate()).getTime()));
            preparedStatement.setFloat(6, book.getEdition());
            preparedStatement.setString(7, book.getPublisher());
            preparedStatement.setFloat(8, book.getPrice());
            preparedStatement.setString(9, book.getId());
            int result = preparedStatement.executeUpdate();
            System.out.println(result);
            return (result > 0);
        }catch (SQLException | ParseException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);
        }
            return false;
    }
    public boolean updateMember(Member member) throws SQLException {
        String query="UPDATE LMS_MEMBERS SET member_id= ?,member_name = ?,member_address = ?,member_phone = ?,member_email = ? WHERE member_id= ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, member.getId());
            preparedStatement.setString(2, member.getName());
            preparedStatement.setString(3, member.getAddress());
            preparedStatement.setString(4, member.getPhone());
            preparedStatement.setString(5, member.getEmail());
            preparedStatement.setString(6, member.getId());
            int result=preparedStatement.executeUpdate();;
            System.out.println(result);
            return (result > 0);
        }catch (SQLException e){
            Logger.getLogger(ListBookController.class.getName()).log(Level.SEVERE,null,e);
        }
        return false;
    }


}
