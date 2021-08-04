import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

// Data structure for a node in a linked list
class Item {
    int data;
    Item next;
 
    Item(int data, Item next) {
       this.data = data;
       this.next = next;
    }
        
}

// Data structure for representing a graph
class Graph {
    int n;  // # of nodes in the graph
 
    Item[] A; 
    // For u in [0..n), A[u] is the adjecency list for u
 
    Graph(int n) {
       // initialize a graph with n vertices and no edges
       this.n = n;
       A = new Item[n];
    }
 
    void addEdge(int u, int v) {
       // add an edge u -> v to the graph
 
       A[u] = new Item(v, A[u]);
    }
    
    Graph getTranspose(){
         
        Graph g = new Graph(n);
        for (int u = 0; u < n; u++){
            Item adj = A[u];
            while(adj != null) {
                int v = adj.data;
                g.addEdge(v, u);
                adj = adj.next;
            }
        }
        return g;
    }
   
}

// Data structure holding data computed by DFS
class DFSInfo {
    
    int k; 
    // # of trees in DFS forest
    
    int[] T;
    // For u in [0..n), T[u] is initially 0, but when DFS discovers
    // u, T[u] is set to the index (which is in [1..k]) of the tree 
    // in DFS forest in which u belongs.
 
    int[] L;
    // List of nodes in order of decreasing finishing time
 
    int count;
    // initially set to n, and is decremented every time
    // DFS finishes with a node and is recorded in L
 
    DFSInfo(Graph graph) {
       int n = graph.n;
       k = 0;
       T = new int[n];
       L = new int[n];
       count = n;
    }
}


// your "main program" should look something like this:

public class Solution {
    
    static void recDFS(int u, int tree, Graph graph, DFSInfo info) {
        
        // perform a recursive DFS, starting at u
        if(info.T[u] == 0) {
            info.T[u] = tree; 
        }
        
        // Recur for all the vertices adjacent to this
        // vertex
        Item adj = graph.A[u];
        
        while(adj != null){
           
            int v = adj.data;
            if (info.T[v] == 0) {
                
                recDFS(v, tree, graph, info);
            }
            adj = adj.next;
        }
        info.L[info.count-1] = u;
        info.count--;
    }

    static DFSInfo DFS(int[] order, Graph graph) {
        // performs a "full" DFS on given graph
        
        DFSInfo info = new DFSInfo(graph);
        
        for(int v = 0; v<graph.n; v++) {
            info.k++;
            if(info.T[order[v]] == 0)
                recDFS(order[v], info.k, graph, info);
        }
        return info;
    }
    
    static boolean[] computeSafeNodes(Graph graph, DFSInfo info) {
        // returns a boolean array indicating which nodes
        // are safe nodes.  The DFSInfo is that computed from the
        // second DFS.
        boolean[] unSafe = new boolean[info.k];
        for (int u = 0; u < graph.n; u++){
        	
            if(!unSafe[info.T[u]]) {
            	
                Item adj = graph.A[u];
                while(adj != null) {
                    int v = adj.data;
                    if(info.T[u] != info.T[v]) {
                        unSafe[info.T[u]] = true;
                    }
                    adj = adj.next;
                }
            }
        }
         
        boolean[] safe = new boolean[graph.n];
        for(int u = 0; u < graph.n; u++){
            if(!unSafe[info.T[u]]) {
                safe[u] = true; 
            }
        }
        return safe;
    }

    public static void main(String[] args) throws IOException {
    	
 	    Scanner sc = new Scanner(new File("input0.txt"));
//      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);

        String[] nums = sc.nextLine().split(" ");
        int n = Integer.parseInt(nums[0]);
        int m = Integer.parseInt(nums[1]);
            
        Graph graph = new Graph(n);
        for(int i = 0; i<m; i++){
            String[] nums2 = sc.nextLine().split(" ");
            graph.addEdge(Integer.parseInt(nums2[0]), Integer.parseInt(nums2[1]));
        }
        Graph trans = graph.getTranspose();
        
        //initial order is 1, 2,...n
        int[] order = new int[n];
        for(int i = 0; i<n; i++) {
            order[i] = i;
        }
         
        DFSInfo transInfo = DFS(order, trans);
        DFSInfo info = DFS(transInfo.L, graph);
        
        boolean[] safeNodes = computeSafeNodes(graph, info);
        for(int i = 0; i<graph.n; i++) {
            if(safeNodes[i] == true) {
//            	out.write(i + " ");
            	System.out.print(i + " ");
            }
        }
//        out.flush();
    }

}