import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import java.io.*;

public class test {
    public static void main(String[] args) throws IOException {
        Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Sample graph based on the simple open, close, error FSM for files
        String open = "Open";
        String close = "Close";
        String error = "Error";
        g.addVertex(open);
        g.addVertex(close);
        g.addVertex(error);

        g.addEdge(open,close);
        g.addEdge(close,close);
        g.addEdge(open,error);

        System.out.println(g);

        // Exports the graph into DOT format
        DOTExporter<String, DefaultEdge> de = new DOTExporter<>(Vertex -> Vertex);
        de.exportGraph(g, new File("test2"));


        //Imports graph object from DOT format
        DefaultDirectedGraph<String, DefaultEdge> g2 = new DefaultDirectedGraph<>(DefaultEdge.class);
        DOTImporter<String,DefaultEdge> di = new DOTImporter<>();
        di.setVertexFactory(v -> v);
        di.importGraph(g2, new FileReader("test2"));



        System.out.println(g2);

    }
}
