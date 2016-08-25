# 算法列表

本文从时间效率和占用空间内存角度评估，找出最优算法。

 1. 经典递归算法Recursive algorithm（很慢）
 2. 动态存储算法Dynamic programming（慢）
 3. 矩阵幂算法Matrix exponentiation（快）
 4. 倍数公式算法Fast doubling（很快）
 5. 倍数公式算法+快速乘法Fast doubling with Karatsuba(最快)

# Fibonacci数列

**1.数列介绍**

斐波那契数列（Fibonacci sequence），又称黄金分割数列、因数学家列昂纳多·斐波那契（Leonardoda Fibonacci）以兔子繁殖为例子而引入，故又称为“兔子数列”，指的是这样一个数列：
0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, ...

**2.数列规律**

当我们将这些数字画成正方形时，会得到一个螺旋扩大的图形，如下。
![螺旋](http://img.blog.csdn.net/20160414092821498)

数列的规律是：F（n）= F(n-1) + F(n-2)

而且当n趋向于无穷大时，前一项与后一项的比值越来越逼近**黄金分割0.618**（或者说后一项与前一项的比值小数部分越来越逼近0.618）。

1÷1=1，1÷2=0.5，2÷3=0.666...，3÷5=0.6，5÷8=0.625…，
55÷89=0.617977…144÷233=0.618025…46368÷75025=0.6180339886…...

越到后面，这些比值越接近黄金比。

**3.通用公式：**

![公式](http://img.blog.csdn.net/20160414093608112)

或者（φ为 1.618034...）
![公式](http://img.blog.csdn.net/20160414093654881)

# 经典递归算法

**1.递归公式**

F（n）= F(n-1) + F(n-2)

**2.代码**

[点击下载代码](https://github.com/tclxspy/Articles/blob/master/algorithm/Code/FibonacciSequence.java)

```Java

private static long i = 0;

public static long F(int n)  {    	
    if (n == 0)
        return 0;
    if (n == 1) 
        return 1;
        
    i++;//记录计算次数
    return F(n-1) + F(n-2);
}
public static void main(String[] args)  {
    for (int n = 0; n < 40; n++)
        System.out.println(n + " " + F(n)+ " " + i + " "+ (long)Math.pow(1.618, n+1));
}    
```

输出：
0  0  0  1
1  1  0  2
2  1  1  4
3  2  3  6
4  3  7  11
5  5  14  17
.......
35  9227465  39088132  33360044
36  14930352  63245948  53976552
37  24157817  102334116  87334061
38  39088169  165580101  141306511
39  63245986  267914255  228633935

**3.评估**

时间效率-->**运算次数O(φ^n) **。n越大，越接近此值，见上最后两列。
空间内存-->**占用内存O(n)**。

# 动态存储算法

将计算过的F(n-1)和F(n-2)储存起来，不用再次计算。

**1.代码**

```java

public static long F(int n)
{
	long a = 0, b = 1, c, i;
	
	if( n == 0)
		return a;
	
	for (i = 2; i <= n; i++){
		c = a + b;
		a = b;
		b = c;
	}
	return b;
}

public static void main(String[] args)
{
    for (int n = 0; n < 100; n++)
    	System.out.println(n + " " + F(n));
}
```

**2.评估**
时间效率-->**运算次数O(n) **。
空间内存-->**占用内存O(1)**。

# 矩阵幂算法

**1.公式**

![公式](http://img.blog.csdn.net/20160414100601358)

**2.证明**

![证明过程](http://img.blog.csdn.net/20160414100829827)

**3.代码**

```java

public static long fib(int n)
{
	long[][] f = {{1,1},{1,0}};
	if (n == 0)
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
```

**4.评估**
时间效率-->**运算次数O(logn) **。
空间内存-->**占用内存O(1)**。

# 倍数公式算法

**1.公式**

![公式](http://img.blog.csdn.net/20160414102652011)

**2.证明**

在矩阵幂公式基础上。

![证明过程](http://img.blog.csdn.net/20160414102818450)

**3.代码**

放到下一个算法里。

# 倍数公式算法+快速乘法

**1.公式**

还是跟倍数公式算法一样，只是里面的乘法用快速乘法替代。

首先介绍快速乘法。

Karatsuba乘法是一种快速乘法算法。由Anatolii Alexeevitch Karatsuba于1960年提出，并于1962年发表。一般我们做高精度乘法，普通算法的复杂度为O(n^2)，而Karatsuba算法的复杂度为O(3n^log3) ≈ O(3n^1.585)。

**2.证明**

Karatsuba算法主要应用于两个大数的相乘，原理是将大数分成两段后变成较小的数位，然后做3次乘法。推导过程如下：

计算两个很大的数xy相乘，取一个正整数m。
![推导过程](http://img.blog.csdn.net/20160414103601745)

**3.代码**

[点击下载代码](https://github.com/tclxspy/Articles/blob/master/algorithm/Code/FastDoublingWithKaratsuba.java)

```java

//倍数公式算法
private static long Fibonacci(int n) {
	long a = 0;
	long b = 1;		
	
	for (int i = 31; i >= 0; i--) {//n最大为2^31-1，所以只需要移位32次
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

//倍数公式算法+快速算法
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

//快速算法
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
```

输出结果一样。

0   0   0
1   1   1
2   1   1
3   2   2
4   3   3
5   5   5
....
45  1134903170  1134903170
46  1836311903  1836311903
47  2971215073  2971215073
48  4807526976  4807526976
49  7778742049  7778742049
50  12586269025  12586269025

**4.评估**
时间效率-->**运算次数O(logn) **。
空间内存-->**占用内存O(1)**。

# 算法时间效率对比

运行环境：
Intel Core 2 Quad Q6600 (2.40 GHz)
单线程 
Windows XP SP 3, Java 1.6.0_22.

![这里写图片描述](http://img.blog.csdn.net/20160414105250405)

![这里写图片描述](http://img.blog.csdn.net/20160414105323922)

![这里写图片描述](http://img.blog.csdn.net/20160414105304064)

单位ns。
![这里写图片描述](http://img.blog.csdn.net/20160414105559285)


参考资料：
https://www.nayuki.io/page/fast-fibonacci-algorithms

https://en.wikipedia.org/wiki/Karatsuba_algorithm

https://www.mathsisfun.com/numbers/fibonacci-sequence.html