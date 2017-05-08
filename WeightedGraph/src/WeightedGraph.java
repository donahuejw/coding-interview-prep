import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.*;

public class WeightedGraph {

    private Map<Vertex, List<Edge>> adjacencyLists;
    private Map<Character, Vertex> vertices;

    public WeightedGraph(int initialCapacity) {
        adjacencyLists = new HashMap<>(2*initialCapacity);
        vertices = new HashMap<>(2*initialCapacity);
    }

    public WeightedGraph() {
        this(50);
    }

    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(new HashSet<>(vertices.values()));
    }

    public void addVertex(Vertex newVertex) {
        if (!vertices.containsKey(newVertex.label)) {
            adjacencyLists.put(newVertex, new LinkedList<>());
            vertices.put(newVertex.label, newVertex);
        }
    }

    public Vertex addVertex(char newVertexLabel) {
        Vertex newVertex;
        if (!vertices.containsKey(newVertexLabel)) {
            newVertex = new Vertex(newVertexLabel);
            adjacencyLists.put(newVertex, new LinkedList<>());
            vertices.put(newVertexLabel, newVertex);
        } else {
            newVertex = vertices.get(newVertexLabel);
        }

        return newVertex;
    }

    public void addEdge(Vertex v1, Vertex v2) {
        addVertex(v1);
        addVertex(v2);

        adjacencyLists.get(v1).add(new Edge(v1, v2));
        adjacencyLists.get(v2).add(new Edge(v2, v1));
    }

    public void addEdge(char v1Label, char v2Label) {
        addEdge(addVertex(v1Label), addVertex(v2Label));
    }

    public void addEdge(Vertex v1, Vertex v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        adjacencyLists.get(v1).add(new Edge(v1, v2, weight));
        adjacencyLists.get(v2).add(new Edge(v2, v1, weight));
    }

    public void addEdge(char v1Label, char v2Label, int weight) {
        addEdge(addVertex(v1Label), addVertex(v2Label), weight);
    }

    public List<Edge> getOutgoingEdges(Vertex vertex) {
        Preconditions.checkArgument(vertex != null && vertices.containsKey(vertex.label),
                "The provided vertex must be non-null and a part of this graph");
        return adjacencyLists.get(vertex);
    }
    public List<Edge> getOutgoingEdges(char vertexLabel) {
        return getOutgoingEdges(vertices.get(vertexLabel));
    }

    public boolean containsEdge(Vertex startVertex, Vertex endVertex) {
        return getOutgoingEdges(startVertex).stream().anyMatch(e -> e.endVertex.equals(endVertex));
    }
    public boolean containsEdge(char startVertex, char endVertex) {
        return containsEdge(vertices.get(startVertex), vertices.get(endVertex));
    }

    public Vertex getVertex(char vertexLabel) {
        return vertices.get(vertexLabel);
    }

    public void removeEdge(Vertex v1, Vertex v2) {
        Preconditions.checkArgument(v1 != null && vertices.containsKey(v1.label),
                "Argument v1 cannot be null, and must be a Vertex that is part of this graph");
        Preconditions.checkArgument(v2 != null && vertices.containsKey(v2.label),
                "Argument v2 cannot be null, and must be a Vertex that is part of this graph");

        List<Edge> v1Edges = adjacencyLists.get(v1);
        for (int i=0; i<v1Edges.size(); i++) {
            if (v1Edges.get(i).endVertex == v2) {
                v1Edges.remove(i);
            }
        }

        List<Edge> v2Edges = adjacencyLists.get(v2);
        for (int i=0; i<v2Edges.size(); i++) {
            if (v2Edges.get(i).endVertex == v1) {
                v2Edges.remove(i);
            }
        }
    }

    public void removeEdge(char v1Label, char v2Label) {
        removeEdge(vertices.get(v1Label), vertices.get(v2Label));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        Set<Edge> edgesOutput = new HashSet<>();
        adjacencyLists.forEach((k,v) -> {
            sb.append("Vertex: ").append(k.label).append(" has edges to:\n");
            v.forEach(e -> {
                if (!edgesOutput.contains(e)) {
                    sb.append("Vertex: ").append(e.endVertex).append("\n");
                }
                edgesOutput.add(e);
            });
            sb.append("\n");
        });

        return sb.toString();
    }

    public List<Vertex> findPathViaBFS(Vertex start, Vertex end) {
        if (start == end) {
            return Collections.singletonList(start);
        }

        // need to track how we got to each node so that we can build the path backwards at the end
        Map<Vertex, Vertex> pathTracker = new HashMap<>(vertices.size()*2);
        pathTracker.put(start, null);

        Set<Vertex> visited = new HashSet<>(vertices.size()*2);
        Queue<Vertex> q = new LinkedList<>();
        q.add(start);

        boolean found = false;
        while (!found && !q.isEmpty()) {
            Vertex currentVertex = q.remove();
            if (!visited.contains(currentVertex)) {
                if (currentVertex == end) {
                    found = true;
                } else {
                    visited.add(currentVertex);
                    List<Edge> vertices = getOutgoingEdges(currentVertex);
                    vertices.stream().filter(e -> !visited.contains(e.endVertex)).map(e -> e.endVertex).forEach(v -> {
                        q.add(v);
                        pathTracker.put(v, currentVertex); // i.e., reached vertex v from currentVertex
                    });
                }
            }
        }

        List<Vertex> path;
        if (!found) {
            path = Collections.emptyList();
        } else {
            // found a path, so reconstruct it beginning from the end and moving backward towards the start
            path = buildPathStartingFromEnd(end, pathTracker);
        }

        return path;
    }

    public List<Vertex> findPathViaBFS(char start, char end) {
        return findPathViaBFS(vertices.get(start), vertices.get(end));
    }

    public List<Vertex> findPathViaDFS(char start, char end) {
        return findPathViaDFS(vertices.get(start), vertices.get(end));
    }

    public List<Vertex> findPathViaDFS(Vertex start, Vertex end) {
        if (start == end) {
            return Collections.singletonList(start);
        }

        // need to track how we got to each node so that we can build the path backwards at the end
        Map<Vertex, Vertex> pathTracker = new HashMap<>(vertices.size() * 2);
        pathTracker.put(start, null);

        Set<Vertex> visited = new HashSet<>(vertices.size() * 2);
        Stack<Vertex> s = new Stack<>();
        s.push(start);

        boolean found = false;
        visited.add(start);

        while (!found && !s.isEmpty()) {
            Vertex currentVertex = s.peek();
            Vertex adjacentVertex = getAdjacentUnvisitedVertex(currentVertex, visited);
            if (adjacentVertex == null) {
                s.pop();
            } else {
                visited.add(adjacentVertex);
                if (adjacentVertex == end) {
                    found = true;
                } else {
                    s.push(adjacentVertex);
                }
                pathTracker.put(adjacentVertex, currentVertex);
            }
        }

        List<Vertex> path;
        if (!found) {
            path = Collections.emptyList();
        } else {
            path = buildPathStartingFromEnd(end, pathTracker);
        }

        return path;
    }

    public List<List<Edge>> computeMinimumSpanningForest() {
        // this method utilizes Prim's Algorithm to compute the Minimum Spanning Forest (MSF) for this graph.
        // If the graph is connected then the forest will consist of a single Minimum Spanning Tree (MST),
        // otherwise it will consist of the union of the MSTs covering all the graph's vertices.
        // (https://en.wikipedia.org/wiki/Prim%27s_algorithm)

        List<List<Edge>> msf = new LinkedList<>();
        int numVertices = vertices.size();
        if (vertices.size() == 0) {
            System.out.println("graph is empty, so no MST exists");
            return Collections.emptyList();
        }

        Set<Vertex> inTheForest = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(numVertices*2, (e1,e2) -> Integer.compare(e1.weight, e2.weight));

        Vertex currentVertex = vertices.values().iterator().next();
        inTheForest.add(currentVertex);

        List<Edge> currentMST = new LinkedList<>();
        msf.add(currentMST);

        do {
            while (inTheForest.size() < numVertices) {
                List<Edge> outboundEdges = adjacencyLists.get(currentVertex);
                outboundEdges.stream().filter(e -> !inTheForest.contains(e.endVertex)).forEach(e -> pq.offer(e));
                Edge newEdgeForMST = null;
                do {
                    newEdgeForMST = (!pq.isEmpty()) ? pq.remove() : null;
                } while (!pq.isEmpty() && inTheForest.contains(newEdgeForMST.endVertex));

                if (newEdgeForMST == null) {
                    //no edges in the tree are to vertices that are not already part of the mst
                    break;
                }

                currentMST.add(newEdgeForMST);
                currentVertex = newEdgeForMST.endVertex;
                inTheForest.add(currentVertex);
            }

            if (inTheForest.size() < numVertices) {
                // graph must not be fully connected, so pick a vertex not yet in the MSF and restart from there
                Sets.SetView<Vertex> verticesNotYetInForest = Sets.difference(getVertices(), inTheForest);
                currentVertex = verticesNotYetInForest.iterator().next();
                currentMST = new LinkedList<>();
                msf.add(currentMST);
                pq.clear();
            }
        } while (inTheForest.size() < numVertices);

        return msf;
    }

    private List<Vertex> buildPathStartingFromEnd(Vertex end, Map<Vertex, Vertex> pathTrackerMap) {
        List<Vertex> path = new LinkedList<>();
        Vertex v = end;

        while (v != null) {
            path.add(0, v);
            v = pathTrackerMap.get(v);
        }
        return path;
    }

    private Vertex getAdjacentUnvisitedVertex(Vertex currentVertex, Set<Vertex> visited) {
        List<Edge> adjacentVertices = adjacencyLists.get(currentVertex);
        for (Edge edge : adjacentVertices) {
            Vertex endVertex = edge.endVertex;
            if (!visited.contains(endVertex)) {
                return endVertex;
            }
        }

        return null;
    }

    static class Edge implements Comparable<Edge> {
        private Vertex startVertex;
        private final Vertex endVertex;
        private final int weight;

        public Vertex getStartVertex() {
            return startVertex;
        }

        public Vertex getEndVertex() {
            return endVertex;
        }

        public int getWeight() {
            return weight;
        }

        public Edge(Vertex startVertex, Vertex endVertex, int weight) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
            this.weight = weight;
        }

        public Edge(Vertex startVertex, Vertex endVertex) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
            this.weight = Integer.MAX_VALUE;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Edge)) {
                return false;
            }

            Edge that = (Edge) o;
            return ((this.startVertex.equals(that.startVertex) && this.endVertex.equals(that.endVertex))
                    || (this.startVertex.equals(that.endVertex) && this.endVertex.equals((that.startVertex))));
        }

        public int compareTo(Edge o) {
            return Integer.compare(this.weight, o.weight);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Vertex: ").append(startVertex).append(" ---> ").append("Vertex: ").append(endVertex)
                    .append("; weight: ").append(weight);

            return sb.toString();
        }
    }

    static class Vertex {
        private char label;

        public char getLabel() {
            return label;
        }

        public Vertex(char label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object obj) {
            return ((obj instanceof Vertex) && (this.label == ((Vertex) obj).label));
        }

        @Override
        public int hashCode() {
            return Character.hashCode(this.label);
        }

        @Override
        public String toString() {
            return Character.toString(this.label);
        }
    }
}
