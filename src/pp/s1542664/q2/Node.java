package pp.s1542664.q2;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Node in a Data Dependency Graph. */
public class Node {
	/** Graph in which this node occurs. */
	private final Graph graph;
	/** Node number; used to distinguish nodes. */
	private final int nr;
	/** String ID for nodes, mainly for human readability. */
	private String id;
	/** Set of nodes to which there is an edge from this node. */
	private final Set<Node> edges;

	/**
	 * Creates a node with a given number.
	 * The number is also used as ID.
	 * Do not invoke directly: use {@link Graph#addNode()} instead.
	 */
	Node(Graph graph, int nr) {
		this(graph, nr, null);
	}

	/**
	 * Creates a node with a given number and ID.
	 * Do not invoke directly: use {@link Graph#addNode(String} instead.
	 */
	Node(Graph graph, int nr, String id) {
		this.graph = graph;
		this.nr = nr;
		this.id = id == null ? "" + nr : id;
		this.edges = new LinkedHashSet<>();
	}

	/** Returns the graph to which this node belongs. */
	public Graph getGraph() {
		return graph;
	}

	/** Returns the number of this node. */
	public int getNr() {
		return this.nr;
	}

	/** Sets the node identifier. */
	public void setId(String id) {
		this.id = id;
	}

	/** Returns the node identifier. */
	public String getId() {
		return this.id;
	}

	/** Adds an outgoing edge to a given target node. */
	public boolean addEdge(Node target) {
		return this.edges.add(target);
	}

	/** Returns the set of outgoing edge targets. */
	public Set<Node> getEdges() {
		return this.edges;
	}

	@Override
	public String toString() {
		if (this.edges.isEmpty()) {
			return "Node " + this.id + ": no outgoing edges";
		} else {
			List<String> targetIds = new ArrayList<>();
			for (Node t : this.edges) {
				targetIds.add(t.id);
			}
			return "Node " + this.id + ": edges to " + targetIds;
		}
	}

	@Override
	public int hashCode() {
		// node that we don't include the graph equality or edges in the node equality
		final int prime = 31;
		return prime * nr;
	}

	@Override
	public boolean equals(Object obj) {
		// node that we don't include the graph equality or edges in the node equality
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		if (this.nr != other.nr) {
			return false;
		}
		return true;
	}
}
