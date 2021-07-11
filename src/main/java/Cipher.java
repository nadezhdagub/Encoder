import java.util.ArrayList;

class Cipher {
    static byte[] coding (byte[] data, ArrayList<Integer> keyNumber) {
        byte[] text = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            text[i] = (byte) (data[i] ^ keyNumber.get(i % keyNumber.size()));
        return text;
    }
}
