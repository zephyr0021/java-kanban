package exceptions;

import java.io.File;
import java.util.NoSuchElementException;

public class ManagerLoadFromFileException extends NoSuchElementException {
    File file;

    public ManagerLoadFromFileException(String message, File file) {
        super(message);
        this.file = file;
    }

    public String getDetailMessage() {
        return String.format("%s из файла - %s", getMessage(), file.getAbsolutePath());
    }
}
