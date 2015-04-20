package res;

import java.io.Serializable;

public class Highscore implements Serializable {
	private static final long serialVersionUID = -2609560858125163826L;
	public String name;
	public int score;
	
	public Highscore(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String toString() {
		return this.name + " " + "this.score";
	}
}
