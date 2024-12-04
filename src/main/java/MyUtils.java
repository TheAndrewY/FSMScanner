import org.jetbrains.annotations.Nullable;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {
    /**
     * Method to print out all paths in a FSM/graph that start in a state and end in the err state.
     *
     * @param inputG the directed graph from which to obtain all error sequences and subsequences.
     * @param start the label of the starting vertex for the desired err paths
     * @return a set of all error-inducing subsequences (paths) in the FSM.
     */
    public Set<GraphPath<String, LabeledEdge>> allErrPaths(DefaultDirectedGraph<String, LabeledEdge> inputG,String start) {
        AllDirectedPaths<String, LabeledEdge> allPathSupplier = new AllDirectedPaths<>(inputG);
        HashSet<GraphPath<String, LabeledEdge>> setPaths = new HashSet<>();
        List<GraphPath<String, LabeledEdge>> allPaths = allPathSupplier.getAllPaths(start, "err", true, null);
        setPaths.addAll(allPaths);
        return setPaths;
    }
    /**
     * Method to convert a GraphPath object into a DefaultDirectedGraph object
     *
     * @param path the GraphPath object you want to convert into a directed graph.
     * @return Directed Graph containing all the path's vertices and edges
     */
    public DefaultDirectedGraph<String,LabeledEdge> pathToGraph(GraphPath<String, LabeledEdge> path){
        DefaultDirectedGraph<String,LabeledEdge> result = new DefaultDirectedGraph<>(LabeledEdge.class);
        for(String x : path.getVertexList()){
            result.addVertex(x);
        }
        for(LabeledEdge x : path.getEdgeList()){
            result.addEdge(x.getSource(),x.getTarget(),new LabeledEdge(x.getLabels()));
        }
        return result;
    }
    /**
     * Method that takes in a list of LabeledEdges and converts it into a GraphPath
     *
     * @param listPath An arraylist containing ordered LabeledEdges
     * @return GraphPath with same-ordered LabeledEdges, or null if listPath represents an invalid path
     */
    public @Nullable GraphPath<String,LabeledEdge> listToPath(List<LabeledEdge> listPath){
        // Construct a graph from the list
        DefaultDirectedGraph<String,LabeledEdge> result = new DefaultDirectedGraph<>(LabeledEdge.class);
        for(LabeledEdge x : listPath){
            result.addVertex(x.getSource());
            result.addVertex(x.getTarget());
            result.addEdge(x.getSource(),x.getTarget(),new LabeledEdge(x.getLabels()));
        }
        AllDirectedPaths<String,LabeledEdge> adp = new AllDirectedPaths<>(result);
        String pathSrc = listPath.get(0).getSource();
        String pathTarget = listPath.get(listPath.size()-1).getTarget();
        // We look for a non-simple path when said path is just a looped edge to allow
        // adp to fetch the relatively "non-trivial" path of going through the looped edge
        // rather than going through no edge
        if(pathSrc.equals(pathTarget)){
            return adp.getAllPaths(pathSrc,pathTarget,false,1).get(1);
        }
        //If the path is not just a looped edge, we need to consider the most complicated path in this
        // minimized graph mimicking the listPath, which will give us the identical graphPath
        List<GraphPath<String, LabeledEdge>> resolvedPath = adp.getAllPaths(pathSrc,pathTarget,false,listPath.size());
        if (!resolvedPath.isEmpty()){
            return resolvedPath.get(resolvedPath.size()-1);
        }
        // Return null if adp cannot find any valid path from pathSrc to pathTarget,
        // usually the result of an invalid listPath provided.
        return null;
    }
    /**
     * Parses a DOT file using a regex pattern to manually import a DOT-formatted file into
     * a LabeledEdge DefaultDirected Graph
     *
     * @param filePath the path to the DOT file
     * @return a DefaultDirectedGraph containing vertices and labeled edges as specified in the DOT file
     * @throws IOException if an I/O error occurs during file reading
     */
    public DefaultDirectedGraph<String,LabeledEdge> dotToFSM(String filePath) throws IOException {
        DefaultDirectedGraph<String, LabeledEdge> graph = new DefaultDirectedGraph<>(LabeledEdge.class);
        Pattern pattern = Pattern.compile("(.+) -> (.+) \\[ label=\"([^\"]+)\" \\];"
        );
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String source = matcher.group(1).trim().replaceAll("\"","");
                    String target = matcher.group(2).trim().replaceAll("\"","");
                    String label = matcher.group(3).trim().replaceAll("\"","");

                    // Ensure the vertices are added to the graph
                    graph.addVertex(source);
                    graph.addVertex(target);

                    // Add the new label to the edge if edge already
                    // exists, if it doesn't exist, create a new edge.
                    if(graph.containsEdge(source,target)){
                        graph.getEdge(source,target).addLabel(label);
                    }else{
                        graph.addEdge(source, target, new LabeledEdge(label));
                    }
                }
            }
            return graph;
        }
    }
    /**
     * Generates all non-empty, ordered sub paths from a given GraphPath.
     *
     * @param path the GraphPath from which to generate sub paths
     * @return a set of graphPaths, where each path represents a valid subpath of "path"
     */
    public Set<GraphPath<String,LabeledEdge>> generateSubpaths(GraphPath<String, LabeledEdge> path) {
        List<LabeledEdge> edgeList = new ArrayList<>(path.getEdgeList());
        List<List<LabeledEdge>> subpaths = new ArrayList<>();
        // Utilize TreeSet to be able to compare string representation of GraphPaths for equality
        Set<GraphPath<String,LabeledEdge>> result = new TreeSet<>(Comparator.comparing(GraphPath<String,LabeledEdge>::toString));
        subpathHelper(edgeList, new ArrayList<>(), subpaths);
        for(List<LabeledEdge> listPath : subpaths){
            if(listPath!=null){
                GraphPath<String,LabeledEdge> listToPathHolder = listToPath(listPath);
                if(listToPathHolder != null){
                    result.add(listToPathHolder);
                }
            }
        }
        return result;
    }

    /**
     * Recursively generates all non-empty ordered subsets of edges from a given list.
     * <p>
     * This method creates subsets by iterating through the edges from edgeList, adding each edge to the current subset,
     * and recursively backtracking with the remaining edges in edgeList. Each non-empty subset is thus added to the list
     * of subpaths by the end.
     *
     * @param edgeList the original list of edges to generate subsets from
     * @param current the current subset of edges being constructed
     * @param subpaths a list to store all generated non-empty subsets of edges
     */
    private void subpathHelper(List<LabeledEdge> edgeList, List<LabeledEdge> current, List<List<LabeledEdge>> subpaths) {
        if (!current.isEmpty()) {
            subpaths.add(new ArrayList<>(current));
        }
        for (int i = 0; i < edgeList.size(); i++) {
            LabeledEdge edge = edgeList.get(i);
            current.add(edge);
            subpathHelper(edgeList.subList(i + 1, edgeList.size()), current, subpaths);
            current.remove(current.size() - 1);
        }
    }
}