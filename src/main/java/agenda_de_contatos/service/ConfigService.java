package agenda_de_contatos.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigService {

    private final String CONFIG_FILE = "config.properties";
    private Properties props = new Properties();

    public ConfigService() {
        loadConfig();
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            props.setProperty("storage.type", "sqlite");
            props.setProperty("mysql.url", "jdbc:mysql://localhost:3306/db_agenda_de_contatos");
            props.setProperty("mysql.user", "root");
            props.setProperty("mysql.pass", "");
            saveConfig();
        }
    }

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Configurações da Agenda de Contatos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStorageType() {
        return props.getProperty("storage.type", "sqlite");
    }

    public void setStorageType(String type) {
        props.setProperty("storage.type", type);
    }

    public String getMysqlUrl() {
        return props.getProperty("mysql.url");
    }

    public void setMysqlUrl(String url) {
        props.setProperty("mysql.url", url);
    }

    public String getMysqlUser() {
        return props.getProperty("mysql.user");
    }

    public void setMysqlUser(String user) {
        props.setProperty("mysql.user", user);
    }

    public String getMysqlPass() {
        return props.getProperty("mysql.pass");
    }

    public void setMysqlPass(String pass) {
        props.setProperty("mysql.pass", pass);
    }
}