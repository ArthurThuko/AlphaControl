package alphacontrol.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                + "data DATE NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela movimentacaocaixa: " + e.getMessage());
        }
    }

    // ============================
    // VALIDAÇÕES DOS CAMPOS
    // ============================
    private boolean validar(MovimentacaoCaixa mov) {

        // Nome
        if (mov.getNome() == null || mov.getNome().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo NOME é obrigatório!");
            return false;
        }
        if (mov.getNome().length() < 2) {
            JOptionPane.showMessageDialog(null, "O nome deve ter pelo menos 2 caracteres!");
            return false;
        }

        // Valor
        if (mov.getValor() <= 0) {
            JOptionPane.showMessageDialog(null, "O valor deve ser maior que zero!");
            return false;
        }

        // Data
        if (mov.getData() == null || mov.getData().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo DATA é obrigatório!");
            return false;
        }

        if (!validarDataBR(mov.getData())) {
            JOptionPane.showMessageDialog(null, "A data deve estar no formato DD/MM/AAAA!");
            return false;
        }

        return true;
    }

    private boolean validarDataBR(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // garante validade real
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String paraDataSql(String dataBR) {
        try {
            SimpleDateFormat br = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd");
            return sql.format(br.parse(dataBR));
        } catch (Exception e) {
            return null;
        }
    }

    public void inserir(MovimentacaoCaixa movimentacao) {

        if (!validar(movimentacao)) {
            return; // Evita inserir dados inválidos
        }

        String sql = "INSERT INTO movimentacaocaixa (nome, tipo, valor, data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, movimentacao.getNome());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getValor());
            stmt.setString(4, paraDataSql(movimentacao.getData()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inserir movimentação: " + e.getMessage());
        }
    }

    public void atualizar(MovimentacaoCaixa movimentacao) {

        if (!validar(movimentacao)) {
            return; // Evita atualizar com dados inválidos
        }

        String sql = "UPDATE movimentacaocaixa SET nome=?, tipo=?, valor=?, data=? WHERE idMovimentacaoCaixa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, movimentacao.getNome());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getValor());
            stmt.setString(4, paraDataSql(movimentacao.getData()));
            stmt.setInt(5, movimentacao.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar movimentação: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM movimentacaocaixa WHERE idMovimentacaoCaixa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao deletar movimentação: " + e.getMessage());
        }
    }

    public List<MovimentacaoCaixa> listar() {
        List<MovimentacaoCaixa> lista = new ArrayList<>();

        String sql = "SELECT * FROM movimentacaocaixa ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat brFormat = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                String dataSql = rs.getString("data");
                String dataBR = "";

                try {
                    dataBR = brFormat.format(sqlFormat.parse(dataSql));
                } catch (ParseException ex) {
                    dataBR = dataSql; // fallback
                }

                MovimentacaoCaixa e = new MovimentacaoCaixa(
                        rs.getInt("idMovimentacaoCaixa"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("valor"),
                        dataBR);

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