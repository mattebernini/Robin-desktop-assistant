
package tk.mattebernini.robin;

import com.thoughtworks.xstream.*; 
import com.thoughtworks.xstream.converters.basic.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.text.*;
import com.thoughtworks.xstream.XStream;
import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class Parametri implements Serializable
{
    public String IP_server_log;
    public int porta_server_log;
    
    public String IP_db;
    public int porta_db;
    public String nome_db;
    public String user_db;
    
    public String[] comandi;
    
    public String exe_browser;
    
    public int giorni_visualizzati_grafico;
    
    public Parametri(){}
}

public class ParametriDiConfigurazione 
{   
    static Parametri params;
    
    public ParametriDiConfigurazione()
    {
        params = new Parametri();        
        //validaFileConfigurazione();
        leggiFileDiConfigurazione();
    }
    private static void validaFileConfigurazione() 
    {
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
          SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
          Document d = db.parse(new File("FileDiConfigurazione.xml")); 
          Schema s = sf.newSchema(new StreamSource(new File("FileDiConfigurazione.xsd"))); 
          s.newValidator().validate(new DOMSource(d));
        } catch (Exception e) {
          if (e instanceof SAXException) 
            System.out.println("Errore di validazione: " + e.getMessage());
          else
            System.out.println(e.getMessage());    
        }
        System.out.println("Validazione andata a buon fine"); 
    }
    public static void leggiFileDiConfigurazione()
    {
        XStream xs = new XStream();
        xs.addImplicitArray(Parametri.class, "comandi");
        xs.alias("Parametri", Parametri.class);
        File file_xml = new File("FileDiConfigurazione.xml");
        params = (Parametri)xs.fromXML(file_xml);
    }
    
    public String get_IP_server_log(){ return params.IP_server_log; }
    public int get_porta_server_log(){ return params.porta_server_log; }
    public String get_IP_db(){ return params.IP_db; }
    public int get_porta_db(){ return params.porta_db; }
    public String get_nome_db(){ return params.nome_db; }
    public String get_user_db(){ return params.user_db; }
    public String[] getComandi() { return params.comandi; };
    public int get_giorni_visualizzati_grafico(){ return params.giorni_visualizzati_grafico; }
    public String get_exe_browser() { return params.exe_browser; }
    int giorni_visualizzati_grafico() { return params.giorni_visualizzati_grafico; }
}