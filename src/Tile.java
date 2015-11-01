import java.util.*;
public class Tile {
	
	boolean initial;
	boolean movable;
	int xPosition;
	int yPosition;
	
	char symbol;
	
	char south_west='\u2513'; 
	char south_east='\u250F'; 
	char south_north='\u2503'; 
	char north_west='\u251B'; 
	char north_east='\u2517';
	char west_east='\u2501'; 
	
	
	char initial_north='\u222A'; 
	char initial_west='\u2282'; 
	char initial_east='\u2283'; 
	char initial_south='\u2229';
	
	boolean north;
	boolean south;
	boolean west;
	boolean east;
	
	public Tile(int x, int y, boolean move){
		xPosition=x;
		yPosition=y;
		movable=move;
		initial = false;
		
	}
	
	public boolean[] getOrientation(){
		boolean [] result = new boolean [4];
		result [0] = north;
		result [1] = south;
		result [2] = west;
		result [3] = east;
		return result;
	}
	public static void main(String []args){
		char initial_north='\u222A';
		System.out.println("\033[31;1m"+initial_north+"\033[0m");
	    System.out.println("\033[31mRed\033[32m, Green\033[33m, Yellow\033[34m, Blue\033[0m");
	    
	}

	
}
