package analyzer;
import data.Movie;
import util.DataLoader;
import graph.*;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * a movie analyzer class that reads movies from a file then lets users pick options for building graphs
 * from those movies and gives them options for what to do with the graph once it's been generated
 *
 * @authors Robert Weaver and Kyler Greenway
 * @version 12/10/2021
 */
public class MovieLensAnalyzer {

	private static Graph graph = new Graph();
	
	public static void main(String[] args) throws FileNotFoundException {
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genre


		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		MovieLensAnalyzer graphBuilder = new MovieLensAnalyzer();
		DataLoader info = new DataLoader();
		graphBuilder.USER_INTERFACE(args);
		info.loadData(args[1],args[0]);
		Map<Integer, Movie> movies = info.getMovies();


		if(graphBuilder.USER_INTERFACE(args) == 1){
			System.out.print("Creating the graph...");
			graphBuilder.buildGraphOp1(movies);
			System.out.print("The graph has been created.");
			System.out.println(graph.getVertices());
		}
		else{
			System.out.print("Creating the graph...");
			graphBuilder.buildGraphOp2(movies);
			System.out.print("The graph has been created\n.");
		}

		graphBuilder.POST_CREATION_USER_INTERFACE(movies);


	}

	/**
	 * method that prompts the user for how they want to build the graph and gets the user's input
	 * @param args user input
	 * @return the option the user chose for building the graph
	 */
	private static int USER_INTERFACE(String [] args){
		Scanner input = new Scanner(System.in);
		System.out.println("========== Welcome to MovieLens Analyzer ==========");
		System.out.println("The files being analyzed are: ");
		System.out.println(args[0] + "\n" + args[1]);
		System.out.println("\nThere are 2 choices for defining adjacency: ");
		System.out.println("[Option 1] u and v are adjacent if the 12 same users watched both movies (regardless of rating)");
		System.out.println("[Option 2] u and v are adjacent if both movies have the same average rating");
		System.out.print("\nChoose an option to build the graph (1-2): ");
		int choice = input.nextInt();
		return choice;
	}

	/**
	 * method that prompts the user for what they want to do with their created graph
	 * @param movies if the user chooses option 2 then we have to pass the movie map to the method call
	 */
	private static void POST_CREATION_USER_INTERFACE(Map<Integer,Movie>movies){
		Scanner input = new Scanner (System.in);
		int choice = 0;
		while(choice != 4){
			System.out.println("\n[Option 1] Print out statistics about the graph");
			System.out.println("[Option 2] Print node information");
			System.out.println("[Option 3] Display shortest path between two nodes");
			System.out.println("[Option 4] Quit");
			System.out.print("Choose an option (1-4): ");
			choice = input.nextInt();

			if(choice == 4){
				break;
			}
			else if (choice == 1){
				graphStatistics();
			}
			else if (choice == 2){
				printNodeInfo(movies);
			}
			else{
				System.out.print("Enter Starting node (1-1000): ");
				int startingNode = input.nextInt();
				System.out.print("Enter Ending node (1-1000): ");
				int endingNode = input.nextInt();
				printShortestPath(startingNode,endingNode,movies);
			}
		}

	}

	/**
	 * method that takes two nodes and prints the movies along the shortest path between the two input movies
	 * @param ID1 the id of the first movie in the path
	 * @param ID2 the id of the last movie in the path
	 * @param movies the map of all movies
	 */
	private static void printShortestPath(int ID1, int ID2, Map<Integer,Movie>movies){
		GraphAlgorithms algs = new GraphAlgorithms();
		algs.getMovieMap(movies);
		int [] prev = algs.dijkstrasAlgorithm(graph, ID1); //get the shortest path from first movie to all others

		int current = ID2;
		Stack<String> moviePath = new Stack<>(); //initialize stack to help with printing the path
		boolean running = true;
		while(current != ID1 && running){

			if(prev[current] != 0){ //if there was a previous node in the path add to stack
				moviePath.push(movies.get(prev[current]).getTitle() + " ===> " + movies.get(current).getTitle());
				current = prev[current]; //update our current movie
			}
			else{ //if there wasn't a previous node in the path then the movies are not connected in the graph
				System.out.println("Movies were not connected");
				running = false;
			}
		}

		while(!moviePath.isEmpty()){ //print the path off the stack
			System.out.println(moviePath.pop());
		}

	}

	/**
	 * method that takes in a map of movies with their id and then the movie object and builds a graph
	 * where two nodes are connected if they were reviewed by the same 12 reviewers
	 * @param movies the map of id, movie (key, value) pairs to build the graph from
	 */
	private static void buildGraphOp1(Map<Integer,Movie>movies){


		for(Integer key : movies.keySet()){
			graph.addVertex(key);
		}


		for(int i = 1; i <movies.size()-1; i++){

			for(int k = i+1; k < movies.size(); k++){
				Map<Integer,Double>currentRatings = movies.get(i).getRatings();
				Set<Integer> currentReviewersSet = currentRatings.keySet();
				Object[] reviewersArray = currentReviewersSet.toArray();
				int count = 0;
				int j = 0;
				while(count < 12 && j < reviewersArray.length){
					if(movies.get(k).getRatings().containsKey((Integer)reviewersArray[j])){
						count++;
					}
					j++;
				}
				if(count == 12){ //when the count reaches twelve we add an edge between the nodes
					graph.addEdge(i,k);
					graph.addEdge(k,i);
				}
			}

		}

	}

	/**
	 * method that reads the graph built from the movie map and prints the graph's statistics
	 * prints the number of vertices, the number of edges, the density, the max degree, the diameter,
	 * and the average path length between nodes
	 */
	private static void graphStatistics(){
		GraphAlgorithms gA = new GraphAlgorithms();
		int[][] fWResult = gA.floydWarshall(graph);
		double possibleEdges = graph.numVertices() * (graph.numVertices()-1);
		float density = (float)graph.numEdges() / (float)possibleEdges;
		System.out.println("Graph statistics:");
		System.out.println("|V| = " + graph.numVertices() + " vertices");
		System.out.println("|E| = " + graph.numEdges() + " edges" );
		System.out.println("Density = " + density);
		System.out.println("Max. Deg = " + graph.degree(graph.maxDegree()) + " (Node " + graph.maxDegree() + ")");
		System.out.println("Diameter = " + getDiameter(fWResult));
		System.out.println("Avg path length = " + getAveragePathLength(fWResult));
	}

	/**
	 * method that takes a 2d array of shortest paths between nodes in a graph and finds the largest
	 * shortest path to return. When there are sub graphs within the graph the shortest paths array is
	 * created from this method ignores the infinite weights only returning the diameter of the largest sub
	 * graph
	 * @param shortestPaths 2d array of shortest paths between all nodes in a graph
	 * @return the largest shortest path within the graph
	 */
	private static int getDiameter(int[][]shortestPaths){
		int diameter = -1;
		for (int i = 0; i < shortestPaths.length; i++){ //iterate over all elements
			for (int j = 0; j < shortestPaths.length; j++){
				if (shortestPaths[i][j] > diameter && shortestPaths[i][j] < 19999999){//if the shortest path is greater than cur diameter
					diameter = shortestPaths[i][j]; //update the diameter
				}
			}
		}
		return diameter;
	}

	/**
	 * method that gets the average path length between nodes. When the graph that generated the shortestPaths
	 * array has disconnected sub graphs within the main graph the method ignores the
	 * @param shortestPaths
	 * @return
	 */
	private static double getAveragePathLength(int[][]shortestPaths){
		double avg = -1;
		int totalCount = 0;
		int totalNodes = shortestPaths.length * shortestPaths.length;
		for (int i = 0; i < shortestPaths.length; i++){
			for (int j = 0; j < shortestPaths.length; j++){
				if (shortestPaths[i][j] < 19999999){
					totalCount += shortestPaths[i][j];
				}
				else{
					totalCount--;
				}
			}
		}
		avg = (double) totalCount / (double) totalNodes;
		return avg;
	}

	/**
	 * method that prompts user to pick a movie id and then prints out the movie's info
	 * @param movies the map of all movies
	 */
	private static void printNodeInfo (Map<Integer,Movie>movies){
		Scanner input = new Scanner (System.in);
		System.out.print("\n Enter movie id (1-1000): ");
		int movieID = input.nextInt();
		System.out.println(movies.get(movieID).toString());
		System.out.println("\n Neighbors:");
		ArrayList<Integer> neighbors = (ArrayList<Integer>) graph.getNeighbors(movieID);
		for (Integer i : neighbors){
			System.out.println(movies.get(i).getTitle());
		}
	}

	/**
	 * method that builds a graph out of the map of movies by connecting two nodes if they have the same
	 * average integer movie rating.
	 * @param movies the map of all id's and movie objects
	 */
	private static void buildGraphOp2 (Map<Integer,Movie> movies){
		for(Integer key : movies.keySet()){
			graph.addVertex(key);
		}

		int currentAvg = 0;
		int nextAvg = 0;

		for(int i = 1; i <movies.size()-1; i++){
			Map<Integer,Double>currentRatings = movies.get(i).getRatings();
			double sum1 = 0;
			for(Integer ID1 : currentRatings.keySet()){
				sum1 += currentRatings.get(ID1);
			}
			currentAvg = (int)sum1/currentRatings.size();

			for(int k = i+1; k < movies.size(); k++){
				Map<Integer,Double>  nextRatings = movies.get(k).getRatings();
				double sum2 = 0;
				for(Integer ID2 : nextRatings.keySet()){
					sum2 += nextRatings.get(ID2);
				}
				nextAvg = (int)sum2/nextRatings.size();

				if(currentAvg==nextAvg){
					graph.addEdge(i,k);
					graph.addEdge(k,i);
				}
			}
		}
	}


}
