package tk.mattebernini.robin;

import javafx.scene.control.TextField;


public class ComandoCapitoOutput extends TextField
{
    private CacheUltimoComando cache;
           
    public ComandoCapitoOutput()
    {
        super();
        cache = new CacheUltimoComando();
        
        setMinWidth(400);
        setLayoutX(30); setLayoutY(570);
        setEditable(false);
        setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
    }

    public void mostra_nuovo_comando()
    {
        String testo = cache.preleva_comando();
        boolean capito = cache.preleva_capito();
        setText(testo);
        if(!capito)
            setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: red;"); 
        else
            setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
    }
}
