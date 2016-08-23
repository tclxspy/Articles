
public class DepthFirstSearch {
	private boolean[] marked;//记录是否被dfs访问过
    private int[] edgesTo;//记录最后一个到当前节点的顶点
    private int s;//搜索的起始点
 
    public DepthFirstSearch(Graph g, int s) {
        marked = new boolean[g.GetVerticals()];
        edgesTo = new int[g.GetVerticals()];
        this.s = s;
        dfs(g, s);
    }
 
    private void dfs(Graph g, int v) {
        marked[v] = true;
        for(Integer w : g.GetAdjacency(v)) {
            if (!marked[w]) {
                edgesTo[w] = v;
                dfs(g,w);
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
 
        for (int x = v; x!=s; x=edgesTo[x]) {
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
    	
    	DepthFirstSearch dfs = new DepthFirstSearch(G, 0);
    	Stack<Integer> path = dfs.PathTo(12);
    	while(path.size() != 0) {
    		System.out.print(path.pop() + " ");
    	}    	
    }
}

