import java.util.HashMap;
import java.util.Set;

public class Vertex {
    private HashMap<String, Integer> neighbors = new HashMap<>();
    private String name;
    private int degree;

    public Vertex(String name){
        this.name = name;
        this.degree = 0;
    }
    public void updateDegree(){
        this.degree = this.neighbors.size();
    }

    public int getDegree(){
        return this.degree;
    }

    public String getName() {
        return name;
    }

    public boolean hasNeighbor(String neighbor){
        return this.neighbors.containsKey(neighbor);
    }
    public void addNeighbor(String neighbor, int value){
        this.neighbors.put(neighbor, value);
    }
    public void updateNeighbor(String neighbor){
        if(this.neighbors.containsKey(neighbor)){
            this.neighbors.put(neighbor, this.neighbors.get(neighbor) + 1);
        }
    }

    public int getNeighbor(String neighbor){
        return this.neighbors.getOrDefault(neighbor, -1);
    }

    public void removeNeighbor(String neighbor){
        this.neighbors.remove(neighbor);
    }

    public Set<String> getKeySet(){
        return this.neighbors.keySet();
    }

    public int getSize(){
        return this.neighbors.size();
    }

    public int sumVisits(){
        int sum = 0;
        for (String neighbor : this.neighbors.keySet()){
            sum += this.getNeighbor(neighbor);
        }
        return sum;
    }

}
