/*
 * Student ID : 2018412
 * UOW ID : w1761910
 * Name : Parinda Malith Kulathilake
 */

import java.util.*;

public class Main {

    /**
     * Breadth first search to find augmented path
     *
     * @param rCapacity 2-D array that holds residual capacity
     * @param parent           path
     * @param source           source node
     * @param sink             sink node
     * @return found Augmented Path
     */
    private boolean breadthFirstTraversal(int[][] rCapacity, Map<Integer, Integer> parent, int source, int sink) {
        Set<Integer> visited = new HashSet<>(); // keeps track of visited nodes
        Queue<Integer> queue = new LinkedList<>(); // queue implementation for breadth first traversal
        queue.add(source);
        visited.add(source);
        boolean foundAugmentedPath = false;

        // see if we can find augmented path from source to sink
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < rCapacity.length; v++) {
                // explore the node only if it is not visited and its residual capacity is
                // greater than 0
                if (!visited.contains(v) && rCapacity[u][v] > 0) {
                    // add in parent map saying v got explored by u
                    parent.put(v, u);
                    // add v to visited
                    visited.add(v);
                    // add v to queue for BFS
                    queue.add(v);
                    // if sink is found then augmented path is found
                    if (v == sink) {
                        foundAugmentedPath = true;
                        break;
                    }
                }
            }
        }
        // returns if augmented path is found from source to sink or not
        return foundAugmentedPath;
    }

    /**
     * @param graph graph
     * @param source   source node
     * @param sink     sink node
     * @return maximum flow
     */
    public int maxFlow(int[][] graph, int source, int sink) {

        // initially declare and initialize residual capacity as total available capacity.
        int[][] rGraph = new int[graph.length][graph[0].length];
        for (int i = 0; i < graph.length; i++) {
            System.arraycopy(graph[i], 0, rGraph[i], 0, graph[0].length);
        }

        // parent map for storing BFS parent
        Map<Integer, Integer> parent = new HashMap<>();

        // list to store all augmented paths
        List<List<Integer>> augPaths = new ArrayList<>();

        // max flow we can get in this network
        int maxFlow = 0;

        // see if augmented path can be found from source to sink.
        while (breadthFirstTraversal(rGraph, parent, source, sink)) {
            List<Integer> augPath = new ArrayList<>();
            int flow = Integer.MAX_VALUE;
            /*
            find minimum residual capacity (bottle neck capacity) in augmented path
            and add nodes to augmented path list
             */
            int v = sink;
            while (v != source) {
                augPath.add(v);
                int u = parent.get(v);
                if (flow > rGraph[u][v]) {
                    flow = rGraph[u][v];
                }
                v = u;
            }
            augPath.add(source);
            Collections.reverse(augPath);
            augPaths.add(augPath);

            // add min capacity to max flow
            maxFlow += flow;

            // decrease residual capacity by min capacity from u to v in augmented path
            // and increase residual capacity by min capacity from v to u
            v = sink;
            while (v != source) {
                int u = parent.get(v);
                rGraph[u][v] -= flow;
                rGraph[v][u] += flow;
                v = u;
            }
        }
        augmentedPaths(augPaths);
        return maxFlow;
    }

    /**
     * Prints all the augmented path which contribute to max flow
     * @param augmentedPaths List of augmented paths
     */
    private void augmentedPaths(List<List<Integer>> augmentedPaths) {
        System.out.println("\n----------- Augmented paths ---------------\n");

        /* augPaths contains all the augmented paths that contributed to max flow,
           in the following loop we output all the augmented paths
         */
        for (List<Integer> path : augmentedPaths) {
            for (Integer i : path) {
                System.out.print(i + " ");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {

        System.out.println("\n——————————————— Maximum Flow ——————————————————");

        Main fordFulkerson = new Main(); // create object from FordFulkerson class
        List<String> inputs = Parser.getFileInputs(); // call getFileInputs method from Parser class

        // remove the first element of inputs, assign the value to numOfVertices and convert to int value
        int numOfVertices = Integer.parseInt(inputs.remove(0));
        System.out.println("\n⬤ Number of nodes : " + numOfVertices);

        // graph data structure utilizing 2D array
        int[][] graph = new int[numOfVertices][numOfVertices];

        // loop to construct data structure using data read from text file
        for (String input : inputs) {
            String[] temp = input.split(" ");
            graph[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = Integer.parseInt(temp[2]);
        }

        System.out.println("\n⬤ Graph Data structure : \n\n\n" + Arrays.deepToString(graph)); // print constructed data structure

        boolean exit = false;
        while (!exit){
            try {
                // --------------------- Menu ------------------------------- //
                System.out.println("\n-- CHOOSE AN OPTION -- \n");
                System.out.println("⬤ Press 1 to calculate Maximum flow");
                System.out.println("⬤ Press 2 to change an edge");
                System.out.println("⬤ Press 3 to delete an edge");

                Scanner keyboardInput = new Scanner (System.in); // scanner object to take user input
                int choice = keyboardInput.nextInt();

                switch (choice){
                    case 1:
                        int sink = graph.length - 1; // declare sink node

                        Stopwatch timer = new Stopwatch(); // create stop watch object to measure time elapsed

                        // call maxFlow() method, give data structure, source and sink as parameters
                        System.out.println("\n" + Arrays.deepToString(graph));
                        System.out.println("\n⬤ Maximum Flow : " + fordFulkerson.maxFlow(graph, 0, sink));
                        System.out.println("\n⬤ Elapsed time = " + timer.elapsedTime()); // time taken to run algorithm

                        exit = true;
                        break;
                    case 2:
                        try{
                            Scanner input = new Scanner (System.in);

                            System.out.println("\n - Enter first node : ");
                            int n1 = input.nextInt();

                            System.out.println(" - Enter second node : ");
                            int n2 = input.nextInt();

                            System.out.println(" - Enter edge capacity to change : ");
                            int ec = input.nextInt();

                            // condition checks if the user input nodes are not the same, exists in the graph and isn't equal to sink node
                            if((n1 != n2) && (n1 < graph.length && n2 < graph.length) && (n1 != graph.length - 1 && n2 != graph.length - 1)){
                                graph[n1][n2] = ec; // change the required edge's capacity in the graph
                                System.out.println("\n" + Arrays.deepToString(graph));
                            } else {
                                System.out.println("\nERROR : Invalid input.");
                            }
                        } catch (Exception e){
                            System.out.println("\nERROR : Invalid input.");
                        }

                        break;
                    case 3:
                        try{
                            Scanner delInput = new Scanner (System.in);

                            System.out.println("\n - Enter first node : ");
                            int node1 = delInput.nextInt();

                            System.out.println(" - Enter second node : ");
                            int node2 = delInput.nextInt();

                            // condition checks if the user input nodes are not the same, exists in the graph and isn't equal to sink node
                            if((node1 != node2) && (node1 < graph.length && node2 < graph.length) && (node1 != graph.length - 1 && node2 != graph.length - 1)){
                                graph[node1][node2] = 0; // delete the edge in the graph
                                System.out.println("\n" + Arrays.deepToString(graph));
                            } else {
                                System.out.println("\nERROR : Invalid input.");
                            }
                        } catch (Exception e){
                            System.out.println("\nERROR : Invalid input.");
                        }

                        break;
                }
            } catch (Exception e){
                System.out.println("\nERROR : Invalid input.");
            }
        }
    }
}
