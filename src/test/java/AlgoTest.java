import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        MyUtils utils = new MyUtils();
        DefaultDirectedGraph<String, LabeledEdge> algoGraph1 = utils.dotToFSM("src/test/resources/algo-test1");
        Set<GraphPath<String, LabeledEdge>> errSubseqs = utils.allErrPaths(algoGraph1);
        //If the found number of error-inducing subsequences is not equal, immediately fail testcase
        assertEquals(errSubseqs.size(), 8, "# of error-inducing subsequences does not match");

        // Set of predetermined (manually found) expected results
        // String representation of all subsequences/paths
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("[free, assign null, free]");
        expectedSet.add("[free, free]");
        expectedSet.add("[assign null, free]");
        expectedSet.add("[assign valid, assign null, free]");
        expectedSet.add("[free]");
        expectedSet.add("[assign valid, free, free]");
        for (GraphPath<String, LabeledEdge> x : errSubseqs) {
            assertTrue(expectedSet.contains(x.toString()), "error-inducing subsequence mismatch");
        }
    }
    /**
     * Tests the pathToGraph method in Utils specifically with "import3" FSM/Graph
     *
     * @throws IOException if an I/O error occurs during the reading of the DOT file.
     */

    @Test
    public void pathToGraph1() throws IOException {
        MyUtils utils = new MyUtils();
        DefaultDirectedGraph<String, LabeledEdge> origGraph = utils.dotToFSM("src/test/resources/import3");
        AllDirectedPaths<String,LabeledEdge> adp = new AllDirectedPaths<>(origGraph);
        ArrayList<String> pathGraphs = new ArrayList<>();
        for(GraphPath<String,LabeledEdge> x : adp.getAllPaths("Not Studying","Sleeping",true,null)){
            pathGraphs.add(utils.pathToGraph(x).toString().replaceAll("\\s", ""));
        }
        ArrayList<String> validGraphs = new ArrayList<>();
        validGraphs.add("([NotStudying,Studying,Playing,Sleeping],[Study=(NotStudying,Studying),Play=(Studying,Playing),Sleep=(Playing,Sleeping)])");
        validGraphs.add("([NotStudying,Playing,Sleeping],[Play=(NotStudying,Playing),Sleep=(Playing,Sleeping)])");
        validGraphs.add("([NotStudying,Sleeping],[Sleep=(NotStudying,Sleeping)])");
        assertTrue(pathGraphs.containsAll(validGraphs));
    }
}
