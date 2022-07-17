import java.util.*;
public class DiffusionSimulator {


    //Currently only generates some number of 3D self avoiding random walkers
    public static void main(String[] args) {
        // Take integer n and runs, and seed from user
        int n, runs, seed, vertices;
        Scanner sc = new Scanner(System.in);
        System.out.print("Give the seed for random sequence: ");
        seed = sc.nextInt();
        System.out.print("Give the number of random steps: ");
        n = sc.nextInt();
        System.out.print("Give the number of starting random walkers: ");
        runs = sc.nextInt();
        System.out.print("Give the number of starting vertices for graph: ");
        vertices = sc.nextInt();
        sc.close();


        //Create the random walkers
        //Perhaps we would like to randomly choose starting vertices based on weight
        //This way hot spots are generated

        Random rand = new Random(seed);
        Graph_M g = generateGraph(vertices, rand, 0.5);
        ArrayList<RandomWalker> rws = new ArrayList<>();

        //add the random walker's starting point randomly
        Set<String> ks = g.getVertices().keySet();
        String[] keys = ks.toArray(new String[ks.size()]);

        for (int i = 0; i < runs; i++) {
            int max = g.numVertex();
            int vertex = rand.nextInt(max);
            rws.add(new RandomWalker(g.getVertex(keys[vertex]), g));
        }


        // do random walk n times
        int step = 0;
        while (step < n) {
            for (int j = 0; j < runs; j++) {
                rws.get(j).run(rand);
            }
            //add random walkers as simulation goes on by placing them on hotspots
            if (step % 2 == 0) {
                HashMap<String, Double> probabilities = createProb(g);
                double choice = rand.nextDouble();
                String vertex = chooseRange(choice, probabilities);
                rws.add(new RandomWalker(g.getVertex(vertex), g));
                n++;
            }
            step++;
        }

        g.display_Map();
    }

    //Choose the range in hashmap given probability
    private static String chooseRange(double choice, HashMap<String, Double> probabilities) {
        String lastVertex = null;
        for (String vertex: probabilities.keySet()) {
            if(lastVertex != null && choice > probabilities.get(lastVertex) &&
                    choice < probabilities.get(vertex)){
                return vertex;
            }
            lastVertex = vertex;
        }
        return lastVertex;
    }

    //Create a probability range
    private static HashMap<String, Double> createProb(Graph_M g) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        int sum = 0;
        for (String vertex : g.getVertices().keySet()) {
            int vertexFrequency = g.getVertex(vertex).sumVisits();
            frequencies.put(vertex, g.getVertex(vertex).sumVisits());
            sum += vertexFrequency;
        }
        HashMap<String, Double> prob = new HashMap<>();
        for (String vertex : frequencies.keySet()) {
            prob.put(vertex, frequencies.get(vertex) / (double)sum);
        }

        double curRange = 0.0;
        for (String vertex : prob.keySet()) {
            double probVertex = prob.get(vertex);
            curRange = probVertex + curRange;
            prob.put(vertex, curRange);
        }
        return prob;
    }

    /**
     * Generates graph as stochastic process
     *
     * @return the generated graph
     */
    private static Graph_M generateGraph(int num, Random rand, double probability) {
        Graph_M graph = new Graph_M();

        //add two vertices and connect them
        int start = 0;
        int end = 1;
        graph.addVertex(Integer.toString(start));
        graph.addVertex(Integer.toString(end));
        graph.addEdge(Integer.toString(start), Integer.toString(end), 1);


        //add until there are num number of vertices
        int i = 2;
        while (i < num) {
            Set<String> ks = graph.getVertices().keySet();
            String[] keys = ks.toArray(new String[ks.size()]);
            int max = graph.numVertex();
            int vertex = rand.nextInt(max);

            //stochastically grow the graph
            if (rand.nextDouble() < probability) {
                int index = i;
                graph.addVertex(Integer.toString(index));
                graph.addEdge(Integer.toString(index), keys[vertex], 1);
                i++;
            } else {
                startRW(graph, keys[vertex], rand);
            }
        }
        return graph;
    }

    /**
     * Helper method to start a random walk when growing the graph.
     *
     * @param graph graph to grow
     * @param key   key of starting vertex
     * @param rand  random generator
     */
    private static void startRW(Graph_M graph, String key, Random rand) {
        RandomWalker rw = new RandomWalker(graph.getVertex(key), graph);
        rw.generateRun(rand);
        while ((!rw.getCurVertex().getName().equals(key)) && (rw.getCurVertex().getDegree() != 1)) {
            rw.generateRun(rand);
        }
        String curVert = rw.getCurVertex().getName();
        if (!Objects.equals(curVert, key) && !graph.containsEdge(key, curVert)) {
            graph.addEdge(key, curVert, 1);
        }
    }



//    /**
//     * Given integer runs, return the radius of the circle of random walkers
//     * @param runs the number of runs of the simulation
//     * @return the radius of the random walkers
//     */
//    private static int estimateRadius(int runs) {
//        int points = 0;
//        int radius = 0;
//
//        //When the number of points is equal to or greater than the number of runs,
//        //then return that radius
//        while(points < runs){
//            radius++;
//            points = 0;
//            //goes through each point in the cube of the radius and counts points in the sphere
//            for (int x = -radius - 1; x < radius + 1; x++) {
//                for (int y = -radius - 1; y < radius + 1; y++) {
//                    for (int z = -radius - 1; z < radius + 1; z++) {
//                        if ((- x) * (- x) + (- y) * (- y)
//                                + (- z) * (- z) <= radius * radius) {
//                            points++;
//                        }
//                    }
//                }
//            }
//        }
//        return radius;
//    }
//
//
//    /**
//     * Create random walkers in a sphere
//     * @param c The sphere's coordinates and radius given in (x, y, z, r)
//     * @param rws The array of random walkers
//     * @param steps The number of steps of each random walker
//     * @return The successfully created number of random walkers
//     */
//    private static int createWalks(int[] c, SelfAvoidRW3D[] rws, int steps) {
//        int index = 0;
//
//        int radius = c[3];
//
//        for (int x = -radius - 1; x < radius + 1; x++) {
//            for (int y = -radius - 1; y < radius + 1; y++) {
//                for (int z = -radius - 1; z < radius + 1; z++) {
//                    if ((c[0] - x) * (c[0] - x) + (c[1] - y) * (c[1] - y)
//                            + (c[2] - z) * (c[2] - z) <= c[3] * c[3]) {
//                        //only creates random walker if we have space in the array rws
//                        if (index < rws.length) {
//                            rws[index] = new SelfAvoidRW3D(x, y, z, steps);
//                            index++;
//                        }else{
//                            return index;
//                        }
//                    }
//                }
//            }
//        }
//
//        return index;
//    }

}
