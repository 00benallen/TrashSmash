package res;

public class MovePattern {
	private int typeCode;
	private int[] moveArray;
	public final static int STRAIGHT = 0, RIGHT = 1, LEFT = 2;
	
	public MovePattern(int typeCode) {
		this.setTypeCode(typeCode);
		this.setMoveArray(generateMoveArray());
	}
	
	private void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	
	private int[] generateMoveArray() {
		if(typeCode < 2) {
			int[] moveArray = {RIGHT, RIGHT, LEFT, LEFT, LEFT, LEFT, RIGHT, RIGHT};
			return moveArray;
		}
		else if(typeCode > 1 && typeCode < 4) {
			int[] moveArray = {RIGHT, RIGHT, STRAIGHT, STRAIGHT, LEFT, LEFT, STRAIGHT, STRAIGHT};
			return moveArray;
		}
		else if(typeCode > 3 && typeCode < 6) {
			int[] moveArray = {STRAIGHT, STRAIGHT, STRAIGHT, STRAIGHT, STRAIGHT, STRAIGHT, STRAIGHT, STRAIGHT};
			return moveArray;
		}
		else if(typeCode > 5 && typeCode < 8) {
			int[] moveArray = {RIGHT, RIGHT, RIGHT, STRAIGHT, LEFT, LEFT, STRAIGHT, RIGHT};
			return moveArray;
		}
		else if(typeCode == 8) {
			int[] moveArray = {RIGHT, LEFT, STRAIGHT, RIGHT, RIGHT, LEFT, LEFT, RIGHT};
			return moveArray;
		}
		else {
			return null;
		}
	}

	public int[] getMoveArray() {
		return moveArray;
	}

	private void setMoveArray(int[] moveArray) {
		this.moveArray = moveArray;
	}

}
