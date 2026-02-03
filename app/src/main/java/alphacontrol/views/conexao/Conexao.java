package alphacontrol.views.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class Conexao {

    private static final String PASTA_DB = "database";
    private static final String CAMINHO_DB = PASTA_DB + File.separator + "alphacontrol.db";
    private static final String URL = "jdbc:sqlite:" + CAMINHO_DB;

    public static Connection getConexao() throws SQLException {
        criarPastaSeNaoExistir();
        return DriverManager.getConnection(URL);
    }

    private static void criarPastaSeNaoExistir() {
        File pasta = new File(PASTA_DB);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }
}