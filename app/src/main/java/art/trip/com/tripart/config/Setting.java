package art.trip.com.tripart.config;

import java.util.List;

import art.trip.com.tripart.model.Audio;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.model.Video;

/**
 * Created by David on 14/01/2018.
 */

public class Setting {

    public static final String BASE_URL = "https://trip-art-14f19.firebaseio.com";
    public static final int TIMEOUT = 10;

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 303;

    public static List<Image> imageList;
    public static final String IMAGE = "image";

    public static List<Audio> audioList;
    public static final String AUDIO = "audio";

    public static List<Video> videoList;
    public static final String VIDEO = "video";

    public static final String TRANSITION = "transition_image";
    public static final String ID = "\"id\"";
    public static final int LENGTH_20 = 20;
}
