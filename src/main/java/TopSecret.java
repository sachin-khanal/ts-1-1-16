public class TopSecret {
    public static void main(String[] args) {
        FileHandler handler = new FileHandler();
        ProgramController controller = new ProgramController(handler);

        String output = controller.run(getArgs(args));
        System.out.println(output);
    }

    public static String[] getArgs(String[] args) {
        return args;
    }
}
