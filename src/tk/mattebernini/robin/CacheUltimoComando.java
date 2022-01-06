package tk.mattebernini.robin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.paint.Color;


public class CacheUltimoComando 
{
    private String file_comando, file_capito;
    
    public CacheUltimoComando()
    {
        file_comando = "comando_capito.txt";
        file_capito = "capito.bin";
    }
    public void conserva(String comando, boolean capito)
    {
        conserva_comando(comando);
        conserva_capito(capito);
    }
    private void conserva_comando(String testo) 
    { 
        try { 
          Files.write(Paths.get(file_comando), testo.getBytes());
        } catch (IOException ex) { 
            System.out.println("errore: impossibile scrivere nuovo comando su txt!");
        }
    }
    private void conserva_capito(boolean capito) 
    {
        try ( FileOutputStream fout = new FileOutputStream(file_capito);
              ObjectOutputStream oout = new ObjectOutputStream(fout);) { 
          oout.writeObject(capito);               
        } catch (IOException ex) {
            System.out.println("errore: impossibile scrivere nuovo comando su txt!");
        }
    }
    public String preleva_comando() 
    { 
        String nuovo_comando = "";
        try {  
            nuovo_comando = new String(Files.readAllBytes(Paths.get(file_comando)));

        } catch (IOException ex) {
            System.out.println("errore: impossibile mostare comando capito!");
        }
        return nuovo_comando;
    }
    public boolean preleva_capito() 
    { 
        boolean ris = false;
        try ( FileInputStream fin = new FileInputStream(file_capito);
              ObjectInputStream oin = new ObjectInputStream(fin); ) { //01
          ris = (boolean) oin.readObject();  
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("errore: impossibile mostare comando capito!");
        }
        return ris;
    }
    
}
