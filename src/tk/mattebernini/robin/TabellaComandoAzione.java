
package tk.mattebernini.robin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import tk.mattebernini.robin.ParametriDiConfigurazione;


public class TabellaComandoAzione extends TableView
{
    ParametriDiConfigurazione params;
    ArchivioComandoAzione db;
    EventoDiNavigazioneGUI ev_nav_GUI;
    
    public TabellaComandoAzione()
    {
        super();
        setEditable(true);
        db = new ArchivioComandoAzione();
        ev_nav_GUI = new EventoDiNavigazioneGUI();
        
        TableColumn colonna_comando = new TableColumn("comando");
        colonna_comando.setCellValueFactory(
            new PropertyValueFactory<ComandoAzione, String>("comando"));
        colonna_comando.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn colonna_azione = new TableColumn("azione");
        colonna_azione.setCellValueFactory(
            new PropertyValueFactory<ComandoAzione, String>("azione"));
        colonna_azione.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // modifiche su colonna azione concluse
        colonna_azione.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<ComandoAzione, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ComandoAzione, String> t) {
                    ComandoAzione item = ((ComandoAzione) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            );
                    item.setAzione(t.getNewValue());
                    int posizione = t.getTablePosition().getRow();
                    String nuova_azione = t.getNewValue();
                    db.azione_modificata(posizione, nuova_azione);
                    ev_nav_GUI.modificaTabella(posizione);
                }
            }
        );
        
        // inserimento dati
        params = new ParametriDiConfigurazione();
        ObservableList<ComandoAzione> dati = getDati();
        setItems(dati);
        getColumns().addAll(colonna_comando, colonna_azione);
        
        // stile
        setLayoutX(10); setLayoutY(45);
        setMaxHeight(350);  
        setMinWidth(800);
        colonna_azione.setMinWidth(500);
        colonna_comando.setMinWidth(300);
        setStyle("");
    }
    private ObservableList<ComandoAzione> getDati()
    {
        ObservableList<ComandoAzione> ris = FXCollections.observableArrayList();
        ArchivioComandoAzione db = new ArchivioComandoAzione();        
        String[] comandi = params.getComandi();
        String[] azioni = db.getAzioni(comandi.length);
        for(int i = 0; i < comandi.length; ++i)
            ris.add(new ComandoAzione(comandi[i], azioni[i]));
        return ris;
    }
        
    public class ComandoAzione
    {
        private final SimpleStringProperty comando;
        private final SimpleStringProperty azione;
        private ComandoAzione(String c, String a)
        {
            comando = new SimpleStringProperty(c);
            azione = new SimpleStringProperty(a);
        }
        public String getComando(){return comando.get();}
        public String getAzione(){return azione.get();}
        public void setAzione(String a){azione.set(a);}
        public void setComando(String a){comando.set(a);}
    }
    
}
