public class PathTile extends Tile {

	public PathTile(int x, int y, boolean move, boolean n, boolean s,
			boolean w, boolean e) {
		super(x, y, move);
		north = n;
		south = s;
		west = w;
		east = e;

		if (n && s) {
			symbol = '|';
		}
		if (n && w) {
			symbol = '/';
		}
		if (n && e) {
			symbol = 'L';
		}
		if (e && s) {
			symbol = 'r';
		}
		if (w && s) {
			symbol = '7';
		}
		if (e && w) {
			symbol = '-';
		}

	}

}
