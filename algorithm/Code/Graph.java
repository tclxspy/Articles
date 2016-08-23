import java.util.ArrayList;
import java.util.List;

public class Graph {
 	private int verticals;//顶点个数
    private int edges;//边的个数
    private List<Integer>[] adjacency;//顶点联接列表
 
    @SuppressWarnings("unchecked")
	public Graph(int vertical) {
        this.verticals = vertical;
        this.edges = 0;
        adjacency = (List<Integer>[])new List[vertical]; 
        for (int v = 0; v < vertical; v++) {
            adjacency[v]=new ArrayList<Integer>();
        }
    }
 
    public int GetVerticals () {
        return verticals;
    }
 
    public int GetEdges() {
        return edges;
    }
 
    public void AddEdge(int verticalStart, int verticalEnd) {
        adjacency[verticalStart].add(verticalEnd);
        adjacency[verticalEnd].add(verticalStart);
        edges++;
    }
 
    public List<Integer> GetAdjacency(int vetical) {
        return adjacency[vetical];
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
    	
    	List<Integer> list = G.GetAdjacency(4);
    	for(int w : list) {
    		System.out.print(w + " ");
    	}
    }
}
