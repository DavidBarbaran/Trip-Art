package art.trip.com.tripart.model;

import java.io.Serializable;

/**
 * Created by David on 26/01/2018.
 */

public class Video implements Serializable {

    private int id;
    private String path;
    private String title;
    private String description;
    private String author;
    private String image;

    public Video(int id, String path, String title, String description, String author, String image) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.description = description;
        this.author = author;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}