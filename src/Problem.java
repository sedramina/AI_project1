import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class Problem {
	static ArrayList<Object> state_space = new ArrayList<Object>();
	static Object intital_state;
	static int[] operator;
	PriorityQueue<Node> search_queue;

	// Priority Queue Example PriorityQueue<String> queue = new
	// PriorityQueue<String>(10, comparator);

	public abstract int path_cost_function(Node current_node);

	public abstract int heuristic_function(Node current_node);

	public abstract boolean goal_test(Node current_node);

	// apply this operator on this Node and return the result node
	public abstract Node apply(Node test_node, int operator);

	public Node general_search(Strategy strategy) {

		Comparator<Node> comparator;

		switch (strategy) {
		case AS:
			comparator = new NodeComparatorAS();
			break;
		case DS:
			comparator = new NodeComparatorDS();
			break;
		case BS:
			comparator = new NodeComparatorBS();
			break;
		case ID:
			comparator = new NodeComparatorID();
			break;
		case GR1:
			comparator = new NodeComparatorGR();
			break;
		case GR2:
			comparator = new NodeComparatorGR();
			break;
		default:
			comparator = new NodeComparatorAS();
			break;
		}
		search_queue = new PriorityQueue<Node>(100, comparator);

		Node test_node;
		Node intital_node = new Node(intital_state, 0, 0, null, operator, 0);
		this.search_queue.add(intital_node);
		while (true) {

			if (search_queue.isEmpty()) {
				return null;
			}
			test_node = this.search_queue.remove();
			if (this.goal_test(test_node)) {
				return test_node;
			}
			this.search_queue.addAll(EXPAND(test_node, operator));

		}

	}

	public Collection<? extends Node> EXPAND(Node test_node, int[] operator) {
		ArrayList<Node> temp = new ArrayList<Node>();
		for (int i = 0; i < operator.length; i++) {
			temp.add(apply(test_node, operator[i]));
		}
		return temp;
	}

}
