import org.jgrapht.graph.DefaultEdge;

/** LabeledEdge class extending DefaultEdge to give edges/transitions in FSMs/Graphs labels. */
public class LabeledEdge extends DefaultEdge {
  private final String label;

  /**
   * Constructs a LabeledEdge with the specified label.
   *
   * @param label the label associated with this edge
   */
  public LabeledEdge(String label) {
    this.label = label;
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
   * Returns the label associated with this edge.
   *
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * String representation of this edge is just the label.
   *
   * @return the label of this edge
   */
  @Override
  public String toString() {
    return label;
  }
}
