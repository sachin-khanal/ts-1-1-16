public class Command {
    public enum Type {
        list_files,
        show_file,
        error
    }

    private Type type;
    private String fileNumber;
    private String key;
    private String errorMessage;

    public Command(Type type) {
        this.type = type;
    }

    public static Command list() {
        return new Command(Type.list_files);
    }

    public static Command show(String fileNumber, String key) {
        Command c = new Command(Type.show_file);
        c.fileNumber = fileNumber;
        c.key = key;
        return c;
    }

    public static Command error(String msg) {
        Command c = new Command(Type.error);
        c.errorMessage = msg;
        return c;
    }

    public Type getType() {
        return type;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public String getKey() {
        return key;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
