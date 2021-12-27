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
        
        setWidth(500);
        setLayoutX(30); setLayoutY(570);
        setEditable(false);
        setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
    }

    public String aggiorna_output() 
    {
        String nuovo_comando = "";
        try {  
            nuovo_comando = new String(Files.readAllBytes(Paths.get("./comando_capito.txt")));
            System.out.println("aggiorno output gui: " + nuovo_comando);

        } catch (IOException ex) {
            System.out.println("errore: impossibile mostare comando capito!");
        }
        return nuovo_comando;
    }
}
