import java.util.Scanner;

public class WeightedQuickUnionUF {
	private int[] id; // 分量id（以触点作为索引）      
    
    private int[] sz; // （由触点索引的）各个根节点所对应的分量的大小
    
    private int count; // 分量数量  
    
    public WeightedQuickUnionUF(int N)  
    {  
        // 初始化分量id数组.  
        count = N;  
        id = new int[N]; 
        sz = new int[N];
        for (int i = 0; i < N; i++)  {
            id[i] = i;  
            sz[i] = 1;
        }              
    }  
    
    public int count()  { 
    	return count; 
    }  
    public boolean connected(int p, int q)  { 
    	return find(p) == find(q); 
    }  
    
    public int find(int p)   { 
    	// 寻找p节点所在组的根节点，根节点具有性质id[root] = root  
        while (p != id[p]) p = id[p];  
        return p; 
    }
    
    public void union(int p, int q)  {   
    	int i = find(p);  
        int j = find(q);  
        if (i == j) return;  
        // 将小树作为大树的子树  
        if (sz[i] < sz[j]) { 
        	id[i] = j; 
        	sz[j] += sz[i]; 
        }  
        else { 
        	id[j] = i; 
        	sz[i] += sz[j]; 
        }  
        count--; 
    }  
    
    public static void main(String[] args) {
    	String data = "4 3 3 8 6 5 9 4 2 1 5 0 7 2 6 1";
	    Scanner sc = new Scanner(data);
	    WeightedQuickUnionUF uf = new WeightedQuickUnionUF(10);
	    while (sc.hasNext()) {
	    	int p = sc.nextInt();
			int q = sc.nextInt();
			if(uf.connected(p, q) ) {
				continue;
			}
			uf.union(p, q);
			System.out.println(p+ " " + q);
	    }
	    sc.close();
		System.out.println(uf.count() + " 分量");
    }
}
