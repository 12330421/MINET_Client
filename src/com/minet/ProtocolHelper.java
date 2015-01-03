package com.minet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolHelper {
	private String inStr;
	private Map<String, Object> requestData;
	private String action;

	public ProtocolHelper() {

	}

	public void setInStr(String inStr) {
		this.inStr = inStr;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setRequestData(Map<String, Object> data) {
		this.requestData = data;
	}

	public String generateOutStr() {
		String outStr = "From:Client|";
		outStr += "Method:Request|";
		outStr += "Action:" + this.action + "|";
		outStr += "Status:OK|";
		outStr += "Data:" + (new JSONObject(this.requestData));
		return outStr;
	}

	public String getPara(String str) {
		int start = inStr.indexOf(str) + str.length() + 1;
		int end = inStr.indexOf("|", start);
		return inStr.substring(start, end);
	}

	public Map<String, Object> getData() {
		int start = inStr.indexOf("Data") + 4 + 1;
		return toMap(inStr.substring(start));
	}

	public static Map<String, Object> toMap(String jsonString) throws JSONException {

		JSONObject jsonObject = new JSONObject(jsonString);

		Map<String, Object> result = new HashMap<String, Object>();
		Iterator<?> iterator = jsonObject.keys();
		String key = null;
		Object value = null;

		while (iterator.hasNext()) {

			key = (String) iterator.next();
			value = jsonObject.get(key);
			result.put(key, value);

		}
		return result;

	}

	public String generateResponse() {
	    String outStr = "From:Client|";
		outStr += "Method:Response|";
		outStr += "Action:" + this.action + "|";
		outStr += "Status:OK|";
		outStr += "Data:";
	    return outStr;
	}
}