//三向字符串快速排序
public class Quick3string {
	public static void sort (String[] a)
	{
		sort(a, 0, a.length - 1, 0);
	}
	
	private static void sort(String[] a, int lo, int hi, int d)
	{
		if(hi <= lo)
		{
			return;
		}
		int lt = lo, gt = hi;
		int v = charAt(a[lo], d);
		int i = lo + 1;
		while(i <= gt)
		{
			int t = charAt(a[i], d);
			if(t < v)
			{
				swap(a, lt, i);
				lt++;
				i++;
			}
			else if(t > v) 
			{
				swap(a, gt, i);
				gt--;
			}
			else 
			{
				i++;
			}
		}
		sort(a, lo, lt - 1, d);		
		if(v >= 0)
		{
			sort(a, lt, gt, d + 1);
		}
		sort(a, gt + 1, hi, d);
	}
	
	private static int charAt(String s, int d)
	{
		if(d < s.length())
		{
			return s.charAt(d);
		}
		else
		{
			return -1;
		}
	}
	
	private static void swap(String[] a, int i, int j) 
	{
		String temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void main(String[] args) 
	{	
		String[] sort = new String[]{"Nicholas", "Nate", "Jenny", "Penny", "Cynthina", "Michael"};
		Quick3string.sort(sort);
		for (int i = 0; i < sort.length; i++)
		{
			System.out.println(sort[i]);
		}
	}
}
