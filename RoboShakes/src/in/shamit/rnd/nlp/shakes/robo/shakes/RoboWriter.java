package in.shamit.rnd.nlp.shakes.robo.shakes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

	private String startWriting() {
		List<NGram> grams=gCol.getNGramsByLength(2);
		grams=grams.stream()
				.filter(g->!(g.getTokens()[0].matches(".*\\W.*")))
				.sorted((n2,n1)-> Integer.compare(gCol.getCount(n1), gCol.getCount(n2)))
				.limit(2000)
				.collect(Collectors.toList());
		int index=rnd.nextInt(grams.size());
		String tkn=grams.get(index).getTokens()[0];
		text.add(tkn);
		return tkn;
	}	
	
	public static void main(String[] args) {
		RoboWriter robo = new RoboWriter();
		String initText=robo.startWriting();
		System.out.println(initText);
	}



}
