package spiderman;

import java.util.HashMap;
//import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    //private instance variables 
    private HashMap<Integer, List<Integer>> adjList; 
    private ArrayList<LinkedList<Integer>> clusters; 
    private int table; 
    private double cap; 
    private int items; 
    private LinkedList<Person> newPeople; 


    public Collider(int i, double c) {
        this.table = i; 
        this.cap = c; 
        this.items = 0; 
        this.clusters = new ArrayList<>(i); 
        for(int k = 0; k < i; k++) {
            clusters.add(new LinkedList<>()); 
        }

        this.adjList = new HashMap<>(); 
        this.newPeople = new LinkedList<>(); 
    }


    private int calcHash(int dim) {
        return dim%table; 
    }

    private void hash() {
        table *= 2; 
        ArrayList<LinkedList<Integer>> newClusters = new ArrayList<>(table); 
        for(int k = 0; k < table; k++) {
            newClusters.add(new LinkedList<>()); 
        }
        //for each loop 
        for(LinkedList<Integer> clus: this.clusters) {
            for(int dim: clus) {
                int index = calcHash(dim); 
                newClusters.get(index).addFirst(dim);
            }
        }
        this.clusters = newClusters; 
    } 

    public void newInsert(int dim) {
        int index = calcHash(dim); 

        clusters.get(index).addFirst(dim);
        items++; 

        double factor = (double) items/table; 
        if(factor >= cap) {
            hash(); 
        }
    }

    private void Connectclusters() {
        for(int i = 0; i < clusters.size(); i++) {
            LinkedList<Integer> clus = clusters.get(i); 
            if(clus.isEmpty()) {
                continue; //keep going 
            }

            int prev1 = (i - 1 + clusters.size()) % clusters.size(); 
            int prev2 = (i - 2 + clusters.size()) % clusters.size();
            
            if (!clusters.get(prev1).isEmpty()) {
                clus.add(clusters.get(prev1).getFirst());
            }

            if (!clusters.get(prev2).isEmpty() && prev2 != prev1) {
                clus.add(clusters.get(prev2).getFirst());
            }
        }
    }

    //create the adjacecy list
    public void createAdjList() {
        for(LinkedList<Integer> cluster: this.clusters) {
            if(cluster.isEmpty()) {
                //keep going 
                continue; 
            }

        
        int firstDim = cluster.getFirst(); 
        adjList.computeIfAbsent(firstDim, k -> new ArrayList<>()); 

        for(int j = 1; j < cluster.size(); j++) {
            int d = cluster.get(j); 


            adjList.get(firstDim).add(d); 
            adjList.computeIfAbsent(d, k -> new ArrayList<>()).add(firstDim); 
        }
        }
    }

    public void output(String outFile) {
        StdOut.setFile(outFile);

        for(Integer key: adjList.keySet()) {
            List<Integer> val = adjList.get(key); 
            if(val != null && !val.isEmpty()) {
                String output = key.toString(); 
                for(Integer vals: val) {
                    output += " " + vals; 
                }
                StdOut.println(output);
            }
        }
    }


    public void read(String InputFile) {
        StdIn.setFile(InputFile);

        int people = StdIn.readInt();



        for (int i = 0; i < people; i++) {
            int dim = StdIn.readInt();
            String name = StdIn.readString();
            int sign = StdIn.readInt();

            Person newPerson = new Person(name, sign, dim);
            newPeople.add(newPerson);
        }
    }
    



    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
        String inputFile = args[0]; 
        String outputFile = args[1]; 

        StdIn.setFile(inputFile);
        //number of dimensions 
        int dimNum = StdIn.readInt();
        //inital size 
        int size = StdIn.readInt();
        //capacity 
        double capcacity = StdIn.readDouble();

        //new Collider 
        Collider colliders = new Collider(size, capcacity);


        for (int i = 0; i < dimNum; i++) {

            int d = StdIn.readInt();
            StdIn.readInt(); 
            StdIn.readInt(); 

            colliders.newInsert(d);
        }

        colliders.Connectclusters(); 

        colliders.createAdjList();
        colliders.read(args[1]);

        
        colliders.output(outputFile);     
    }    
}

//create new Person class 
class Person {
    //variables 
    public String name;
    public int sign;
    public int currDim;

    public Person(String n, int s, int cD) {
        this.name = n;
        this.sign = s;
        this.currDim = cD;
    }
    public String toString() {
        return "Person{name='" + this.name + "', dimension=" + this.currDim + ", signature=" + this.sign + "}";
    }
} 
