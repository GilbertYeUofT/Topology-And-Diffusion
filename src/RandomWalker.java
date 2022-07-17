import java.util.ArrayList;
import java.util.Random;

public class RandomWalker {
    private Vertex curVertex;
    private Graph_M graph;

    public RandomWalker(Vertex vertex, Graph_M graph) {
        this.curVertex = vertex;
        this.graph = graph;
    }

    public void setCurVertex(Vertex curVertex) {
        this.curVertex = curVertex;
    }

    public Vertex getCurVertex() {
        return curVertex;
    }


    /**
     * Runs the random walker algorithm once, update fields
     */
    public void run(Random rand) {

        Object[] neighbors = curVertex.getKeySet().toArray();

        int choice = rand.nextInt(neighbors.length);
        //move randomwalker by 1 step
        String nextVertex = (String) neighbors[choice];
        this.graph.updateEdge(curVertex.getName(), nextVertex);
        curVertex = this.graph.getVertex(nextVertex);
    }

    /**
     * Helper method for generating graphs
     * @param rand
     */
    public void generateRun(Random rand){
        Object[] neighbors = curVertex.getKeySet().toArray();

        int choice = rand.nextInt(neighbors.length);
        //move randomwalker by 1 step
        String nextVertex = (String) neighbors[choice];
        curVertex = this.graph.getVertex(nextVertex);
    }
}
