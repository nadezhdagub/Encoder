import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;


public class MessageManager {
    Logger log = LogManager.getLogger(MessageManager.class.getName());

    void basicMsg (int code, String remark) throws Exception {
        switch (code) {
            case (0):
                log.info("The program started with arguments: " + remark);
                break;
            case (1):
                System.out.println("Encoding completed");
                log.info("Encoding completed");
                break;
            case (2):
                System.out.println("Decoding completed");
                log.info("Decoding completed");
                break;
            default:
                throw new Exception("There is no such message code");
        }
    }

    void error (int code, String remark) throws Exception {
        String help = "Valid flags are:\n" +
                "-c encryption\n" +
                "-d decryption\n" +
                "This is followed by specifying the encryption key. The key is set in hexadecimal format.\n" +
                "-o (optional) specify the location of the output file.\n" +
                "By default, output files are created in the directory of the incoming file.\n" +
                "-h help";
        String author = "\nImplementation of ciphxor\n" +
                "Version 1.0\n" +
                "Mikhail Shomov(mikle@shomov.spb.ru), student of St. Petersburg Polytechnic University";
        switch (code) {
            case (0):
                log.info("Help called");
                throw new Exception(help + author);
            case (1):
                log.error("Invalid arguments " + remark);
                throw new IllegalArgumentException("Error in the use of commands. \n" + help);
            case (2):
                log.error("Incorrect cipher key " + remark);
                throw new IllegalArgumentException("Incorrect cipher key. The key is set in hexadecimal format.");
            case (3):
                log.error("The input file does not exist " + remark);
                throw new FileNotFoundException("The input file does not exist");
            case (4):
                log.error("File " + remark + " already exists");
                throw new FileAlreadyExistsException("A file with this name already exists in this directory. Please use the -o argument.");
            case (5):
                log.error("Access to the folder " + remark + " is forbidden");
                throw new AccessDeniedException("Access to the folder " + remark + " is forbidden");
            default:
                throw new Exception("There is no such error code");
        }
    }

    void attention (int code, String remark) throws Exception {
        switch (code) { //не взирая на предупреждения IDEA принял решение использовать конструкцию switch для возможности масштабирования
            case (1):
                System.out.println("Attention! You are trying to decrypt a file without the .crp extension. Possible failure.");
                log.warn("Attempt to decrypt non .crp " + remark);
                break;
            default:
                throw new Exception("There is no such attention code");
        }
    }
}
