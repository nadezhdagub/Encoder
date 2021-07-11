import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

class FileManager {
    MessageManager msg = new MessageManager();

    String creator (boolean approach, String pathOut, boolean custom) throws Exception {
        Path fOut = Paths.get(pathOut);
        if (custom)
            fOut = Paths.get(pathOut);
        else
            if (approach)
                fOut = Paths.get(fOut.toString() + ".crp");
            else
                if (fOut.getFileName().toString().contains(".crp"))
                    fOut = Paths.get(pathOut.substring(0, pathOut.lastIndexOf(".")));
                else {
                    fOut = Paths.get(pathOut + ".txt");
                    msg.attention(1, fOut.toString());
                }
        try {
            Files.createFile(fOut);
        }
        catch (FileAlreadyExistsException e) {
            msg.error(4, fOut.toString());
        }
        catch (AccessDeniedException e) {
            msg.error(5, fOut.getParent().toString());
        }
        return fOut.toString();
    }

    void reader (FlagManager flag) throws Exception {
        ArrayList<Integer> keyNumber = new ArrayList<>();
        long temp = flag.key;
        int j = 1;
        while (temp > 0) {
            keyNumber.add((int) (temp % (10 * j)));
            temp /= 10;
            j++;
        }
        FileInputStream fis = new FileInputStream(flag.pathIn);
        byte[] data = fis.readAllBytes();
        byte[] text = Cipher.coding(data, keyNumber);
        writer(flag.pathOut, text);
        fis.close();
        if (flag.approach)
            msg.basicMsg(1, null);
        else
            msg.basicMsg(2, null);
    }

    void writer (String path, byte[] dataByte) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, true);
        fos.write(dataByte, 0, dataByte.length);
        fos.close();
    }
}
