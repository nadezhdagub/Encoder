import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class FlagManager {
    public boolean approach;
    public long key;
    public String pathIn;
    public String pathOut;
    MessageManager msg = new MessageManager();

    @Option(name = "-h")
    boolean help;

    @Option(name = "-c")
    String keyEncryption = "";

    @Option(name = "-d")
    String keyDecryption = "";

    @Argument()
    String inputFileName;

    @Option(name = "-o")
    String outputFileName;

    void parsing(String[] args) throws Exception {
        final CmdLineParser parser = new CmdLineParser(this);
        msg.basicMsg(0, String.join(" ", args));
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            msg.error(1, String.join(" ", args));
        }
        if (help)
            msg.error(0, null);
        if (!keyEncryption.equals(""))
            setKey(keyEncryption);
        else if (!keyDecryption.equals(""))
            setKey(keyDecryption);
        else
            msg.error(1, String.join(" ", args));
        approach = !keyEncryption.equals("");
        setPathIn(inputFileName);
        if (outputFileName != null)
            pathOut = outputFileName;
        else
            pathOut = pathIn;
        setPathOut(outputFileName != null);
    }

    void setKey (String inputKey) throws Exception {
        try {
            key = Integer.parseInt (inputKey, 16);
        }
        catch (NumberFormatException e) {
            msg.error(2, inputKey);
        }
    }

    void setPathIn (String path) throws Exception {
        Path fInput = Paths.get(path);
        if (Files.notExists(fInput.toAbsolutePath()) || !Files.isRegularFile(fInput.toAbsolutePath()))
            msg.error(3, fInput.toAbsolutePath().toString());
        pathIn = path;
    }

    void setPathOut (boolean custom) throws Exception {
        FileManager file = new FileManager();
        pathOut = file.creator(approach, pathOut, custom);
    }
}
