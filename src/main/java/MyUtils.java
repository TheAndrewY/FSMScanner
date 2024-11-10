import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyUtils {
    /**
     * Method to print out all paths in a FSM/graph that start in any state (except err) and end in the err state.
     *
     * @param inputG the directed graph from which to obtain all error sequences and subsequences.
     * @return a set of all error-inducing subsequences (paths) in the FSM.
     */
    public Set<GraphPath<String, DefaultEdge>> allErrPaths(DefaultDirectedGraph<String, DefaultEdge> inputG) {
        AllDirectedPaths<String, DefaultEdge> allPathSupplier = new AllDirectedPaths<>(inputG);
        HashSet<GraphPath<String, DefaultEdge>> setPaths = new HashSet<>();
        for (String vertex : inputG.vertexSet()) {
            if (!vertex.equals("err")) {
                List<GraphPath<String, DefaultEdge>> allPaths = allPathSupplier.getAllPaths(vertex, "err", true, null);
                setPaths.addAll(allPaths);
            }
        }
        return setPaths;
    }
}