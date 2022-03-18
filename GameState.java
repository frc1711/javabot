public class GameState {
	
	private int x;
	
	public void update () {
		x ++;
	}
	
	public String getDataString () {
		return "{ \"x\" : "+x+"}";
	}
	
}