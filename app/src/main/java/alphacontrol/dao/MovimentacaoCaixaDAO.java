package alphacontrol.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import alphacontrol.Conexao;
import alphacontrol.models.MovimentacaoCaixa;

public class MovimentacaoCaixaDAO {

    public void inserir(MovimentacaoCaixa movimentacao) {
        String sql = "INSERT INTO movimentacaocaixa (nome, tipo, valor, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MovimentacaoCaixa> listar() {
        List<MovimentacaoCaixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacaocaixa ORDER BY data DESC";

        try (Connection conn = Conexao.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MovimentacaoCaixa e = new MovimentacaoCaixa(
                        rs.getInt("idMovimentacaoCaixa"), // ← pega o ID corretamente
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("valor"),
                        rs.getString("data"));
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
