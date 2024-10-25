import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Class specializing in Serialization testing
 */
public class SerializationTest {

    /**
     * Tests exporting a created graph/FSM into DOT format
     *
     * @throws IOException
     */
    @Test
    public void testExport() throws IOException {
        Graph<String, DefaultEdge> exportGraph1 = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Sample graph based on the simple open, close, error FSM for files
        String open = "Open";
        String close = "Close";
        String error = "Error";
        exportGraph1.addVertex(open);
        exportGraph1.addVertex(close);
        exportGraph1.addVertex(error);

        exportGraph1.addEdge(open,close);
        exportGraph1.addEdge(close,close);
        exportGraph1.addEdge(open,error);


        // Exports the graph into DOT format
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(Vertex -> Vertex);
        File output = new File("src/test/resources/test-output/output1");
        exporter.exportGraph(exportGraph1, output);

        long exitCode = Files.mismatch(Paths.get(output.getAbsolutePath()),Paths.get("src/test/resources/expected1"));

        assertEquals(-1,exitCode,"Serialization Export test1 Failed");
    }


    /**
     * Tests the import of a graph from a DOT file.
     *
     * @throws IOException
     */
    @Test
    public void testImportGraphFromFile() throws IOException {

        // Checking deserialization of "import1" graph/FSM
        DefaultDirectedGraph<String, DefaultEdge> testGraph1 = new DefaultDirectedGraph<>(DefaultEdge.class);
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        // JGraphT by default doesn't format DOT with vertex names.
        // Use identity function to replace vertex indices with names in output.
        importer.setVertexFactory(Function.identity());

        try (FileReader reader = new FileReader("src/test/resources/import1")) {
            importer.importGraph(testGraph1, reader);
        }

        String expectedToString = "([Open, Close, Error], [(Open,Close), (Close,Close), (Open,Error)])";
        assertEquals(expectedToString.replaceAll("\\s",""), testGraph1.toString().replaceAll("\\s",""), "Graph String representation test 1 failed");

        // Checking deserialization of "import2" graph/FSM
        testGraph1 = new DefaultDirectedGraph<>(DefaultEdge.class);
        try (FileReader reader = new FileReader("src/test/resources/import2")) {
            importer.importGraph(testGraph1, reader);
        }



        String expectedToString2 = "([Not Studying, Studying, Playing], [(Not Studying,Studying), (Not Studying,Playing), " +
                "(Studying,Playing), (Playing,Not Studying), (Playing,Studying)])";
        //Ignoring whitespaces to check equality
        assertEquals(expectedToString2.replaceAll("\\s",""), testGraph1.toString().replaceAll("\\s",""), "Graph String representation test 2 failed");
    }
}
