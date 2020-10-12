import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The user interface. Containing mainly tables with channels or schedules.
 * @author id15msd
 * @since 2018-01-07
 */
public class GUI {

    private JFrame frame;
    private JButton backBtn;
    private JTable channelTable;
    private JTable scheduleTable;
    private DefaultTableModel channelTableModel;
    private DefaultTableModel scheduleTableModel;
    private JPanel cards;
    private final String CHANNELPANEL = "channels";
    private final String SCHEDULEPANEL = "schedule";
    private final String PROGRAMINFO = "programInfo";
    private Boolean scheduleVisible = false;
    private Boolean programInfoVisible = false;
    private MouseAdapter channelMouseAdapter, scheduleMouseAdapter;
    java.util.Timer t;

    private JLabel programLabel;
    private Image programImage = null;
    private JLabel descriptionLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JPanel imagePanel;
    private UpdateDialog updateDialog;
    private JMenuItem menuItemUpdate;

    /**
     * Constructor of the class. Creates a frame and adds panels to it.
     *
     * @param channelMouseAdapter MouseAdapter for channel table.
     * @param scheduleMouseAdapter MouseAdapter for schedule table.
     * @param update ActionListener for update-button.
     */
    public GUI(MouseAdapter channelMouseAdapter,
               MouseAdapter scheduleMouseAdapter, ActionListener update){
        this.channelMouseAdapter = channelMouseAdapter;
        this.scheduleMouseAdapter = scheduleMouseAdapter;

        frame = new JFrame("RadioInfo");
        frame.setPreferredSize(new Dimension(900,700));
        frame.setMinimumSize(new Dimension(500,500));

        frame.setJMenuBar(createMenuBar(update));

        frame.add(createTopPanel(), BorderLayout.NORTH);
        frame.add(createCenterPanel());
    }

    /**
     * Creates JMenuBar for application.
     *
     * @param update ActionListener for update-button.
     * @return a JMenuBar.
     */
    private JMenuBar createMenuBar(ActionListener update){
        JMenuBar menuBar = new JMenuBar();

        //Menu 1
        JMenu radioInfoMenu = new JMenu("RadioInfo");
        menuItemUpdate = new JMenuItem("Uppdatera");
        JMenuItem menuItemQuit = new JMenuItem("Avsluta");

        menuItemQuit.addActionListener(e -> System.exit(0));
        menuItemUpdate.addActionListener(update);
        menuItemUpdate.setEnabled(false);
        radioInfoMenu.add(menuItemUpdate);
        radioInfoMenu.add(menuItemQuit);

        menuBar.add(radioInfoMenu);

        //Menu 2
        JMenu infoMenu = new JMenu("Info");
        JMenuItem menuItemAbout = new JMenuItem("Om programmet");
        JMenuItem menuItemHelp = new JMenuItem("Hjälp");
        menuItemAbout.addActionListener(e -> new AboutDialog());
        menuItemHelp.addActionListener(e -> new HelpDialog());

        infoMenu.add(menuItemAbout);
        infoMenu.add(menuItemHelp);

        menuBar.add(infoMenu);

        return menuBar;
    }

    /**
     * Creates the top panel of the view.
     *
     * @return a JPanel.
     */
    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel();

        backBtn = new JButton("Tillbaka");
        backBtn.addActionListener(e -> goBack());
        backBtn.setVisible(false);

        topPanel.add(backBtn, BorderLayout.WEST);

        return topPanel;
    }

    /**
     * Creates the center panel of the view.
     * @return a JPanel.
     */
    private JPanel createCenterPanel(){

        //panel 1 (channel table)
        createChannelTable();

        JPanel channelPanel = new JPanel();
        channelPanel.setPreferredSize(new Dimension(400,400));
        channelPanel.setLayout(new GridLayout());
        JScrollPane channelSP  = new JScrollPane(channelTable);
        channelPanel.add(channelSP);

        //panel 2 (schedule table)
        createScheduleTable();

        JPanel schedulePanel = new JPanel();
        schedulePanel.setPreferredSize(new Dimension(400,400));
        schedulePanel.setLayout(new GridLayout());
        JScrollPane scheduleSP = new JScrollPane(scheduleTable);
        schedulePanel.add(scheduleSP);

        //panel 3 (program info)
        JPanel programInfo = createProgramInfoPanel();


        //Create panel with cards
        cards = new JPanel(new CardLayout());
        cards.add(channelPanel, CHANNELPANEL);
        cards.add(schedulePanel,SCHEDULEPANEL);
        cards.add(programInfo, PROGRAMINFO);

        return cards;
    }

    /**
     * Creates a program info panel.
     *
     * @return a JPanel.
     */
    private JPanel createProgramInfoPanel(){
        JPanel programInfo = new JPanel();
        programInfo.setLayout(new BoxLayout(programInfo, BoxLayout.Y_AXIS));

        //Channel name
        programLabel = new JLabel("Kanal: ");
        programInfo.add(programLabel);

        //Description
        descriptionLabel = new JLabel("Programbeskrivning");
        programInfo.add(descriptionLabel);

        //Program times
        startTimeLabel = new JLabel();
        endTimeLabel = new JLabel();
        programInfo.add(startTimeLabel);
        programInfo.add(endTimeLabel);

        //program image
        imagePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(programImage, 10, 10, 300, 300, this);
            }

        };
        programInfo.add(imagePanel);

        return programInfo;
    }


    /**
     * Sets the information used by the program information panel.
     *
     * @param description the description of the program.
     * @param programName the name of the program.
     * @param image the image of the program.
     * @param startTime the start time of the program.
     * @param endTime the end time of the program.
     * @param subtitle the subtitle of the program.
     */
    public void setProgramInfo(String description, String programName,
                               Image image, String startTime, String endTime,
                               String subtitle){
        //Program description text
        descriptionLabel.setText("<html> <b>Programbeskrivning:</b> "+description+
                " " +
                "</html>");

        //Program name
        if (subtitle != null) {
            programLabel.setText("<html><b>Program:</b> " + programName +
                    subtitle + "</html>");
        }
        else {
            programLabel.setText("<html><b>Program:</b> " + programName +
                    "</html>");
        }

        //Program times
        startTimeLabel.setText("<html> <b>Börjar:</b> "+startTime+"</html>");
        endTimeLabel.setText("<html> <b>Slutar:</b> "+endTime+"</html>");

        //Program image
        if (image != null) {
            programImage = image;
        }
        else{
            try {
                InputStream is = RadioInfo.class.getResourceAsStream(
                        "/placeholder.png");
                programImage = ImageIO.read(is);
            } catch (IOException e) {
                System.err.println("Ingen bild");
                programImage = null;
            }
        }
        imagePanel.setToolTipText(programName);
    }

    /**
     * Creates the schedule table.
     */
    private void createScheduleTable(){
        String[] columns = {"Program", "Börjar", "Slutar"};
        scheduleTableModel = new DefaultTableModel(null, columns){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(scheduleTableModel);
        scheduleTable = createTableRenderer(scheduleTable);

        scheduleTable.addMouseListener(scheduleMouseAdapter);

    }

    /**
     * Creates the channel table.
     */
    private void createChannelTable(){
        String[] columns = {"Kanal"};
        channelTableModel = new DefaultTableModel(null, columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        channelTable = new JTable(channelTableModel);
        //channelTable.setDefaultRenderer();

        channelTable.addMouseListener(channelMouseAdapter);
    }

    /**
     * Creates a table renderer used by the schedule table for coloring table
     * rows.
     * @param table JTable to create TableRenderer for.
     * @return the input table, however with a TableRenderer.
     */
    private JTable createTableRenderer(final JTable table){
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                String status =
                        (String)table.getModel().getValueAt(row, 2);
                if (programHasEnded(status)){
                    setBackground(Color.GRAY);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());

                }
                return this;
            }
        });

        return table;
    }

    /**
     * Checks if the program end time has past.
     *
     * @param endDate the end date and time of the program.
     * @return true if program has ended, false if not.
     */
    private boolean programHasEnded(String endDate){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd" +
                " // HH:mm");

        Date endTime = null;
        try {
            endTime = sourceFormat.parse(endDate);
        } catch (ParseException e) {
            System.err.println("Time is in incorrect format");
            return true;
        }

        return endTime.before(new Date());

    }

    /**
     * Updates the channel table.
     *
     * @param channels the channels to update the table with.
     */
    public void updateChannelTable(String[][] channels){
        while(channelTableModel.getRowCount() > 0){
            channelTableModel.removeRow(0);
        }

        for (int row = 0; row < channels.length; row++){
            channelTableModel.addRow(channels[row]);
        }
    }

    /**
     * Updates the schedule table.
     *
     * @param schedule the programs to update the table with.
     */
    public void updateScheduleTable(String[][] schedule){
        while(scheduleTable.getRowCount() > 0){
            scheduleTableModel.removeRow(0);
        }

        for (int row = 0; row < schedule.length; row++){
            scheduleTableModel.addRow(schedule[row]);
        }
    }

    /**
     * Changes the view to the schedule table.
     */
    public void showSchedule(){
        CardLayout cl = (CardLayout) cards.getLayout();

        cl.show(cards, SCHEDULEPANEL);
        scheduleVisible = true;
        toggleBackBtn();

    }

    /**
     * Go back one step in the view.
     */
    private void goBack(){
        CardLayout cl = (CardLayout) cards.getLayout();
        if (scheduleVisible){
            cl.show(cards, CHANNELPANEL);
            scheduleVisible = false;
            toggleBackBtn();
        }
        else if(programInfoVisible){
            cl.show(cards, SCHEDULEPANEL);
            programInfoVisible = false;
            scheduleVisible = true;
        }
    }

    /**
     * Changes to the program information of the program.
     */
    public void showProgramInfo(){
        CardLayout cl = (CardLayout) cards.getLayout();

        cl.show(cards, PROGRAMINFO);
        scheduleVisible = false;
        programInfoVisible = true;
    }

    /**
     * Toggles viability of the back-button.
     */
    private void toggleBackBtn(){
        if(backBtn.isVisible()){
            backBtn.setVisible(false);
        }
        else{
            backBtn.setVisible(true);
        }
    }

    /**
     * Packs the frame and sets it visible.
     */
    public void packAndShow(){
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Enables the update button in the JMenuBar.
     */
    public void enableUpdate(){
        menuItemUpdate.setEnabled(true);
    }

    /**
     * Disables the update button in the JMenuBar.
     */
    public void disableUpdate(){
        menuItemUpdate.setEnabled(false);
    }

    /**
     * Creates a UpdateDialog. Called after channel table is updated.
     */
    public void showUpdateDialog(){
        disableUpdate();
        updateDialog = new UpdateDialog();

        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                updateDialog.indicateTimeHasPassed();
            }
        }, 10, 1000);
    }

    /**
     * Calls to update updateDialog and stops timer.
     */
    public void updateUpdateDialog(){
        if (updateDialog != null) {
            t.cancel();
            updateDialog.changeTextToDone();
        }
    }
}
