package alphacontrol.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import alphacontrol.models.MovimentacaoCaixa;
import javax.swing.JOptionPane;

public class MovimentacaoCaixaDAO {

    private final Connection connection;

    public MovimentacaoCaixaDAO(Connection connection) {
        this.connection = connection;
        if (this.connection == null) {
            throw new RuntimeException("Erro: A conexão com o banco de dados é nula em MovimentacaoCaixaDAO.");
        }
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS movimentacaocaixa ("
                + "idMovimentacaoCaixa INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "nome TEXT NOT NULL,"
                + "tipo TEXT NOT NULL,"
                + "valor REAL NOT NULL,"
                + "data TEXT NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela movimentacaocaixa: " + e.getMessage());
        }
    }

    public void inserir(MovimentacaoCaixa movimentacao) {
        String sql = "INSERT INTO movimentacaocaixa (nome, tipo, valor, data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, movimentacao.getNome());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getValor());
            stmt.setString(4, movimentacao.getData());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(MovimentacaoCaixa movimentacao) {
        String sql = "UPDATE movimentacaocaixa SET nome=?, tipo=?, valor=?, data=? WHERE idMovimentacaoCaixa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, movimentacao.getNome());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getValor());
            stmt.setString(4, movimentacao.getData());
            stmt.setInt(5, movimentacao.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM movimentacaocaixa WHERE idMovimentacaoCaixa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MovimentacaoCaixa> listar() {
        List<MovimentacaoCaixa> lista = new ArrayList<>();

        String sql = "SELECT * FROM movimentacaocaixa " +
                "ORDER BY STR_TO_DATE(data, '%d/%m/%Y') DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MovimentacaoCaixa e = new MovimentacaoCaixa(
                        rs.getInt("idMovimentacaoCaixa"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("valor"),
                        rs.getString("data"));
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erro ao listar movimentações: " + e.getMessage());
        }

        return lista;
    }
}