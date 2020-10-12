import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Reads xml files from Sveriges Radios open API for radio broadcasts.
 * @author id15msd
 * @since 2018-01-07
 */
public class XMLParserSAX {

    /**
     * Creates a SAXParserFactory which reads a XML file over radio channels,
     * returns the channels as a list of channels.
     *
     * @return A list of channels.
     */
    public ArrayList<Channel> getChannels(){
        ArrayList<Channel> channels;

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ChannelHandler handler = new ChannelHandler();
            InputStream input = new URL("https://api.sr" +
                    ".se/api/v2/channels?pagination=false").openStream();
            saxParser.parse(input, handler);

            channels = handler.getChannelList();
            for (Channel channel: channels){
                channel.setSchedule(getSchedule(channel.getId()));
            }

        }catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        return channels;
    }

    /**
     * Creates a XMLParserFactory which reads a XML file for a schedule for a
     * radio channel.
     *
     * @param id The id number of the channel.
     * @return A schedule as a list of programs for the channel.
     */
    private ArrayList<Program> getSchedule(int id){
        ArrayList<Program> schedule;

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ProgramHandler handler = new ProgramHandler();
            InputStream input = new URL("https://api.sr.se/v2/" +
                    "scheduledepisodes?channelid="+id+"&pagination=false")
                    .openStream();

            saxParser.parse(input, handler);

            schedule = handler.getShortSchedule();

        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return joinSchedules(id, schedule);
    }

    /**
     * Creates and returns a date string in the format "yyyy/mm/dd" of
     * yesterdays date.
     *
     * @return yesterdays date.
     */
    private String getYesterdaysDate(){
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date yesterdaysDate;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        yesterdaysDate=cal.getTime();

        return destFormat.format(yesterdaysDate);
    }

    /**
     * Creates and returns a date string in the format "yyyy/mm/dd" of
     * tomorrows date.
     *
     * @return tomorrows date.
     */
    private String getTomorrowsDate(){
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date tomorrowsDate;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        tomorrowsDate=cal.getTime();

        return destFormat.format(tomorrowsDate);
    }

    /**
     * Creates a XMLParserFactory which reads a XML file for a schedule for a
     * radio channel. The schedule is for yesterdays schedule.
     *
     * @param id The id number of the channel.
     * @return A schedule as a list of yesterdays programs for the channel.
     */
    private ArrayList<Program> getYesterdaysSchedule(int id){
        ArrayList<Program> schedule;

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ProgramHandler handler = new ProgramHandler();
            InputStream input = new URL("https://api.sr.se/v2/" +
                    "scheduledepisodes?channelid="+id+"&pagination=false"+
                    "&date="+getYesterdaysDate())
                    .openStream();

            saxParser.parse(input, handler);

            schedule = handler.getShortSchedule();

        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return schedule;
    }

    /**
     * Creates a XMLParserFactory which reads a XML file for a schedule for a
     * radio channel. The schedule is for tomorrows schedule.
     *
     * @param id The id number of the channel.
     * @return A schedule as a list of tomorrows programs for the channel.
     */
    private ArrayList<Program> getTomorrowsSchedule(int id){
        ArrayList<Program> schedule;

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ProgramHandler handler = new ProgramHandler();
            InputStream input = new URL("https://api.sr.se/v2/" +
                    "scheduledepisodes?channelid="+id+"&pagination=false"+
                    "&date="+getTomorrowsDate())
                    .openStream();

            saxParser.parse(input, handler);

            schedule = handler.getShortSchedule();

        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return schedule;
    }

    /**
     * Joins nd returns yesterday's, today's and tomorrow's schedules to one
     * complete schedule.
     *
     * @param id the id of the channel.
     * @param todaysSchedule todays schedule.
     * @return the joined schedule.
     */
    private ArrayList<Program> joinSchedules(int id,
                                             ArrayList<Program> todaysSchedule){
        ArrayList<Program> schedule = new ArrayList<>();

        ArrayList<Program> temp;

        temp = getYesterdaysSchedule(id);
        if (temp != null) {
            schedule.addAll(temp);

        }
        schedule.addAll(todaysSchedule);

        temp = getTomorrowsSchedule(id);
        if (temp != null) {
            schedule.addAll(temp);
        }

        return schedule;
    }
}
