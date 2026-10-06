import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager
{
    private JFrame parent;

    public FileManager(JFrame parent)
    {
        this.parent = parent;
    }

    public Path openFile()
    {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Open File");

        int result =
            fileChooser.showOpenDialog(parent);

        if (result != JFileChooser.APPROVE_OPTION)
        {
            return null;
        }

        return fileChooser.getSelectedFile().toPath();
    }


    public String readFile(Path path)
        throws IOException
    {
        return Files.readString(
            path,
            StandardCharsets.UTF_8
        );
    }


    public boolean saveFile(
        Path path,
        String content)
    {
        try
        {
            Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            return true;
        }
        catch (IOException exception)
        {
            JOptionPane.showMessageDialog(
                parent,
                "Could not save the file.\n\n"
                + exception.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }


    public Path chooseSaveFile(Path currentFile)
    {
        JFileChooser fileChooser =
            new JFileChooser();

        fileChooser.setDialogTitle(
            "Save File As"
        );

        if (currentFile != null)
        {
            fileChooser.setSelectedFile(
                currentFile.toFile()
            );
        }
        else
        {
            fileChooser.setSelectedFile(
                new File("Untitled.txt")
            );
        }

        int result =
            fileChooser.showSaveDialog(parent);

        if (result != JFileChooser.APPROVE_OPTION)
        {
            return null;
        }

        Path selectedPath =
            fileChooser
                .getSelectedFile()
                .toPath();

        if (Files.exists(selectedPath))
        {
            int answer =
                JOptionPane.showConfirmDialog(
                    parent,
                    "The file already exists.\n"
                    + "Do you want to replace it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

            if (answer != JOptionPane.YES_OPTION)
            {
                return null;
            }
        }

        return selectedPath;
    }


    public void showOpenError(IOException exception)
    {
        JOptionPane.showMessageDialog(
            parent,
            "Could not open the file.\n\n"
            + exception.getMessage(),
            "Open Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}