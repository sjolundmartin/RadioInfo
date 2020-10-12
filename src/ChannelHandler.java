import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * A handler used by a SAX parser for creating a list of channel objects.
 * @author id15msd
 * @since 2018-01-07
 */
public class ChannelHandler extends DefaultHandler {

    private ArrayList<Channel> channelList = null;
    private Channel channel = null;
    private StringBuilder data = null;

    private boolean imageURL = false;
    private boolean description = false;
    private boolean siteURL = false;
    private boolean type = false;
    private boolean scheduleURL = false;

    /**
     * Returns a list of channel objects read from a xml file.
     *
     * @return a list of channel objects.
     */
    public ArrayList<Channel> getChannelList(){
        return channelList;
    }

    /**
     * Receive notification of the start of an element. Creates a new Channel
     * with every new "channel"-tag.
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

        if (qName.equalsIgnoreCase("channel")){
            //Create new Channel
            String id = attributes.getValue("id");
            String name = attributes.getValue("name");

            channel = new Channel();
            channel.setId(Integer.parseInt(id));
            channel.setName(name);

            if (channelList == null){
                channelList = new ArrayList<>();
            }
        }
        else if (qName.equalsIgnoreCase("image")){
            imageURL = true;
        }
        else if (qName.equalsIgnoreCase("tagline")){
            description = true;
        }
        else if (qName.equalsIgnoreCase("siteurl")){
            siteURL = true;
        }
        else if (qName.equalsIgnoreCase("channeltype")){
            type = true;
        }
        else if (qName.equalsIgnoreCase("scheduleurl")){
            scheduleURL = true;
        }

        data = new StringBuilder();

    }

    /**
     * Receive notification of the end of an element. Sets the Channel
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
            channel.setImageURL(dataString);
            imageURL = false;
        }
        else if (description){
            channel.setDescription(dataString);
            description = false;
        }
        else if (siteURL){
            channel.setSiteURL(dataString);
            siteURL = false;
        }
        else if (type){
            channel.setType(dataString);
            type = false;
        }
        else if (scheduleURL){
            channel.setScheduleURL(dataString);
            scheduleURL = false;
        }

        if (qName.equalsIgnoreCase("channel")){
            channelList.add(channel);
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
