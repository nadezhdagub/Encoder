public class Main {
    public static void main(String[] args) throws Exception {
        FlagManager flag = new FlagManager();
        flag.parsing(args);
        FileManager file = new FileManager();
        file.reader(flag);
    }
}