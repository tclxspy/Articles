
public class FastDoublingWithKaratsuba {
	private static long Fibonacci(int n) {
		long a = 0;
		long b = 1;		
		
		for (int i = 31; i >= 0; i--) {
			long d = a * (b * 2 - a);
			long e = a * a + b * b;
			a = d;
			b = e;
			if (((n >> i) & 1) != 0) {
				long c = a + b;
				a = b;
				b = c;
			}
		}
		return a;
	}
	
	private static long FibonacciWithKaratsuba(int n) {
		long a = 0;
		long b = 1;		
		
		for (int i = 31; i >= 0; i--) {
			long d = Karatsuba(a, b * 2 - a);
			long e = Karatsuba(a, a) + Karatsuba(b, b);
			a = d;
			b = e;
			if (((n >> i) & 1) != 0) {
				long c = a + b;
				a = b;
				b = c;
			}
		}
		return a;
	}
	
	public static long Karatsuba(long x, long y){
		if((x < 10) || (y < 10)){
			return x * y;
		}
		
		String s1 = String.valueOf(x);
		String s2 = String.valueOf(y);		
		int maxLength = Math.max(s1.length(), s2.length());
		int m = (int)Math.pow(10, maxLength/2);//取10 的（maxLength长度一半）次幂为除数		
	
		long xHigh = x / m;
		long xLow = x % m;
		long yHigh = y / m;
		long yLow = y % m;
	
		long a = Karatsuba(xHigh, yHigh);		
		long b = Karatsuba((xLow + xHigh), (yLow + yHigh));
		long c = Karatsuba(xLow, yLow);	
		
		return a * m * m + (b - a - c) * m + c;
	}	
	
	public static void main(String[] args)  {	
	    for (int N = 0; N < 100; N++)
	        System.out.println(N + " " + Fibonacci(N) + " "+ FibonacciWithKaratsuba(N));
	}   
}
	
