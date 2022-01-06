package tk.mattebernini.robin;

import edu.cmu.sphinx.api.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AssistenteVocale extends Thread
{
    private ComandoCapitoOutput output_GUI;
    private ArchivioComandoAzione db;
    private ArchivioComandiCapiti archivio_capiti;
    private String exe_browser;
    private ParametriDiConfigurazione params;
    private  CacheUltimoComando cache;
    
    public AssistenteVocale(ComandoCapitoOutput cco)
    {
        super();
        output_GUI = cco;
        db = new ArchivioComandoAzione();
        archivio_capiti = new ArchivioComandiCapiti();
        exe_browser = (new ParametriDiConfigurazione()).get_exe_browser();
        params = new ParametriDiConfigurazione();
        cache = new CacheUltimoComando();
    }
    public void run()
    {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        String DICTIONARY_PATH = AssistenteVocale.class.getResource("vocabolario/2024.dic").toString();
        configuration.setDictionaryPath(DICTIONARY_PATH);
        String LANGUAGE_MODEL_PATH = AssistenteVocale.class.getResource("vocabolario/2024.lm").toString();
        configuration.setLanguageModelPath(LANGUAGE_MODEL_PATH);

        try
        {
            riconoscimento_vocale(configuration);
        }
        catch(Exception e){ e.printStackTrace();}        
    }    
    
    private void riconoscimento_vocale(Configuration configuration) throws IOException
    {
        LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);
        recognize.startRecognition(true);
        SpeechResult result;
        while ((result = recognize.getResult()) != null) 
        {
            String comando = result.getHypothesis();
            String azione = db.get_azione(comando);
            boolean capito = false;

            capito = esegui(azione);
            comando.toUpperCase();

            if(!comando.equals("") & comando.startsWith("ROBIN"))
                archivio_capiti.salva_comando_capito(comando, capito);
            cache.conserva(comando, capito);
            capito = false;
        }
    }
    private boolean esegui(String azione)
    {
        
        if(is_chiudi(azione))
        {
            try{ chiudi_filesystem(azione);}
            catch(IOException e){e.printStackTrace();};
            return true;
        }
        if(is_eseguibile(azione))
        {
            try{ lancia_programma(azione);}
            catch(IOException e){e.printStackTrace();};
            return true;
        }
        else
        {
            if(is_cartella_o_file(azione))
            {
                try{ apri_filesystem(azione);}
                catch(IOException e){e.printStackTrace();};
                return true;
            }
        }
        if(is_link(azione))
        {
            try{ apri_link(azione);}
            catch(IOException e){e.printStackTrace();};
            return true;
        }
        
        return false;
    }
    
    private void apri_filesystem(String percorso) throws IOException
    {
        ProcessBuilder explorerProcess = new ProcessBuilder("explorer.exe", percorso);
        explorerProcess.start();
    }
    private void apri_link(String link) throws IOException 
    {
        ProcessBuilder chromeProcess = new ProcessBuilder(exe_browser, link); 
        chromeProcess.start();
        System.out.print(chromeProcess.toString());
    }
    private void lancia_programma(String exe) throws IOException 
    {
        ProcessBuilder exeProcess = new ProcessBuilder(exe);
        exeProcess.start();
    }
    private void chiudi_filesystem(String percorso) throws IOException
    {
        percorso = percorso.replaceAll(" chiudi", "");
        System.out.println(percorso);
        ProcessBuilder killer = new ProcessBuilder("taskkill", "/im", "explorer.exe"); 
        killer.start();
    }

    private boolean is_eseguibile(String azione) 
    {
        return azione.endsWith(".exe");
    }
    private boolean is_cartella_o_file(String azione)
    {
        return azione.startsWith("C:"); 
    }
    private boolean is_link(String azione)
    {
        return azione.startsWith("htt");
    }
    private boolean is_chiudi(String azione)
    {
        return azione.endsWith("chiudi");
    }    
}
