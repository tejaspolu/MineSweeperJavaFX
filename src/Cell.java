

public class Cell {
	private boolean mine;
	private boolean flag;
	private boolean covered = true;
	private boolean question;

	public Cell(boolean isMine) {
		mine = isMine;
	}

	public boolean isMine() {
		return mine;
	}
	
	public boolean isFlag() {
		return flag;
	}

	public boolean isCovered() {
		return covered;
	}

	public boolean isQuestion() {
		return question;
	}
	
	public void setMine() {
		mine = true;
	}
	
	public void setFlag(boolean isFlag) {
		flag = isFlag;
	}
	
	public void setCovered(boolean covered) {
		this.covered = covered;
	}
	
	public void setQuestion(boolean isQuestion) {
		question = isQuestion;
	}
}
