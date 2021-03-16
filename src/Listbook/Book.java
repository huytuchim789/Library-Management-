package Listbook;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public  class Book {
    private final SimpleStringProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty publisher;
    private final SimpleBooleanProperty isavail;
    private final SimpleStringProperty category;
    private final SimpleFloatProperty price;
    private final SimpleIntegerProperty edition;
    private final SimpleStringProperty date;



    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public float getPrice() {
        return price.get();
    }

    public SimpleFloatProperty priceProperty() {
        return price;
    }

    public void setPrice(float price) {
        this.price.set(price);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public boolean isIsavail() {
        return isavail.get();
    }

    public SimpleBooleanProperty isavailProperty() {
        return isavail;
    }

    public int getEdition() {
        return edition.get();
    }

    public SimpleIntegerProperty editionProperty() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition.set(edition);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public Book(String id, String title, String category, String author, String publisher, String date, int edtion, float price, Boolean isavail) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.isavail = new SimpleBooleanProperty(isavail);
        this.price=new SimpleFloatProperty(price);
        this.category=new SimpleStringProperty(category);
        this.edition=new SimpleIntegerProperty(edtion);
        this.date=new SimpleStringProperty(date);
    }
}