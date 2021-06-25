package clientserver;
import java.util.*;

class Graph {

    /**
     * the
     */

    int V;
    ArrayList<ArrayList<Integer>> adjListArray;
    HashSet<ArrayList<Integer>> components;
    List<List<Integer>> allPaths;


    // constructor
    Graph(int V) {
        this.V = V; // define the size of array as number of vertices
        adjListArray = new ArrayList<>();
        components = new HashSet<>();
        allPaths = new ArrayList<>();

        // Create a new list for each vertex such that adjacent nodes can be stored
        for (int i = 0; i < V; i++)
            adjListArray.add(i, new ArrayList<>());
    }

    /**
     * @param src source node
     * @param dest destination node
     * Adds an edge to an undirected graph
     */
    void addEdge(int src, int dest) {
        adjListArray.get(src).add(dest);
    }


    /**
     * @param start the starting node
     * @param end the target of the path
     * @return a List that represent al the paths from the start node to the end node
     */
    public List<List<Integer>> findAllPaths(int start, int end)
    {
        boolean[] isVisited = new boolean[V];
        ArrayList<Integer> pathList = new ArrayList<>();
        pathList.add(start);
        findAllPathsUtil(start, end, isVisited, pathList);
        return allPaths;
    }


    /**
     * A recursive function to find all paths from 'u' to 'd'.
     * @param u start index
     * @param d end index
     * @param isVisited keeps track of vertices in current path
     * @param localPathList stores actual vertices in the current path
     */
    public void findAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList)
    {
        if (u.equals(d)) {
            allPaths.add(new ArrayList<>(localPathList));
            // if match found then no need to traverse more till depth
            return;
        }
        isVisited[u] = true;

        // Recur for all the vertices adjacent to current vertex
            for (Integer i : adjListArray.get(u)) {
            if (!isVisited[i]) {
                // store current node in path[]
                localPathList.add(i);
                findAllPathsUtil(i, d, isVisited, localPathList);

                // remove current node in path[]
                localPathList.remove(i);
            }
        }
        // Mark the current node
        isVisited[u] = false;
    }

    /**
     * DFS Algorithm
     * @param v current vertex
     * @param visited checks if we visited the vertex before
     * @param arr collects the vertexes into the component
     */
    void DFSUtil(int v, boolean[] visited, ArrayList<Integer> arr) {
        // Mark the current node as visited and print it
        visited[v] = true;
        arr.add(v);
        System.out.print(v + " ");
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                DFSUtil(x, visited, arr);
        }
        components.add(arr);
    }


    /**
     * @return a list that represents the connected components in the graph
     */
    Collection<ArrayList<Integer>> connectedComponents() {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {
                // finds all reachable vertices from v
                ArrayList<Integer> arr = new ArrayList<>();
                DFSUtil(v, visited, arr);
                System.out.println();
            }
        }
        return components;
    }


}
