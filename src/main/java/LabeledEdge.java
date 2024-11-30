import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * LabeledEdge class extending DefaultEdge to give
 * edges/transitions in FSMs/Graphs labels.
 */
public class LabeledEdge extends DefaultEdge {
    private final ArrayList<String> labels;

    /**
     * Constructs a LabeledEdge with the specified label.
     *
     * @param labels the label(s) associated with this edge
     */
    public LabeledEdge(String... labels) {
        this.labels = new ArrayList<>(Arrays.asList(labels));
    }

    /**
     * Constructs a LabeledEdge with the specific list of labels
     *
     * @param labels A list containing multiple labels that represent the edge
     */
    public LabeledEdge(ArrayList<String> labels) {
        this.labels = labels;
    }

    /**
     * Returns the source vertex of this edge.
     *
     * @return the source vertex
     */
    public String getSource() {
        return (String) super.getSource();
    }

    /**
     * Returns the target vertex of this edge.
     *
     * @return the target vertex
     */
    public String getTarget() {
        return (String) super.getTarget();
    }

    /**
     * Returns the list of labels of this edge
     *
     * @return the ArrayList of labels
     */
    public ArrayList<String> getLabels(){
        return labels;
    }
    /**
     * Adds a label to the labels list
     *
     * @param label the label which is being added
     */
    public void addLabel(String label){
        labels.add(label);
    }
    /**
     * String representation of this edge is just the label, or all labels is multiple are present.
     *
     * @return the label of this edge, or list of labels if there are multiple labels
     */
    @Override
    public String toString() {
        if(labels.size()==1) return labels.get(0);
        return labels.toString();
    }
    /**
     * Checks the equality of two labeled edges based on label, source, and target
     *
     * @return boolean of whether two edges are equivalent
     */
    boolean equals(LabeledEdge edge2){
        return labels.equals(edge2.getLabels()) && this.getSource().equals(edge2.getSource()) && this.getTarget().equals(edge2.getTarget());
    }
}
