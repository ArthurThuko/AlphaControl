package alphacontrol.dao;

import alphacontrol.models.Fiado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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

    public void adicionarFiado(Fiado fiado) throws SQLException {
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
    
}