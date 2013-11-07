import java.util.ArrayList;

import org.snmp4j.mp.SnmpConstants;

import com.meteor.module.Snmp_module;



public class Main {

	public static void main(String[] args) throws Exception
	{
		  
		Snmp_module snmp = new Snmp_module();
		
		
		
		ArrayList<String> al = new ArrayList<String>();
		
		al.add(".1.3.6.1.2.1.1.1.0");
		al.add(".1.3.6.1.2.1.1.2.0");
		al.add(".1.3.6.1.2.1.1.3.0");
		al.add(".1.3.6.1.2.1.1.4.0");
		al.add(".1.3.6.1.2.1.1.5.0");

		
		snmp.analyze_reponse( snmp.send_snmp_get(al) );
		
	  }
	
}
