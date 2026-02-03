package alphacontrol.dao;

import alphacontrol.models.Fiado;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FiadoDAO {

    private Connection conexao;

    public FiadoDAO(Connection conexao) {
        this.conexao = conexao;
        if (this.conexao == null) {
            throw new RuntimeException("Erro: A conexão com o banco de dados é nula.");
        }
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS fiado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                valor REAL NOT NULL,
                data TEXT NOT NULL,
                status TEXT NOT NULL,
                FOREIGN KEY (cliente_id) REFERENCES clientes(id)
            )
        """;

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela fiado", e);
        }
    }

    public int inserir(Fiado fiado) throws SQLException {

        String sql = "INSERT INTO fiado (cliente_id, valor, data, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexao.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, fiado.getClienteId());
            pstmt.setDouble(2, fiado.getValor());
            pstmt.setString(3, fiado.getData().toString()); // LocalDateTime → TEXT
            pstmt.setString(4, fiado.getStatus());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public List<Fiado> listarPorCliente(int clienteId) throws SQLException {
        List<Fiado> fiados = new ArrayList<>();
        String sql = "SELECT * FROM fiado WHERE cliente_id = ? ORDER BY data DESC";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fiados.add(instanciarFiado(rs));
                }
            }
        }
        return fiados;
    }

    public void quitarTudo(int clienteId) throws SQLException {
        String sql = """
            UPDATE fiado
            SET status = 'QUITADO'
            WHERE cliente_id = ?
              AND status = 'PENDENTE'
        """;

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
        }
    }

    public void pagarParcial(int clienteId, double valorPago) throws SQLException {
        Fiado pagamento = new Fiado();
        pagamento.setClienteId(clienteId);
        pagamento.setValor(valorPago);
        pagamento.setData(LocalDateTime.now());
        pagamento.setStatus("PAGAMENTO");

        inserir(pagamento);
    }

    private Fiado instanciarFiado(ResultSet rs) throws SQLException {
        Fiado fiado = new Fiado();
        fiado.setId(rs.getInt("id"));
        fiado.setClienteId(rs.getInt("cliente_id"));
        fiado.setValor(rs.getDouble("valor"));
        fiado.setData(LocalDateTime.parse(rs.getString("data"))); // TEXT → LocalDateTime
        fiado.setStatus(rs.getString("status"));
        return fiado;
    }
}