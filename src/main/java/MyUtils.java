import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;

public class MyUtils {
  /**
   * Method to print out all paths in a FSM/graph that start in any state (except err) and end in
   * the err state.
   *
   * @param inputG the directed graph from which to obtain all error sequences and subsequences.
   * @return a set of all error-inducing subsequences (paths) in the FSM.
   */
  public Set<GraphPath<String, LabeledEdge>> allErrPaths(
      DefaultDirectedGraph<String, LabeledEdge> inputG) {
    AllDirectedPaths<String, LabeledEdge> allPathSupplier = new AllDirectedPaths<>(inputG);
    HashSet<GraphPath<String, LabeledEdge>> setPaths = new HashSet<>();
    for (String vertex : inputG.vertexSet()) {
      if (!vertex.equals("err")) {
        List<GraphPath<String, LabeledEdge>> allPaths =
            allPathSupplier.getAllPaths(vertex, "err", true, null);
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
  public DefaultDirectedGraph<String, LabeledEdge> pathToGraph(
      GraphPath<String, LabeledEdge> path) {
    DefaultDirectedGraph<String, LabeledEdge> result =
        new DefaultDirectedGraph<>(LabeledEdge.class);
    for (String x : path.getVertexList()) {
      result.addVertex(x);
    }
    for (LabeledEdge x : path.getEdgeList()) {
      result.addEdge(x.getSource(), x.getTarget(), new LabeledEdge(x.getLabel()));
    }
    return result;
  }

  /**
   * Parses a DOT file using a regex pattern to manually import a DOT-formatted file into a
   * LabeledEdge DefaultDirected Graph
   *
   * @param filePath the path to the DOT file
   * @return a DefaultDirectedGraph containing vertices and labeled edges as specified in the DOT
   *     file
   * @throws IOException if an I/O error occurs during file reading
   */
  public DefaultDirectedGraph<String, LabeledEdge> dotToFSM(String filePath) throws IOException {
    DefaultDirectedGraph<String, LabeledEdge> graph = new DefaultDirectedGraph<>(LabeledEdge.class);
    Pattern pattern = Pattern.compile("(.+) -> (.+) \\[ label=\"([^\"]+)\" \\];");
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String source = matcher.group(1).trim().replaceAll("\"", "");
          String target = matcher.group(2).trim().replaceAll("\"", "");
          String label = matcher.group(3).trim().replaceAll("\"", "");

          // Ensure the vertices are added to the graph
          graph.addVertex(source);
          graph.addVertex(target);

          // Add the labeled edge to the graph
          graph.addEdge(source, target, new LabeledEdge(label));
        }
      }
      return graph;
    }
  }
}