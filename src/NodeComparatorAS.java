


import java.util.Comparator;

public class NodeComparatorAS implements Comparator<Node>
{
    @Override
    public int compare(Node x, Node y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.depth - y.depth,
        // which would be more efficient.
        if ((x.heuristic + x.pathCost) < (y.heuristic + y.pathCost))
        {
            return -1;
        }
        if ((x.heuristic + x.pathCost) > (y.heuristic + y.pathCost))
        {
            return 1;
        }
        return 0;
    }
}