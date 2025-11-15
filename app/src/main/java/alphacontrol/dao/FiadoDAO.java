package alphacontrol.dao;

import alphacontrol.models.Fiado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
        String sql = "CREATE TABLE IF NOT EXISTS fiado ("
                 + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                 + "cliente_id INTEGER NOT NULL,"
                 + "venda_id INTEGER,"
                 + "valor REAL NOT NULL,"
                 + "data DATETIME NOT NULL,"
                 + "status TEXT NOT NULL,"
                 + "FOREIGN KEY (cliente_id) REFERENCES cliente(id),"
                 + "FOREIGN KEY (venda_id) REFERENCES venda(id)"
                 + ");";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela fiado: " + e.getMessage());
        }
    }

    public void inserir(Fiado fiado) throws SQLException {
        String sql = "INSERT INTO fiado (cliente_id, venda_id, valor, data, status) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, fiado.getClienteId());
            
            if (fiado.getVendaId() != null) {
                pstmt.setInt(2, fiado.getVendaId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setDouble(3, fiado.getValor());
            pstmt.setTimestamp(4, Timestamp.valueOf(fiado.getData()));
            pstmt.setString(5, fiado.getStatus());
            pstmt.executeUpdate();
        }
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
        String sql = "UPDATE fiado SET status = 'QUITADO' WHERE cliente_id = ? AND status = 'PENDENTE'";
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
        
        this.inserir(pagamento);
    }
    
    private Fiado instanciarFiado(ResultSet rs) throws SQLException {
        Fiado fiado = new Fiado();
        fiado.setId(rs.getInt("id"));
        fiado.setClienteId(rs.getInt("cliente_id"));
        fiado.setVendaId(rs.getObject("venda_id") != null ? rs.getInt("venda_id") : null);
        fiado.setValor(rs.getDouble("valor"));
        fiado.setData(rs.getTimestamp("data").toLocalDateTime());
        fiado.setStatus(rs.getString("status"));
        return fiado;
    }
}