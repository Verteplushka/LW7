package mainProgramms;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Hash {
    public static String getHash(String data) {
        try {
            // Создание экземпляра MessageDigest с алгоритмом MD2
            MessageDigest md = MessageDigest.getInstance("MD2");

            // Преобразование данных в массив байтов
            byte[] dataBytes = data.getBytes();

            // Вычисление хэша
            byte[] hashBytes = md.digest(dataBytes);

            // Преобразование байтов хэша в шестнадцатеричную строку
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
