package ginterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.image.Image;

public class MoviesInfo {
	
	private String title;
	private Image image;
	private String rating;
	private boolean possibleSave=false;

	
	public MoviesInfo(String link){
		
		BufferedReader in = null;
	    try {
	        URL url = new URL("http://www.imdb.com/title/tt1674771/");
	        URLConnection urlc;
	        urlc = url.openConnection();
	        urlc.addRequestProperty("user-agent", "Firefox");
	        in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null){
        		//System.out.println("\n"+inputLine);
	        	addImage(inputLine);
	        	addRating(inputLine);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            in.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private String getSubString(String string, char from, char to) {
	    String sub;
	    Integer beginIndex = getIndexOfChar(string, from, 0);
	    if (beginIndex == null) {
	        return null;
	    }

	    Integer endIndex = getIndexOfChar(string, to, beginIndex + 1);
	    if (endIndex == null) {
	        return null;
	    }
	    sub = string.substring(beginIndex + 1, endIndex);
	    return sub;
	}
	
	private Integer getIndexOfChar(String string, char c, int start) {
	    Integer index = null;
	    for (int i = start; i < string.length(); i++) {
	        if (string.charAt(i) == c) {
	            index = i;
	            break;
	        }
	    }
	    return index;
	}
	
	private Integer getCompareIndex(char[] compare, char[] input) {
	    Integer compareIndex = null;
	    for (int i = 0; i < input.length; i++) {
	        if (input[i] == compare[0] && containsCompareAtIndex(compare, input, i)) {
	            compareIndex = i;
	            break;
	        }
	    }
	    return compareIndex;
	}
	
	private boolean containsCompareAtIndex(char[] compare, char[] input, int index) {
		  //check wether the lenght of the rest input is enough
		    if(compare.length > input.length - index){
		        return false;
		    }
		  //check for inequality
		    for (int j = 1; j < compare.length; j++) {
		        if (compare[j] != input[++index]) {
		            return false;
		        }
		    }
		    return true;
		}
	
	private void addImage(String inputLine) {
    	//if(possibleSave){
    		if(inputLine.contains("<img") /*&& inputLine.contains("itemprop=\"image\">")*/){
        		possibleSave=false;
        		//System.out.println("\n"+inputLine);
    		}
    	//}
	}
	
	private void addRating(String inputLine){
    	if(inputLine.contains("titlePageSprite star-box-giga-star")){
    		rating = getSubString(inputLine, '>', '<').trim();
    	}
	}
	
	public double getRating(){
		return Double.parseDouble(rating);
	}
}
