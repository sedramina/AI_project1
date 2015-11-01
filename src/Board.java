import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Board {
	Tile[][] board;
	int rows;
	int cols;
	int initialX;
	int initialY;
	int goalX;
	int goalY;

	int emptyTiles = 0;

	int neglect = 0;

	ArrayList<Tile> currentPath;

	public Board(int x, int y, boolean solution) throws Exception {
		boolean repeat = false;
		while (!repeat) {
			board = new Tile[x][y];
			rows = x;
			cols = y;

			emptyTiles = 0;
			int pathStartX;
			int pathStartY;
			boolean validStart = false;

			int pathEndX;
			int pathEndY;
			boolean validEnd = false;

			ArrayList<int[]> pathCells;

			while (!validEnd) {
				Random randomGenerator2 = new Random();
				int rand = randomGenerator2.nextInt(100);
				int initX = rand % x;
				int initY = rand * rand % y;

				int type2 = rand % 4;
				switch (type2) {
				case 0:
					board[initX][initY] = new GoalTile(initX, initY,
							rand % 3 == 0 ? true : false, true, false, false,
							false);
					break;// NW
				case 1:
					board[initX][initY] = new GoalTile(initX, initY,
							rand % 3 == 0 ? true : false, false, true, false,
							false);
					break;// NS
				case 2:
					board[initX][initY] = new GoalTile(initX, initY,
							rand % 2 == 0 ? true : false, false, false, false,
							true);
					break;// NE
				case 3:
					board[initX][initY] = new GoalTile(initX, initY,
							rand % 3 == 0 ? true : false, false, false, true,
							false);
					break;// WE
				}

				goalX = initX;
				goalY = initY;

				boolean[] currentOrientation = board[initX][initY]
						.getOrientation();

				if (currentOrientation[0]) {
					validEnd = isValidTile(initX - 1, initY);
					pathEndX = initX - 1;
					pathEndY = initY;
				}
				// check south
				if (currentOrientation[1]) {
					validEnd = isValidTile(initX + 1, initY);
					pathEndX = initX + 1;
					pathEndY = initY;

				}
				// check west
				if (currentOrientation[2]) {
					validEnd = isValidTile(initX, initY - 1);
					pathEndX = initX;
					pathEndY = initY - 1;
				}
				if (currentOrientation[3]) {
					validEnd = isValidTile(initX, initY + 1);
					pathEndX = initX;
					pathEndY = initY + 1;

				}
				if (!validEnd) {
					board[initX][initY] = null;

				}

			}

			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					Random randomGenerator = new Random();
					int rand = randomGenerator.nextInt(99);
					int type = rand % 10;
					if (board[i][j] == null) {

						switch (type) {
						default:
							board[i][j] = new EmptyTile(i, j,
									rand % 2 == 0 ? true : false);
							System.out.println("creating new empty tile:"
									+ emptyTiles);
							emptyTiles++;
							break;
						}
					}
				}
			}

			continuePath(3, getTile(goalX, goalY).getOrientation(), goalX,
					goalY);
			System.out.println("new number of empty tiles: " + emptyTiles);

			if (hasPathToGoal(initialX, initialY)) {
				repeat = true;
				printBoard();
				for (int[] t : getPossibleMoves()) {
					for (int i = 0; i < t.length; i++) {
						System.out.print(t[i]);
					}
					System.out.println();
				}
			}

			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					Random randomGenerator = new Random();
					int rand = randomGenerator.nextInt(99);
					int type = rand % 10;
					if (board[i][j] == null || board[i][j] instanceof EmptyTile) {

						switch (type) {
						case 0:
							int type2 = rand % 6;
							switch (type2) {
							case 0:
								board[i][j] = new PathTile(i, j,
										rand % 3 == 0 ? true : false, true,
										false, true, false);
								break;// NW
							case 1:
								board[i][j] = new PathTile(i, j,
										rand % 3 == 0 ? true : false, true,
										true, false, false);
								break;// NS
							case 2:
								board[i][j] = new PathTile(i, j,
										rand % 2 == 0 ? true : false, true,
										false, false, true);
								break;// NE
							case 3:
								board[i][j] = new PathTile(i, j,
										rand % 3 == 0 ? true : false, true,
										false, true, true);
								break;// WE
							case 4:
								board[i][j] = new PathTile(i, j,
										rand % 2 == 0 ? true : false, false,
										true, true, false);
								break;// SW
							case 5:
								board[i][j] = new PathTile(i, j,
										rand % 2 == 0 ? true : false, false,
										true, false, true);
								break;// SE
							}
							emptyTiles--;
							break;
						case 9:
							board[i][j] = new BlockTile(i, j,
									rand % 2 == 0 ? true : false);
							emptyTiles--;
							break;
						default:
							;
							break;
						}
					}
				}
			}
			System.out.println();
			printBoard();
			System.out.println();

			boolean shuffle = false;
			int iter = 100;// cols*rows-emptyTiles;
			while (!shuffle) {

				Random randomGenerator = new Random();
				int rand = randomGenerator.nextInt(99);
				ArrayList<int[]> t = getPossibleMoves();
				int turn = rand % t.size();
				int[] s = t.get(turn);
				if (s == null || iter == 0) {
					shuffle = true;
				} else {
					move(s[0], s[1], s[2], s[3]);
					printBoard();
					iter--;
				}
			}
		}

	}

	public void continuePath(int l, boolean[] prev, int prevX, int prevY)
			throws Exception {
		boolean n = false;
		boolean s = false;
		boolean w = false;
		boolean e = false;

		int passOn = 0;
		int passX = -1;
		int passY = -1;
		if (prev[0]) {
			s = true;
			if (isValidTile(prevX - 1, prevY - 1)
					&& getTile(prevX - 1, prevY - 1) instanceof EmptyTile) {
				w = true;
				passOn = 2;
			}

			else {
				if (isValidTile(prevX - 2, prevY)
						&& getTile(prevX - 2, prevY) instanceof EmptyTile) {
					n = true;
					passOn = 0;
				} else {
					if (isValidTile(prevX - 1, prevY + 1)
							&& getTile(prevX - 1, prevY + 1) instanceof EmptyTile) {
						e = true;
						passOn = 3;
					}
				}
			}
			passX = prevX - 1;
			passY = prevY;
		}
		// check south
		if (prev[1]) {
			n = true;
			if (isValidTile(prevX + 2, prevY)
					&& getTile(prevX + 2, prevY) instanceof EmptyTile) {
				s = true;
				passOn = 1;
			} else {
				if (isValidTile(prevX + 1, prevY + 1)
						&& getTile(prevX + 1, prevY + 1) instanceof EmptyTile) {
					e = true;
					passOn = 3;
				} else {
					if (isValidTile(prevX + 1, prevY - 1)
							&& getTile(prevX + 1, prevY - 1) instanceof EmptyTile) {
						w = true;
						passOn = 2;

					}
				}
			}
			passX = prevX + 1;
			passY = prevY;

		}
		// check west
		if (prev[2]) {
			e = true;
			if (isValidTile(prevX - 1, prevY - 1)
					&& getTile(prevX - 1, prevY - 1) instanceof EmptyTile) {
				n = true;
				passOn = 0;
			} else {
				if (isValidTile(prevX, prevY - 2)
						&& getTile(prevX, prevY - 2) instanceof EmptyTile) {
					w = true;
					passOn = 2;
				} else {
					if (isValidTile(prevX + 1, prevY - 1)
							&& getTile(prevX + 1, prevY - 1) instanceof EmptyTile) {
						s = true;
						passOn = 1;
					}
				}
			}
			passX = prevX;
			passY = prevY - 1;
		}
		if (prev[3]) {
			w = true;
			if (isValidTile(prevX, prevY + 2)
					&& getTile(prevX, prevY + 2) instanceof EmptyTile) {
				e = true;
				passOn = 3;
			} else {
				if (isValidTile(prevX - 1, prevY + 1)
						&& getTile(prevX - 1, prevY + 1) instanceof EmptyTile) {
					n = true;
					passOn = 0;
				} else {
					if (isValidTile(prevX + 1, prevY + 1)
							&& getTile(prevX + 1, prevY + 1) instanceof EmptyTile) {
						s = true;
						passOn = 1;
					}
				}
			}
			passX = prevX;
			passY = prevY + 1;

		}
		if (passX == -1) {
			System.out.println("wrong new XX");
			throw new Exception("wrong new X");
		}

		if (passY == -1) {
			System.out.println("wrong new YY");
			throw new Exception("wrong new XX");
		}
		board[passX][passY] = new PathTile(passX, passY,
				(passX + passY) * 13 % 4 == 0 ? false : true, n, s, w, e);
		emptyTiles--;
		for (int u = 0; u < prev.length; u++)
			prev[u] = u == passOn ? true : false;

		if (l != 0) {
			continuePath(l - 1, prev, passX, passY);
		} else {
			if (prev[0]) {
				s = true;
				n = false;
				e = false;
				w = false;
				passX = passX - 1;

			}
			// check south
			if (prev[1]) {
				n = true;
				s = false;
				e = false;
				w = false;
				passX = passX + 1;

			}
			// check west
			if (prev[2]) {
				e = true;
				s = false;
				n = false;
				w = false;
				passY = passY - 1;

			}
			if (prev[3]) {
				w = true;
				s = false;
				n = false;
				e = false;
				passY = passY + 1;
			}

			if (passX == -1) {
				System.out.println("wrong new X");
				throw new Exception("wrong new X");
			}

			if (passY == -1) {
				System.out.println("wrong new Y");
				throw new Exception("wrong new X");
			}
			if (getTile(passX, passY) instanceof EmptyTile) {
				initialX = passX;
				initialY = passY;
				board[passX][passY] = new InitialTile(passX, passY, false, n,
						s, w, e);
				emptyTiles--;
			} else {
				board[passX][passY] = new InitialTile(passX, passY, false, n,
						s, w, e);
			}
		}

	}

	public Board(boolean anything) {
		board = new Tile[4][4];
		rows = 4;
		cols = 4;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (i == 0 && j == 0) {
					Tile c = new InitialTile(i, j, false, false, false, false,
							true);
					board[i][j] = c;

					initialX = i;
					initialY = j;

				}
				if (i == 0 && j == 1) {
					board[i][j] = new PathTile(i, j, false, false, false, true,
							true);
				}
				if (i == 0 && j == 2) {
					board[i][j] = new PathTile(i, j, false, false, true, true,
							false);
				}
				if (i == 0 && j == 3) {
					board[i][j] = new PathTile(i, j, false, false, true, true,
							false);
				}

				if (i == 1 && j == 0) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 1 && j == 1) {
					board[i][j] = new PathTile(i, j, false, true, false, false,
							true);
				}
				if (i == 1 && j == 2) {
					board[i][j] = new GoalTile(i, j, false, true, false, false,
							false);
					goalX = i;
					goalY = j;
				}
				if (i == 1 && j == 3) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}

				if (i == 2 && j == 0) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 2 && j == 1) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 2 && j == 2) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 2 && j == 3) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}

				if (i == 3 && j == 0) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 3 && j == 1) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 3 && j == 2) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}
				if (i == 3 && j == 3) {
					board[i][j] = new PathTile(i, j, false, true, false, true,
							false);
				}

			}
		}

	}

	public Board(int n, int m) {
		board = new Tile[n][m];
		rows = n;
		cols = m;

		int pathStartX;
		int pathStartY;
		boolean validStart = false;

		int pathEndX;
		int pathEndY;
		boolean validEnd = false;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				Random randomGenerator = new Random();
				int rand = randomGenerator.nextInt(99);
				int type = rand % 13;

				switch (type) {
				case 0:
					board[i][j] = new PathTile(i, j, rand % 3 == 0 ? true
							: false, true, false, true, false);
					break;// NW
				case 1:
					board[i][j] = new PathTile(i, j, rand % 3 == 0 ? true
							: false, true, true, false, false);
					break;// NS
				case 2:
					board[i][j] = new PathTile(i, j, rand % 2 == 0 ? true
							: false, true, false, false, true);
					break;// NE
				case 3:
					board[i][j] = new PathTile(i, j, rand % 3 == 0 ? true
							: false, true, false, true, true);
					break;// WE
				case 4:
					board[i][j] = new PathTile(i, j, rand % 2 == 0 ? true
							: false, false, true, true, false);
					break;// SW
				case 5:
					board[i][j] = new PathTile(i, j, rand % 2 == 0 ? true
							: false, false, true, false, true);
					break;// SE
				case 6:
					board[i][j] = new EmptyTile(i, j, rand % 2 == 0 ? true
							: false);
					emptyTiles++;
					break;
				case 7:
					board[i][j] = new EmptyTile(i, j, rand % 2 == 0 ? true
							: false);
					emptyTiles++;
					break;
				case 8:
					board[i][j] = new EmptyTile(i, j, rand % 2 == 0 ? true
							: false);
					emptyTiles++;
					break;
				case 9:
					board[i][j] = new BlockTile(i, j, rand % 2 == 0 ? true
							: false);
					break;
				default:
					board[i][j] = new EmptyTile(i, j, rand % 2 == 0 ? true
							: false);
					emptyTiles++;
					break;
				}
			}
		}
		int x = n;
		int y = m;
		while (!validStart) {

			Random randomGenerator2 = new Random();
			int rand = randomGenerator2.nextInt(100);
			int initX = rand / 3 % x;
			int initY = rand % y;
			if (board[initX][initY] instanceof EmptyTile)
				emptyTiles--;
			board[initX][initY] = new InitialTile(initX, initY, false, true,
					false, false, false);
			initialX = initX;
			initialY = initY;
			boolean[] currentOrientation = board[initX][initY].getOrientation();
			if (currentOrientation[0]) {
				validStart = isValidTile(initX - 1, initY);
				pathStartX = initX - 1;
				pathStartY = initY;
			}
			// check south
			if (currentOrientation[1]) {
				validStart = isValidTile(initX + 1, initY);
				pathStartX = initX + 1;
				pathStartY = initY;
			}
			// check west
			if (currentOrientation[2]) {
				validStart = isValidTile(initX, initY - 1);
				pathStartX = initX;
				pathStartY = initY - 1;
			}
			if (currentOrientation[3]) {
				validStart = isValidTile(initX, initY + 1);
				pathStartX = initX;
				pathStartY = initY + 1;
			}

		}

		while (!validEnd) {
			Random randomGenerator2 = new Random();
			int rand = randomGenerator2.nextInt(100);
			int initX = rand / 4 % x;
			int initY = rand % y;
			if (board[initX][initY] instanceof EmptyTile)
				emptyTiles--;
			board[initX][initY] = new GoalTile(initX, initY, false, true,
					false, false, false);
			goalX = initX;
			goalY = initY;

			boolean[] currentOrientation = board[initX][initY].getOrientation();
			if (currentOrientation[0]) {
				validEnd = isValidTile(initX - 1, initY);
				pathEndX = initX - 1;
				pathEndY = initY;
			}
			// check south
			if (currentOrientation[1]) {
				validEnd = isValidTile(initX + 1, initY);
				pathEndX = initX + 1;
				pathEndY = initY;

			}
			// check west
			if (currentOrientation[2]) {
				validEnd = isValidTile(initX, initY - 1);
				pathEndX = initX;
				pathEndY = initY - 1;
			}
			if (currentOrientation[3]) {
				validEnd = isValidTile(initX, initY + 1);
				pathEndX = initX;
				pathEndY = initY + 1;

			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Tile currentTile = board[i][j];
				if (!currentTile.movable) {
					System.out.print(" " + currentTile.symbol + " |");
				} else {
					System.out.print(" " + currentTile.symbol + " |");
				}
			}
			System.out.println();
			for (int k = 0; k < board[i].length; k++) {
				System.out.print("----");
			}
			System.out.println();
		}

	}

	public void printShrinkBoard() {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Tile currentTile = board[i][j];
				if (!currentTile.movable) {
					System.out.print("\033[31;1m" + currentTile.symbol
							+ "\033[0m");
				} else {
					System.out.print(currentTile.symbol);
				}
			}
			System.out.println();
			System.out.println();
		}

	}

	public Tile getTile(int x, int y) {
		return board[x][y];
	}

	public void move(int x1, int y1, int x2, int y2) {
		if (getTile(x1, y1).movable && getTile(x2, y2).movable) {
			Tile temp = getTile(x1, y1);
			board[x1][y1] = getTile(x2, y2);
			board[x2][y2] = temp;
		}
	}

	public int getNumberOfEmpty() {
		int result = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] instanceof EmptyTile) {
					result++;
				}

			}
		}
		return result;
	}

	public int[][] getEmptyTiles() {
		int[][] result = new int[getNumberOfEmpty()][2];
		int count = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] instanceof EmptyTile) {
					result[count][0] = i;
					result[count][1] = j;
					count++;
				}

			}
		}
		return result;
	}

	public ArrayList<int[]> getPossibleMoves() {
		ArrayList<int[]> result = new ArrayList<int[]>();
		int[][] emptyTiles = getEmptyTiles();
		System.out.println("number of empty: " + emptyTiles.length);
		for (int i = 0; i < emptyTiles.length; i++) {
			int currentX = emptyTiles[i][0];
			int currentY = emptyTiles[i][1];
			int[] toBeAdded = new int[4];
			// check north
			if (currentX - 1 >= 0 && getTile(currentX - 1, currentY).movable
					&& !(getTile(currentX - 1, currentY) instanceof EmptyTile)) {
				toBeAdded = new int[4];
				toBeAdded[0] = currentX;
				toBeAdded[1] = currentY;
				toBeAdded[2] = currentX - 1;
				toBeAdded[3] = currentY;
				result.add(toBeAdded);
			}

			// check west
			if (currentY + 1 < cols && getTile(currentX, currentY + 1).movable
					&& !(getTile(currentX, currentY + 1) instanceof EmptyTile)) {
				toBeAdded = new int[4];
				toBeAdded[0] = currentX;
				toBeAdded[1] = currentY;
				toBeAdded[2] = currentX;
				toBeAdded[3] = currentY + 1;
				result.add(toBeAdded);
			}
			// check south
			if (currentX + 1 < rows && getTile(currentX + 1, currentY).movable
					&& !(getTile(currentX + 1, currentY) instanceof EmptyTile)) {
				toBeAdded = new int[4];
				toBeAdded[0] = currentX;
				toBeAdded[1] = currentY;
				toBeAdded[2] = currentX + 1;
				toBeAdded[3] = currentY;
				result.add(toBeAdded);
			}
			// check east
			if (currentY - 1 >= 0 && getTile(currentX, currentY - 1).movable
					&& !(getTile(currentX, currentY - 1) instanceof EmptyTile)) {
				toBeAdded = new int[4];
				toBeAdded[0] = currentX;
				toBeAdded[1] = currentY;
				toBeAdded[2] = currentX;
				toBeAdded[3] = currentY - 1;
				result.add(toBeAdded);
			}

		}
		return result;
	}

	public static boolean checkForOrientation(Tile t1, Tile t2) {
		return t1.east == t2.east && t1.north == t2.north && t1.west == t2.west
				&& t1.south == t2.south;
	}

	public static boolean repeatedBoards(Board b1, Board b2) {
		if (b1.board.length != b2.board.length
				|| b1.board[0].length != b2.board[0].length) {
			return false;
		}
		for (int i = 0; i < b1.board.length; i++) {
			for (int j = 0; j < b1.board[i].length; j++) {
				Class t1 = b1.board[i][j].getClass();
				Class t2 = b2.board[i][j].getClass();

				if (t1.equals(t2)) {
					Tile tile1 = b1.board[i][j];
					if (tile1 instanceof PathTile || tile1 instanceof GoalTile
							|| tile1 instanceof InitialTile) {
						Tile tile3 = b1.board[i][j];
						Tile tile4 = b2.board[i][j];
						if (!checkForOrientation(tile3, tile4))
							return false;
						else
							continue;
					}
				} else {
					return false;
				}

			}
		}
		return true;

	}

	public boolean isValidTile(int x, int y) {
		boolean i = x >= 0 && x < rows && y >= 0 && y < cols;
		return i;
	}

	public boolean pathBetweenTwoTiles(Tile t1, Tile t2) {
		boolean[] origin = t1.getOrientation();
		boolean[] goal = t2.getOrientation();

		if (!(t1 instanceof InitialTile))
			origin[neglect] = false;

		if (origin[0]) {
			neglect = 1;
			return goal[1];
		}
		if (origin[1]) {
			neglect = 0;
			return goal[0];
		}
		if (origin[2]) {
			neglect = 3;
			return goal[3];
		}
		if (origin[3]) {
			neglect = 2;
			return goal[2];
		}

		return false;

	}

	public boolean hasPathToGoal(int x, int y) {
		Tile t1 = getTile(x, y);
		if (getTile(x, y) instanceof GoalTile)
			return true;
		else {
			boolean[] currentOrientation = getTile(x, y).getOrientation();
			if (!(t1 instanceof InitialTile) || !(t1 instanceof GoalTile))
				currentOrientation[neglect] = false;

			if (currentOrientation[0]) {
				if (isValidTile(x - 1, y)
						&& pathBetweenTwoTiles(t1, getTile(x - 1, y)))
					return hasPathToGoal(x - 1, y);
			}
			// check south
			if (currentOrientation[1]) {
				if (isValidTile(x + 1, y)
						&& pathBetweenTwoTiles(t1, getTile(x + 1, y)))
					return hasPathToGoal(x + 1, y);
			}
			// check west
			if (currentOrientation[2]) {
				if (isValidTile(x, y - 1)
						&& pathBetweenTwoTiles(t1, getTile(x, y - 1)))
					return hasPathToGoal(x, y - 1);
			}
			if (currentOrientation[3]) {
				if (isValidTile(x, y + 1)
						&& pathBetweenTwoTiles(t1, getTile(x, y + 1)))
					return hasPathToGoal(x, y + 1);
			}
		}
		return false;
	}

	public int[] getTail(int x, int y) {
		Tile t1 = getTile(x, y);
		int resX = -1;
		int resY = -1;
		boolean[] currentOrientation = t1.getOrientation();
		if (!(t1 instanceof InitialTile))
			currentOrientation[neglect] = false;

		if (currentOrientation[0]) {
			if (isValidTile(x - 1, y)
					&& pathBetweenTwoTiles(t1, getTile(x - 1, y))) {
				resX = x - 1;
				resY = y;
				return getTail(resX, resY);
			}

		}
		// check south
		if (currentOrientation[1]) {
			if (isValidTile(x + 1, y)
					&& pathBetweenTwoTiles(t1, getTile(x + 1, y))) {
				resX = x + 1;
				resY = y;
				return getTail(resX, resY);
			}
		}
		// check west
		if (currentOrientation[2]) {
			if (isValidTile(x, y - 1)
					&& pathBetweenTwoTiles(t1, getTile(x, y - 1))) {
				resX = x;
				resY = y - 1;
				return getTail(resX, resY);
			}
		}
		if (currentOrientation[3]) {
			if (isValidTile(x, y + 1)
					&& pathBetweenTwoTiles(t1, getTile(x, y + 1))) {
				resX = x;
				resY = y + 1;
				return getTail(resX, resY);
			}
		}
		int[] result = { x, y };
		return result;
	}

	public int getTailsDifference() {
		int[] initial = getTail(initialX, initialY);
		int[] goal = getTail(goalX, goalY);

		return Math.abs(initial[0] - goal[0]) + Math.abs(initial[1] - goal[1]);
	}

	public static void main(String[] args) throws Exception {
//		Board b;
//
//		b = new Board(4, 4, true);
//		System.out.println("##########################################");
//		b.printBoard();
//		System.out.println(b.getPossibleMoves());
	
		// System.out.println(b.getTail(b.initialX, b.initialY)[0] + " "
		// + b.getTail(b.initialX, b.initialY)[1]);
		//
		// System.out.println(b.getTail(b.goalX, b.goalY)[0] + " "
		// + b.getTail(b.goalX, b.goalY)[1]);
		  PriorityQueue<String> queue = 
		            new PriorityQueue<String>(10);
		        queue.add("short");
	}
}
