package in.rnd.nlp.shakes.robo.shakes.parser.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseTest {

	static String removeLicenseBlurb(String sourceText){
		String licRegex="<<.*?>>";
		Pattern ptrn=Pattern.compile(licRegex, Pattern.MULTILINE | Pattern.DOTALL);
		return ptrn.matcher(sourceText).replaceAll(" ");
	}
	
	static String removeEmptyLines(String sourceText){
		String licRegex="\n\\s*\n";
		Pattern ptrn=Pattern.compile(licRegex);
		return ptrn.matcher(sourceText).replaceAll("");
	}
	
	static String getShakesText(){
		try {
			InputStream ins =  ParseTest.class.getResourceAsStream("/in/rnd/nlp/shakes/robo/resources/shakes/shakes.txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(ins));
			StringBuffer sb = new StringBuffer();
			String line=null;
			while((line=r.readLine())!=null){
				if(!line.trim().equals("")){
					sb.append(line+"\r\n");
				}
			}
			ins.close();
			return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	static boolean isValidLine(String s){
		String tokens[] = s.trim().split("\\s");
		if(tokens.length>=4){
			return true;
		}else{
			return false;
		}
	}
	public static void main(String[] args) {
		String testText="First line"
				+ "\r\n" + "<< text >>"
				+ "\r\n" + "Some line"
				+ "\r\n" + "<< more"
				+ "\r\n" + " text >>"
				+ "\r\n" + "<< amother text >>      "
				+ "\r\n" + "Line after ";
		//System.out.println(testText);
		//String text = removeLicenseBlurb(testText);
		String text = getShakesText();
		//System.out.println(text);
		text = removeLicenseBlurb(text);
		text = removeEmptyLines(text);
		String lines[] = text.split("\r\n");
		List<String> validLines = Arrays.asList(lines).stream().filter(ParseTest::isValidLine).collect(Collectors.toList());
		System.out.println("#Lines "+validLines.size());
		long tokenCount=0;
		Set<String> uniqTokens=new HashSet<>();
		for (String s : validLines) {
			String tokens[]=s.split("\\W");
			for (int i = 0; i < tokens.length; i++) {
				uniqTokens.add(tokens[i].trim().toLowerCase());
			}
			tokenCount+=tokens.length;
		}
		System.out.println("#Tokens "+tokenCount);
		System.out.println("#Unique Tokens "+uniqTokens.size());
	}

}
