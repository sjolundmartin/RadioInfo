
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * Controller of the application. Responsible for the communication between
 * the GUI and the API.
 * @author id15msd
 * @since 2018-01-07
 */
public class Controller {

    private final int TIME = 60*60;

    private GUI gui;

    private ArrayList<Channel> channels;
    private ArrayList<Program> schedule;

    private ActionListener update = e -> update();
    private MouseAdapter channelMouseAdapter;
    private MouseAdapter scheduleMouseAdapter;

    private boolean firstUpdate = true;
    /**
     * Constructor of the class.
     */
    public Controller(){
        createGUI();
        startHourlyUpdate();
    }

    /**
     * Creates mouse adapters for making the GUI tables clickable.
     */
    private void createMouseAdapters(){
        channelMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();

                    for (Channel channel: channels){
                        if (channel.getName().equals(target.getValueAt(row,0))){
                            schedule = channel.getSchedule();
                            callUpdateSchedule(channel);
                            break;
                        }

                    }
                    gui.showSchedule();
                }
            }
        };

        scheduleMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();

                    String title = (String)target.getValueAt(row, 0);

                    for (Program program: schedule){
                        if (program.getTitle().equals(title)){
                            gui.setProgramInfo(program.getDescription(),
                                    program.getName(), program.getImage(),
                                    program.getStartDate(),
                                    program.getEndDate(),program.getSubtitle());
                            gui.showProgramInfo();
                            break;
                        }
                    }
                }
            }
        };
    }

    /**
     * Creates the GUI on a new thread.
     */
    private void createGUI(){
        createMouseAdapters();

        SwingUtilities.invokeLater(()-> {
            gui = new GUI(channelMouseAdapter,
                        scheduleMouseAdapter, update);
            gui.packAndShow();
        });
    }

    /**
     * Starts a timer that updates the channel list every hour.
     */
    private void startHourlyUpdate(){
        java.util.Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!firstUpdate){
                    update();
                }
                else {
                    new ChannelGetter().execute();
                    firstUpdate = false;
                }
            }
        }, 10, 1000*TIME);
    }

    /**
     * Creates a String array of the channel list and then updates the
     * channel table in the GUI.
     */
    private void callUpdateChannels(ArrayList<Channel> channels){
        String[][] channelArray = new String[channels.size()][1];

        int i = 0;
        for (Channel channel: channels){
            channelArray[i][0] = channel.getName();
            i++;
        }

        gui.updateChannelTable(channelArray);
        gui.updateUpdateDialog();
    }

    /**
     * Creates a String array of the program list and then updates the
     * program table in the GUI.
     *
     * @param channel the channel containing the schedule to update.
     */
    private void callUpdateSchedule(Channel channel){
        String[][] scheduleArray = new String[channel.getSchedule().size()][3];

        int i = 0;
        for (Program program : channel.getSchedule()) {
            scheduleArray[i][0] = program.getTitle();
            scheduleArray[i][1] = program.getStartDate();
            scheduleArray[i][2] = program.getEndDate();
            i++;
        }

        gui.updateScheduleTable(scheduleArray);
    }

    /**
     * Calls to update channels.
     */
    private void update(){
        gui.disableUpdate();
        new ChannelGetter().execute();
        gui.showUpdateDialog();
    }

    /**
     * SwingWorker responsible for fetching channel information.
     */
    public class ChannelGetter extends SwingWorker<ArrayList<Channel>, Integer>{

        /**
         * Fetches a channel list.
         *
         * @return unused return integer.
         * @throws Exception if unable to compute a result
         */
        @Override
        protected ArrayList<Channel> doInBackground() throws Exception {
            XMLParserSAX parser = new XMLParserSAX();

            return parser.getChannels();
        }

        /**
         * Calls to update channel table when doInBackground() is finished.
         */
        @Override
        protected void done() {
            try {
                channels = get();
                callUpdateChannels(channels);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            gui.enableUpdate();
        }

    }

}
