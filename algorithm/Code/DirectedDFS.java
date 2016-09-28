
public class DirectedDFS 
{
    private boolean[] marked;  // marked[v] = true if v is reachable
                               // from source (or sources)
    private int count;         // number of vertices reachable from s

    /**
     * Computes the vertices in digraph {@code G} that are
     * reachable from the source vertex {@code s}.
     * @param G the digraph
     * @param s the source vertex
     */
    public DirectedDFS(Digraph G, int s) 
    {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    /**
     * Computes the vertices in digraph {@code G} that are
     * connected to any of the source vertices {@code sources}.
     * @param G the graph
     * @param sources the source vertices
     */
    public DirectedDFS(Digraph G, Iterable<Integer> sources) 
    {
        marked = new boolean[G.V()];
        for (int v : sources) 
        {
            if (!marked[v]) 
            {
            	dfs(G, v);
            }
        }
    }

    private void dfs(Digraph G, int v) 
    { 
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) 
        {
            if (!marked[w]) 
            {
            	dfs(G, w);
            }
        }
    }

    /**
     * Is there a directed path from the source vertex (or any
     * of the source vertices) and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     */
    public boolean marked(int v) 
    {
        return marked[v];
    }

    /**
     * Returns the number of vertices reachable from the source vertex
     * (or source vertices).
     * @return the number of vertices reachable from the source vertex
     *   (or source vertices)
     */
    public int count() 
    {
        return count;
    }

    /**
     * Unit tests the {@code DirectedDFS} data type.
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

        // multiple-source reachability
        DirectedDFS dfs = new DirectedDFS(G, 11);

        // print out vertices reachable from sources
        for (int v = 0; v < G.V(); v++) 
        {
            if (dfs.marked(v)) 
            {
            	System.out.print(v + " ");
            }
        }
        System.out.println();
    }

}