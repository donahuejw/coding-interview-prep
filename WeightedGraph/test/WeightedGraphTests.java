import com.google.common.collect.Lists;
import org.hamcrest.CustomMatcher;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsCollectionContaining.*;
import static org.junit.Assert.*;


public class WeightedGraphTests {
    @Test
    public void getVerticesReturnsExpectedVerticesTest() throws Exception {
        WeightedGraph wg = new WeightedGraph();
        WeightedGraph.Vertex vA = wg.addVertex('a');
        WeightedGraph.Vertex vB = wg.addVertex('b');
        WeightedGraph.Vertex vC = wg.addVertex('c');
        WeightedGraph.Vertex vD = wg.addVertex('d');

        final List<WeightedGraph.Vertex> expectedVertices = Lists.newArrayList(vA, vB, vC, vD);
        assertThat(wg.getVertices(), new CustomMatcher<Set<WeightedGraph.Vertex>>("Expecting vertices 'a', 'b', 'c', and 'd'") {
            @Override
            public boolean matches(Object o) {
                return ((Set<WeightedGraph.Vertex>) o).containsAll(expectedVertices);
            }
        });
    }

    @Test
    public void getOutgoingEdgesReturnsExpectedEdgesTest() throws Exception {
        WeightedGraph wg = new WeightedGraph();
        WeightedGraph.Vertex vA = wg.addVertex('a');
        WeightedGraph.Vertex vB = wg.addVertex('b');
        WeightedGraph.Vertex vC = wg.addVertex('c');
        WeightedGraph.Vertex vD = wg.addVertex('d');
        wg.addEdge(vA, vB);
        wg.addEdge(vA, vC);
        wg.addEdge(vA, vD);

        final List<WeightedGraph.Vertex> expectedVertices = Lists.newArrayList(vA, vB, vC, vD);
        assertThat(wg.getVertices(), new CustomMatcher<Set<WeightedGraph.Vertex>>("Expecting vertices 'a', 'b', 'c', and 'd'") {
            @Override
            public boolean matches(Object o) {
                return ((Set<WeightedGraph.Vertex>) o).containsAll(expectedVertices);
            }
        });

        List<WeightedGraph.Edge> outgoingEdges = wg.getOutgoingEdges(vA);
        assertEquals(3, outgoingEdges.size());
        assertThat(outgoingEdges.stream().map(e -> e.getEndVertex()).collect(Collectors.toList()), hasItems(vB, vC, vD));
    }

    @Test
    public void getVerticesReturnsEmptySetForEmptyGraphTest() throws Exception {
        WeightedGraph wg = new WeightedGraph();
        Set<WeightedGraph.Vertex> vertices = wg.getVertices();
        assertNotNull(vertices);
        assertTrue(vertices.isEmpty());
    }

    @Test
    public void findPathViaBFSReturnsValidPathTest() throws Exception {
        WeightedGraph weightedGraph = buildComplexGraph();
        List<WeightedGraph.Vertex> path = weightedGraph.findPathViaBFS('a', 'k');
        assertEquals(path.get(0).getLabel(), 'a');
        assertEquals(path.get(path.size() - 1).getLabel(), 'k');

        WeightedGraph.Vertex last = null;
        for (WeightedGraph.Vertex pathVertex : path) {
            if (last != null) {
                weightedGraph.containsEdge(last, pathVertex);
            }
            last = pathVertex;
        }
    }

    @Test
    public void findPathViaDFSReturnsValidPathTest() throws Exception {
        WeightedGraph weightedGraph = buildComplexGraph();
        List<WeightedGraph.Vertex> path = weightedGraph.findPathViaDFS('a', 'k');
        assertEquals(path.get(0).getLabel(), 'a');
        assertEquals(path.get(path.size() - 1).getLabel(), 'k');

        WeightedGraph.Vertex last = null;
        for (WeightedGraph.Vertex pathVertex : path) {
            if (last != null) {
                weightedGraph.containsEdge(last, pathVertex);
            }
            last = pathVertex;
        }
    }

    @Test
    public void buildMSTForSimpleGraphTest() {
        WeightedGraph wg = buildSimpleGraph();

        List<WeightedGraph.Edge> mst = wg.computeMinimumSpanningTree();
        assertThat(mst, hasItems(
                new WeightedGraph.Edge(wg.getVertex('a'), wg.getVertex('d')),
                new WeightedGraph.Edge(wg.getVertex('a'), wg.getVertex('b')),
                new WeightedGraph.Edge(wg.getVertex('b'), wg.getVertex('e')),
                new WeightedGraph.Edge(wg.getVertex('e'), wg.getVertex('c')),
                new WeightedGraph.Edge(wg.getVertex('e'), wg.getVertex('f'))
        ));

    }

    @Test
    public void buildMSTForComplexGraphTest() {
        WeightedGraph wg = buildComplexGraph();

        List<WeightedGraph.Edge> mst = wg.computeMinimumSpanningTree();
        assertThat(mst, hasItems(
                new WeightedGraph.Edge(wg.getVertex('a'), wg.getVertex('c')),
                new WeightedGraph.Edge(wg.getVertex('c'), wg.getVertex('b')),
                new WeightedGraph.Edge(wg.getVertex('b'), wg.getVertex('d')),
                new WeightedGraph.Edge(wg.getVertex('d'), wg.getVertex('f')),
                new WeightedGraph.Edge(wg.getVertex('f'), wg.getVertex('e')),
                new WeightedGraph.Edge(wg.getVertex('e'), wg.getVertex('j')),
                new WeightedGraph.Edge(wg.getVertex('j'), wg.getVertex('l')),
                new WeightedGraph.Edge(wg.getVertex('l'), wg.getVertex('k')),
                new WeightedGraph.Edge(wg.getVertex('k'), wg.getVertex('g')),
                new WeightedGraph.Edge(wg.getVertex('g'), wg.getVertex('i')),
                new WeightedGraph.Edge(wg.getVertex('g'), wg.getVertex('h'))
        ));

    }


    private WeightedGraph buildSimpleGraph() {
        WeightedGraph wg = new WeightedGraph();
        wg.addEdge('a', 'b', 6);
        wg.addEdge('a', 'd', 4);
        wg.addEdge('b', 'c', 10);
        wg.addEdge('b', 'd', 7);
        wg.addEdge('b', 'e', 7);
        wg.addEdge('c', 'd', 8);
        wg.addEdge('c', 'e', 5);
        wg.addEdge('c', 'e', 6);
        wg.addEdge('d', 'e', 12);
        wg.addEdge('e', 'f', 7);

        return wg;
    }

    private WeightedGraph buildComplexGraph() {
        WeightedGraph wg = new WeightedGraph(12);
        wg.addEdge('a', 'b', 13);
        wg.addEdge('a', 'c', 6);
        wg.addEdge('b', 'c', 7);
        wg.addEdge('b', 'd', 1);
        wg.addEdge('c', 'd', 14);
        wg.addEdge('c', 'e', 8);
        wg.addEdge('c', 'h', 20);
        wg.addEdge('d', 'e', 9);
        wg.addEdge('d', 'f', 3);
        wg.addEdge('e', 'f', 2);
        wg.addEdge('e', 'j', 18);
        wg.addEdge('g', 'h', 15);
        wg.addEdge('g', 'i', 5);
        wg.addEdge('g', 'j', 19);
        wg.addEdge('g', 'k', 10);
        wg.addEdge('h', 'j', 17);
        wg.addEdge('i', 'k', 11);
        wg.addEdge('j', 'k', 16);
        wg.addEdge('j', 'l', 4);
        wg.addEdge('k', 'l', 12);

        return wg;
    }

    private WeightedGraph buildDeepGraph() {
        WeightedGraph wg = new WeightedGraph(9);
        wg.addEdge('a', 'b');
        wg.addEdge('a', 'c');
        wg.addEdge('a', 'd');
        wg.addEdge('a', 'e');
        wg.addEdge('b', 'f');
        wg.addEdge('f', 'h');
        wg.addEdge('d', 'g');
        wg.addEdge('g', 'i');

        return wg;
    }
}
