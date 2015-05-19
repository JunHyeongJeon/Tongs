package com.csform.android.managerapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketJSONParser {
	
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject){
		
		JSONArray jTickets = null;
		try {			
			/** Retrieves all the elements in the 'countries' array */
			jTickets = jObject.getJSONArray("tickets");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getCountries with the array of json object
		 * where each json object represent a country
		 */
		return getTickets(jTickets);
	}
	
	
	private List<HashMap<String, String>> getTickets(JSONArray jTickets){
		int ticketCount = jTickets.length();
		List<HashMap<String, String>> ticketList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> ticket = null;

		/** Taking each country, parses and adds to list object */
		for(int i=0; i<ticketCount;i++){
			try {
				/** Call getCountry with country JSON object to parse the country */
                ticket = getTicket((JSONObject)jTickets.get(i));
                ticketList.add(ticket);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return ticketList;
	}
	
	/** Parsing the Country JSON object */
	private HashMap<String, String> getTicket(JSONObject jTicket){

		HashMap<String, String> ticket = new HashMap<String, String>();

        String ticketNum = "";
		String uid="";
		String num = "";
		String gcm = "";
		String mdn = "";
		String createTime = "";
		
		try {

            ticketNum = jTicket.getString("ticket");
            uid = jTicket.getString("uid");
            num = jTicket.getString("num");
            gcm = jTicket.getString("gcm");
            mdn = jTicket.getString("currency");
            createTime = jTicket.getString("createTime");
			/*
			String details =        "Language : " + language + "\n" +
                    "Capital : " + capital + "\n" +
                    "Currency : " + currencyName + "(" + currencyCode + ")";
*/
            ticket.put("ticket", ticketNum);
            ticket.put("uid", uid);
            ticket.put("num", num);
           // ticket.put("mdn", mdn);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return ticket;
	}
}
