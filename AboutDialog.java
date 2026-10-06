import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog
{
    public AboutDialog(JFrame parent)
    {
        super(
            parent,
            "About MyEditor",
            true
        );

        createComponents();

        setSize(400, 300);

        setLocationRelativeTo(parent);

        setResizable(false);
    }


    private void createComponents()
    {
        JPanel panel =
            new JPanel(
                new BorderLayout(10, 10)
            );


        panel.setBorder(
            BorderFactory.createEmptyBorder(
                20,
                20,
                20,
                20
            )
        );


        JLabel title =
            new JLabel(
                "MyEditor",
                SwingConstants.CENTER
            );


        title.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                26
            )
        );


        panel.add(
            title,
            BorderLayout.NORTH
        );


        JTextArea information =
            new JTextArea(
                "A Java Swing Text Editor\n\n"
                + "Features:\n"
                + "• New, Open, Save and Save As\n"
                + "• Undo and Redo\n"
                + "• Find and Replace\n"
                + "• Font customization\n"
                + "• Bold and Italic\n"
                + "• Word Wrap\n"
                + "• Status Bar\n"
                + "• Dark Mode\n"
                + "• UTF-8 file support\n\n"
                + "Created using Java Swing."
            );


        information.setEditable(false);

        information.setOpaque(false);

        information.setLineWrap(true);

        information.setWrapStyleWord(true);


        panel.add(
            information,
            BorderLayout.CENTER
        );


        JButton closeButton =
            new JButton("Close");


        closeButton.addActionListener(
            e -> dispose()
        );


        JPanel buttonPanel =
            new JPanel();


        buttonPanel.add(closeButton);


        panel.add(
            buttonPanel,
            BorderLayout.SOUTH
        );


        setContentPane(panel);
    }
}