Problem:

Given a set of projects to build and a listing of dependencies that specifices when specific projects must be built before others in the set, determine a valid build order for the entire set or if there is no valid build order (i.e., one that satisfies all the dependencies) then determine that.

Solution:

This type of problem is best modeled as a directed graph.  Once that is done, we know that any vertices (i.e., "projects") with no incoming edges can be added to the build order (since they have no dependencies), and then we remove those vertices and their outgoing edges from the graph, look for projects with no incoming edges to add to the build order and repeat until we either are able to add all the projects to the build order or end up with a subset of projects that cannot be added since they have unresolvable dependencies, which should only happen if there is a cycle in the graph (e.g., two projects depend on each other).