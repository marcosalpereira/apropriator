package br.com.marcosoft.apropriator.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.marcosoft.apropriator.util.Util;

public class ApropriationFile extends BaseModel {
    private final File inputFile;

    private final List<TaskRecord> tasksRecords = new ArrayList<TaskRecord>();

    private final Config config = new Config();

    private TasksHandler tasksHandler;

    public ApropriationFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public TasksHandler getTasksHandler() {
        if (tasksHandler == null) {
            tasksHandler = new TasksHandler(tasksRecords);
        }
        return tasksHandler;
    }

    public Config getConfig() {
        return config;
    }

    public class Config {
        private static final String BROWSER_FIREFOX_PROFILE = "browser.firefox.profile";

        private static final String BROWSER_TYPE = "browser.type";

        private static final String ALM_URL = "alm.url";

        private static final String SGI_MACROS_VERSION = "macros.version";

        private static final String LOGIN_CPF = "login.cpf";

        public static final String LOGIN_SALVAR_SENHA = "login.salvar.senha";

        public void setProperty(String key, String value) {
            if (key == null || key.trim().length() == 0)
                return;

            final String newKeyName = enforceNewKeyName(key);

            System.setProperty(newKeyName, value);
        }

        private String enforceNewKeyName(String key) {
            if ("cpf".equals(key)) {
                return LOGIN_CPF;

            } else if ("version".equals(key)) {
                return SGI_MACROS_VERSION;

            } else if ("firefoxProfile".equals(key)) {
                return BROWSER_FIREFOX_PROFILE;
            }
            return key;
        }

        public String getPlanilhaDir() {
            final String parent = inputFile.getParent();
            if (parent == null)
                return ".";
            return parent;
        }

        public String getMacrosVersion() {
            return System.getProperty(SGI_MACROS_VERSION);
        }

        public String getCpf() {
            return System.getProperty(LOGIN_CPF);
        }

        public String getUrlAlm() {
            return System.getProperty(ALM_URL, "https://alm.serpro/");
        }

        public String getBrowserType() {
            return System.getProperty(BROWSER_TYPE, "firefox");
        }

        public String getFirefoxProfile() {
            final String firefoxProfile = System.getProperty(BROWSER_FIREFOX_PROFILE,
                "default");
            return checkCompatibilidadeFirefoxProfile(firefoxProfile);
        }

        private String checkCompatibilidadeFirefoxProfile(String firefoxProfile) {
            return substringAfterLast(firefoxProfile, File.separatorChar);
        }

        /**
         * Retorna a substring da posicao do ultimo separador encontrado ate o
         * final. Ex: substringAfterLast("/a/b/c", "/") --> "c" Ex:
         * substringAfterLast("a.b", ".") --> "b" Ex: substringAfterLast("a",
         * ".") --> "a"
         * @param string string
         * @param separator separator
         * @return a substring
         */
        private String substringAfterLast(String string, char separator) {
            if (string == null) {
                return null;
            }
            final int lastPos = string.lastIndexOf(separator);
            if (lastPos == -1) {
                return string;
            }
            return string.substring(lastPos + 1);
        }

        public int getMinimoMinutosApropriacaoDia() {
            final String minutos =
                System.getProperty(getChaveMinimoMinutosApropriacaoDia());
            return Util.parseInt(minutos, 480 - 80);
        }

        public String getChaveMinimoMinutosApropriacaoDia() {
            return "minutosApropriacaoDia.minimo";
        }

        public String getChaveMaximoMinutosApropriacaoDia() {
            return "minutosApropriacaoDia.maximo";
        }

        public int getMaximoMinutosApropriacaoDia() {
            final String minutos =
                System.getProperty(getChaveMaximoMinutosApropriacaoDia());
            return Util.parseInt(minutos, 480);
        }

    }

    public File getInputFile() {
        return inputFile;
    }

    public void adicionarTasksRecord(TaskRecord taskRecord) {
        tasksRecords.add(taskRecord);
    }

}
