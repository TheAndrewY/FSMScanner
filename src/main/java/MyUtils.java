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
    /**
     * Method to convert a GraphPath object into a DefaultDirectedGraph object
     *
     * @param path the GraphPath object you want to convert into a directed graph.
     * @return Directed Graph containing all the path's vertices and edges
     */
    public DefaultDirectedGraph<String,DefaultEdge> pathToGraph(GraphPath<String,DefaultEdge> path){
        DefaultDirectedGraph<String,DefaultEdge> result = new DefaultDirectedGraph<>(DefaultEdge.class);
        for(String x : path.getVertexList()){
            result.addVertex(x);
        }
        for(DefaultEdge x : path.getEdgeList()){
            result.addEdge(path.getGraph().getEdgeSource(x),path.getGraph().getEdgeTarget(x));
        }
        return result;
    }
}