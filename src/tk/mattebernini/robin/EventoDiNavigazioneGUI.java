
package tk.mattebernini.robin;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

class Evento implements Serializable{
    public String msg;
    Evento(String s) 
    {
        msg = s;
    }
}

public class EventoDiNavigazioneGUI 
{
    private String IP_server_log;
    private int porta_server_log;
    
    public EventoDiNavigazioneGUI()
    {
        ParametriDiConfigurazione params = new ParametriDiConfigurazione();
        IP_server_log = params.get_IP_server_log();
        porta_server_log = params.get_porta_server_log();
    }
    
    public void avvioRobin() 
    {
        Evento ev = new Evento("AVVIO");
        serializzaXML(ev);
    }
    public void chiusuraRobin() 
    {
        Evento ev = new Evento("CHIUSURA");
        serializzaXML(ev);
    }
    public void modificaTabella(int pos) 
    {
        Evento ev = new Evento("MODIFICA comando n°" + (pos+1));
        serializzaXML(ev);
    }
    
    private void serializzaXML(Evento ev)
    {
        XStream xs = new XStream();
        String stirnga_xml = xs.toXML(ev);
        try ( Socket s = new Socket(IP_server_log, porta_server_log);
              ObjectOutputStream oout = new ObjectOutputStream (s.getOutputStream());
        ) { oout.writeObject(stirnga_xml);
        } catch (IOException e) { e.printStackTrace(); }
        System.out.println("al file di log: "+ev.msg);
    }
}
