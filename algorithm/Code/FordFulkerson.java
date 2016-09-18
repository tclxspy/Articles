
public class FordFulkerson 
{
	private boolean[] marked;	//在剩余网络中是否存在从s到v的路径
	private FlowEdge[] edgeTo;	//从s到v的最短路径上的最后一条边
	private double value; 		//当前最大流量
	
	public FordFulkerson(FlowNetwork G, int s, int t)
	{	//找出从s到t的流量网络G的最大流量配置
		while(hasAugmentingPath(G, s, t))
		{	//利用所有存在的增广路径
			//计算当前的瓶颈容量
			double bottle = Double.POSITIVE_INFINITY;
			for(int v = t; v != s; v = edgeTo[v].other(v))
			{
				bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
			}
			//增大流量
			for(int v = t; v != s; v = edgeTo[v].other(v))
			{
				edgeTo[v].addResidualFlowTo(v, bottle);
			}
			value += bottle;
		}		
	}
	
	private boolean hasAugmentingPath(FlowNetwork G, int s, int t)
	{
		marked = new boolean[G.V()];	//标记路径已知的顶点
		edgeTo = new FlowEdge[G.V()]; 	//路径上的最后一条边
		Queue<Integer> q = new Queue<Integer>();
		
		marked[s] = true;	//标记起点
		q.enqueue(s); 		//并将它入列
		while(!q.isEmpty())
		{
			int v = q.dequeue();
			for(FlowEdge e : G.adj(v))
			{
				int w = e.other(v);
				if(e.residualCapacityTo(w) > 0 && !marked[w])
				{//(在剩余网络中)对于任意一条连接到一个未标记的顶点的边
					edgeTo[w] = e;	//保持路径上的最后一条边
					marked[w] = true;	//标记w，因为路径现在是已知的了
					q.enqueue(w);	//将它入列
				}
			}
		}
		return marked[t];
	}
	
	public double value()
	{
		return value;
	}
	
	public boolean inCut(int v)
	{
		return marked[v];
	}
	
	public static void main(String[] args)
	{
		FlowNetwork G = new FlowNetwork(6);
		int[] from = new int[]{0, 0, 1, 1, 2, 2, 3, 4};
		int[] to = new int[]{1, 2, 3, 4, 3, 4, 5, 5};
		double[] capacity = new double[]{2.0, 3.0, 3.0, 1.0, 1.0, 1.0, 2.0, 3.0};
		for(int i = 0; i < from.length; i++)
		{
			FlowEdge edge = new FlowEdge(from[i], to[i], capacity[i]);
			G.addEdge(edge);
		}		
		
		int s = 0, t = G.V() - 1;
		FordFulkerson maxflow = new FordFulkerson(G, s, t);
		System.out.println("Max flow from " + s + " to " + t);
		for(int v = 0; v < G.V(); v++)
		{
			for(FlowEdge e : G.adj(v))
			{
				if(v == e.from() && e.flow() > 0)
				{
					System.out.println(" " + e);
				}
			}
		}
		System.out.println("Max flow value = " + maxflow.value());
	}
}
