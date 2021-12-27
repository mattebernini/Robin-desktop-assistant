package tk.mattebernini.robin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class ComandoCapitoOutput extends TextField
{
    public ComandoCapitoOutput()
    {
        super();
        
        setMinWidth(400);
        setLayoutX(30); setLayoutY(570);
        setEditable(false);
        setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
    }

    public String mostra_nuovo_comando() 
    {
        String nuovo_comando = "";
        try {  
            nuovo_comando = new String(Files.readAllBytes(Paths.get("./comando_capito.txt")));

        } catch (IOException ex) {
            System.out.println("errore: impossibile mostare comando capito!");
        }
        return nuovo_comando;
    }
}
