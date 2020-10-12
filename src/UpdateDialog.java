import javax.swing.*;
import java.awt.*;

/**
 * A pop up dialog that is shown when the "About" button is pushed in the
 * drop down menu in the menubar in the gui window. The dialog contains a
 * short presentation about the game developers and the project.
 * @author id15msd
 * @since 2018-12-13
 */
public class UpdateDialog {

    private JTextArea textArea;
    private int time = 0;
    /**
     * Constructor of class. Creates and shows the dialog window.
     */
    public UpdateDialog()
    {

        JDialog jdialog = new JDialog();
        jdialog.setPreferredSize(new Dimension(400,100));
        textArea = new JTextArea();
        textArea.setEditable(false);
        jdialog.setLocationRelativeTo(null);

        textArea.append("Kanaltablåerna uppdateras. Vänligen vänta.");

        jdialog.add(textArea);
        jdialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jdialog.pack();
        jdialog.setVisible(true);

    }

    /**
     * Changes textArea text to display that the update is finished.
     */
    public void changeTextToDone(){
        textArea.setText("Kanaltablåerna är nu uppdaterade!");
    }

    /**
     * Adds dot to show time has passed. Used as loading indicator.
     */
    public void indicateTimeHasPassed(){
        time++;
        if(time<20){
            textArea.append(".");
        }
        else{
            textArea.setText("Uppdateringen av kanaltablåer misslyckades.\n " +
                    "Testa igen om en stund.");
        }
    }

}
