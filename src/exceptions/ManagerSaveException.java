package exceptions;

import java.io.File;

public class ManagerSaveException extends Error {
    File file;
    public ManagerSaveException(String message, File file) {
        super(message);
        this.file = file;
    }

    public String getDetailMessage() {
        return String.format("%s в файл - %s", getMessage(), file.getAbsolutePath());
    }

}
