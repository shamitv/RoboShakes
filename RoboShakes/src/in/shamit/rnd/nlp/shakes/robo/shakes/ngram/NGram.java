package in.shamit.rnd.nlp.shakes.robo.shakes.ngram;

import java.util.Arrays;

public class NGram {
	String tokens[];

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

	public NGram(String[] tokens) {
		super();
		this.tokens = tokens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tokens);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NGram other = (NGram) obj;
		if (!Arrays.equals(tokens, other.tokens))
			return false;
		return true;
	}


	
}
