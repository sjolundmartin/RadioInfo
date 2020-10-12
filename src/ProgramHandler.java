import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * A handler used by a SAX parser for creating a list of program objects, a
 * schedule for a channel.
 * @author id15msd
 * @since 2018-01-07
 */
public class ProgramHandler extends DefaultHandler {

    private ArrayList<Program> schedule = null;
    private Program program = null;
    private StringBuilder data = null;

    private boolean episodeID = false;
    private boolean startDate = false;
    private boolean endDate = false;
    private boolean imageURL = false;
    private boolean description = false;
    private boolean title = false;
    private boolean subtitle = false;

    /**
     * Returnes the list of programs.
     *
     * @return the list of programs(a schedule).
     */
    public ArrayList getSchedule(){
        return schedule;
    }

    /**
     * Returns a shortened list containing programs only broadcasting
     * twelve hours before and after the method is called.
     *
     * @return the list of programs.
     */
    public ArrayList<Program> getShortSchedule(){
        ArrayList<Program> shortSchedule = new ArrayList<>();

        if (schedule != null) {
            for (Program program : schedule) {
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd" +
                        " // HH:mm");

                Date startTime = null;
                Date endTime = null;
                try {
                    startTime = sourceFormat.parse(program.getStartDate());
                    endTime = sourceFormat.parse(program.getEndDate());
                } catch (ParseException e) {
                    System.err.println("Time is in incorrect format");
                }

                Instant twelveHoursAgo =
                        new Date().toInstant().minus(Duration.ofHours(12));
                Instant twelveHoursLater =
                        new Date().toInstant().plus(Duration.ofHours(12));

                if (!startTime.toInstant().isAfter(twelveHoursLater) &&
                        !endTime.toInstant().isBefore(twelveHoursAgo)) {
                    shortSchedule.add(program);
                }
            }
        }

        return shortSchedule;
    }
    /**
     * Receive notification of the start of an element. Creates a new Program
     * with every new "scheduledepisode"-tag.
     *
     * @param uri The Namespace URI, or the empty string if the element has no
     *            Namespace URI or if Namespace processing is not being
     *            performed.
     * @param localName The local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if
     *              qualified names are not available.
     * @param attributes The attributes attached to the element. If there are no
     *                  attributes, it shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another
     *                      exception.
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("scheduledepisode")){

            program = new Program();

            if (schedule == null){
                schedule = new ArrayList<>();
            }
        }
        else if (qName.equalsIgnoreCase("imageurl")){
            imageURL = true;
        }
        else if (qName.equalsIgnoreCase("description")){
            description = true;
        }
        else if (qName.equalsIgnoreCase("episodeid")){
            episodeID = true;
        }
        else if (qName.equalsIgnoreCase("starttimeutc")){
            startDate = true;
        }
        else if (qName.equalsIgnoreCase("endtimeutc")){
            endDate = true;
        }
        else if (qName.equalsIgnoreCase("title")){
            title = true;
        }
        else if (qName.equalsIgnoreCase("subtitle")){
            subtitle = true;
        }
        else if (qName.equalsIgnoreCase("program")){
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");

            program.setId(Integer.parseInt(id));
            program.setName(name);
        }

        data = new StringBuilder();

    }

    /**
     * Receive notification of the end of an element. Sets the Program
     * information.
     *
     * @param uri The Namespace URI, or the empty string if the element has no
     *            Namespace URI or if Namespace processing is not being
     *            performed.
     * @param localName The local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if
     *              qualified names are not available.
     * @throws SAXException Any SAX exception, possibly wrapping another
     *                      exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        String dataString = data.toString().trim();

        if (imageURL){
            program.setImageURL(dataString);
            imageURL = false;
        }
        else if (description){
            program.setDescription(dataString);
            description = false;
        }
        else if (episodeID){
            program.setEpisodeId(Integer.parseInt(dataString));
            episodeID = false;
        }
        else if (title){
            program.setTitle(dataString);
            title = false;
        }
        else if (startDate){
            program.setStartDateString(dataString);
            startDate = false;
        }
        else if (endDate){
            program.setEndDateString(dataString);
            endDate = false;
        }
        else if (subtitle){
            program.setSubtitle(dataString);
            subtitle = false;
        }

        if (qName.equalsIgnoreCase("scheduledepisode")){
            schedule.add(program);
        }
    }

    /**
     * Receive notification of character data inside an element. Creates a
     * String of the character data.
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the character array.
     * @throws SAXException Any SAX exception, possibly wrapping another
     *                      exception.
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        data.append(new String(ch, start, length));
    }
}
