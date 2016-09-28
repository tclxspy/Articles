
public class Digraph 
{
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;           // number of vertices in this digraph
    private int E;                 // number of edges in this digraph
    private Bag<Integer>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;        // indegree[v] = indegree of vertex v
    
    /**
     * Initializes an empty digraph with <em>V</em> vertices.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    @SuppressWarnings("unchecked")
    public Digraph(int V) 
    {
        if (V < 0) 
        {
        	throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) 
        {
            adj[v] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of the specified digraph.
     *
     * @param  G the digraph to copy
     */
    public Digraph(Digraph G) 
    {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < V; v++)
        {
            this.indegree[v] = G.indegree(v);
        }
        for (int v = 0; v < G.V(); v++) 
        {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) 
            {
                reverse.push(w);
            }
            for (int w : reverse) 
            {
                adj[v].add(w);
            }
        }
    }
        
    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int V() 
    {
        return V;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
     */
    public int E() 
    {
        return E;
    }


    // throw an IndexOutOfBoundsException unless {@code 0 <= v < V}
    private void validateVertex(int v) 
    {
        if (v < 0 || v >= V)
        {
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    /**
     * Adds the directed edge v¡úw to this digraph.
     *
     * @param  v the tail vertex
     * @param  w the head vertex
     * @throws IndexOutOfBoundsException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) 
    {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the vertices adjacent from vertex {@code v} in this digraph.
     *
     * @param  v the vertex
     * @return the vertices adjacent from vertex {@code v} in this digraph, as an iterable
     * @throws IndexOutOfBoundsException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> adj(int v) 
    {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}               
     * @throws IndexOutOfBoundsException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) 
    {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}               
     * @throws IndexOutOfBoundsException unless {@code 0 <= v < V}
     */
    public int indegree(int v) 
    {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns the reverse of the digraph.
     *
     * @return the reverse of the digraph
     */
    public Digraph reverse() 
    {
        Digraph reverse = new Digraph(V);
        for (int v = 0; v < V; v++) 
        {
            for (int w : adj(v)) 
            {
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,  
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() 
    {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) 
        {
            s.append(String.format("%d: ", v));
            for (int w : adj[v]) 
            {
                s.append(String.format("%d ", w));
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code Digraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) 
    {
        Digraph G = new Digraph(13);
        G.addEdge(4, 2);
    	G.addEdge(2, 3);
    	G.addEdge(3, 2);
    	G.addEdge(6, 0);
    	G.addEdge(0, 1);
    	G.addEdge(2, 0);
    	G.addEdge(11, 12);
    	G.addEdge(12, 9);
    	G.addEdge(9, 10);
    	G.addEdge(9, 11);
    	G.addEdge(8, 9);
    	G.addEdge(10, 12);
    	G.addEdge(11, 4);
    	G.addEdge(4, 3);
    	G.addEdge(3, 5);
    	G.addEdge(7, 8);
    	G.addEdge(8, 7);
    	G.addEdge(5, 4);
    	G.addEdge(0, 5);
    	G.addEdge(6, 4);
    	G.addEdge(6, 9);
    	G.addEdge(7, 6);
       	System.out.println(G);
    }

}
