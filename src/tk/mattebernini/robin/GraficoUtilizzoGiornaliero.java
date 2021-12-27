
package tk.mattebernini.robin;

import java.time.LocalDate;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class GraficoUtilizzoGiornaliero extends LineChart<Number,Number>
{
        
    public GraficoUtilizzoGiornaliero(int giorni_visualizzati_grafico)
    {
        super(new NumberAxis(LocalDate.now().minusDays(giorni_visualizzati_grafico).getDayOfMonth(), LocalDate.now().getDayOfMonth(), 1), new NumberAxis() );

        XYChart.Series utilizzo = new XYChart.Series();
        utilizzo.setName("Utilizzo Giornaliero");

        XYChart.Series comandi_soddisfatti = new XYChart.Series();
        comandi_soddisfatti.setName("Comandi Soddisfatti Giornalieri");
        
        ArchivioComandiCapiti archivio_capiti = new ArchivioComandiCapiti();
        int dati_utilizzo_giornaliero[] = archivio_capiti.get_dati_utilizzo_giornaliero();
        int dati_capiti_giornalieri[] = archivio_capiti.get_dati_capiti_giornalieri();
        LocalDate data_oggi = LocalDate.now();
        for(int i = dati_utilizzo_giornaliero.length-1; i>=0; i--)
        {
            utilizzo.getData().add(new XYChart.Data(data_oggi.minusDays(i).getDayOfMonth(), dati_utilizzo_giornaliero[i]));
            comandi_soddisfatti.getData().add(new XYChart.Data(data_oggi.minusDays(i).getDayOfMonth(), dati_capiti_giornalieri[i]));
        }
        
        getData().add(utilizzo);
        getData().add(comandi_soddisfatti);
        
        // stile
        setLayoutX(600); setLayoutY(450);
        setMaxSize(400, 250);

    }
}
