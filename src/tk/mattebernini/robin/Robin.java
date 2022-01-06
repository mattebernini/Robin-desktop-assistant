package tk.mattebernini.robin;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Robin extends Application {
    
    @Override
    public void start(Stage stage) 
    {        
        EventoDiNavigazioneGUI ev_nav_GUI = new EventoDiNavigazioneGUI();
        ev_nav_GUI.avvioRobin();
        ParametriDiConfigurazione params = new ParametriDiConfigurazione();
        
        TabellaComandoAzione tabella = new TabellaComandoAzione();
        Label label_nome_utente = new Label("Nome utente: "+ params.getNome_utente()); 
        Label label_comando_capito = new Label("Robin ha capito");
        ComandoCapitoOutput comando_capito = new ComandoCapitoOutput();
        GraficoUtilizzoGiornaliero grafico = new GraficoUtilizzoGiornaliero(params.get_giorni_visualizzati_grafico());
        
        label_nome_utente.setLayoutX(10); label_nome_utente.setLayoutY(10);
        label_comando_capito.setLayoutX(60); label_comando_capito.setLayoutY(520);

        label_nome_utente.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #f9ca24;");
        label_comando_capito.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #f9ca24;");
        
        AssistenteVocale assistente = new AssistenteVocale(comando_capito);
        assistente.start();
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    comando_capito.mostra_nuovo_comando();
                });
            }
        }, 0, 1000);
        stage.setOnCloseRequest((WindowEvent we) -> {
            ev_nav_GUI.chiusuraRobin();
            assistente.stop();
            timer.cancel();
            Platform.exit();
        });
        
        Group root = new Group(label_nome_utente, tabella, grafico, label_comando_capito, comando_capito);
        Scene scene = new Scene(root, 1000,700);
        imposta_stile(stage, scene);
    }

    private void imposta_stile(Stage stage, Scene scene) 
    {
        stage.setTitle("Robin");
        scene.setFill(Color.web("#130f40"));
        Image icona = new Image("file:./img/Robin_logo.jpg");
        stage.getIcons().add(icona);
        stage.setScene(scene);
        stage.show();
    }
}
