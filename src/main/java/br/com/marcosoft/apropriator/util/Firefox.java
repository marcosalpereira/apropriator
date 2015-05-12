package br.com.marcosoft.apropriator.util;

import java.io.File;

public class Firefox {
    /**
     * Procurar localizar o profile default do firefox.
     * @return o profile default ou <code>null</code> se nao encontrar
     */
    protected String procurarProfileDefault() {
        File profileDir = getFirefoxProfilesFolder();
        for (File file : profileDir.listFiles()) {
            if (file.isDirectory() && file.getName().matches("^.+\\.[Dd]efault.*$")) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * Determinar a pasta onde ficam os profiles do firefox.
     * @return a pasta dos profiles
     */
    protected File getFirefoxProfilesFolder() {
        final String path;
        if (osIsLinux()) {
            path = "\\Dados de aplicativos\\Mozilla\\Firefox\\Profiles";
        } else {
            path = "/.mozilla/firefox";
        }
        return new File(System.getProperty("user.home") + path);
    }

    private boolean osIsLinux() {
        return File.pathSeparatorChar ==  '/';
    }

}
