package com.home.nms.Libs;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;



public class SNMPLIB {

	private Snmp snmp = null;

	String address = null;
	String community = null;
	
	int snmpVersion = 0;

	/**
	 * Start the SNMP session and put the Session in a listening state
	 * @throws IOException 
	 */
	void initSNMPSession() throws IOException 
	{
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
	}
	
	
	/**
	 * This method will set SNMP Target Information
	 * @param address
	 * @param community
	 * @param snmpVersion
	 * @return
	 */
	private Target getTarget() {
		
		//SNMP Versions is defined in "SnmpConstants"
		
		Address targetAddress = GenericAddress.parse(address);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(snmpVersion);
		return target;
	
	}
	
	/**
	 * This method is capable of handling multiple OIDs
	 * @param oids
	 * @param address
	 * @param community
	 * @param snmpVersion
	 * @return
	 * @throws IOException
	 */
	
	public ResponseEvent get(OID oids[]) throws IOException {
		PDU pdu = new PDU();
	 	for (OID oid : oids) {
	 	     pdu.add(new VariableBinding(oid));
	 	}
	 	pdu.setType(PDU.GET);
	 	ResponseEvent event = snmp.send(pdu, getTarget(), null);
		if(event != null) {
	             return event;
		}
		throw new RuntimeException("GET timed out");
	}
	
	/**
	 * This method takes a single OID and returns the response from the agent as a String
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	
	public String getAsString(OID oid) throws IOException {
		ResponseEvent event = get(new OID[]{oid});
		return event.getResponse().get(0).getVariable().toString();
	}
	
	
	/**
	 * Constructor is used to intialize target variables
	 * @param targetAddress
	 * @param community
	 * @param snmpVersion
	 * @throws Exception
	 */
	public SNMPLIB(String address,String community,int snmpVersion) throws Exception {
		
		this.community = community;
		this.snmpVersion = snmpVersion;
		this.address = address;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		
		//To use this class you need to do exactly as below
		SNMPLIB client = new SNMPLIB("udp:10.0.0.50/161","public",SnmpConstants.version2c);
		String sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.1.1.0"));
		System.out.println(sysDescr);

	}

}
