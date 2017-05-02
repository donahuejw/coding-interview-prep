import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.BitSet;
import java.util.Set;
import java.util.HashSet;
import com.google.common.base.Preconditions;

public class BuildOrderManager {
  private static final int ASCII_VALUE_A = 97;

  public static void main(String[] args) {
    char[] projects = new char[] {'a','b','c','d','e','f','g','h'};
    List<ProjectPair> dependencies = new LinkedList<>();
    dependencies.add(new ProjectPair('d','g'));
    dependencies.add(new ProjectPair('f','c'));
    dependencies.add(new ProjectPair('f','b'));
    dependencies.add(new ProjectPair('c','a'));
    dependencies.add(new ProjectPair('f','a'));
    dependencies.add(new ProjectPair('b','a'));
    dependencies.add(new ProjectPair('b','h'));
    dependencies.add(new ProjectPair('b','e'));
    dependencies.add(new ProjectPair('a','e'));


    List<Character> buildOrder = computeBuildOrder(projects, dependencies);
    if (!buildOrder.isEmpty()) {
      System.out.println("valid build order is:");
      buildOrder.forEach(c -> System.out.println(c));
    } else {
      System.out.println("No valid build order could be determined");
    }
  }

  private static List<Character> computeBuildOrder(char[] projects, List<ProjectPair> projectDependencies) {
    // algorithm assumes all projects chars are consecutive, so validate
    for (int idx = 0; idx<projects.length-1; idx++) {
      int currentProject = (int) projects[idx];
      int nextProject = (int) projects[idx+1];
      Preconditions.checkArgument((nextProject - currentProject) == 1, "Expecting project letters to be consecutive");
    }

    // build the matrix of dependencies
    BitSet[] dependencyMatrix = new BitSet[projects.length];
    for (int idx = 0; idx<projects.length; idx++) {
      dependencyMatrix[idx] = new BitSet(projects.length);
    }

    BitSet projectsWithUnmetDependencies = new BitSet(projects.length);
    projectDependencies.forEach(pp -> {
      int dependencyIndex = ((int) pp.dependency) % ASCII_VALUE_A;
      int dependentProjIndex = ((int) pp.dependentProject) % ASCII_VALUE_A;
      dependencyMatrix[dependentProjIndex].set(dependencyIndex);
      projectsWithUnmetDependencies.set(dependentProjIndex);
    });

    // projects that have no dependencies can be added to the build order to start, since they can be built in any order
    List<Character> buildOrder = new LinkedList<>();
    for (int idx = 0; idx<projects.length; idx++) {
      if (!projectsWithUnmetDependencies.get(idx)) {
        buildOrder.add(projects[idx]);
      }
    }

    if (!buildOrder.isEmpty()) { //i.e., we have at least one project to build
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
              buildOrder.add(projectToPossiblyAddToBuildOrder);
              projectsWithUnmetDependencies.clear(dmIdx);
              atLeastOneProjectAddedToBuildOrder = true;
            }
          }
        }
        noProjectsCanBeAddedToBuildOrder = !atLeastOneProjectAddedToBuildOrder;
      }

      if (projectsWithUnmetDependencies.isEmpty()) {
        return buildOrder;
      }
    }

    // no valid build order - i.e., no projects with zero dependencies to start, or no way to resolve all dependencies for every project
    return Collections.emptyList();
  }

  private static class ProjectPair {
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
