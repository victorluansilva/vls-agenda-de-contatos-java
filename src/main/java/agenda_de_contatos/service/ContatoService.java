package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContatoService {

    private static final List<Contato> contatos = new ArrayList<>();
    private static boolean dbLoaded = false;

    public ContatoService() {
        if (!dbLoaded) {
            carregarContatosDoBanco();
            dbLoaded = true;
        }
    }

    private void carregarContatosDoBanco() {
        contatos.clear();
        String sql = "SELECT * FROM contatos";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contato contato = new Contato();
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setTelefone(rs.getString("telefone"));
                java.sql.Date sqlDate = rs.getDate("dataNasc");
                if (sqlDate != null) {
                    contato.setDataNascimento(sqlDate.toLocalDate());
                    //contato.setIdade();
                } else {
                    contato.setDataNascimento(null);
                }
                contato.setEmail(rs.getString("email"));
                contato.setEndereco(rs.getString("endereco"));
                contatos.add(contato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contato> listarContatos() {
        this.carregarContatosDoBanco();
        return contatos;
    }

    public void adicionarContato(Contato contato) {
        String sql = "INSERT INTO contatos(nome, telefone, dataNasc, email, endereco) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            if (contato.getDataNascimento() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(contato.getDataNascimento()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, contato.getEmail());
            stmt.setString(5, contato.getEndereco());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contato.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do contato criado.");
                }
            }
            contatos.add(contato);

        } catch (SQLException e) {
            showAlert("Operação em Memória", "Conexão indisponível. O novo contato foi salvo apenas nesta sessão.");
            contato.setId(-(contatos.size() + 1));
            contatos.add(contato);
            e.printStackTrace();
        }
    }

    public void atualizarContato(Contato contato) {
        int indexNaLista = -1;
        for (int i = 0; i < contatos.size(); i++) {
            if (contatos.get(i).getId() == contato.getId()) {
                indexNaLista = i;
                break;
            }
        }

        if (contato.getId() < 0) {
            if (indexNaLista != -1) contatos.set(indexNaLista, contato);
            showAlert("Operação em Memória", "Contato atualizado na sessão. Sincronize para salvar no banco.");
            return;
        }

        String sql = "UPDATE contatos SET nome = ?, telefone = ?, dataNasc = ?,email = ?, endereco = ? WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            if (contato.getDataNascimento() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(contato.getDataNascimento()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, contato.getEmail());
            stmt.setString(5, contato.getEndereco());
            stmt.setInt(6, contato.getId());

            stmt.executeUpdate();

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0 && indexNaLista != -1) {
                contatos.set(indexNaLista, contato);
            }

        } catch (SQLException e) {
            if (indexNaLista != -1) contatos.set(indexNaLista, contato);
            showAlert("Operação em Memória", "Conexão indisponível. O contato foi atualizado apenas nesta sessão.");
            e.printStackTrace();
        }
    }


    public void excluirContato(Contato contato) {
        boolean removed = contatos.remove(contato);

        if (contato.getId() < 0) {
            if(removed) showAlert("Operação em Memória", "Contato offline removido da sessão.");
            return;
        }

        String sql = "DELETE FROM contatos WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contato.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if(removed) showAlert("Operação em Memória", "Conexão indisponível. O contato foi excluído apenas desta sessão.");
            e.printStackTrace();
        }
    }

    public void sincronizarComBanco() {
        if (!DatabaseService.testarConexao()) {
            showAlert("Sincronização Falhou", "Não foi possível reconectar ao banco de dados.");
            return;
        }

        List<Contato> contatosParaSincronizar = contatos.stream()
                .filter(u -> u.getId() < 0)
                .collect(Collectors.toList());

        if (!contatosParaSincronizar.isEmpty()) {
            String sql = "INSERT INTO contatos(nome, telefone, dataNasc, email, endereco) VALUES(?, ?, ?, ?, ?)";
            int sucessoCount = 0;
            try (Connection conn = DatabaseService.getConnection()) {
                for (Contato contato : contatosParaSincronizar) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                        stmt.setString(1, contato.getNome());
                        stmt.setString(2, contato.getTelefone());
                        if (contato.getDataNascimento() != null) {
                            stmt.setDate(3, java.sql.Date.valueOf(contato.getDataNascimento()));
                        } else {
                            stmt.setNull(3, Types.DATE);
                        }
                        stmt.setString(4, contato.getEmail());
                        stmt.setString(5, contato.getEndereco());
                        stmt.executeUpdate();

                        sucessoCount++;
                    } catch (SQLException e) {
                        System.err.println("Erro ao sincronizar contato " + contato.getNome() + ": " + e.getMessage());
                    }
                }
                showAlert("Sincronização", sucessoCount + " contato(s) foram salvos no banco de dados.");
            } catch (SQLException e) {
                showAlert("Erro na Sincronização", "Ocorreu um erro geral durante a sincronização.");
                e.printStackTrace();
            }
        } else {
            showAlert("Reconectado", "Conexão com o banco de dados restabelecida. Nenhum dado pendente.");
        }
        carregarContatosDoBanco();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
