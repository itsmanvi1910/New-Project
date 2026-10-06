import javax.swing.*;
import java.awt.*;

public class FindReplaceDialog extends JDialog
{
    private MyEditor editor;

    private JTextField findField;
    private JTextField replaceField;

    private JCheckBox matchCaseCheckBox;

    private JButton findButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JButton closeButton;

    private boolean replaceMode;


    public FindReplaceDialog(
        MyEditor editor,
        boolean replaceMode)
    {
        super(
            editor,
            replaceMode
                ? "Find and Replace"
                : "Find",
            false
        );

        this.editor = editor;
        this.replaceMode = replaceMode;

        createComponents();

        setSize(
            replaceMode ? 500 : 400,
            replaceMode ? 220 : 170
        );

        setLocationRelativeTo(editor);

        setResizable(false);
    }


    private void createComponents()
    {
        JPanel mainPanel =
            new JPanel(
                new BorderLayout(10, 10)
            );

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
            )
        );


        int rows = replaceMode ? 3 : 2;

        JPanel inputPanel =
            new JPanel(
                new GridLayout(
                    rows,
                    2,
                    8,
                    8
                )
            );


        inputPanel.add(
            new JLabel("Find:")
        );

        findField =
            new JTextField();

        inputPanel.add(findField);


        if (replaceMode)
        {
            inputPanel.add(
                new JLabel("Replace with:")
            );

            replaceField =
                new JTextField();

            inputPanel.add(
                replaceField
            );
        }


        matchCaseCheckBox =
            new JCheckBox("Match case");

        inputPanel.add(
            matchCaseCheckBox
        );


        mainPanel.add(
            inputPanel,
            BorderLayout.CENTER
        );


        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT
                )
            );


        findButton =
            new JButton("Find Next");

        buttonPanel.add(findButton);


        if (replaceMode)
        {
            replaceButton =
                new JButton("Replace");

            replaceAllButton =
                new JButton("Replace All");

            buttonPanel.add(
                replaceButton
            );

            buttonPanel.add(
                replaceAllButton
            );
        }


        closeButton =
            new JButton("Close");

        buttonPanel.add(closeButton);


        mainPanel.add(
            buttonPanel,
            BorderLayout.SOUTH
        );


        setContentPane(mainPanel);


        addListeners();
    }


    private void addListeners()
    {
        findButton.addActionListener(
            e -> performFind()
        );


        if (replaceMode)
        {
            replaceButton.addActionListener(
                e -> performReplace()
            );

            replaceAllButton.addActionListener(
                e -> performReplaceAll()
            );
        }


        closeButton.addActionListener(
            e -> dispose()
        );


        findField.addActionListener(
            e -> performFind()
        );


        SwingUtilities.invokeLater(
            () -> findField.requestFocus()
        );
    }


    private void performFind()
    {
        String searchText =
            findField.getText();

        if (searchText.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "Please enter text to find.",
                "Find",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        int result =
            editor.findText(
                searchText,
                matchCaseCheckBox.isSelected()
            );


        if (result == -1)
        {
            JOptionPane.showMessageDialog(
                this,
                "Text not found.",
                "Find",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    private void performReplace()
    {
        String searchText =
            findField.getText();

        String replacement =
            replaceField.getText();

        if (searchText.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "Please enter text to find.",
                "Replace",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        boolean replaced =
            editor.replaceCurrent(
                searchText,
                replacement,
                matchCaseCheckBox.isSelected()
            );


        if (replaced)
        {
            editor.markChanged();

            editor.findText(
                searchText,
                matchCaseCheckBox.isSelected()
            );
        }
        else
        {
            int result =
                editor.findText(
                    searchText,
                    matchCaseCheckBox.isSelected()
                );

            if (result == -1)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Text not found.",
                    "Replace",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }


    private void performReplaceAll()
    {
        String searchText =
            findField.getText();

        String replacement =
            replaceField.getText();

        if (searchText.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "Please enter text to find.",
                "Replace All",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        int count =
            editor.replaceAllText(
                searchText,
                replacement,
                matchCaseCheckBox.isSelected()
            );


        if (count > 0)
        {
            editor.markChanged();

            JOptionPane.showMessageDialog(
                this,
                count
                + " occurrence(s) replaced.",
                "Replace All",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        else
        {
            JOptionPane.showMessageDialog(
                this,
                "Text not found.",
                "Replace All",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}