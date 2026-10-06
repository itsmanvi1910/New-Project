import javax.swing.*;
import java.awt.*;

public class FontChooserDialog extends JDialog
{
    private JComboBox<String> fontBox;
    private JSpinner sizeSpinner;

    private JButton okButton;
    private JButton cancelButton;

    private String selectedFont;
    private int selectedSize;

    private boolean confirmed;


    public FontChooserDialog(
        JFrame parent,
        String currentFont,
        int currentSize)
    {
        super(
            parent,
            "Choose Font",
            true
        );

        confirmed = false;

        createComponents(
            currentFont,
            currentSize
        );

        setSize(400, 180);

        setLocationRelativeTo(parent);

        setResizable(false);
    }


    private void createComponents(
        String currentFont,
        int currentSize)
    {
        GraphicsEnvironment environment =
            GraphicsEnvironment
                .getLocalGraphicsEnvironment();


        String[] fonts =
            environment
                .getAvailableFontFamilyNames();


        fontBox =
            new JComboBox<>(fonts);

        fontBox.setSelectedItem(
            currentFont
        );


        SpinnerNumberModel sizeModel =
            new SpinnerNumberModel(
                currentSize,
                8,
                72,
                1
            );


        sizeSpinner =
            new JSpinner(sizeModel);


        JPanel inputPanel =
            new JPanel(
                new GridLayout(
                    2,
                    2,
                    8,
                    8
                )
            );


        inputPanel.add(
            new JLabel("Font:")
        );

        inputPanel.add(fontBox);


        inputPanel.add(
            new JLabel("Size:")
        );

        inputPanel.add(sizeSpinner);


        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT
                )
            );


        okButton =
            new JButton("OK");

        cancelButton =
            new JButton("Cancel");


        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);


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


        mainPanel.add(
            inputPanel,
            BorderLayout.CENTER
        );


        mainPanel.add(
            buttonPanel,
            BorderLayout.SOUTH
        );


        setContentPane(mainPanel);


        okButton.addActionListener(
            e -> confirm()
        );


        cancelButton.addActionListener(
            e -> dispose()
        );
    }


    private void confirm()
    {
        selectedFont =
            (String) fontBox.getSelectedItem();

        selectedSize =
            (Integer) sizeSpinner.getValue();

        confirmed = true;

        dispose();
    }


    public boolean isConfirmed()
    {
        return confirmed;
    }


    public String getSelectedFont()
    {
        return selectedFont;
    }


    public int getSelectedSize()
    {
        return selectedSize;
    }
}