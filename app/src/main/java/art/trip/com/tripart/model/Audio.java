package art.trip.com.tripart.model;

import java.io.Serializable;

/**
 * Created by David on 04/12/2017.
 */

public class Audio implements Serializable {

    private int id;
    private String title;
    private String author;
    private String track;
    private String image;

    public Audio(int id, String title, String author, String track, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.track = track;
        this.image = image;
    }

    public Audio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}