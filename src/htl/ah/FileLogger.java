package htl.ah;

import java.io.IOException;
import java.util.logging.*;

public class FileLogger {
    private static final Logger logger = Logger.getLogger(FileLogger.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("C:/LOGGING/app.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }
    
	public static FileHandler getFileHandler() {
		return fileHandler;
	}
}
