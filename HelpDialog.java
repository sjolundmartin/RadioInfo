import javax.swing.*;
import java.awt.*;

/**
 * A pop up dialog that is shown when the "About" button is pushed in the
 * drop down menu in the menubar in the gui window. The dialog contains a
 * short presentation about the game developers and the project.
 * @author id15msd
 * @since 2018-12-13
 */
public class HelpDialog {


    /**
     * Constructor of class. Creates and shows the dialog window.
     */
    public HelpDialog()
    {

        JDialog jdialog = new JDialog();
        jdialog.setTitle("Hjälp");
        jdialog.setPreferredSize(new Dimension(500,200));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        textArea.append("Programmet visar en lista av Sveriges Radios \n" +
                "radiokanaler. Genom att trycka på kanaler kan man se tablån " +
                        "\n" +
                "för kanalen med program sända som mest tolv timmar bak och\n" +
                "framåt i tiden.\n\n");
        textArea.append("De program som redan har sänts är markerade i grå \n" +
                "färg. Man kan få mer information om programmet genom att \n" +
                "trycka på det i listan.\n\n");
        textArea.append("Backa genom att trycka på 'Tillbaka'-knappen.\n\n");

        JScrollPane sp = new JScrollPane(textArea);

        jdialog.add(sp);
        jdialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jdialog.pack();

        jdialog.setVisible(true);

    }
}
