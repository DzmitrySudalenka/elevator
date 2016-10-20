package by.epamlab.utils;
import java.util.ResourceBundle;

import by.epamlab.Constants;

/**
 * @author Dzmitry Sudalenka
 *
 */
public final class ConfigReader {
  private ConfigReader() { }
  private static final ResourceBundle CONFIG_RESOURCE_BUNDLE =
      ResourceBundle.getBundle(Constants.CONFIG);
  /**
   * @param key key
   * @return return
   */
  public static int getParamVal(final String key) {
    return Integer.parseInt(CONFIG_RESOURCE_BUNDLE.getString(key));
  }
}
