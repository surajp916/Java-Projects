package spiderman;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    //private instance variables 
    private ArrayList<LinkedList<Integer>> clusters; 
    private int items, table; 
    private double capacity; 

    //constructor 
    public Clusters(int s, double f) {
        this.table = s; 
        this.capacity = f; 

        this.clusters = new ArrayList<>(s); 
        for(int i = 0; i < s; i++) {
            clusters.add(new LinkedList<>()); 
        }
        this.items = 0; 
    }

    public void newInsertion(int d) {
        int index = d % table;
        clusters.get(index).addFirst(d);
        items++;
        if ((double) items / table >= capacity) {
            hash();
        }
    }

    public void hash() {
        ArrayList<LinkedList<Integer>> tempClusters = clusters; 
        table *= 2; 
        clusters = new ArrayList<>(table); 

        for(int i  = 0; i < table; i++) {
            clusters.add(new LinkedList<>()); 
        }
        items = 0; 

        for(LinkedList<Integer> list: tempClusters) {
            for(int k: list) {
                newInsertion(k);
            }
        }
    }

    public void connectClusters() {
        for(int i = 0; i < clusters.size(); i++) {
            int prev = (i - 1 + clusters.size()) % clusters.size();
            int prev2 = (i - 2 + clusters.size()) % clusters.size();
            LinkedList<Integer> ptr = clusters.get(i);

            if (!clusters.get(prev).isEmpty()) {

                ptr.add(clusters.get(prev).getFirst());
            }

            if (!clusters.get(prev2).isEmpty() && prev2 != prev) {

                ptr.add(clusters.get(prev2).getFirst());
            }
        }
    }

    public void output() {
        for(LinkedList<Integer> c: clusters) {
            if(!c.isEmpty()) {
                StdOut.println(c.toString().replaceAll("[\\[\\],]", "")); 
            }
        }
    }
    

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String inputFile = args[0];
        String outputFile = args[1];

        StdIn.setFile(inputFile);
        int dimensions = StdIn.readInt();
        int size = StdIn.readInt();
        double factor = StdIn.readDouble();

        Clusters manageClusters = new Clusters(size, factor);

        for (int i = 0; i < dimensions; i++) {

            int dimensionNum = StdIn.readInt();
            StdIn.readInt(); 
            StdIn.readInt(); 

            manageClusters.newInsertion(dimensionNum);
        }

        manageClusters.connectClusters(); 
        
        StdOut.setFile(outputFile);
        manageClusters.output();
    }
}
