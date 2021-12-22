package graph;
import java.util.*;

/**
 * this class is an implementation of the GraphIfc interface. It uses
 * a HashMap of vertex keys and list of vertex values.
 * @authors Robert Weaver, Kyler Greenway
 * This implementation assumes a directed graph
 * @version 10/6/2021
 */
public class Graph<V> implements GraphIfc<V>{

    HashMap<V, List<V>> graph;
    int numEdges = 0;


    public Graph(){
        this.graph = new HashMap<>();
    }

    /**
     * Returns the number of vertices in the graph
     * @return The number of vertices in the graph
     */
    public int numVertices(){
        return this.graph.keySet().size();
    }

    /**
     * Returns the number of edges in the graph
     * @return The number of edges in the graph
     */
    public int numEdges(){
        return numEdges;
    }

    /**
     * Removes all vertices from the graph
     */
    public void clear(){
        this.graph.clear();
    }

    /**
     * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph.
     * @param v The vertex to be added
     */
    public void addVertex(V v){
        if (!this.graph.containsKey(v)) {
            this.graph.put(v, new ArrayList<>());
        }
    }

    /**
     * Adds an edge between vertices u and v in the graph.
     *
     * @param u A vertex in the graph
     * @param v A vertex in the graph
     * @throws IllegalArgumentException if either vertex does not occur in the graph.
     */
    public void addEdge(V u, V v){
        if(!this.graph.containsKey(u) || !this.graph.containsKey(v)){
            throw new IllegalArgumentException("a vertex did not exist in the graph");
        }
         if (!edgeExists(u,v)){
             this.graph.get(u).add(v);
             numEdges++;
        }

    }

    /**
     * Returns the set of all vertices in the graph.
     * @return A set containing all vertices in the graph
     */
    public Set<V> getVertices(){
        return this.graph.keySet();
    }

    /**
     * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
     * v by an edge. If the graph is directed, this returns the vertices u for which an
     * edge (v, u) exists.
     *
     * @param v An existing node in the graph
     * @return All neighbors of v in the graph.
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    public List<V> getNeighbors(V v){
        if(!this.graph.containsKey(v)){
            throw new IllegalArgumentException("Vertex did not appear in the graph");
        }
        return this.graph.get(v);
    }

    /**
     * Determines whether the given vertex is already contained in the graph. The comparison
     * is based on the <code>equals()</code> method in the class V.
     *
     * @param v The vertex to be tested.
     * @return True if v exists in the graph, false otherwise.
     */
    public boolean containsVertex(V v){
        return this.graph.containsKey(v);
    }

    /**
     * Determines whether an edge exists between two vertices. In a directed graph,
     * this returns true only if the edge starts at v and ends at u.
     * @param v A node in the graph
     * @param u A node in the graph
     * @return True if an edge exists between the two vertices
     * @throws IllegalArgumentException if either vertex does not occur in the graph
     */
    public boolean edgeExists(V v, V u){
        if (!this.graph.containsKey(v) || !this.graph.containsKey(u)){
            throw new IllegalArgumentException("a vertex did not appear in the graph");
        }
        return this.graph.get(v).contains(u);
    }

    /**
     * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
     * vertex.
     * @param v A vertex in the graph
     * @return The degree of the vertex
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    public int degree(V v){
        if (!this.graph.containsKey(v)){
            throw new IllegalArgumentException("Vertex did not appear in graph");
        }
        return this.graph.get(v).size();
    }

    public V maxDegree(){
        V maxDegree = null;
        int mDegree = 0;
        for(V v : graph.keySet()){
           if(degree(v)>mDegree){
               mDegree = degree(v);
               maxDegree = v;
           }
        }
        return maxDegree;

    }

    /**
     * Returns a string representation of the graph. The string representation shows all
     * vertices and edges in the graph.
     * @return A string representation of the graph
     */
    public String toString(){
        return this.graph.toString();
    }

    public static void main(String[] args) {
        Graph g = new Graph();

        System.out.println("Add a Vertex: ");
        g.addVertex('a');
        System.out.println(g.toString());
        g.addVertex('a'); // test adding a vertex that already exists
        g.addVertex('b');
        g.addVertex('c');
        g.addVertex('d');
        g.addEdge('a','b');
        g.addEdge('b','c');
        g.addEdge('c','d');
        g.addEdge('a','d');
        g.addEdge('c','d');
        g.addEdge('d','c'); 
        System.out.println(g.toString());
        //g.addEdge('a','e'); // test to see if crashes when adding edge with a vertex that doesn't exist
        System.out.println("Vertices: " + g.getVertices());
        System.out.println("Neighbors To vertex 'a' : " + g.getNeighbors('a'));
        System.out.println("Edge Exist between 'a' and 'b'? : " + g.edgeExists('a', 'b'));
        System.out.println("Degree of vertex 'a' : " + g.degree('a'));
        System.out.println("Vertex 'a' exits? : " + g.containsVertex('a'));
        System.out.println("Num Vertices: " + g.numVertices());
        System.out.println("Num Edges: " + g.numEdges());
        g.clear();
        System.out.println(g.toString());


    }
}
