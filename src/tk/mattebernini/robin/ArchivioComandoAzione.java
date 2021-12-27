
package tk.mattebernini.robin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ArchivioComandoAzione 
{
    private String IP_db;
    private int porta_db;
    private String nome_db;
    private String user_db;
    private String[] comandi;
            
    public ArchivioComandoAzione()
    {
        ParametriDiConfigurazione params = new ParametriDiConfigurazione();
        IP_db = params.get_IP_db();
        porta_db = params.get_porta_db();
        nome_db = params.get_nome_db();
        user_db = params.get_user_db();
        comandi = params.getComandi();
    }
    
    public String[] getAzioni(int dim)
    {
        String[] ris = new String[dim];
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
            Statement st = co.createStatement();
        ) { 
              ResultSet rs = st.executeQuery("SELECT id, azione FROM ComandoAzione ORDER BY id");
              for(int i = 0; i<dim; i++)
              {
                  rs.next();
                  if(rs.getInt("id")== i)
                      ris[i] = rs.getString("azione");
                  else
                      ris[i] = "";
              }                
        } catch (SQLException e) {System.err.println(e.getMessage());}     
        return ris;
    }
    public void azione_modificata(int pos, String nuova_azione)
    {
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
            Statement st = co.createStatement();
            PreparedStatement ps1 = co.prepareStatement("UPDATE ComandoAzione SET azione = ? WHERE id = ?"); 
            PreparedStatement ps2 = co.prepareStatement("INSERT INTO ComandoAzione(id, azione) VALUES (?, ?)"); 
        ) { 
            ResultSet rs = st.executeQuery("SELECT id, azione FROM ComandoAzione ORDER BY id");
            while (rs.next())
              if(pos == rs.getInt("id"))
              {
                  ps1.setString(1,nuova_azione); ps1.setInt(2,pos);
                  System.out.println("rows affected(update): " + ps1.executeUpdate());
                  return;
              }
            ps2.setInt(1,pos); ps2.setString(2,nuova_azione);
            System.out.println("rows affected(insert): " + ps2.executeUpdate());
        } catch (SQLException e) {System.err.println(e.getMessage());}    
    }
    
    public String getAzione(String comando)
    {
        String ris = "nessuna azione";
        for(int i = 0; i < comandi.length; i++)
        {
            //System.out.println(comandi[i].toUpperCase() +" == "+ comando.toUpperCase());
            if(comandi[i].toUpperCase().equals(comando.toUpperCase()))
            {
                //System.out.println(i);
                try ( Connection co = DriverManager.getConnection("jdbc:mysql://"+ IP_db+":"+ porta_db+"/"+nome_db, user_db,"");  
                    Statement st = co.createStatement();
                ) { 
                        ResultSet rs = st.executeQuery("SELECT azione FROM ComandoAzione WHERE id = "+i);                        
                        while(rs.next())
                        {
                            //System.out.print(rs.getString("azione"));
                            ris = rs.getString("azione");
                        }
                } catch (SQLException e) {System.err.println(e.getMessage());}     
            }
        }
        return ris;
    }
    
}
