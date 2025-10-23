package agenda_de_contatos.controller;


import agenda_de_contatos.model.Contato;
import agenda_de_contatos.service.ContatoService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ContatoFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private DatePicker dataNascPicker;
    @FXML private TextField emailField;
    @FXML private TextField enderecoField;


    private Stage stage;
    private Contato contato;
    private ContatoService contatoService;

    public void initialize() {
        contatoService = new ContatoService();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
        if (contato != null) {
            titleLabel.setText("Editar Usuário");
            nomeField.setText(contato.getNome());
            telefoneField.setText(contato.getTelefone());
            if (contato.getDataNascimento() != null) {
                dataNascPicker.setValue(contato.getDataNascimento());
            }
            emailField.setText(contato.getEmail());
            enderecoField.setText(contato.getEndereco());
        } else {
            titleLabel.setText("Adicionar Usuário");
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
        stage.close();
    }
}