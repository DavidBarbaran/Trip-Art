package art.trip.com.tripart.model;

/**
 * Created by David on 04/12/2017.
 */

public class Sound {

    private String title;
    private int image;

    public Sound(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}