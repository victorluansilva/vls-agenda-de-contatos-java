package agenda_de_contatos.controller;

import agenda_de_contatos.service.ConfigService;
import agenda_de_contatos.service.MysqlRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfigController {

    @FXML private ChoiceBox<String> storageChoiceBox;
    @FXML private TextField hostField;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button testButton;
    @FXML private Label configStatusLabel;

    @FXML private Label hostLabel;
    @FXML private Label userLabel;
    @FXML private Label passLabel;

    private MainController mainController;
    private ConfigService configService;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configService = new ConfigService();
        storageChoiceBox.setItems(FXCollections.observableArrayList("SQLite (Local)", "MySQL (Rede)"));
        loadConfigData();

        storageChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            toggleMysqlFields(newVal.equals("MySQL (Rede)"));
        });
    }

    private void loadConfigData() {
        String storageType = configService.getStorageType();
        storageChoiceBox.setValue(storageType.equals("mysql") ? "MySQL (Rede)" : "SQLite (Local)");

        hostField.setText(configService.getMysqlUrl());
        userField.setText(configService.getMysqlUser());
        passField.setText(configService.getMysqlPass());

        toggleMysqlFields(storageType.equals("mysql"));
    }

    private void toggleMysqlFields(boolean enable) {
        hostLabel.setVisible(enable);
        hostField.setVisible(enable);
        userLabel.setVisible(enable);
        userField.setVisible(enable);
        passLabel.setVisible(enable);
        passField.setVisible(enable);
        testButton.setVisible(enable);
    }

    @FXML
    private void handleSalvar() {
        String storageType = storageChoiceBox.getValue().equals("MySQL (Rede)") ? "mysql" : "sqlite";
        configService.setStorageType(storageType);

        configService.setMysqlUrl(hostField.getText());
        configService.setMysqlUser(userField.getText());
        configService.setMysqlPass(passField.getText());

        configService.saveConfig();

        configStatusLabel.setText("Configurações salvas! Reinicie o app para aplicar.");

        if(mainController != null) {
            mainController.initialize();
        }
    }

    @FXML
    private void handleTestar() {
        ConfigService testConfig = new ConfigService();
        testConfig.setMysqlUrl(hostField.getText());
        testConfig.setMysqlUser(userField.getText());
        testConfig.setMysqlPass(passField.getText());

        MysqlRepository testRepo = new MysqlRepository(testConfig);
        if (testRepo.testarConexao()) {
            configStatusLabel.setText("Conexão bem-sucedida!");
            configStatusLabel.setStyle("-fx-text-fill: green");
        } else {
            configStatusLabel.setText("Falha na conexão.");
            configStatusLabel.setStyle("-fx-text-fill: red");
        }
    }
}