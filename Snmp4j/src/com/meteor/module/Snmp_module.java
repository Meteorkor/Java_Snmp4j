package com.meteor.module;

import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Snmp_module {

	
	CommunityTarget comtarget;
	
	public Snmp_module(){
		
	}
	
	
	public CommunityTarget settarget(String IP, int Port, String community, int snmpVersion){
		
		comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(community));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(IP+"/"+Port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);
		
		return comtarget;
	}
	
	
	
	public PDU set_oids(ArrayList<String> lst){
	
		if(lst.size()>0){
			PDU pdu = new PDU();	
			
			for(String oid : lst){
				pdu.add(new VariableBinding(new OID(oid)));	
			}
			pdu.setType(PDU.GETNEXT);
			pdu.setRequestID(new Integer32(1));
			
			return pdu;
		}else{
			return null;	
		}
		
	}
	
	public ResponseEvent send_snmp_get(ArrayList<String> lst){
		try {
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();
			
			Snmp snmp = new Snmp(transport);

			PDU pdu = set_oids(lst);
			
			ResponseEvent responses = snmp.get(pdu, comtarget);
				
			snmp.close();
			return responses;
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public void analyze_reponse(ResponseEvent re){
		
		if(re != null){
			PDU responsePDU = re.getResponse();
			if(responsePDU != null){
				
				int errorStatus = responsePDU.getErrorStatus();
				int errorIndex = responsePDU.getErrorIndex();
				String errorStatusText = responsePDU.getErrorStatusText();
				
				if(errorStatus == PDU.noError){
					//no error
					 for(int i=0;i<responsePDU.getVariableBindings().size();i++){
			        	  System.out.println(i);
			        	  System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings().get(i) );
			         }
					
					
				}else{
					//response error
					 System.out.println("Error: Request Failed");
			         System.out.println("Error Status = " + errorStatus);
			         System.out.println("Error Index = " + errorIndex);
			         System.out.println("Error Status Text = " + errorStatusText);
				}
				
				
			}else{
				//PDU null
			}
			
			
		}else{
			//Response null : Agent Timeout
		}
		
	}
	
}
