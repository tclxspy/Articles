
public class BreadthFirstSearch {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;//搜索的起始点
 
    public BreadthFirstSearch(Graph g, int s) {
        marked=new boolean[g.GetVerticals()];
        edgeTo=new int[g.GetVerticals()];
        this.s = s;
        bfs(g, s);
    }
 
    private void bfs(Graph g, int s) {
        Queue<Integer> queue = new Queue<Integer>();
        marked[s] = true;
        queue.enqueue(s);
        while (queue.size()!=0) {
            int v = queue.dequeue();
            for(int w : g.GetAdjacency(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.enqueue(w);
                }
            }
        }
    }
 
    public boolean HasPathTo(int v) {
        return marked[v];
    }
 
    public Stack<Integer> PathTo(int v) {
        if (!HasPathTo(v)) {
        	return null;
        }
 
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x!=s; x=edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    } 
    
    public static void main(String[] args) {
    	Graph G = new Graph(13);
    	G.AddEdge(0, 5);
    	G.AddEdge(4, 3);
    	G.AddEdge(0, 1);
    	G.AddEdge(9, 12);
    	G.AddEdge(6, 4);
    	G.AddEdge(5, 4);
    	G.AddEdge(0, 2);
    	G.AddEdge(11, 12);
    	G.AddEdge(9, 10);
    	G.AddEdge(0, 6);
    	G.AddEdge(7, 8);
    	G.AddEdge(9, 11);
    	G.AddEdge(5, 3);
    	G.AddEdge(4, 9);
    	
    	BreadthFirstSearch bfs = new BreadthFirstSearch(G, 0);
    	Stack<Integer> path = bfs.PathTo(11);
    	while(path.size() != 0) {
    		System.out.print(path.pop() + " ");
    	}    	
    }
}
