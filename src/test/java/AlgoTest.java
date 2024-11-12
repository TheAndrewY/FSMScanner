import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test Class specializing in Algorithm testing.
 */
public class AlgoTest {

    /**
     * Tests input of supplied FSM and output of all error-inducing subsequences for non-accumulation typestate FSM 3.3: C-style pointers.
     *
     * @throws IOException when provided 'algo-test1' file is not found or error in reading file.
     */
    @Test
    public void algoTest1() throws IOException {
        DefaultDirectedGraph<String, DefaultEdge> algoGraph1 = new DefaultDirectedGraph<>(DefaultEdge.class);
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(Function.identity());
        try (FileReader reader = new FileReader("src/test/resources/algo-test1")) {
            importer.importGraph(algoGraph1, reader);
        }
        MyUtils utils = new MyUtils();
        Set<GraphPath<String, DefaultEdge>> errSubseqs = utils.allErrPaths(algoGraph1);
        //If the found number of error-inducing subsequences is not equal, immediately fail testcase
        assertEquals(errSubseqs.size(), 8, "# of error-inducing subsequences does not match");

        // Set of predetermined (manually found) expected results
        // String representation of all subsequences/paths
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("[(valid:dangle),(dangle:null),(null:err)]");
        expectedSet.add("[(valid:dangle),(dangle:err)]");
        expectedSet.add("[(valid:null),(null:err)]");
        expectedSet.add("[(dangle:valid),(valid:null),(null:err)]");
        expectedSet.add("[(dangle:null),(null:err)]");
        expectedSet.add("[(dangle:err)]");
        expectedSet.add("[(null:valid),(valid:dangle),(dangle:err)]");
        expectedSet.add("[(null:err)]");

        for (GraphPath<String, DefaultEdge> x : errSubseqs) {
            assertTrue(expectedSet.contains(x.toString().replaceAll("\\s", "")), "error-inducing subsequence mismatch");
        }
    }
    /**
     * Tests the pathToGraph method in Utils specifically with "import3" FSM/Graph
     *
     * @throws IOException if an I/O error occurs during the reading of the DOT file.
     */

    @Test
    public void pathToGraph1() throws IOException {
        DefaultDirectedGraph<String, DefaultEdge> origGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(Function.identity());
        try (FileReader reader = new FileReader("src/test/resources/import3")) {
            importer.importGraph(origGraph, reader);
        }
        MyUtils utils = new MyUtils();
        AllDirectedPaths<String,DefaultEdge> adp = new AllDirectedPaths<>(origGraph);
        ArrayList<String> pathGraphs = new ArrayList<>();
        for(GraphPath<String,DefaultEdge> x : adp.getAllPaths("Not Studying","Sleeping",true,null)){
            pathGraphs.add(utils.pathToGraph(x).toString().replaceAll("\\s", ""));
        }
        ArrayList<String> validGraphs = new ArrayList<>();
        validGraphs.add("([NotStudying,Studying,Playing,Sleeping],[(NotStudying,Studying),(Studying,Playing),(Playing,Sleeping)])");
        validGraphs.add("([NotStudying,Playing,Sleeping],[(NotStudying,Playing),(Playing,Sleeping)])");
        validGraphs.add("([NotStudying,Sleeping],[(NotStudying,Sleeping)])");
        assertTrue(pathGraphs.containsAll(validGraphs));
    }
}
