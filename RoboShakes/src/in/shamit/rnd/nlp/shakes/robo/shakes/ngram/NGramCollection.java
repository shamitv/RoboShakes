package in.shamit.rnd.nlp.shakes.robo.shakes.ngram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
	public List<NGram>getNGramsByLength(int length){
		Set<NGram> gset=grams.keySet();
		return gset.stream().filter(g->g.getTokens().length==length).collect(Collectors.toList());
	}
	public int getCount(NGram n) {
		return grams.get(n);
	}
	public List<NGram> getMatches(String[] prefix,int maxLen){
		Set<NGram> gset=grams.keySet();
		return gset.stream()
				.filter(g->g.getTokens().length<=maxLen)
				.filter(g->g.startsWith(prefix))
				.collect(Collectors.toList());
	}
	public List<NGram> getMatches(String[] prefix,int maxLen,int limit){
		Set<NGram> gset=grams.keySet();
		return gset.stream()
				.filter(g->g.getTokens().length<=maxLen)
				.filter(g->g.startsWith(prefix))
				.limit(limit)
				.collect(Collectors.toList());
	}
}
