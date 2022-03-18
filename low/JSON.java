package low;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSON {
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public void put (String key, double val) {
		map.put("\""+key+"\"", val+"");
	}
	
	public void put (String key, int val) {
		map.put("\""+key+"\"", val+"");
	}
	
	public void put (String key, boolean val) {
		map.put("\""+key+"\"", val+"");
	}
	
	public void put (String key, String val) {
		map.put("\""+key+"\"", "\""+val+"\"");
	}
	
	public String getJSONString () {
		String json = "";
		for (Entry<String, String> entry : map.entrySet()) json += ","+entry.getKey() + ":" + entry.getValue();
		return "{"+json.substring(1)+"}";
	}
	
}