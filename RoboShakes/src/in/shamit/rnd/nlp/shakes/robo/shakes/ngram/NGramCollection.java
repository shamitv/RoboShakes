package in.shamit.rnd.nlp.shakes.robo.shakes.ngram;

import java.util.HashMap;
import java.util.Map;

public class NGramCollection {
	Map<NGram, Integer> grams=new HashMap<>();
	public void addNGram(String[] tokens){
		NGram ng = new NGram(tokens);
		if(!grams.containsKey(ng)){
			grams.put(ng, 1);
		}else{
			int i = grams.get(ng);
			i++;
			grams.put(ng, i);
		}
	}
	public int getCount(){
		return grams.size();
	}
	public Map<NGram, Integer> getNGrams() {
		return grams;
	}
}
