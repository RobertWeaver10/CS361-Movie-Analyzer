package graph;
import data.Movie;

import java.util.List;
import java.util.Map;
import java.util.Set;
import util.PriorityQueue;

/**
 * a class that has two methods that implement Floyd Warhsall's algorithm to find the shortest path between
 * all nodes in a graph, and Dijkstra's algorithm to find the shortest path from a start node to all other
 * nodes in the graph
 *
 * @authors Robert Weaver and Kyler Greenway
 * @version 12/10/2021
 */
public class GraphAlgorithms {
	// FILL IN
    private static Map<Integer,Movie> movies;
    /**
     * an implementation of floyd warshall algorithm that gives the shortest path between all nodes
     * @param graph the graph that is being searched for shortest paths
     * @return a 2d array of the distances of the shortest paths between all nodes
     */
    public static int[][] floydWarshall(Graph<Integer> graph){
        Set<Integer> nodes = graph.getVertices();
        Object index[] = nodes.toArray();
        int vertices = index.length;
        int distance[][] = new int [vertices][vertices]; //create a 2d array to track distances between vertices

        for (int i = 0; i < vertices; i++){
            distance[i][i] = 0; //fill the diagonal with weights of 0
        }
//

        for(int i = 0; i < vertices; i++){ //for each edge
            int v = (int) index[i]; //get the vertex at index i
            for (int j = 0; j < vertices; j++){
                int u = (int) index[j]; //get the vertex at index j
                if (graph.edgeExists(v, u)){ //if an edge exists between the two vertices
                    distance[i][j] = 1; //add the weight to the matrix
                }
                else if (i == j){
                    distance[i][j] = 0;
                }
                else{
                    distance[i][j] = 19999999; //used arbitrarily large value for infinity
                }
            }
        }

        for (int k = 0; k < vertices; k++){
            for (int i = 0; i < vertices; i++){
                for (int j = 0; j < vertices; j++){

                    if (distance[i][j] > distance[i][k] + distance[k][j]){ //if the distance is shorter visiting k
                        distance[i][j] = distance[i][k] + distance[k][j]; //update distance
                    }
                }
            }
        }
        return distance;
    }

    /**
     * an implementation of Dijkstras algorithm that finds the shortest path from a starting
     * source node to all other nodes
     * @param graph the graph containing the source node
     * @param source the node to find the shortest paths from
     * @return the list of each node's previous node in the path
     */
    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
        PriorityQueue Q = new PriorityQueue();//create priority queue
        int vertices = graph.numVertices();
        int previous[] = new int[vertices];//create array for previous nodes
        int distance[] = new int[vertices];//create array for shortest path lengths

        for (int i = 0; i < distance.length; i++){ //initialize distances to infinity (or arbitrarily large val)
            distance[i] = 19999999;
        }
        distance[source-1] = 0; //set the distance from source to source as 0
        for (int movieId : graph.getVertices()){
            Q.push(distance[movieId-1], movieId); //push movieID (node) and infinite weight
        }
        while (!Q.isEmpty()){
            int u = Q.topElement(); //save the top node
            Q.pop(); //remove the node from the queue
            List<Integer> adjList= graph.getNeighbors(u);
            for (int v : adjList){
                int alt = distance[u-1] + 1;//alt = distance [u] + 1

                if (alt < distance[v-1]){//if (alt < distance[v])
                    distance[v-1] = alt;//distance[v] = alt : distance[indexOf(v)] = alt
                    previous[v-1] = u;//previous[v] = u : previous[indexOf(v)] = u
                    Q.changePriority(alt, v);//Q.changePriority(v, alt)
                }
            }
        }
        return previous;
    }

    public void getMovieMap(Map <Integer, Movie> movies){
        this.movies = movies;
    }

    public static void main (String[] args){

    }
}
