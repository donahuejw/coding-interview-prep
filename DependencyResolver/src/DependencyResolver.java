import java.util.*;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class DependencyResolver {

  public static DependencyResolver INSTANCE = new DependencyResolver();

  private DependencyResolver(){}

  public List<Character> computeValidOrder(char[] projects, List<ProjectPair> projectDependencies) {
    // To process the list of dependencies we'll need to map each project to its index in the adjacency matrix
    // so build a map here for that purpose
    Map<Character, Integer> projectIndices = new HashMap<>();
    for (int i=0; i<projects.length; i++) {
      projectIndices.put(projects[i], i);
    }

    // build the matrix of dependencies
    BitSet[] dependencyMatrix = new BitSet[projects.length];
    for (int idx = 0; idx<projects.length; idx++) {
      dependencyMatrix[idx] = new BitSet(projects.length);
    }

    BitSet projectsWithUnmetDependencies = new BitSet(projects.length);
    projectDependencies.forEach(pp -> {
      verifyDependency(pp, projectIndices);
      int dependencyIndex = projectIndices.get(pp.dependency);
      int dependentProjIndex = projectIndices.get(pp.dependentProject);
      dependencyMatrix[dependentProjIndex].set(dependencyIndex);
      projectsWithUnmetDependencies.set(dependentProjIndex);
    });

    // All projects that have no dependencies can be added to the build
    // order to start, since they can be built in any order
    List<Character> validOrder = new LinkedList<>();
    for (int idx = 0; idx<projects.length; idx++) {
      if (!projectsWithUnmetDependencies.get(idx)) {
        validOrder.add(projects[idx]);
      }
    }

    if (!validOrder.isEmpty()) { //i.e., we have at least one project to build
      boolean noProjectsCanBeAddedToBuildOrder = false;

      while (!projectsWithUnmetDependencies.isEmpty() && !noProjectsCanBeAddedToBuildOrder) {
        // look for project with unmet dependencies that are only contained in the set of projects with no remaining dependencies
        boolean atLeastOneProjectAddedToBuildOrder = false;

        for (int dmIdx = projectsWithUnmetDependencies.nextSetBit(0); dmIdx>=0; dmIdx = projectsWithUnmetDependencies.nextSetBit(dmIdx+1)) {
          Character projectToPossiblyAddToBuildOrder = projects[dmIdx];

          BitSet dependencies = dependencyMatrix[dmIdx];
          if (!dependencies.isEmpty()) {
            for (int dIdx = dependencies.nextSetBit(0); dIdx>=0; dIdx = dependencies.nextSetBit(dIdx+1)) {
              if (dependencyMatrix[dIdx].isEmpty()) {
                dependencies.clear(dIdx);
              }
            }

            if (dependencies.isEmpty()) {
              validOrder.add(projectToPossiblyAddToBuildOrder);
              projectsWithUnmetDependencies.clear(dmIdx);
              atLeastOneProjectAddedToBuildOrder = true;
            }
          }
        }
        noProjectsCanBeAddedToBuildOrder = !atLeastOneProjectAddedToBuildOrder;
      }

      if (projectsWithUnmetDependencies.isEmpty()) {
        return validOrder;
      }
    }

    // no valid build order - must be a cycle in the dependency graph
    return Collections.emptyList();
  }

  private void verifyDependency(ProjectPair pp, Map<Character, Integer> projectIndices) {
    if (!projectIndices.containsKey(pp.dependency)) {
      throw new IllegalArgumentException("provided list of dependencies contains an entry ('" + pp.dependency
              + "') that is not included in the projects list as expected");
    }

    if (!projectIndices.containsKey(pp.dependentProject)) {
      throw new IllegalArgumentException("provided list of dependencies contains an entry ('" + pp.dependentProject
              + "' that is not included in the projects list as expected");
    }
  }

  public static class ProjectPair {
    private char dependentProject;
    private char dependency;
    private static Pattern validationRegex = Pattern.compile("([a-z]|[A-Z]){1}");

    public ProjectPair(char dependency, char dependentProject) {
      Preconditions.checkArgument(validationRegex.matcher("" + dependency).matches(), "dependency argument must be a letter of the english alphabet");
      Preconditions.checkArgument(validationRegex.matcher("" + dependentProject).matches(), "dependentProject argument must be a letter of the english alphabet");

      this.dependentProject = Character.toLowerCase(dependentProject);
      this.dependency = Character.toLowerCase(dependency);
    }
  }
}
