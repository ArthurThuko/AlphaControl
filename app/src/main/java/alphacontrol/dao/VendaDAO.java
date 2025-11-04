package alphacontrol.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class VendaDAO {

    private Connection conexao;

    public VendaDAO(Connection conexao) {
        this.conexao = conexao;
        if (this.conexao == null) {
            throw new RuntimeException("Erro: A conexão com o banco de dados é nula.");
        }
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS venda ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "data_venda DATETIME NOT NULL,"
                + "valor_total REAL NOT NULL"
                + ");";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela venda: " + e.getMessage());
        }
    }
}