package exceptions;
import java.io.File;

public class ManagerLoadFromFileException extends Error {
    File file;
    public ManagerLoadFromFileException(String message, File file) {
        super(message);
        this.file = file;
    }

    public String getDetailMessage() {
        return String.format("%s из файла - %s", getMessage(), file.getAbsolutePath());
    }
}
