package in.shamit.rnd.nlp.shakes.robo.shakes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import in.shamit.rnd.nlp.shakes.robo.shakes.ngram.NGram;
import in.shamit.rnd.nlp.shakes.robo.shakes.ngram.NGramCollection;

public class LoadNGrams {

	public NGramCollection loadNGrams(InputStream in) throws IOException{
		NGramCollection grams= new NGramCollection();
		int maxGram=5;
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String tokens[]=tokenizeText(r);
		if(tokens.length>=4){
			for (int i = 0; i < tokens.length; i++) {
				for(int j=1;j<=maxGram;j++){
					if(i+j < tokens.length){
						String[] words=new String[j];
						for(int k=0;k<j;k++){
							words[k]=tokens[i+k];
						}
						grams.addNGram(words);
					}
				}
			}			
		}
		return grams;
	}
	
	String[] tokenizeText(BufferedReader r) throws IOException{
		List<String> text=new ArrayList<>();
		String line=null;
		while((line=r.readLine())!=null){
			if(!line.trim().equals("")){
				String tokens[]=tokenize(line);
				for (int i = 0; i < tokens.length; i++) {
					String t=tokens[i];
					if(t.matches(".*\\W.*")){
						String [] t2 = breakPunctuations(t);
						text.addAll(Arrays.asList(t2));
					}else{
						text.add(t.toLowerCase());
					}
				}
			}
		}
		return text.stream().map(t->t.toLowerCase()).filter(t-> !t.trim().equals("")).collect(Collectors.toList()).toArray(new String[0]);
	}
	
	Pattern punctuationPattern = Pattern.compile("\\W");
	String[] breakPunctuations(String t) {
		List<String> tokens= new ArrayList<>();
		 Matcher m = punctuationPattern.matcher(t);
		 int idx=0;
		 while(m.find()){
			 int startIdx, endIdx;
			 startIdx=idx;
			 if(startIdx<m.start()){
				 endIdx = m.start();
				 String tkn = t.substring(startIdx, endIdx);
				 tokens.add(tkn);
				 idx=m.start();
			 }
			 
			 startIdx=m.start();
			 endIdx=m.end();
			 if(endIdx<0){
				 endIdx=0;
			 }
			 idx=endIdx;
			 String tkn = t.substring(startIdx, endIdx); 
			 tokens.add(tkn);
		 }
		 
		 if(idx<t.length()-1){
			 String tkn = t.substring(idx);
			 tokens.add(tkn);
		 }
		return tokens.toArray(new String[0]);
	}

	String [] tokenize(String line){
		line=line.trim();
		String tokens[]=line.split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			String t=tokens[i];
			t=t.trim();
			tokens[i]=t;
		}
		return tokens;
	}
	
	public void printFirst100(NGramCollection c){
		Map<NGram, Integer> gmap=c.getNGrams();
		int found=0;
		Set<NGram> grams=gmap.keySet();
		for (NGram g : grams) {
			int count = gmap.get(g);
			if(count > 10){
				found++;
				System.out.println(Arrays.toString(g.getTokens())+"##"+count);
			}
			if(found>=100){
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		LoadNGrams l = new LoadNGrams();
		InputStream ins =  LoadNGrams.class.getResourceAsStream("/in/shamit/rnd/nlp/shakes/robo/resources/shakes/shakes.txt");
		NGramCollection grams = l.loadNGrams(ins);
		System.out.println("N-Grams : "+grams.getCount());
		l.printFirst100(grams);
		//System.out.println(Arrays.toString(l.breakPunctuations(".ab'cd'ef'gh"))); 
	}

}
