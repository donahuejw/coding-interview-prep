This class is an implementation of a WeightedGraph, using adjacency lists to store the edges for each vertex.  Three main operations are provided beyond the methods to add vertices/edge to the graph:

a) findPathViaBFS - finds and returns a path between two nodes in the graph via a breadth-first search
b) findPathViaDFS - finds and returns a path between two nodes in the graph via a depth-first search
c) computeMinimumSpanningTree - determines and returns the minimum spanning tree for this graph, assuming all edges have a finite weight (in this case < Integer.MAX_VALUE).  The minimum spanning tree is the spanning tree of the graph with the minimum aggregate weight.