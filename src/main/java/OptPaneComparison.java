import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class OptPaneComparison extends JFrame {

    protected JOptionPane optPane;

    public static void main(String[] args) {
        JFrame f = new OptPaneComparison("Enter your name");
        f.setVisible(true);
    }

    public OptPaneComparison(final String message) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final int msgType = JOptionPane.QUESTION_MESSAGE;
        final int optType = JOptionPane.OK_CANCEL_OPTION;
        final String title = message;

        setSize(350, 200);

        // Create a desktop for internal frames
        final JDesktopPane desk = new JDesktopPane();
        setContentPane(desk);

        // Add a simple menu bar
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);

        JMenu menu = new JMenu("Dialog");
        JMenu imenu = new JMenu("Internal");
        mb.add(menu);
        mb.add(imenu);

        final JMenuItem construct = new JMenuItem("Constructor");
        final JMenuItem stat = new JMenuItem("Static Method");
        final JMenuItem iconstruct = new JMenuItem("Constructor");
        final JMenuItem istat = new JMenuItem("Static Method");
        menu.add(construct);
        menu.add(stat);
        imenu.add(iconstruct);
        imenu.add(istat);

        // Create our JOptionPane. We're asking for input, so we call
        // setWantsInput.
        // Note that we cannot specify this via constructor parameters.
        optPane = new JOptionPane(message, msgType, optType);
        optPane.setWantsInput(true);

        // Add a listener for each menu item that will display the appropriate
        // dialog/internal frame
        construct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                // Create and display the dialog
                JDialog d = optPane.createDialog(desk, title);
                d.setVisible(true);

                respond(getOptionPaneValue());
            }
        });

        stat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String s = JOptionPane.showInputDialog(desk, message, title,
                        msgType);
                respond(s);
            }
        });

        iconstruct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                // Create and display the dialog
                JInternalFrame f = optPane.createInternalFrame(desk, title);
                f.setVisible(true);

                // Listen for the frame to close before getting the value from
                // it.
                f.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent ev) {
                        if ((ev.getPropertyName()
                                .equals(JInternalFrame.IS_CLOSED_PROPERTY))
                                && (ev.getNewValue() == Boolean.TRUE)) {
                            respond(getOptionPaneValue());
                        }
                    }
                });
            }
        });

        istat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String s = JOptionPane.showInternalInputDialog(desk, message,
                        title, msgType);
                respond(s);
            }
        });
    }

    private String getOptionPaneValue() {
        return "";
    }


    protected void respond(String s) {
        if (s == null)
            System.out.println("Never mind.");
        else
            System.out.println("You entered: " + s);
    }
}
