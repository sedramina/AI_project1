
public class InitialTile extends Tile {


	public InitialTile(int x, int y, boolean move, boolean n, boolean s, boolean w, boolean e) {
		super(x, y, false);
		north = n;
		south = s;
		west = w;
		east = e;
		
		if(n){
			symbol='n';
		}
		if(w){
			symbol='w';
		}
		if(e){
			symbol='e';
		}
		if(s){
			symbol='s';
		}
	}
	
	public static void main(String []args){
		char initial_north='\u222A'; 
		char initial_west='\u2282'; 
		char initial_east='\u2283'; 
		char initial_south='\u2229';
		System.out.println();
		System.out.println("\033[31;1m"+initial_north+"\033[0m");
		System.out.println("\033[31;1m"+initial_west+"\033[0m");
		System.out.println("\033[31;1m"+initial_east+"\033[0m");
		System.out.println("\033[31;1m"+initial_south+"\033[0m");
	}
	

}
