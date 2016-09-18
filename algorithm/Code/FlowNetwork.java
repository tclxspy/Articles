
public class FlowNetwork 
{
	private final int V;
	private int E;
	private Bag<FlowEdge>[] adj;
	
	@SuppressWarnings("unchecked")
	public FlowNetwork(int V)
	{
		this.V = V;
		this.E = 0;
		adj = (Bag<FlowEdge>[]) new Bag[V];
		for(int v = 0; v < V; v++)
		{
			adj[v] = new Bag<FlowEdge>();
		}
	}
	
	public int V()
	{
		return V;
	}
	
	public int E()
	{
		return E;
	}
	
	public void addEdge(FlowEdge e)
	{
		int v = e.either(), w = e.other(v);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}
	
	public Iterable<FlowEdge> adj(int v)
	{
		return adj[v];
	}
	
	public Iterable<FlowEdge> edges()
	{
		Bag<FlowEdge> b = new Bag<FlowEdge>();
		for(int v = 0; v < V; v++)
		{
			for(FlowEdge e : adj[v])
			{
				if(e.other(v) > v)
				{
					b.add(e);
				}
			}
		}
		return b;
	}
}

