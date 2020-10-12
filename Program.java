import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class representing a program (a broadcast) from Sveriges Radios open API
 * for radio channels and broadcasts.
 * @author id15msd
 * @since 2018-01-07
 */
public class Program {

    private int id;
    private String name;
    private int episodeId;
    private String title;
    private String description;
    private String startDateString;
    private String endDateString;
    private String imageURL;
    private Image image;
    private String subtitle;

    private SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd" +
            "'T'HH:mm:ss'Z'");
    private SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd " +
            "// HH:mm");
    private TimeZone utc = TimeZone.getTimeZone("UTC");

    /**
     * Returns a converted date String for the program start time.
     *
     * @return a converted date String.
     */
    public String getStartDate() {
        sourceFormat.setTimeZone(utc);
        Date convertedDate = null;
        try {
            convertedDate = sourceFormat.parse(startDateString);
        } catch (ParseException e) {
            System.err.println("Time is in incorrect format");
            return null;
        }
        return destFormat.format(convertedDate);

    }

    /**
     * Returns a converted date String for the program end time.
     *
     * @return a converted date String.
     */
    public String getEndDate(){
        sourceFormat.setTimeZone(utc);
        Date convertedDate = null;
        try {
            convertedDate = sourceFormat.parse(endDateString);
        } catch (ParseException e) {
            System.err.println("Time is in incorrect format");
            return null;
        }
        return destFormat.format(convertedDate);

    }

    /**
     * Returns the program image fetched from the image URL.
     *
     * @return the image as an Image object.
     */
    public Image getImage(){
        URL url = null;
        try {
            url = new URL(getImageURL());
        } catch (MalformedURLException e) {
            return null;
        }

        if (image == null){
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                return null;
            }
        }
        return image;
    }

    /**
     * Sets the id of the program.
     *
     * @param id the id to set to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the program.
     *
     * @param name the name to set to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the episode id of the program.
     *
     * @param episodeId the id to set to.
     */
    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    /**
     * Sets the title of the program.
     *
     * @param title the title to set to.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the description of the program.
     *
     * @param description the description to set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the start date string for the program.
     *
     * @param startDateString the start date to set to.
     */
    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    /**
     * Sets the end date string of the program.
     *
     * @param endDateString the end date to set to.
     */
    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    /**
     * Sets the image URL of the program.
     *
     * @param imageURL the URL to set to.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Sets the subtitle of the program.
     *
     * @param subtitle the subtitle to set.
     */
    public void setSubtitle(String subtitle){
        this.subtitle = subtitle;
    }

    /**
     * Returns the subtitle of the program.
     *
     * @return the subtitle.
     */
    public String getSubtitle(){
        return subtitle;
    }

    /**
     * Returns the name of the program.
     *
     * @return the name of the program.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the title of the program.
     * @return the title of the program.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the program.
     * @return the description of the program.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the image URL of the program.
     * @return the URL of the program.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Returns all attributes as a String. Used for debugging.
     *
     * @return A string with all attributes.
     */
    @Override
    public String toString() {
        return "ID: "+id+" Name: "+name+" EpisodeID: "+episodeId+
                "\n Title: "+title+
                "\n Description: "+description+
                "\n StartDate: "+startDateString+
                "\n endDate: "+endDateString+
                "\n ImageURL: "+imageURL+"\n";
    }
}
