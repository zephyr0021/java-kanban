package exceptions;

import java.io.File;
import java.util.NoSuchElementException;

public class ManagerSaveException extends NoSuchElementException {
    File file;

    public ManagerSaveException(String message, File file) {
        super(message);
        this.file = file;
    }

    public String getDetailMessage() {
        return String.format("%s в файл - %s", getMessage(), file.getAbsolutePath());
    }

}
