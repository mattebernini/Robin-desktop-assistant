package tk.mattebernini.robin;


import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Robin extends Application {
    
    @Override
    public void start(Stage stage) 
    {        
        EventoDiNavigazioneGUI ev_nav_GUI = new EventoDiNavigazioneGUI();
        ev_nav_GUI.avvioRobin();
        ParametriDiConfigurazione params = new ParametriDiConfigurazione();
        
        // frontend
        TabellaComandoAzione tabella = new TabellaComandoAzione();
        Label label_nome_utente = new Label("Nome utente: "+ "Matteo"); // params.getNome_utente()
        Label label_comando_capito = new Label("Robin ha capito");
        ComandoCapitoOutput comando_capito = new ComandoCapitoOutput();
        GraficoUtilizzoGiornaliero grafico = new GraficoUtilizzoGiornaliero(params.get_giorni_visualizzati_grafico());
        
        label_nome_utente.setLayoutX(10); label_nome_utente.setLayoutY(10);
        label_comando_capito.setLayoutX(60); label_comando_capito.setLayoutY(520);

        label_nome_utente.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        label_comando_capito.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        
        // assistente vocale
        AssistenteVocale assistente = new AssistenteVocale(comando_capito);
        assistente.start();
        
        // eventi
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    comando_capito.setText(comando_capito.aggiorna_output());
                });
            }
        }, 0, 1000);
        stage.setOnCloseRequest((WindowEvent we) -> {
            ev_nav_GUI.chiusuraRobin();
            assistente.stop();
            timer.cancel();
            Platform.exit();
        });
        
        // costruzione GUI   
        Group root = new Group(label_nome_utente, tabella, grafico, label_comando_capito, comando_capito);
        Scene scene = new Scene(root, 1000,700);
        stage.setTitle("Robin");
        stage.setScene(scene);
        stage.show();
    }

    
}
