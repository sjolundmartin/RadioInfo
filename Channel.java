import java.util.ArrayList;

/**
 * Class representing a channel from Sveriges Radios open API for radio channels
 * and broadcasts.
 * @author id15msd
 * @since 2018-01-07
 */
public class Channel {

    private int id;
    private String name;
    private String imageURL;
    private String description;
    private String siteURL;
    private String type;
    private String scheduleURL;
    private ArrayList<Program> schedule;

    /**
     * Sets the schedule of the channel.
     *
     * @param schedule the schedule.
     */
    public void setSchedule(ArrayList<Program> schedule){
        if (schedule != null) {
            this.schedule = schedule;
        }
    }

    /**
     * Returns the schedule of the channel.
     *
     * @return the schedule.
     */
    public ArrayList<Program> getSchedule(){
        return schedule;
    }

    /**
     * Returns channel id.
     *
     * @return the channel id as Integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the channel name.
     *
     * @return the channel name as String.
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the channel id.
     *
     * @param id the id to set as Integer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the channel name.
     *
     * @param name the name to set to as String.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the image URL of the channel.
     *
     * @param imageURL the url to set to as a String.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Sets the channel description.
     *
     * @param description the description to set to as a String.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the site URL of the channel.
     *
     * @param siteURL The url to set as a String.
     */
    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    /**
     * Sets the channel type.
     *
     * @param type the type to set as a String.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the Schedule URL of the channel.
     *
     * @param scheduleURL the URL to set.
     */
    public void setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
    }

    /**
     * Returns all attributes as a String. Used for debugging.
     *
     * @return A string with all attributes.
     */
    @Override
    public String toString(){
        return "ID: "+id+" Name: "+name+"\n imageURL: "+imageURL+"\n siteURL: "
                +siteURL+"\n channelType: "+type+"\n scheduleURL: "+scheduleURL+
                "\n Description: " + description+"\n";
    }
}
