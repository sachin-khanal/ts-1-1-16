import java.util.List;

public class ProgramController {

    private final CommandLineInterface commandLine;
    private final FileHandler fileHandler;

    public ProgramController(CommandLineInterface commandLine, FileHandler fileHandler) {
        this.commandLine = commandLine;
        this.fileHandler = fileHandler;
    }

    public void run(String[] args) {
        UserRequest request = commandLine.parseArguments(args);
        List<String> availableFiles = fileHandler.findFiles();

        if (request.getFileNumber() == null) {
            displayFileList(availableFiles);
            return;
        }
        fileSelection(request, availableFiles);
    }

    private void displayFileList(List<String> files) {
        for (int i = 0; i < files.size(); i++) {
            System.out.println((i + 1) + ". " + files.get(i));
        }
    }

    private void fileSelection(UserRequest request, List<String> files) {
        int index;

        try {
            index = Integer.parseInt(request.getFileNumber()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Error: file needs to be integers");
            return;
        }

        if (index < 0 || index >= files.size()) {
            System.out.println("Error: Out of range");
            return;
        }

        String selectedFile = files.get(index);
        String contents = fileHandler.readFile(selectedFile);

        System.out.println(contents);
    }
}
