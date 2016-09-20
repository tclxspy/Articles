//低位优先的字符串排序
public class LSD 
{
	public static void sort(String[] a, int W)
	{//通过前W个字符将a[]排序
		int N = a.length;
		int R = 256;
		String[] aux = new String[N];
		
		for(int d = W - 1; d >= 0; d--)
		{//根据第d个字符用键索引计算法排序
			int[] count = new int[R+1];	//计算出现的频率
			for(int i = 0; i < N; i++)
			{
				count[a[i].charAt(d) + 1]++;
			}
			
			for(int r = 0; r < R; r++)	//将频率转换为索引
			{
				count[r + 1] += count[r];
			}
			
			for(int i = 0; i < N; i++)	//将元素分类
			{
				aux[count[a[i].charAt(d)]++] = a[i];
			}
			
			for(int i = 0; i < N; i++)	//回写
			{
				a[i] = aux[i];
			}
		}
	}
	
	public static void main(String[] args) 
	 {
		String[] a= {"4PGC938", "2IYE230", "3CI0720", "1ICK750", "1OHV845", "4JZY524", "1ICK750",
    			"3CI0720", "1OHV845", "1OHV845", "2RLA629", "2RLA629", "3ATW723"};
    	
    	LSD.sort(a, 7);
    	for(int i = 0; i < a.length; i++)
    	{
    		System.out.println(a[i]);
    	} 
	 }
}
