/**
 * Command Line Utility
 */

public class TopSecret {
    public static void main(String[] args) {
        Command command = parseArguments(args);
        switch (command.getType()) {
            case list_files:
                System.out.println("Listing files...");
                break;
            case show_file:
                showFile(command.getFileNumber(), command.getKey());
                break;
            case error:
                System.out.println("error" + command.getErrorMessage());
                break;
        }
    }

    public static Command parseArguments(String[] args) {
        if (args.length == 0) {
            return Command.list();
        }
        if (args.length > 2) {
            return Command.error("too many arguments.");
        }
        String fileNumber = args[0];

        if (!fileNumber.matches("\\d{2}")) {
            return Command.error("invalid file number");
        }
        String key = "default";
        if (args.length == 2) {
            key = args[1];
        }
        return Command.show(fileNumber, key);
    }

    private static void listFiles() {
        try {
            Map<String, Path> map = discoverNumberedFiles();
            if (map.isEmpty()) {
                System.out.println("No files found in " + DATA_DIR.toAbsolutePath());
                return;
            }
            for (String num : map.keySet()) {
                System.out.println(num + " " + map.get(num).getFileName().toString());
            }
        } catch (IOException e) {
            System.out.println("error: could not list files: " + e.getMessage());
        }
    }

    private static void showFile(String fileNumber, String key) {
        try {
            Map<String, Path> map = discoverNumberedFiles();
            Path p = map.get(fileNumber);

            if (p == null) {
                System.out.println("error: file number " + fileNumber + " not found.");
                return;
            }

            String contents = Files.readString(p);

            // If your team uses ciphered files, decipher here.
            // If your team uses plain text files, just print contents.
            String output = Cipher.decipher(contents, key);

            System.out.print(output);
        } catch (IOException e) {
            System.out.println("error: could not read file: " + e.getMessage());
        }
    }
}
