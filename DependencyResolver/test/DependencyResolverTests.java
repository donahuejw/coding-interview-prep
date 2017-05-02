import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

import com.google.common.collect.Lists;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;


public class DependencyResolverTests {
	
	@Test
	public void ResolverHandlesEmptyProjectsList() {
		List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(new char[0], Collections.emptyList());
		assertTrue(validOrder != null);
		assertTrue(validOrder.isEmpty());
	}

	@Test
    public void ResolverHandlesEmptyDependenciesList() {
        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(new char[]{'a','b','c','d'}, Collections.emptyList());
        assertTrue(validOrder != null);
        assertEquals(4, validOrder.size());
        assertThat("valid order does not contain all the projects as expected", validOrder, hasItems('a','b','c','d'));
    }

    @Test
    public void SimpleDependencyTreeTest() throws Exception {
	    char[] projects = new char[]{'a','b','c'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'a', 'b'},
                {'b', 'c'}
        });

        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);
        assertEquals("Expected specific order: 'a', 'b', 'c'", Lists.newArrayList('a', 'b', 'c'), validOrder);
    }

    @Test
    public void MultipleDependenciesForSomeProjectsTest() throws Exception {
	    char[] projects = new char[]{'a','b','c','d','e','f'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'a', 'c'},
                {'b', 'c'},
                {'c', 'd'},
                {'e', 'd'},
                {'d', 'f'}
        });

        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);
        assertEquals(validOrder, Lists.newArrayList('a','b','e','c','d','f'));
    }

    @Test
    public void MoreComplexDependenciesTest() throws Exception {
        char[] projects = new char[] {'a','b','c','d','e','f','g','h'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'d', 'g'},
                {'f', 'c'},
                {'f', 'b'},
                {'c', 'a'},
                {'h', 'e'},
                {'b', 'a'},
                {'b', 'h'},
                {'b', 'e'},
                {'a', 'e'}

        });

        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);

        // two valid build orders, so make sure we got back one of them
        assertThat(validOrder, anyOf(equalTo(Lists.newArrayList('d','f','b','c','g','a','h','e')), equalTo(Lists.newArrayList('d','f','b','c','g','h','a','e'))));
    }

    @Test
    public void ResolverHandlesProjectWithNoConnectionToDependencyGraph() {
        char[] projects = new char[]{'a','b','c'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'a', 'b'}
        });

        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);
        assertEquals(validOrder, Lists.newArrayList('a','c','b'));
    }

    @Test
    public void ResolverReportsErrorIfDependenciesListContainsProjectNotInProjectsList() {
        char[] projects = new char[]{'a','b'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'a', 'b'},
                {'b','c'}
        });

        boolean failedAsExpected = false;

        try {
            DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);
        } catch(Exception e) {
            if (e instanceof IllegalArgumentException) {
                failedAsExpected = true;
            }
        }

        assertTrue("Expected the computeValidOrder() method to fail", failedAsExpected);
    }

    @Test
    public void ResolverReturnsEmptyListIfNoValidOrderPossible() throws Exception {
        char[] projects = new char[]{'a','b','c'};
        List<DependencyResolver.ProjectPair> dependencies = buildDependencies(new char[][]{
                {'a','b'},
                {'b','c'},
                {'c','a'}
        });

        List<Character> validOrder = DependencyResolver.INSTANCE.computeValidOrder(projects, dependencies);
        assertTrue("Expected the valid order to be empty because dependency graph contains a cycle", validOrder.isEmpty());
    }

	
	private List<DependencyResolver.ProjectPair> buildDependencies(char[][] dependencies) {
		if (dependencies.length==0) {
			return Collections.emptyList();
		}
		
		List<DependencyResolver.ProjectPair> dependenciesList = new LinkedList<>();
        for (char[] dependency : dependencies) {
            if (dependency.length != 2) {
                throw new IllegalArgumentException("each dependency must be specified as a char[] with two elements");
            }
            dependenciesList.add(new DependencyResolver.ProjectPair(dependency[0], dependency[1]));
        }
		
		return dependenciesList;
	}
}