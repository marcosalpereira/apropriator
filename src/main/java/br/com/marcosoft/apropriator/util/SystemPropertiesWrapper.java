package br.com.marcosoft.apropriator.util;


public class SystemPropertiesWrapper {
    private static final String MY_PROPERTIES_PREFIX = "br.com.marcosoft.apropriator.";

    public static String getCpf() {
        return getProperty("cpf");
    }

    public static String getProperty(String key) {
        return System.getProperty(decorateKey(key));
    }

    public static String getProperty(String key, String defaultValue) {
        return System.getProperty(decorateKey(key), defaultValue);
    }

    public static void setProperty(String key, String value) {
        System.setProperty(decorateKey(key), value);
    }

    private static String decorateKey(String key) {
        if (key.indexOf('.') == -1) {
            return MY_PROPERTIES_PREFIX + key;
        }
        return key;
    }

}
