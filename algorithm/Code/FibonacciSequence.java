
public class FibonacciSequence {
	private static long i = 0;
	
	public static long F(int N)  {    	
	    if (N == 0)
	        return 0;
	    if (N == 1) 
	        return 1;
	    i++;
	    return F(N-1) + F(N-2);
	}
//	public static void main(String[] args)  {
//	    for (int N = 0; N < 100; N++)
//	        System.out.println(N + " " + F(N)+ " " + i + " "+ (long)Math.pow(1.618, N+1));
//	}    

//	public static long F(int n)
//	{
//		long a = 0, b = 1, c, i;
//		
//		if( n == 0)
//			return a;
//		
//		for (i = 2; i <= n; i++){
//			c = a + b;
//			a = b;
//			b = c;
//		}
//		return b;
//	}
	
	public static long fib(int n)
	{
		long[][] f = {{1,1},{1,0}};
		if( n == 0)
			return 0;
		if( n == 1)
			return 1;
		
		return power(f, n-1);
	}
	
	public static long power(long[][] f, int n)
	{
		if( n == 0)
			return 0;
		if( n == 1)
			return 1;
	  
		long[][] m = {{1,1},{1,0}};
	
		power(f, n/2);
		long x = multiply(f, f); 
	  
		if (n % 2 != 0)
			x = multiply(f, m);
	  
	  return x;
	}
	
	public static long multiply(long[][] f, long[][] m)
	{
	  long x =  f[0][0]*m[0][0] + f[0][1]*m[1][0];
	  long y =  f[0][0]*m[0][1] + f[0][1]*m[1][1];
	  long z =  f[1][0]*m[0][0] + f[1][1]*m[1][0];
	  long w =  f[1][0]*m[0][1] + f[1][1]*m[1][1];
	 
	  f[0][0] = x;
	  f[0][1] = y;
	  f[1][0] = z;
	  f[1][1] = w;
	  
	  return x;
	}
	
	public static void main(String[] args)
	{
	    for (int n = 0; n < 100; n++)
	    	System.out.println(n + " " + fib(n));		
	}
}
