import javax.swing.*;
import java.awt.*;

/**
 * A pop up dialog that is shown when the "About" button is pushed in the
 * drop down menu in the menu bar in the gui window. The dialog contains a
 * short presentation about the application and developers.
 * @author id15msd
 * @since 2018-01-07
 */
public class AboutDialog {

    /**
     * Constructor of class. Creates and shows the dialog window.
     */
    public AboutDialog()
    {

        JDialog jdialog = new JDialog();
        jdialog.setTitle("Om Programmet");
        jdialog.setPreferredSize(new Dimension(500,200));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        textArea.append("This Radio Info Application is the result of an " +
                "assignment \n");
        textArea.append("in the course Applikationsutveckling i Java at Umeå " +
                "University.\n");
        textArea.append("The application is built by Martin Sjölund (id15msd)" +
                ".");
        JScrollPane sp = new JScrollPane(textArea);

        jdialog.add(sp);
        jdialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jdialog.pack();

        jdialog.setVisible(true);

    }
}
