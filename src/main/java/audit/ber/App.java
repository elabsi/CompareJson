package audit.ber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;


public class App{
    public static void main( String[] args ) throws Exception{
    	Set<String> diff2 = new HashSet<String>() ;
    	Map< JsonNode,JsonNode> diff = new HashMap< JsonNode,JsonNode>(); 
    	ObjectMapper jackson = new ObjectMapper(); 
    	JsonNode old = jackson.readTree(new File("C:\\Users\\hp\\Downloads\\oldJson.json")); 
    	JsonNode news = jackson.readTree(new File("C:\\Users\\hp\\Downloads\\newJson.json")); 
        String entity=old.toString();
    	String entity2 = null;
    	boolean ok=false;
    	Pattern pattern = Pattern.compile("\"([^\"]*)\"");    
         Matcher matcher = pattern.matcher(entity); 
         while(matcher.find() && !ok){
        	  entity2=matcher.group();
        	  ok=true;
         }
         entity2=entity2.substring(1, entity2.length()-1);
         for(int i=0; i<news.get(entity2).size();i++) {
        	 boolean isExist=false;
        	 for(int j=0; j<old.get(entity2).size();j++) {
    			 int aux= old.get(entity2).get(j).get("id").asInt();
    			 int aux2= news.get(entity2).get(i).get("id").asInt();
    			if(aux==aux2){
    	JsonNode newValue = JsonDiff.asJson(old.get(entity2).get(j),news.get(entity2).get(i)); 
    	JsonNode oldValue = JsonDiff.asJson(news.get(entity2).get(i),old.get(entity2).get(j)); 
    
    	for(JsonNode n:newValue) {
        	for(JsonNode m:oldValue) {
    	if(n.findValue("path").equals(m.findValue("path"))) {
    	diff.put(n,m); 
    	} } }
    	isExist=true;}}
        	 if(!isExist) {
    			diff2.add(news.get(entity2).get(i).toString()); 
    		}
        	 for(Map.Entry<JsonNode,JsonNode> o: diff.entrySet()) {
			  String aux4=o.getKey().findValue("op").toString();
			  if(aux4.equals("\"replace\"")) {
				  System.out.println(entity2+":"+" "+"[Uptdate] of the"+ o.getKey().findValue("path")
						  +" the new value is "+o.getKey().findValue("value")+" the old value is "
						  +o.getValue().findValue("value"));}
			  if(aux4.equals("\"remove\"")){
				  System.out.println("the field "+o.getKey().findValue("path")+" deleted"); } }
        	 	if(!diff2.isEmpty())
        	 		for(String o2: diff2) {
				  System.out.println("new actor created:"+o2);
        	 			}}}}
