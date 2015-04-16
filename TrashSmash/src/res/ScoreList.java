package res;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ScoreList implements Serializable {
	private static final long serialVersionUID = 458975151508888023L;
	ArrayList<Highscore> scores;
	
	public ScoreList() {
		scores = new ArrayList<Highscore>();
	}
	
	public void addScore(String name, int score) {
		scores.add(new Highscore(name, score));
	}
	
	public Highscore getScore(int index) {
		return scores.get(index);
	}
	
	public void save() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("Assets/MenuandUI/ScoreList.obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ScoreList load() {
		ObjectInputStream in = null;
		ScoreList list = new ScoreList();
		try {
			in = new ObjectInputStream(list.getClass().getClassLoader().getResourceAsStream("Assets/MenuandUI/ScoreList.obj"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			list = (ScoreList) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

}
