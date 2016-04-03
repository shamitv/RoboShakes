package in.shamit.rnd.nlp.shakes.robo.shakes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import in.shamit.rnd.nlp.shakes.robo.shakes.ngram.NGram;
import in.shamit.rnd.nlp.shakes.robo.shakes.ngram.NGramCollection;

public class RoboWriter {
	NGramCollection gCol = null;
	List <String> text=new ArrayList<>();
	Random rnd = new Random();
	public RoboWriter(){
		try {
			LoadNGrams l = new LoadNGrams();
			InputStream ins =  LoadNGrams.class.getResourceAsStream("/in/shamit/rnd/nlp/shakes/robo/resources/shakes/shakes.txt");
			gCol = l.loadNGrams(ins);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String startWriting() {
		//List<NGram> grams=gCol.getNGramsByLength(2);
		List<NGram> grams=gCol.getMatches(new String[0], 2);
		grams=grams.stream()
				.filter(g->!(g.getTokens().length==2))
				.filter(g->!(g.getTokens()[0].matches(".*\\W.*")))
				.sorted((n2,n1)-> Integer.compare(gCol.getCount(n1), gCol.getCount(n2)))
				.limit(2000)
				.collect(Collectors.toList());
		String tkn=selectRandom(grams).getTokens()[0];
		text.add(tkn);
		return tkn;
	}	
	
	public void write(int numWords){
		for(int i=0;i<numWords;i++){
			write();
		}
	}
	
	public void write() {
		//Get upto last N words in array
		//N = max length of prefix to lookup NGrams
		int max_prefix=3;
		int prefix_size=max_prefix;
		if(text.size()<max_prefix){
			prefix_size=text.size();
		}
		String prefix[]=new String[prefix_size];
		for (int i = 0; i < prefix.length; i++) {
			String t=text.get(text.size()-(prefix.length-i));
			prefix[i]=t;
		}
		List<NGram> grams=gCol.getMatches(prefix, LoadNGrams.MAX_NGRAM_LENGTH,1000);
		NGram g = selectNGram(grams);
		if(grams.size()==0){
			System.err.println("No more NGrams");
			return ;
		}
		List<String> tokens=extractNonPrefixTokens(prefix, g);
		text.addAll(tokens);
		System.out.println(tokens);
	}

	List<String> extractNonPrefixTokens(String prefix[], NGram ngram){
		List<String> ret=new ArrayList<>();
		int idx=prefix.length;
		while(idx<ngram.getTokens().length){
			ret.add(ngram.getTokens()[idx]);
			idx++;
		}
		return ret;
	}
	
	NGram selectNGram(List<NGram> grams){
		if(grams.size()==0){
			return null;
		}
		//Select from top 10%
		double top_percent=10;
		int max_size=grams.size();
		int limit=max_size;
		if(max_size>1000){
			limit=(int)((double)(top_percent/100)*max_size);
		}
		grams=grams.stream()
				.sorted((n2,n1)-> Integer.compare(gCol.getCount(n1), gCol.getCount(n2)))
				.limit(limit)
				.collect(Collectors.toList());
		return selectRandom(grams);
	}
	
	NGram selectRandom(List<NGram> grams){
		int index=rnd.nextInt(grams.size());
		return grams.get(index);
	}
	
	
	String prettyPrint(){
		StringBuffer sb=new StringBuffer();
		for (String tkn: text) {
			if(tkn.matches("\\W")){
				sb.append(tkn);
			}else{
				sb.append(" ");
				sb.append(tkn);
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		RoboWriter robo = new RoboWriter();
		String initText=robo.startWriting();
		System.out.println(initText);
		for(int i=0;i<100;i++){
			robo.write();	
		}
		System.out.println(robo.prettyPrint());
	}



}
