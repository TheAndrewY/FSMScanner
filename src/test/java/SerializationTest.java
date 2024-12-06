import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.junit.jupiter.api.Test;

/** Test Class specializing in Serialization testing */
public class SerializationTest {

  /**
   * Tests exporting a created graph/FSM into DOT format
   *
   * @throws IOException
   */
  @Test
  public void testExport() throws IOException {
    Graph<String, DefaultEdge> exportGraph1 = new DefaultDirectedGraph<>(LabeledEdge.class);

    // Sample graph based on the simple open, close, error FSM for files
    String open = "Open";
    String close = "Close";
    String error = "Error";
    exportGraph1.addVertex(open);
    exportGraph1.addVertex(close);
    exportGraph1.addVertex(error);

    exportGraph1.addEdge(open, close, new LabeledEdge("closeDoor"));
    exportGraph1.addEdge(close, close, new LabeledEdge("closeDoor"));
    exportGraph1.addEdge(open, error, new LabeledEdge("openDoor"));

    // Exports the graph into DOT format
    DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(Vertex -> Vertex);
    exporter.setEdgeAttributeProvider(
        edge -> {
          Map<String, Attribute> attributes = new LinkedHashMap<>();
          attributes.put("label", DefaultAttribute.createAttribute(edge.toString()));
          return attributes;
        });
    File output = new File("src/test/resources/test-output/output1");
    exporter.exportGraph(exportGraph1, output);

    long exitCode =
        Files.mismatch(
            Paths.get(output.getAbsolutePath()), Paths.get("src/test/resources/expected1"));

    assertEquals(-1, exitCode, "Serialization Export test1 Failed");
  }

  /**
   * Tests the import of a graph from a DOT file.
   *
   * @throws IOException
   */
  @Test
  public void testImportGraphFromFile() throws IOException {
    // Checking deserialization of "import1" graph/FSM
    MyUtils utils = new MyUtils();
    DefaultDirectedGraph<String, LabeledEdge> testGraph1 =
        utils.dotToFSM("src/test/resources/import1");
    String expectedToString =
        "([Open, Close, Error], [closeDoor=(Open,Close), closeDoor=(Close,Close), openDoor=(Open,Error)])";
    assertEquals(
        expectedToString.replaceAll("\\s", ""),
        testGraph1.toString().replaceAll("\\s", ""),
        "Graph String representation test 1 failed");
    // Checking deserialization of "import2" graph/FSM
    testGraph1 = utils.dotToFSM("src/test/resources/import2");
    String expectedToString2 =
        "([Not Studying, Studying, Playing], [Study=(Not Studying,Studying), Play=(Not Studying,Playing), "
            + "Play=(Studying,Playing), Break=(Playing,Not Studying), Study=(Playing,Studying)])";
    // Ignoring whitespaces to check equality
    assertEquals(
        expectedToString2.replaceAll("\\s", ""),
        testGraph1.toString().replaceAll("\\s", ""),
        "Graph String representation test 2 failed");
  }
}
