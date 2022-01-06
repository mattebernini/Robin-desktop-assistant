package tk.mattebernini.robin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ArchivioComandiCapiti 
{
    private String IP_db;
    private int porta_db;
    private String nome_db;
    private String user_db;
    private String[] comandi;
    private int giorni_da_visualizzare;
            
    public ArchivioComandiCapiti()
    {
        ParametriDiConfigurazione params = new ParametriDiConfigurazione();
        IP_db = params.get_IP_db();
        porta_db = params.get_porta_db();
        nome_db = params.get_nome_db();
        user_db = params.get_user_db();
        comandi = params.get_comandi();
        giorni_da_visualizzare = params.get_giorni_visualizzati_grafico();
    }
    
    public void salva_comando_capito(String comando, boolean capito)
    {
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
            PreparedStatement ps = co.prepareStatement("INSERT INTO ComandoCapito(comando, capito) VALUES (?, ?)"); 
        ) { 
            if(!capito)
                ps.setInt(2,0);
            else
                ps.setInt(2,1);
            ps.setString(1,comando); 
            System.out.println("rows affected(comandi capiti): " + ps.executeUpdate());
        } catch (SQLException e) {System.err.println(e.getMessage());}    
    }
    public int[] get_dati_utilizzo_giornaliero()
    {
        int[] ris = new int[giorni_da_visualizzare];
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
            Statement st = co.createStatement();
        ) { 
                ResultSet rs = st.executeQuery("SELECT DATE(giorno) as gg, Count(*) as quanti\n" +
                                                "FROM robin.ComandoCapito\n" +
                                                "WHERE datediff(DATE(current_timestamp), DATE(giorno))< 7\n" +
                                                "GROUP BY  DATE(giorno)\n" +
                                                "ORDER BY DATE(giorno)");
                int c = 0;
                while (rs.next())
                {
                    ris[c] = rs.getInt("quanti");
                    c++;
                }
        } catch (SQLException e) {System.err.println(e.getMessage());} 
        return ris;
    }

    public int[] get_dati_capiti_giornalieri() 
    {
        int[] ris = new int[giorni_da_visualizzare];
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
            Statement st = co.createStatement();
        ) { 
                ResultSet rs = st.executeQuery("SELECT DATE(giorno) as gg, Count(*) as quanti\n" +
                                                "FROM robin.ComandoCapito\n" +
                                                "WHERE datediff(DATE(current_timestamp), DATE(giorno))< 7\n" +
                                                "AND capito = true \n" +
                                                "GROUP BY  DATE(giorno)\n" +
                                                "ORDER BY DATE(giorno)");
                int c = 0;
                while (rs.next())
                {
                    ris[c] = rs.getInt("quanti");
                    c++;
                }
        } catch (SQLException e) {System.err.println(e.getMessage());} 
        return ris;
    }
}
