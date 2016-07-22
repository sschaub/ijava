package javaic;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogFactory {
  static {
    final InputStream inputStream = LogFactory.class
        .getResourceAsStream("/logging.properties");
    try {
      LogManager.getLogManager().readConfiguration(inputStream);
    } catch (final IOException e) {
      Logger.getAnonymousLogger().severe(
          "Could not load default logging.properties file");
      Logger.getAnonymousLogger().severe(e.getMessage());
    }
  }

  public static Logger getLogger(Class cl) {
    return Logger.getLogger(cl.getPackage().getName());
  }
}
