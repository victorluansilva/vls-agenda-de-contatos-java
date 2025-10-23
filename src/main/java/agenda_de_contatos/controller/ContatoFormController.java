package agenda_de_contatos.controller;


import agenda_de_contatos.model.Contato;
import agenda_de_contatos.service.ContatoService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ContatoFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private DatePicker dataNascPicker;
    @FXML private TextField emailField;
    @FXML private TextField enderecoField;

    private Contato contato;
    private ContatoService contatoService;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        contatoService = new ContatoService();
    }

    public void setContato(Contato contato) {
        this.contato = contato;
        if (contato != null) {
            titleLabel.setText("Editar Contato");
            nomeField.setText(contato.getNome());
            telefoneField.setText(contato.getTelefone());
            if (contato.getDataNascimento() != null) {
                dataNascPicker.setValue(contato.getDataNascimento());
            }
            emailField.setText(contato.getEmail());
            enderecoField.setText(contato.getEndereco());
        } else {
            titleLabel.setText("Adicionar Novo Contato");
        }
    }

    @FXML
    private void handleSalvar() {
        boolean isNew = (contato == null);
        if (isNew) {
            contato = new Contato();
        }
        contato.setNome(nomeField.getText());
        contato.setTelefone(telefoneField.getText());
        if (dataNascPicker.getValue() != null) {
            contato.setDataNascimento(dataNascPicker.getValue());
        }
        contato.setEmail(emailField.getText());
        contato.setEndereco(enderecoField.getText());
        if (isNew) {
            contatoService.adicionarContato(contato);
        } else {
            contatoService.atualizarContato(contato);
        }

        if (mainController != null) {
            mainController.initialize();
        }
    }
}