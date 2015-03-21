package py.com.global.educador.engine.utils;

import java.net.URL;
import java.util.Date;

import sun.net.www.protocol.http.HttpURLConnection;

public class SmsSenderSimulator {
	private final String USER_AGENT = "Mozilla/5.0";
	public String baseUrl="http://localhost:88/inject_mo";
	public String baseParameters="short_message=<MESSAGE>&source_addr=<DESTINATION_NUMBER>" +
			"&destination_addr=<SHORT_CODE>&submit=Submit+Message&service_type=" +
			"&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1" +
			"&dest_addr_npi=1&esm_class=0&protocol_ID=" +
			"&priority_flag=&registered_delivery_flag=0&data_coding=0" +
			"&user_message_reference=&source_port=&destination_port=&sar_msg_ref_num=" +
			"&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=" +
			"&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=" +
			"&language_indicator=&tlv1_tag=&tlv1_len=&tlv1_val=&tlv2_tag=&tlv2_len=&tlv2_val=" +
			"&tlv3_tag=&tlv3_len=&tlv3_val=&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=" +
			"&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";
	
	public SmsSenderSimulator() {
		// TODO Auto-generated constructor stub
	}
	
	public int sendSms(String shortCode, String number, String message){
		String param=baseParameters.replace("<SHORT_CODE>", shortCode);
		param=param.replace("<DESTINATION_NUMBER>", number);
		param=param.replace("<MESSAGE>", message);
		return sendUrl(baseUrl, param);
		
	}
	
	public int sendUrl(String url, String parameters){
		try {
			URL obj= new URL(url+"?"+parameters);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
			//add reuqest header
			con.setRequestMethod("GET");
			//String urlParameters = parameters;
			con.setRequestProperty("User-Agent", USER_AGENT);
		
				 
			int responseCode = con.getResponseCode();
			//System.out.println("Response---> "+responseCode);
			return responseCode;
		} catch (Exception e) {
			System.out.println("SmsSenderSimulator.sendUrl(): "+e);
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] args) {
		//String url="http://localhost:88/inject_mo";
		String shortNumber="606";
		String number="0985480131";
		String message="alta";
		//String parameters="short_message=a&source_addr=0981480135&destination_addr=606&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0&user_message_reference=&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=&tlv1_len=&tlv1_val=&tlv2_tag=&tlv2_len=&tlv2_val=&tlv3_tag=&tlv3_len=&tlv3_val=&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";
		SmsSenderSimulator simulator= new SmsSenderSimulator();
		
		
		int baseNumber=983900001;
		int maxNumber= baseNumber+60000;
		int SUCCESS=0;
		int ERROR=0;
		int result=0;
		long t1=System.currentTimeMillis();
		System.out.println("Sending request...");
		System.out.println("Current Time "+new Date());
		for (int i = baseNumber; i <= maxNumber; i++) {
			number="0"+i;
			//System.out.println("Sending to--> "+number);
			result=simulator.sendSms(shortNumber,number , message);
			if (result==200) {
				SUCCESS++;
			}
			else{
				ERROR++;
			}
		}
		long t2=System.currentTimeMillis();
		System.out.println("Time delay--> "+(t2-t1)+"ms.");
		System.out.println("SUCCESS--> "+SUCCESS);
		System.out.println("ERROR--> "+ERROR);
		
	}
}
