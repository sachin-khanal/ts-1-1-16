import java.util.List;

public class ProgramController {

    private final FileHandler fileHandler;

    public ProgramController(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // Returns output instead of printing directly
    public String run(String[] args) {
        List<String> availableFiles = fileHandler.getFiles();

        // No arguments → return list of files
        if (args.length == 0) {
            StringBuilder list = new StringBuilder();
            for (int i = 0; i < availableFiles.size(); i++) {
                list.append((i + 1) + ". " + availableFiles.get(i) + "\n");
            }
            return list.toString();
        }
        int index;
        try {
            index = Integer.parseInt(args[0]) - 1;
        } catch (NumberFormatException e) {
            return "Error: file needs to be an integer";
        }
        if (index < 0 || index >= availableFiles.size()) {
            return "Error: out of range";
        }
        String selectedFile = availableFiles.get(index);
        return fileHandler.readFile(selectedFile);
    }
}