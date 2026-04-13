package alphacontrol.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import alphacontrol.models.Fiado;

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
                 + "FOREIGN KEY (venda_id) REFERENCES venda(venda_id)"
                 + ");";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela fiado: " + e.getMessage());
        }
    }

    public void realizarPagamentoFiado(int clienteId, double valorPago, String nomeCliente) throws Exception {
        String sqlCaixa = "INSERT INTO movimentacaocaixa (nome, tipo, valor, data) VALUES (?, 'ENTRADA', ?, ?)";
        String sqlDebito = "UPDATE cliente SET debito = debito - ? WHERE id = ?";
        // Pega as dívidas PENDENTES ordenadas do valor mais alto para o mais baixo
        String sqlBuscaFiados = "SELECT id, valor FROM fiado WHERE cliente_id = ? AND status = 'PENDENTE' ORDER BY valor DESC";
        String sqlAtualizaFiado = "UPDATE fiado SET valor = ?, status = ? WHERE id = ?";

        try (Connection conn = alphacontrol.conexao.Conexao.getConexao()) {
            conn.setAutoCommit(false); // Trava o banco de dados (Transação) para não ter erro no meio

            try {
                // 1. REGISTRA A ENTRADA NO FLUXO DE CAIXA
                try (PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa)) {
                    stmtCaixa.setString(1, "Pagamento Fiado - " + nomeCliente);
                    stmtCaixa.setDouble(2, valorPago);
                    stmtCaixa.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Data de hoje
                    stmtCaixa.executeUpdate();
                }

                // 2. ABATE O DÉBITO TOTAL NA TABELA DO CLIENTE
                try (PreparedStatement stmtDebito = conn.prepareStatement(sqlDebito)) {
                    stmtDebito.setDouble(1, valorPago);
                    stmtDebito.setInt(2, clienteId);
                    stmtDebito.executeUpdate();
                }

                // 3. EFEITO CASCATA: ABATE OS FIADOS INDIVIDUAIS (Maior pro menor)
                double dinheiroRestante = valorPago;
                
                try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBuscaFiados);
                     PreparedStatement stmtUpdate = conn.prepareStatement(sqlAtualizaFiado)) {
                     
                    stmtBusca.setInt(1, clienteId);
                    ResultSet rs = stmtBusca.executeQuery();

                    while (rs.next() && dinheiroRestante > 0) {
                        int fiadoId = rs.getInt("id");
                        double valorDaDivida = rs.getDouble("valor");

                        if (dinheiroRestante >= valorDaDivida) {
                            // O dinheiro dá pra quitar essa dívida inteira
                            stmtUpdate.setDouble(1, 0.0);
                            stmtUpdate.setString(2, "QUITADO");
                            stmtUpdate.setInt(3, fiadoId);
                            stmtUpdate.executeUpdate();
                            
                            dinheiroRestante -= valorDaDivida; // Subtrai o que já gastou
                        } else {
                            // O dinheiro não dá pra quitar tudo, só abate um pedaço
                            double valorQueSobrou = valorDaDivida - dinheiroRestante;
                            
                            stmtUpdate.setDouble(1, valorQueSobrou);
                            stmtUpdate.setString(2, "PENDENTE");
                            stmtUpdate.setInt(3, fiadoId);
                            stmtUpdate.executeUpdate();
                            
                            dinheiroRestante = 0; // Acabou o dinheiro da mão
                        }
                    }
                }

                // Tudo deu certo! Salva oficialmente no banco.
                conn.commit(); 
            } catch (Exception e) {
                // Deu pau? Desfaz tudo pra não corromper o caixa.
                conn.rollback(); 
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
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