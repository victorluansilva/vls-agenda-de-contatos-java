package agenda_de_contatos.controller;


import agenda_de_contatos.model.Contato;
import agenda_de_contatos.service.DatabaseService;
import agenda_de_contatos.service.ContatoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;


public class ContatoListController {

    @FXML
    private TableView<Contato> tableView;
    @FXML
    private TableColumn<Contato, String> colNome;
    @FXML
    private TableColumn<Contato, String> colTelefone;
    @FXML
    private TableColumn<Contato, String> colEmail;
    @FXML
    private TableColumn<Contato, Void> colAcoes;


    @FXML
    private Label statusLabel;
    @FXML
    private Button syncButton;

    private ContatoService contatoService;
    private ObservableList<Contato> obsContatos;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void initialize(){
        contatoService = new ContatoService();
        carregarDadosTabela();
    }

    private void atualizarStatusConexao(){
        boolean isConnected = DatabaseService.testarConexao();
        if (isConnected){
            statusLabel.setText("DB Status: connected");
            statusLabel.setStyle("-fx-text-fill: green");
            syncButton.setText("refresh");
            syncButton.setDisable(true);
            syncButton.setVisible(false);
        } else{
            statusLabel.setText("DB Status: offline");
            statusLabel.setStyle("-fx-text-fill: red");
            syncButton.setText("retry");
            syncButton.setDisable(false);
            syncButton.setVisible(true);
        }
    }
    @FXML
    private void handleSincronizar(){
        contatoService.sincronizarComBanco();
        carregarDadosTabela();

    }

    private void carregarDadosTabela(){
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        obsContatos = FXCollections.observableArrayList(contatoService.listarContatos());
        tableView.setItems(obsContatos);
        adicionarBotoesDeAcao();
        atualizarStatusConexao();
    }

    private void adicionarBotoesDeAcao() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox pane = new HBox(5, btnEditar, btnExcluir);

            {
                btnEditar.setOnAction(event -> {
                    Contato contato= getTableView().getItems().get(getIndex());
                    if (mainController != null) {
                        mainController.showEditForm(contato);
                    }
                });
                btnExcluir.setOnAction(event -> {
                    Contato contato= getTableView().getItems().get(getIndex());
                    contatoService.excluirContato(contato);
                    carregarDadosTabela();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleAdicionarContato() {
        if (mainController != null) {
            mainController.showEditForm(null);
        }
    }

    }