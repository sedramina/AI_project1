public class GoalTile extends Tile {

	public GoalTile(int x, int y, boolean move, boolean n, boolean s,
			boolean w, boolean e) {
		super(x, y, false);
		north = n;
		south = s;
		west = w;
		east = e;

		if (n) {
			symbol = 'n';
		}
		if (w) {
			symbol = 'w';
		}
		if (e) {
			symbol = 'e';
		}
		if (s) {
			symbol = 's';
		}
	}
}
