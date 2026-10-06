import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Path;


public class MyEditor extends JFrame
{
    // =========================================================
    // FILE INFORMATION
    // =========================================================

    private Path currentFile;

    private boolean changesSaved;


    // =========================================================
    // MAIN COMPONENTS
    // =========================================================

    private JTextArea editorTextArea;

    private JLabel statusLabel;

    private JMenuBar menuBar;


    // =========================================================
    // MENUS
    // =========================================================

    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu formatMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;


    // =========================================================
    // FILE MENU ITEMS
    // =========================================================

    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem exitMenuItem;


    // =========================================================
    // EDIT MENU ITEMS
    // =========================================================

    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;

    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem selectAllMenuItem;

    private JMenuItem findMenuItem;
    private JMenuItem replaceMenuItem;


    // =========================================================
    // FORMAT MENU ITEMS
    // =========================================================

    private JMenuItem fontMenuItem;
    private JMenuItem increaseFontMenuItem;
    private JMenuItem decreaseFontMenuItem;

    private JCheckBoxMenuItem boldMenuItem;
    private JCheckBoxMenuItem italicMenuItem;
    private JCheckBoxMenuItem wordWrapMenuItem;


    // =========================================================
    // VIEW MENU ITEMS
    // =========================================================

    private JCheckBoxMenuItem statusBarMenuItem;
    private JCheckBoxMenuItem darkModeMenuItem;


    // =========================================================
    // HELP MENU ITEMS
    // =========================================================

    private JMenuItem aboutMenuItem;


    // =========================================================
    // OTHER OBJECTS
    // =========================================================

    private UndoManager undoManager;

    private FileManager fileManager;


    // =========================================================
    // FONT INFORMATION
    // =========================================================

    private static final int MIN_FONT_SIZE = 10;
    private static final int MAX_FONT_SIZE = 70;
    private static final int DEFAULT_FONT_SIZE = 16;

    private String currentFontName = "Monospaced";

    private int currentFontSize = DEFAULT_FONT_SIZE;

    private int currentFontStyle = Font.PLAIN;


    // =========================================================
    // THEME
    // =========================================================

    private boolean darkMode;


    // =========================================================
    // INTERNAL CHANGE FLAG
    // =========================================================

    private boolean internalChange;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MyEditor()
    {
        super("Untitled - MyEditor");

        currentFile = null;

        changesSaved = true;

        darkMode = false;

        internalChange = false;

        undoManager =
            new UndoManager();

        fileManager =
            new FileManager(this);


        createComponents();

        createMenuBar();

        createStatusBar();

        addEventListeners();

        setupAppearance();

        setDefaultCloseOperation(
            DO_NOTHING_ON_CLOSE
        );

        updateTitle();

        updateStatus();
    }


    // =========================================================
    // CREATE COMPONENTS
    // =========================================================

    private void createComponents()
    {
        editorTextArea =
            new JTextArea();


        editorTextArea.setFont(
            new Font(
                currentFontName,
                currentFontStyle,
                currentFontSize
            )
        );


        editorTextArea.setLineWrap(false);

        editorTextArea.setWrapStyleWord(true);

        editorTextArea.setTabSize(4);


        editorTextArea.setMargin(
            new Insets(
                8,
                8,
                8,
                8
            )
        );


        JScrollPane scrollPane =
            new JScrollPane(
                editorTextArea
            );


        setLayout(
            new BorderLayout()
        );


        add(
            scrollPane,
            BorderLayout.CENTER
        );
    }


    // =========================================================
    // CREATE MENU BAR
    // =========================================================

    private void createMenuBar()
    {
        menuBar =
            new JMenuBar();


        createFileMenu();

        createEditMenu();

        createFormatMenu();

        createViewMenu();

        createHelpMenu();


        menuBar.add(fileMenu);

        menuBar.add(editMenu);

        menuBar.add(formatMenu);

        menuBar.add(viewMenu);

        menuBar.add(helpMenu);


        setJMenuBar(menuBar);
    }


    // =========================================================
    // FILE MENU
    // =========================================================

    private void createFileMenu()
    {
        fileMenu =
            new JMenu("File");


        newMenuItem =
            new JMenuItem("New");

        openMenuItem =
            new JMenuItem("Open");

        saveMenuItem =
            new JMenuItem("Save");

        saveAsMenuItem =
            new JMenuItem("Save As...");

        exitMenuItem =
            new JMenuItem("Exit");


        newMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        openMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        saveMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        saveAsMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK
                |
                InputEvent.SHIFT_DOWN_MASK
            )
        );


        exitMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        fileMenu.add(newMenuItem);

        fileMenu.add(openMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(saveMenuItem);

        fileMenu.add(saveAsMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(exitMenuItem);
    }


    // =========================================================
    // EDIT MENU
    // =========================================================

    private void createEditMenu()
    {
        editMenu =
            new JMenu("Edit");


        undoMenuItem =
            new JMenuItem("Undo");

        redoMenuItem =
            new JMenuItem("Redo");


        cutMenuItem =
            new JMenuItem("Cut");

        copyMenuItem =
            new JMenuItem("Copy");

        pasteMenuItem =
            new JMenuItem("Paste");

        selectAllMenuItem =
            new JMenuItem("Select All");


        findMenuItem =
            new JMenuItem("Find...");

        replaceMenuItem =
            new JMenuItem("Replace...");


        undoMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        redoMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Y,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        cutMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_X,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        copyMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_C,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        pasteMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_V,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        selectAllMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_A,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        findMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_F,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        replaceMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_H,
                InputEvent.CTRL_DOWN_MASK
            )
        );


        editMenu.add(undoMenuItem);

        editMenu.add(redoMenuItem);

        editMenu.addSeparator();

        editMenu.add(cutMenuItem);

        editMenu.add(copyMenuItem);

        editMenu.add(pasteMenuItem);

        editMenu.add(selectAllMenuItem);

        editMenu.addSeparator();

        editMenu.add(findMenuItem);

        editMenu.add(replaceMenuItem);
    }


    // =========================================================
    // FORMAT MENU
    // =========================================================

    private void createFormatMenu()
    {
        formatMenu =
            new JMenu("Format");


        fontMenuItem =
            new JMenuItem("Choose Font...");


        increaseFontMenuItem =
            new JMenuItem(
                "Increase Font Size"
            );


        decreaseFontMenuItem =
            new JMenuItem(
                "Decrease Font Size"
            );


        boldMenuItem =
            new JCheckBoxMenuItem("Bold");


        italicMenuItem =
            new JCheckBoxMenuItem("Italic");


        wordWrapMenuItem =
            new JCheckBoxMenuItem("Word Wrap");


        formatMenu.add(
            fontMenuItem
        );

        formatMenu.add(
            increaseFontMenuItem
        );

        formatMenu.add(
            decreaseFontMenuItem
        );

        formatMenu.addSeparator();

        formatMenu.add(
            boldMenuItem
        );

        formatMenu.add(
            italicMenuItem
        );

        formatMenu.addSeparator();

        formatMenu.add(
            wordWrapMenuItem
        );
    }


    // =========================================================
    // VIEW MENU
    // =========================================================

    private void createViewMenu()
    {
        viewMenu =
            new JMenu("View");


        statusBarMenuItem =
            new JCheckBoxMenuItem(
                "Status Bar",
                true
            );


        darkModeMenuItem =
            new JCheckBoxMenuItem(
                "Dark Mode",
                false
            );


        viewMenu.add(
            statusBarMenuItem
        );

        viewMenu.add(
            darkModeMenuItem
        );
    }


    // =========================================================
    // HELP MENU
    // =========================================================

    private void createHelpMenu()
    {
        helpMenu =
            new JMenu("Help");


        aboutMenuItem =
            new JMenuItem(
                "About MyEditor"
            );


        helpMenu.add(
            aboutMenuItem
        );
    }


    // =========================================================
    // STATUS BAR
    // =========================================================

    private void createStatusBar()
    {
        statusLabel =
            new JLabel(
                "Ln 1, Col 1"
            );


        statusLabel.setBorder(
            BorderFactory.createEmptyBorder(
                4,
                8,
                4,
                8
            )
        );


        add(
            statusLabel,
            BorderLayout.SOUTH
        );
    }


    // =========================================================
    // EVENT LISTENERS
    // =========================================================

    private void addEventListeners()
    {
        // -----------------------------------------------------
        // NEW
        // -----------------------------------------------------

        newMenuItem.addActionListener(
            e -> newFile()
        );


        // -----------------------------------------------------
        // OPEN
        // -----------------------------------------------------

        openMenuItem.addActionListener(
            e -> openFile()
        );


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        saveMenuItem.addActionListener(
            e -> saveFile()
        );


        // -----------------------------------------------------
        // SAVE AS
        // -----------------------------------------------------

        saveAsMenuItem.addActionListener(
            e -> saveFileAs()
        );


        // -----------------------------------------------------
        // EXIT
        // -----------------------------------------------------

        exitMenuItem.addActionListener(
            e -> closeApplication()
        );


        // -----------------------------------------------------
        // UNDO
        // -----------------------------------------------------

        undoMenuItem.addActionListener(
            e -> undo()
        );


        // -----------------------------------------------------
        // REDO
        // -----------------------------------------------------

        redoMenuItem.addActionListener(
            e -> redo()
        );


        // -----------------------------------------------------
        // CUT
        // -----------------------------------------------------

        cutMenuItem.addActionListener(
            e -> editorTextArea.cut()
        );


        // -----------------------------------------------------
        // COPY
        // -----------------------------------------------------

        copyMenuItem.addActionListener(
            e -> editorTextArea.copy()
        );


        // -----------------------------------------------------
        // PASTE
        // -----------------------------------------------------

        pasteMenuItem.addActionListener(
            e -> editorTextArea.paste()
        );


        // -----------------------------------------------------
        // SELECT ALL
        // -----------------------------------------------------

        selectAllMenuItem.addActionListener(
            e -> editorTextArea.selectAll()
        );


        // -----------------------------------------------------
        // FIND
        // -----------------------------------------------------

        findMenuItem.addActionListener(
            e -> showFindDialog()
        );


        // -----------------------------------------------------
        // REPLACE
        // -----------------------------------------------------

        replaceMenuItem.addActionListener(
            e -> showReplaceDialog()
        );


        // -----------------------------------------------------
        // FONT
        // -----------------------------------------------------

        fontMenuItem.addActionListener(
            e -> chooseFont()
        );


        // -----------------------------------------------------
        // INCREASE FONT
        // -----------------------------------------------------

        increaseFontMenuItem.addActionListener(
            e -> changeFontSize(2)
        );


        // -----------------------------------------------------
        // DECREASE FONT
        // -----------------------------------------------------

        decreaseFontMenuItem.addActionListener(
            e -> changeFontSize(-2)
        );


        // -----------------------------------------------------
        // BOLD
        // -----------------------------------------------------

        boldMenuItem.addActionListener(
            e -> updateFontStyle()
        );


        // -----------------------------------------------------
        // ITALIC
        // -----------------------------------------------------

        italicMenuItem.addActionListener(
            e -> updateFontStyle()
        );


        // -----------------------------------------------------
        // WORD WRAP
        // -----------------------------------------------------

        wordWrapMenuItem.addActionListener(
            e ->
            {
                boolean enabled =
                    wordWrapMenuItem.isSelected();

                editorTextArea.setLineWrap(
                    enabled
                );

                editorTextArea.setWrapStyleWord(
                    enabled
                );
            }
        );


        // -----------------------------------------------------
        // STATUS BAR
        // -----------------------------------------------------

        statusBarMenuItem.addActionListener(
            e ->
            {
                statusLabel.setVisible(
                    statusBarMenuItem.isSelected()
                );

                revalidate();
            }
        );


        // -----------------------------------------------------
        // DARK MODE
        // -----------------------------------------------------

        darkModeMenuItem.addActionListener(
            e ->
            {
                darkMode =
                    darkModeMenuItem.isSelected();

                applyTheme();
            }
        );


        // -----------------------------------------------------
        // ABOUT
        // -----------------------------------------------------

        aboutMenuItem.addActionListener(
            e -> showAboutDialog()
        );


        // -----------------------------------------------------
        // DOCUMENT LISTENER
        // -----------------------------------------------------

        editorTextArea.getDocument()
            .addDocumentListener(
                new DocumentListener()
                {
                    public void insertUpdate(
                        DocumentEvent event)
                    {
                        documentChanged();
                    }


                    public void removeUpdate(
                        DocumentEvent event)
                    {
                        documentChanged();
                    }


                    public void changedUpdate(
                        DocumentEvent event)
                    {
                        documentChanged();
                    }
                }
            );


        // -----------------------------------------------------
        // UNDO LISTENER
        // -----------------------------------------------------

        editorTextArea.getDocument()
            .addUndoableEditListener(
                event ->
                {
                    undoManager.addEdit(
                        event.getEdit()
                    );

                    updateUndoRedoState();
                }
            );


        // -----------------------------------------------------
        // CARET LISTENER
        // -----------------------------------------------------

        editorTextArea.addCaretListener(
            event -> updateStatus()
        );


        // -----------------------------------------------------
        // WINDOW CLOSE
        // -----------------------------------------------------

        addWindowListener(
            new WindowAdapter()
            {
                public void windowClosing(
                    WindowEvent event)
                {
                    closeApplication();
                }
            }
        );
    }


    // =========================================================
    // DOCUMENT CHANGED
    // =========================================================

    private void documentChanged()
    {
        if (!internalChange)
        {
            changesSaved = false;

            updateTitle();

            updateStatus();
        }
    }


    // =========================================================
    // MARK CHANGED
    // =========================================================

    public void markChanged()
    {
        changesSaved = false;

        updateTitle();

        updateStatus();
    }


    // =========================================================
    // UPDATE TITLE
    // =========================================================

    private void updateTitle()
    {
        String name;


        if (currentFile == null)
        {
            name = "Untitled";
        }
        else
        {
            name =
                currentFile
                    .getFileName()
                    .toString();
        }


        if (changesSaved)
        {
            setTitle(
                name + " - MyEditor"
            );
        }
        else
        {
            setTitle(
                "*" + name + " - MyEditor"
            );
        }
    }


    // =========================================================
    // NEW FILE
    // =========================================================

    private void newFile()
    {
        if (!confirmUnsavedChanges())
        {
            return;
        }


        internalChange = true;

        editorTextArea.setText("");

        internalChange = false;


        currentFile = null;

        changesSaved = true;


        undoManager.discardAllEdits();

        updateUndoRedoState();

        updateTitle();

        updateStatus();


        editorTextArea.requestFocus();
    }


    // =========================================================
    // OPEN FILE
    // =========================================================

    private void openFile()
    {
        if (!confirmUnsavedChanges())
        {
            return;
        }


        Path path =
            fileManager.openFile();


        if (path == null)
        {
            return;
        }


        try
        {
            String content =
                fileManager.readFile(path);


            internalChange = true;

            editorTextArea.setText(
                content
            );

            editorTextArea.setCaretPosition(
                0
            );

            internalChange = false;


            currentFile = path;

            changesSaved = true;


            undoManager.discardAllEdits();

            updateUndoRedoState();

            updateTitle();

            updateStatus();
        }
        catch (IOException exception)
        {
            internalChange = false;

            fileManager.showOpenError(
                exception
            );
        }
    }


    // =========================================================
    // SAVE
    // =========================================================

    private boolean saveFile()
    {
        if (currentFile == null)
        {
            return saveFileAs();
        }


        boolean saved =
            fileManager.saveFile(
                currentFile,
                editorTextArea.getText()
            );


        if (saved)
        {
            changesSaved = true;

            updateTitle();

            updateStatus();
        }


        return saved;
    }


    // =========================================================
    // SAVE AS
    // =========================================================

    private boolean saveFileAs()
    {
        Path path =
            fileManager.chooseSaveFile(
                currentFile
            );


        if (path == null)
        {
            return false;
        }


        boolean saved =
            fileManager.saveFile(
                path,
                editorTextArea.getText()
            );


        if (saved)
        {
            currentFile = path;

            changesSaved = true;

            updateTitle();

            updateStatus();
        }


        return saved;
    }


    // =========================================================
    // CONFIRM UNSAVED CHANGES
    // =========================================================

    private boolean confirmUnsavedChanges()
    {
        if (changesSaved)
        {
            return true;
        }


        int result =
            JOptionPane.showConfirmDialog(
                this,
                "The current file has unsaved changes.\n"
                + "Do you want to save them?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );


        if (result ==
            JOptionPane.YES_OPTION)
        {
            return saveFile();
        }


        if (result ==
            JOptionPane.NO_OPTION)
        {
            return true;
        }


        return false;
    }


    // =========================================================
    // CLOSE APPLICATION
    // =========================================================

    private void closeApplication()
    {
        if (!confirmUnsavedChanges())
        {
            return;
        }


        dispose();

        System.exit(0);
    }


    // =========================================================
    // UNDO
    // =========================================================

    private void undo()
    {
        if (undoManager.canUndo())
        {
            try
            {
                undoManager.undo();

                changesSaved = false;

                updateTitle();

                updateStatus();
            }
            catch (CannotUndoException exception)
            {
                Toolkit
                    .getDefaultToolkit()
                    .beep();
            }
        }


        updateUndoRedoState();
    }


    // =========================================================
    // REDO
    // =========================================================

    private void redo()
    {
        if (undoManager.canRedo())
        {
            try
            {
                undoManager.redo();

                changesSaved = false;

                updateTitle();

                updateStatus();
            }
            catch (CannotRedoException exception)
            {
                Toolkit
                    .getDefaultToolkit()
                    .beep();
            }
        }


        updateUndoRedoState();
    }


    // =========================================================
    // UNDO / REDO STATE
    // =========================================================

    private void updateUndoRedoState()
    {
        undoMenuItem.setEnabled(
            undoManager.canUndo()
        );

        redoMenuItem.setEnabled(
            undoManager.canRedo()
        );
    }


    // =========================================================
    // SHOW FIND
    // =========================================================

    private void showFindDialog()
    {
        FindReplaceDialog dialog =
            new FindReplaceDialog(
                this,
                false
            );

        dialog.setVisible(true);
    }


    // =========================================================
    // SHOW REPLACE
    // =========================================================

    private void showReplaceDialog()
    {
        FindReplaceDialog dialog =
            new FindReplaceDialog(
                this,
                true
            );

        dialog.setVisible(true);
    }


    // =========================================================
    // FIND TEXT
    // =========================================================

    public int findText(
        String searchText,
        boolean matchCase)
    {
        if (searchText == null ||
            searchText.isEmpty())
        {
            return -1;
        }


        String text =
            editorTextArea.getText();


        String search =
            searchText;


        if (!matchCase)
        {
            text =
                text.toLowerCase();

            search =
                search.toLowerCase();
        }


        int start =
            editorTextArea.getCaretPosition();


        int index =
            text.indexOf(
                search,
                start
            );


        if (index == -1 && start > 0)
        {
            index =
                text.indexOf(search);
        }


        if (index != -1)
        {
            editorTextArea.requestFocus();

            editorTextArea.select(
                index,
                index + search.length()
            );
        }


        return index;
    }


    // =========================================================
    // REPLACE CURRENT
    // =========================================================

    public boolean replaceCurrent(
        String searchText,
        String replacement,
        boolean matchCase)
    {
        String selected =
            editorTextArea.getSelectedText();


        if (selected == null)
        {
            return false;
        }


        boolean matches;


        if (matchCase)
        {
            matches =
                selected.equals(searchText);
        }
        else
        {
            matches =
                selected.equalsIgnoreCase(
                    searchText
                );
        }


        if (!matches)
        {
            return false;
        }


        editorTextArea.replaceSelection(
            replacement
        );


        return true;
    }


    // =========================================================
    // REPLACE ALL
    // =========================================================

    public int replaceAllText(
        String searchText,
        String replacement,
        boolean matchCase)
    {
        if (searchText == null ||
            searchText.isEmpty())
        {
            return 0;
        }


        String text =
            editorTextArea.getText();


        String search =
            matchCase
                ? searchText
                : searchText.toLowerCase();


        String compareText =
            matchCase
                ? text
                : text.toLowerCase();


        int count = 0;

        int position = 0;


        while ((position =
            compareText.indexOf(
                search,
                position
            )) != -1)
        {
            count++;

            position += search.length();
        }


        if (count == 0)
        {
            return 0;
        }


        StringBuilder result =
            new StringBuilder();


        int current = 0;


        while (true)
        {
            int index =
                compareText.indexOf(
                    search,
                    current
                );


            if (index == -1)
            {
                result.append(
                    text.substring(current)
                );

                break;
            }


            result.append(
                text.substring(
                    current,
                    index
                )
            );


            result.append(
                replacement
            );


            current =
                index + searchText.length();
        }


        internalChange = true;

        editorTextArea.setText(
            result.toString()
        );

        internalChange = false;


        return count;
    }


    // =========================================================
    // CHOOSE FONT
    // =========================================================

    private void chooseFont()
    {
        FontChooserDialog dialog =
            new FontChooserDialog(
                this,
                currentFontName,
                currentFontSize
            );


        dialog.setVisible(true);


        if (dialog.isConfirmed())
        {
            currentFontName =
                dialog.getSelectedFont();

            currentFontSize =
                dialog.getSelectedSize();


            updateFontStyle();
        }
    }


    // =========================================================
    // CHANGE FONT SIZE
    // =========================================================

    private void changeFontSize(int amount)
{
    int newSize =
        currentFontSize + amount;


    // ---------------------------------------------------------
    // MINIMUM LIMIT
    // ---------------------------------------------------------

    if (newSize < MIN_FONT_SIZE)
    {
        newSize = MIN_FONT_SIZE;
    }


    // ---------------------------------------------------------
    // MAXIMUM LIMIT
    // ---------------------------------------------------------

    if (newSize > MAX_FONT_SIZE)
    {
        newSize = MAX_FONT_SIZE;
    }


    currentFontSize = newSize;


    updateFontStyle();

    updateFontMenuState();
}


    // =========================================================
    // UPDATE FONT STYLE
    // =========================================================

    private void updateFontStyle()
    {
        currentFontStyle =
            Font.PLAIN;


        if (boldMenuItem.isSelected())
        {
            currentFontStyle |=
                Font.BOLD;
        }


        if (italicMenuItem.isSelected())
        {
            currentFontStyle |=
                Font.ITALIC;
        }


        editorTextArea.setFont(
            new Font(
                currentFontName,
                currentFontStyle,
                currentFontSize
            )
        );
    }


    // =========================================================
    // UPDATE STATUS
    // =========================================================

    private void updateStatus()
    {
        if (statusLabel == null)
        {
            return;
        }


        try
        {
            int caret =
                editorTextArea
                    .getCaretPosition();


            int line =
                editorTextArea
                    .getLineOfOffset(caret);


            int lineStart =
                editorTextArea
                    .getLineStartOffset(line);


            int column =
                caret - lineStart;


            int wordCount =
                countWords(
                    editorTextArea.getText()
                );


            int characterCount =
                editorTextArea
                    .getText()
                    .length();


            statusLabel.setText(
                "Ln " + (line + 1)
                + ", Col " + (column + 1)
                + "    |    Words: "
                + wordCount
                + "    |    Characters: "
                + characterCount
            );
        }
        catch (BadLocationException exception)
        {
            statusLabel.setText(
                "Ln 1, Col 1"
            );
        }
    }


    // =========================================================
    // COUNT WORDS
    // =========================================================

    private int countWords(String text)
    {
        if (text == null ||
            text.trim().isEmpty())
        {
            return 0;
        }


        return text
            .trim()
            .split("\\s+")
            .length;
    }


    // =========================================================
    // APPLY THEME
    // =========================================================

    private void applyTheme()
    {
        if (darkMode)
        {
            editorTextArea.setBackground(
                new Color(
                    35,
                    35,
                    35
                )
            );


            editorTextArea.setForeground(
                new Color(
                    235,
                    235,
                    235
                )
            );


            editorTextArea.setCaretColor(
                Color.WHITE
            );


            statusLabel.setBackground(
                new Color(
                    50,
                    50,
                    50
                )
            );


            statusLabel.setForeground(
                Color.WHITE
            );


            statusLabel.setOpaque(true);
        }
        else
        {
            editorTextArea.setBackground(
                Color.WHITE
            );


            editorTextArea.setForeground(
                Color.BLACK
            );


            editorTextArea.setCaretColor(
                Color.BLACK
            );


            statusLabel.setBackground(
                UIManager.getColor(
                    "Panel.background"
                )
            );


            statusLabel.setForeground(
                UIManager.getColor(
                    "Label.foreground"
                )
            );


            statusLabel.setOpaque(false);
        }


        repaint();
    }


    // =========================================================
    // ABOUT
    // =========================================================

    private void showAboutDialog()
    {
        AboutDialog dialog =
            new AboutDialog(this);

        dialog.setVisible(true);
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
            () ->
            {
                MyEditor editor =
                    new MyEditor();


                if (args.length > 0)
                {
                    editor.openFileFromCommandLine(
                        args[0]
                    );
                }


                editor.setVisible(true);
            }
        );
    }


    // =========================================================
    // OPEN FILE FROM COMMAND LINE
    // =========================================================

    private void openFileFromCommandLine(
        String fileName)
    {
        try
        {
            Path path =
                Path.of(fileName);


            String content =
                fileManager.readFile(path);


            internalChange = true;

            editorTextArea.setText(
                content
            );

            editorTextArea.setCaretPosition(
                0
            );

            internalChange = false;


            currentFile = path;

            changesSaved = true;


            updateTitle();

            updateStatus();
        }
        catch (IOException exception)
        {
            fileManager.showOpenError(
                exception
            );
        }
    }
}