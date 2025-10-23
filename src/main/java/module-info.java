module agenda_de_contatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires java.dotenv;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens agenda_de_contatos to javafx.fxml;
    opens agenda_de_contatos.controller to javafx.fxml;
    opens agenda_de_contatos.model to javafx.base;
    exports agenda_de_contatos;
}
