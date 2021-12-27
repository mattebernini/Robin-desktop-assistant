package tk.mattebernini.robin;

import edu.cmu.sphinx.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AssistenteVocale extends Thread
{
    private ComandoCapitoOutput output_GUI;
    private ArchivioComandoAzione db;
    private ArchivioComandiCapiti archivio_capiti;
    private String exe_browser;
    
    public AssistenteVocale(ComandoCapitoOutput cco)
    {
        super();
        output_GUI = cco;
        db = new ArchivioComandoAzione();
        archivio_capiti = new ArchivioComandiCapiti();
        exe_browser = (new ParametriDiConfigurazione()).get_exe_browser();
    }
    public void run()
    {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        String DICTIONARY_PATH = AssistenteVocale.class.getResource("vocabolario/8706.dic").toString();
        configuration.setDictionaryPath(DICTIONARY_PATH);
        String LANGUAGE_MODEL_PATH = AssistenteVocale.class.getResource("vocabolario/8706.lm").toString();
        configuration.setLanguageModelPath(LANGUAGE_MODEL_PATH);

        try
        {
            LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);

            recognize.startRecognition(true);
            SpeechResult result;

            while ((result = recognize.getResult()) != null) 
            {
                String comando = result.getHypothesis();
                String azione = db.getAzione(comando);
                boolean capito = false;
                
                if(is_eseguibile(azione))
                {
                    try{ lancia_programma(azione);}
                    catch(IOException e){e.printStackTrace();};
                    capito = true;
                }
                else
                {
                    if(is_cartella(azione))
                    {
                        try{ apri_cartella(azione);}
                        catch(IOException e){e.printStackTrace();};
                        capito = true;
                    }
                }
                if(is_link(azione))
                {
                    try{ apri_link(azione);}
                    catch(IOException e){e.printStackTrace();};
                    capito = true;
                }

                if(!comando.equals(""))
                    archivio_capiti.salva_comando_capito(comando, capito);
                capito = false;
                scrivi_in_GUI(comando);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace(); 
        }
        
    }    
    private void apri_cartella(String percorso) throws IOException
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

    private boolean is_eseguibile(String azione) 
    {
        return azione.endsWith(".exe");
    }
    private boolean is_cartella(String azione)
    {
        return azione.startsWith("C:"); 
    }
    private boolean is_link(String azione)
    {
        return azione.startsWith("htt");
    }

    private void scrivi_in_GUI(String comando) 
    {
        try { 
          Files.write(Paths.get("./comando_capito.txt"), comando.getBytes());
        } catch (IOException ex) { 
            System.out.println("errore: impossibile scrivere nuovo comando su txt!");
        }
    }

    
}
