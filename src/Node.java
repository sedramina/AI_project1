public class Node {
	Object state;
	int depth;
	int pathCost;
	Node parent;
	int[] operator;
	int heuristic;

	public Node(Object state, int depth, int pathCost, Node parent,
			int[] operator, int heuristic) {
		super();
		this.state = state;
		this.depth = depth;
		this.pathCost = pathCost;
		this.parent = parent;
		this.operator = operator;
		this.heuristic = heuristic;
	}
}
