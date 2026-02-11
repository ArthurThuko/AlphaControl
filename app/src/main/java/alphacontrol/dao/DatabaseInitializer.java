package alphacontrol.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import alphacontrol.conexao.Conexao;

public class DatabaseInitializer {

    public static void inicializarBanco() {
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement()) {

            System.out.println("Iniciando criação das tabelas na ordem correta...");

            // 1. ENDEREÇO (Não depende de ninguém)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS endereco ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + "rua TEXT,"
                    + "bairro TEXT,"
                    + "n_casa TEXT,"
                    + "cep TEXT"
                    + ")");

            // 2. CLIENTE (Depende de Endereço)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cliente ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + "nome TEXT NOT NULL,"
                    + "cpf TEXT,"
                    + "telefone TEXT NOT NULL,"
                    + "debito REAL DEFAULT 0.0,"
                    + "endereco_id INTEGER UNIQUE,"
                    + "FOREIGN KEY (endereco_id) REFERENCES endereco(id) ON DELETE CASCADE"
                    + ")");

            // 3. PRODUTOS (Independente)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS produtos ("
                    + "produto_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "nome VARCHAR(255) NOT NULL,"
                    + "categoria VARCHAR(100),"
                    + "valor_compra DECIMAL(10, 2) NOT NULL,"
                    + "valor_venda DECIMAL(10, 2) NOT NULL,"
                    + "qnt_estoque INT DEFAULT 0,"
                    + "qnt_minima INT DEFAULT 5"
                    + ")");

            // 4. VENDA (Independente, mas é Pai de Item_Venda e Fiado)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS venda ("
                    + "venda_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "cliente VARCHAR(100),"
                    + "forma_pagamento VARCHAR(50) NOT NULL,"
                    + "total DECIMAL(10,2) NOT NULL,"
                    + "data_venda DATETIME NOT NULL"
                    + ")");

            // 5. ITEM_VENDA (Depende de Venda e Produto)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS item_venda ("
                    + "item_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "venda_id INT NOT NULL,"
                    + "produto_id INT NOT NULL,"
                    + "quantidade INT NOT NULL,"
                    + "valor_unitario DECIMAL(10,2),"
                    + "valor_total DECIMAL(10,2),"
                    + "FOREIGN KEY (venda_id) REFERENCES venda(venda_id),"
                    + "FOREIGN KEY (produto_id) REFERENCES produtos(produto_id)"
                    + ")");

            // 6. FIADO (Depende de Cliente e Venda)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS fiado ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + "cliente_id INTEGER NOT NULL,"
                    + "venda_id INTEGER,"
                    + "valor REAL NOT NULL,"
                    + "data DATETIME NOT NULL,"
                    + "status TEXT NOT NULL,"
                    + "FOREIGN KEY (cliente_id) REFERENCES cliente(id),"
                    + "FOREIGN KEY (venda_id) REFERENCES venda(venda_id)"
                    + ")");

            // 7. MOVIMENTAÇÃO DE CAIXA (Independente)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movimentacaocaixa ("
                    + "idMovimentacaoCaixa INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + "nome TEXT NOT NULL,"
                    + "tipo TEXT NOT NULL,"
                    + "valor REAL NOT NULL,"
                    + "data DATE NOT NULL"
                    + ")");

            System.out.println("Todas as tabelas foram verificadas/criadas com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro CRÍTICO ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}