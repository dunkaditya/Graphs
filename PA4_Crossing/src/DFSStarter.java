import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Data structure for a node in a linked list
class Item {
   int data;
   Item next;

   Item(int data, Item next) {
      this.data = data;
      this.next = next;
   }
   
   Item(int data) {
	      this.data = data;
	      this.next = null;
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
}

// Data structure holding data computed by DFS
class DFSInfo {
   
   // node colors
   static final int WHITE = 0;
   static final int GRAY  = 1;
   static final int BLACK = 2;

   int[] color;  // variable storing the color
                 // of each node during DFS
                 // (WHITE, GRAY, or BLACK)

   int[] parent; // variable storing the parent 
                 // of each node in the DFS forest

   int d[];      // variable storing the discovery time 
                 // of each node in the DFS forest

   int f[];      // variable storing the finish time 
                 // of each node in the DFS forest

   int t;        // variable storing the current time


   DFSInfo(Graph graph) {
      int n = graph.n;
      color = new int[n];
      parent = new int[n];
      d = new int[n];
      f = new int[n];
      t = 0;
   }
}


// your "main program" should look something like this:

public class DFSStarter {

   static void recDFS(int u, Graph graph, DFSInfo info) {
	   
	   // perform a recursive DFS, starting at u
	   info.color[u] = 1;
       info.d[u] = ++info.t;

       // Recur for all the vertices adjacent to this
       // vertex
       Item adj = graph.A[u];
       
       while(adj != null){
    	   
    	   int v = adj.data;
           if (info.color[v] == 0) {
        	   
        	   info.parent[v] = u;
        	   recDFS(v, graph, info);
           }
           adj = adj.next;
       }
       info.color[u] = 2;
       info.f[u] = ++info.t;
   }

   static DFSInfo DFS(Graph graph) {
       // performs a "full" DFS on given graph
	   
	   DFSInfo info = new DFSInfo(graph);
	   
	   for(int v = 0; v<graph.n; v++) {
		   if(info.color[v] == 0)
			   recDFS(v, graph, info);
	   }
       return info;
   }

   static Item findCycle(Graph graph, DFSInfo info) {
	   
       // If graph contains a cycle x_1 -> ... x_k -> x_1,
       // return a pointer to the head of the linked list
       // (x_1,..., x_k); otherwise, return null.
       // NOTE: if there is a cycle, you should just return
       // one cycle --- it does not matter which one.
 
       // To do this, scan through the edges of graph,
       // using info.f to locate a back edge.
       // Once you find a back edge, use info.parent
       // to build the list of nodes in the cycle
       // in the correct order.
	   
	   for(int u = 0; u<graph.n; u++) {
		   
		   Item adj = graph.A[u];
	       while(adj != null){
	    	   
	    	   int v = adj.data;
	           if (info.f[u] <= info.f[v]) {
	        	   
	        	   Item head = new Item(u);
	        	   
	        	   while(head.data != v) 
	        	   {
	        		   Item add = new Item(info.parent[head.data], head);
	        		   head = add;
	        	   }
	        	   return head;
	           }
	           adj = adj.next;
	       }
	   }
       return null;
   }

   public static void main(String[] args) throws FileNotFoundException {
	   
	   Scanner sc = new Scanner(new File("test4.in"));
	   //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
	   
	   String[] nums = sc.nextLine().split(" ");
	   int n = Integer.parseInt(nums[0]);
	   int m = Integer.parseInt(nums[1]);
	   
	   Graph graph = new Graph(n);
	   
	   for(int i = 0; i<m; i++) {
		   String[] nums2 = sc.nextLine().split(" ");
		   graph.addEdge(Integer.parseInt(nums2[0])-1, Integer.parseInt(nums2[1])-1);
	   }
	   
	   DFSInfo info = DFS(graph);
	   Item sol = findCycle(graph, info);
	   
	   if(sol != null) {
		   System.out.println(1);
		   while(sol != null) {
			   System.out.print(sol.data+1 + " ");
			   sol = sol.next;
		   }
	   }
	   else {
		   System.out.println("0");
	   }
   }
}
