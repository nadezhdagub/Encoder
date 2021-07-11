import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    public void main() throws Exception {
        String dir = "testDirectory/test folder/";
        Files.createDirectory(Paths.get(dir));
        Path file = Paths.get("testDirectory/test.txt");
        Files.copy(file, Paths.get(dir + "test.txt"));
        file = Paths.get(dir + "test.txt");
        String key1 = "123ABC";
        String key2 = "HHFLE7";
        String key3 = "FE54A4D";
        Path fileEncode = Paths.get(dir + "test-encode.txt");
        Path fileDecode = Paths.get(dir + "test.txt.crp");
        Path fileEDE = Paths.get(dir + "test-encode-2.txt.crp");
        Path fileEDED = Paths.get(dir + "test-encode-2.txt");

        Main.main(new String[]{"-c", key1, file.toString()});
        Main.main(new String[]{"-d", key1, fileDecode.toString(), "-o", fileEncode.toString()});
        Main.main(new String[]{"-c", key1, fileEncode.toString()});
        Main.main(new String[]{"-c", key3, fileEncode.toString(), "-o", fileEDE.toString()});
        Main.main(new String[]{"-d", key1, fileEDE.toString()});
        Main.main(new String[]{"-c", key3, file.toString(), "-o", dir + "test.noncrp"});
        Main.main(new String[]{"-d", key3, dir + "test.noncrp"});
        Path fileEncodeDecode = Paths.get(dir + "test-encode.txt.crp");

        {
            assertTrue(textEquals(file, fileEncode));
            assertFalse(textEquals(file, fileDecode)); //проверка соотвествия оригинального файла и зашифрованного
            assertTrue(textEquals(fileDecode, fileEncodeDecode));
            assertFalse(textEquals(fileEncodeDecode, fileEDE));
            assertFalse(textEquals(fileEncode, fileEDED));
            assertFalse(textEquals(file, fileEDED));

            Path finalFile = file;
            assertThrows(Exception.class, () -> Main.main(new String[]{"-h"}));
            assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"-с"}));
            assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"-t", key2, finalFile.toString()}));
            assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"-c", key2, finalFile.toString()}));
            assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"-c", key2}));
            assertThrows(FileNotFoundException.class, () -> Main.main(new String[]{"-c", key1, dir + "test-fail.txt"}));
            assertThrows(FileAlreadyExistsException.class, () -> Main.main(new String[]{"-c", key1, dir + "test.txt"}));
            assertThrows(FileAlreadyExistsException.class, () -> Main.main(new String[]{"-c", key3, finalFile.toString(), "-o", dir + "test.noncrp"}));
            assertThrows(FileNotFoundException.class, () -> Main.main(new String[]{"-c", key1, dir}));
            assertThrows(AccessDeniedException.class, () -> Main.main(new String[]{"-c", key1, "/bin/bash"}));
            assertThrows(FileAlreadyExistsException.class, () -> Main.main(new String[]{"-c", key1, dir + "test.txt", "-o", dir + "test.txt"}));
        }

        {
            MessageManager msg = new MessageManager();
            assertThrows(Exception.class, () -> msg.basicMsg(3, null));
            assertThrows(Exception.class, () -> msg.error(9, null));
            assertThrows(Exception.class, () -> msg.attention(2, null));
        }

    }

    boolean textEquals (Path file1, Path file2) throws IOException {
        FileReader fR1 = new FileReader(file1.toString());
        FileReader fR2 = new FileReader(file2.toString());
        BufferedReader reader1 = new BufferedReader(fR1);
        BufferedReader reader2 = new BufferedReader(fR2);
        String line1;
        String line2;
        boolean equal = true;
        while (equal && ((line1 = reader1.readLine()) != null) && ((line2 = reader2.readLine()) != null))
            if (!line1.equalsIgnoreCase(line2))
                equal = false;
        reader1.close();
        reader2.close();
        return equal;
    }

    @AfterEach
    void deleteFolderAndItsContent() throws IOException {
        Path folder = Paths.get("testDirectory/test folder");
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    //The idea is borrowed from https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java
}