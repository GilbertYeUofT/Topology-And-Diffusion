//Code from https://github.com/KISLAYA-SRI/THE-METRO-APP/blob/master/Graph_M.java

import java.util.*;

public class Graph_M {

    private HashMap<String, Vertex> vertices;

    public Graph_M() {
        this.vertices = new HashMap<>();
    }

    public void addVertex(String vname) {
        Vertex vtx = new Vertex(vname);
        this.vertices.put(vname, vtx);

    }

    public void removeVertex(String vname) {
        Vertex vertex = this.vertices.get(vname);
        ArrayList<String> keys = new ArrayList<>(vertex.getKeySet());

        for (String key : keys) {
            Vertex neighborVertex = this.vertices.get(key);
            neighborVertex.removeNeighbor(vname);
            neighborVertex.updateDegree();
        }

        this.vertices.remove(vname);
    }

    public Vertex getVertex(String name) {
        return this.vertices.get(name);
    }

    public int numVertex() {
        return this.vertices.size();
    }

    public boolean containsVertex(String vname) {
        return this.vertices.containsKey(vname);
    }

    public HashMap<String, Vertex> getVertices() {
        return this.vertices;
    }

    public int numEdges() {
        ArrayList<String> keys = new ArrayList<>(this.vertices.keySet());
        int count = 0;

        for (String key : keys) {
            Vertex vertex = this.vertices.get(key);
            count = count + vertex.getSize();
        }

        return count / 2;
    }

    public boolean containsEdge(String vname1, String vname2) {
        Vertex vtx1 = this.vertices.get(vname1);
        Vertex vtx2 = this.vertices.get(vname2);

        return vtx1 != null && vtx2 != null && vtx1.hasNeighbor(vname2);
    }

    public void addEdge(String vname1, String vname2, int value) {
        Vertex vtx1 = this.vertices.get(vname1);
        Vertex vtx2 = this.vertices.get(vname2);

        if (vtx1 == null || vtx2 == null || vtx1.hasNeighbor(vname2)) {
            return;
        }

        vtx1.addNeighbor(vname2, value);
        vtx1.updateDegree();
        vtx2.addNeighbor(vname1, value);
        vtx2.updateDegree();
    }

    public void removeEdge(String vname1, String vname2) {
        Vertex vtx1 = this.vertices.get(vname1);
        Vertex vtx2 = this.vertices.get(vname2);

        //check if the vertices given or the edge between these vertices exist or not
        if (vtx1 == null || vtx2 == null || !vtx1.hasNeighbor(vname2)) {
            return;
        }

        vtx1.removeNeighbor(vname2);
        vtx1.updateDegree();
        vtx2.removeNeighbor(vname1);
        vtx2.updateDegree();
    }

    public void updateEdge(String vname1, String vname2) {
        Vertex vtx1 = this.vertices.get(vname1);
        Vertex vtx2 = this.vertices.get(vname2);

        if (vtx1 == null || vtx2 == null) {
            return;
        }

        vtx1.updateNeighbor(vname2);
        vtx2.updateNeighbor(vname1);
    }

    public void display_Map() {
        System.out.println("----------------------------------------------------\n");
        ArrayList<String> keys = new ArrayList<>(this.vertices.keySet());

        for (String key : keys) {
            String str = key + " -- degree: " + this.vertices.get(key).getDegree() + " =>\n";
            Vertex vtx = this.vertices.get(key);
            ArrayList<String> vtxnbrs = new ArrayList<>(vtx.getKeySet());

            for (String nbr : vtxnbrs) {
                str = str + "\t" + nbr + "\t";
                str = str + vtx.getNeighbor(nbr) + "\n";
            }
            System.out.println(str);
        }
        System.out.println("---------------------------------------------------\n");

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) {
        // DIR EDGE
        if (containsEdge(vname1, vname2)) {
            return true;
        }

        //MARK AS DONE
        processed.put(vname1, true);

        Vertex vtx = this.vertices.get(vname1);
        ArrayList<String> nbrs = new ArrayList<>(vtx.getKeySet());

        //TRAVERSE THE NBRS OF THE VERTEX
        for (String nbr : nbrs) {

            if (!processed.containsKey(nbr))
                if (hasPath(nbr, vname2, processed))
                    return true;
        }

        return false;
    }


    private class DijkstraPair implements Comparable<DijkstraPair> {
        String vname;
        String psf;
        int cost;

			/*
			The compareTo method is defined in Java.lang.Comparable.
			Here, we override the method because the conventional compareTo method
			is used to compare strings,integers and other primitive data types. But
			here in this case, we intend to compare two objects of DijkstraPair class.
			*/

        /*
        Removing the overridden method gives us this error:
        The type Graph_M.DijkstraPair must implement the inherited abstract method Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)
        This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract
        method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
        we have to override the method compareTo
         */
        @Override
        public int compareTo(DijkstraPair o) {
            return o.cost - this.cost;
        }
    }

    public int dijkstra(String src, String des, boolean nan) {
        int val = 0;
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, DijkstraPair> map = new HashMap<>();

        Heap<DijkstraPair> heap = new Heap<>();

        for (String key : this.vertices.keySet()) {
            DijkstraPair np = new DijkstraPair();
            np.vname = key;
            //np.psf = "";
            np.cost = Integer.MAX_VALUE;

            if (key.equals(src)) {
                np.cost = 0;
                np.psf = key;
            }

            heap.add(np);
            map.put(key, np);
        }

        //keep removing the pairs while heap is not empty
        while (!heap.isEmpty()) {
            DijkstraPair rp = heap.remove();

            if (rp.vname.equals(des)) {
                val = rp.cost;
                break;
            }

            map.remove(rp.vname);

            ans.add(rp.vname);

            Vertex v = this.vertices.get(rp.vname);
            for (String nbr : v.getKeySet()) {
                if (map.containsKey(nbr)) {
                    int oc = map.get(nbr).cost;
                    Vertex k = this.vertices.get(rp.vname);
                    int nc;
                    if (nan)
                        nc = rp.cost + 120 + 40 * k.getNeighbor(nbr);
                    else
                        nc = rp.cost + k.getNeighbor(nbr);

                    if (nc < oc) {
                        DijkstraPair gp = map.get(nbr);
                        gp.psf = rp.psf + nbr;
                        gp.cost = nc;

                        heap.updatePriority(gp);
                    }
                }
            }
        }
        return val;
    }

    private class Pair {
        String vname;
        String psf;
        int min_dis;
        int min_time;
    }

    public String Get_Minimum_Distance(String src, String dst) {
        int min = Integer.MAX_VALUE;
        //int time = 0;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in stack
        stack.addFirst(sp);

        // while stack is not empty keep on doing the work
        while (!stack.isEmpty()) {
            // remove a pair from stack
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname)) {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst)) {
                int temp = rp.min_dis;
                if (temp < min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = this.vertices.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.getKeySet());

            for (String nbr : nbrs) {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    np.min_dis = rp.min_dis + rpvtx.getNeighbor(nbr);
                    //np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
                    stack.addFirst(np);
                }
            }
        }
        ans = ans + min;
        return ans;
    }


    public String Get_Minimum_Time(String src, String dst) {
        int min = Integer.MAX_VALUE;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in queue
        stack.addFirst(sp);

        // while queue is not empty keep on doing the work
        while (!stack.isEmpty()) {

            // remove a pair from queue
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname)) {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst)) {
                int temp = rp.min_time;
                if (temp < min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = this.vertices.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.getKeySet());

            for (String nbr : nbrs) {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    //np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
                    np.min_time = rp.min_time + 120 + 40 * rpvtx.getNeighbor(nbr);
                    stack.addFirst(np);
                }
            }
        }
        double minutes = Math.ceil((double) min / 60);
        ans = ans + minutes;
        return ans;
    }

    public ArrayList<String> get_Interchanges(String str) {
        ArrayList<String> arr = new ArrayList<>();
        String[] res = str.split("  ");
        arr.add(res[0]);
        int count = 0;
        for (int i = 1; i < res.length - 1; i++) {
            int index = res[i].indexOf('~');
            String s = res[i].substring(index + 1);

            if (s.length() == 2) {
                String prev = res[i - 1].substring(res[i - 1].indexOf('~') + 1);
                String next = res[i + 1].substring(res[i + 1].indexOf('~') + 1);

                if (prev.equals(next)) {
                    arr.add(res[i]);
                } else {
                    arr.add(res[i] + " ==> " + res[i + 1]);
                    i++;
                    count++;
                }
            } else {
                arr.add(res[i]);
            }
        }
        arr.add(Integer.toString(count));
        arr.add(res[res.length - 1]);
        return arr;
    }
}
