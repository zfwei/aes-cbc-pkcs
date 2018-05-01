package com.github.alpha.aes;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Hello world!
 *
 */
public class App {
	
	private static final String XML_STRING = "PUT /ISAPI/VCS/event/notification HTTP/1.1";
	private static final String XML_STRING1 = "Content-Type: text/xml";
	private static final String XML_STRING2 = "Content-Length: 70";
	private static final String XML_STRING3 = "Connection: Keep-Alive";
	private static final String XML_STRING4 = "Accept-Encoding: gzip";
	private static final String XML_STRING5 = "Accept-Language: zh-CN,en,*";
	private static final String XML_STRING6 = "User-Agent: Mozilla/5.0";
	private static final String XML_STRING7 = "Host: 127.0.0.1:720";
	private static final String XML_STRING8 = "\r";
	private static final String XML_STRING9 = "<NotifyEvent><notificationType>online</notificationType></NotifyEvent>";
	private static ConcurrentMap<String, Map<String, String>> cMap = new ConcurrentHashMap<String, Map<String, String>>();
	
	public static void main(String[] args) {
		//System.out.println("Hello World!");
		StringBuilder builder = new StringBuilder();
		builder.append(XML_STRING);
		builder.append("\n");
		builder.append(XML_STRING1);
		builder.append("\n");
		builder.append(XML_STRING2);
		builder.append("\n");
		builder.append(XML_STRING3);
		builder.append("\n");
		builder.append(XML_STRING4);
		builder.append("\n");
		builder.append(XML_STRING5);
		builder.append("\n");
		builder.append(XML_STRING6);
		builder.append("\n");
		builder.append(XML_STRING7);
		builder.append("\n");
		builder.append(XML_STRING8);
		builder.append("\n");
		builder.append(XML_STRING9);
		System.out.println(builder.toString());
		System.out.println("--------------------");
		/*String[] strArray = builder.toString().split("\r\n");
		for (String str : strArray) {
			System.out.println(str);
		}*/
		StringTokenizer st = new StringTokenizer(builder.toString(), "\r|\n");
		Map<String, String> map = new HashMap<String, String>();
		while (st.hasMoreTokens()) {
			String tokenizer = st.nextToken();
			System.out.println(tokenizer);
			int index = tokenizer.indexOf(":");
			if (index > -1) {
				map.put(tokenizer.substring(0, index), tokenizer.substring(index + 1));
			} else {
				map.put("body", tokenizer);
			}
		}
		System.out.println("==========");
		Iterator<Entry<String, String>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, String> entry = entries.next();
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		Map<String, String> tMap = cMap.get("127.0.0.1");
		if (tMap != null) {
			tMap.put("", "");
		}
		
	}
	
}

