
/**
 * 霍夫曼压缩
 * @author nicholas.tang
 *
 */
public class Huffman 
{
	public static int R = 256;
	public static final int asciiLength = 8;//ascii码，一个字符等于8个bit
	public static String bitStreamOfTrie = "";//使用前序遍历将霍夫曼单词查找树编码为比特流
	public static int lengthOfText = 0;//要压缩文本的长度
	
	private static String next = "";//读取霍夫曼单词查找树用到的next字符串，它指向下一个比特流的子字符串，用了遍历比特流
	
	/**
	 * 霍夫曼单词查找树中的结点
	 * @author nicholas.tang
	 *
	 */
 	private static class Node implements Comparable<Node>
	{
		private char ch;	//内部结点不会使用该变量
		private int freq;	//展开过程不会使用该变量
		private final Node left, right;
		
		Node(char ch, int freq, Node left, Node right)
		{
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		public boolean isLeaf()
		{
			return left == null && right == null;
		}
		
		public int compareTo(Node that)
		{
			return this.freq - that.freq;
		}
	}
	
 	/**
 	 * 解压
 	 * @param bitSteam 霍夫曼单词查找树编码为的比特流
 	 * @param length 文本长度
 	 * @param huffmanCode 霍夫曼编码
 	 * @return 解压后的文本
 	 */
	public static String expand(String bitSteam, int length, String huffmanCode)
	{	
	    Node root = null;
	    if(bitSteam == "")
	    {
	    	return "";
	    }
	    else
	    {
	    	root = readTrie(bitSteam);	
	    }

	    int j = 0;
	    String text = "";
	    for(int i = 0; i < length; i++)
	    {
		Node x = root;
		while(!x.isLeaf())
		{
			if(huffmanCode.substring(j, j+1).equals("1"))
			{
				x = x.right;
			}
			else
			{
				x = x.left;
			}
			j++;
		}
		text +=x.ch;
	    }	
	    return text;
	}
	
	/**
	 * 压缩
	 * @param s 要压缩的文本
	 * @return 压缩后，反馈的霍夫曼编码
	 */
	public static String compress(String s)
	{
		//读取输入
		char[] input = s.toCharArray();
		
		//统计频率
		int[] freq = new int[R];
		for(int i = 0; i < input.length; i++)
		{
			freq[input[i]]++;
		}
		
		//构造霍夫曼编码树
		Node root = buildTrie(freq);
		
		//递归地构造编译表
		String[] st = new String[R];
		buildCode(st, root, "");
		
		//递归地打印解码用的单词查找树，即比特流
		writeTrie(root);
		
		//打印字符总数
		lengthOfText = input.length;
		
		//使用霍夫曼编码处理输入
		String codeOfHuffman = "";
		for(int i = 0; i < input.length; i ++)
		{
			String code = st[input[i]];			
			for(int j = 0; j < code.length(); j++)
			{
				if(code.charAt(j) == '1')
				{
					codeOfHuffman += '1';
				}
				else
				{
					codeOfHuffman += '0';
				}
			}
		}
		return codeOfHuffman;//返回霍夫曼编码
	}
	
	/**
	 * 构建霍夫曼单词查找树
	 * @param freq 字符在文本出现的频率
	 * @return 霍夫曼单词查找树
	 */
	private static Node buildTrie(int[] freq)
	{
		MinPQ<Node> pq = new MinPQ<Node>(R);
		for(char c = 0; c < R; c++)
		{
			if(freq[c] > 0)
			{
				pq.insert(new Node(c, freq[c], null, null));
			}
		}
		while(pq.size() > 1)
		{//合并两颗频率最小的树
			Node x = pq.delMin();
			Node y = pq.delMin();
			Node parent = new Node('\0', x.freq + y.freq, x, y);
			pq.insert(parent);
		}
		return pq.delMin();
	}
	
	/**
	 * 构造编译表
	 * @param st 编译表
	 * @param x 霍夫曼单词查找树中的结点
	 * @param s 编译表内容
	 */
	private static void buildCode(String[] st, Node x, String s)
	{
		if(x.isLeaf())
		{
			st[x.ch] = s;
			return;
		}
		buildCode(st, x.left, s + '0');
		buildCode(st, x.right, s + '1');
	}
	
	/**
	 * 使用前序遍历将霍夫曼单词查找树编码为比特流
	 * @param x 霍夫曼单词查找树
	 */
	private static void writeTrie(Node x)
	{//输出单词查找树的比特字符串
		if(x.isLeaf())
		{
			bitStreamOfTrie += '1';
			String temp = Integer.toBinaryString(x.ch);
			int n = asciiLength - temp.length();
			temp = repeatStrings("0", n) + temp;
			bitStreamOfTrie += temp;
			return ;
		}
		bitStreamOfTrie += '0';
		writeTrie(x.left);
		writeTrie(x.right);
	}	
	
	/**
	 * 用比特流构造霍夫曼单词查找树
	 * @param s 比特流
	 * @return 霍夫曼单词查找树
	 */
	private static Node readTrie(String s)
	{    		
    	    if(s.substring(0, 1).equals("1"))
    	    {  
    	    	int value = Integer.parseInt(s.substring(1, 1 + asciiLength),2);
    		next = s.substring(1 + asciiLength);
    		return new Node((char)value, 0, null, null);    		
    	    }
    	    else
    	    {    		
    		next = s.substring(1);
    		return new Node('\0', 0, readTrie(next), readTrie(next));
    	    }    	
	}
	
	/**
	 * 重复字符串
	 * @param s 需要重复的字符串
	 * @param n 重复次数
	 * @return 重复后的字符串
	 */
	private static String repeatStrings(String s , int n)
	{
		  String temp = "";
		  for(int i = 0; i < n;i++)
		  {
			  temp += s;
		  }
		  return temp;
	}
	
	public static void main(String[] args) 
	{
    	    String text = "ABRACADABRA!";
    	    System.out.println("Input text: " + text);
    	
    	    String HuffmanCode = Huffman.compress(text);
    	    int bitsOfText = Huffman.lengthOfText * Huffman.asciiLength;
    	    String bitStream = Huffman.bitStreamOfTrie;
    	    double compressionRatio = 1.0 * HuffmanCode.length() / bitsOfText;
    	
	    System.out.println("Huffman Code: " + HuffmanCode);	    
	    System.out.println("BitStream: " + bitStream);
	    System.out.println("Huffman Code length(bit): " + HuffmanCode.length());
	    System.out.println("Length of text(bit): " + bitsOfText);
	    System.out.println("Compression ratio: " + compressionRatio * 100 + "%");
	    
	    String expandText = Huffman.expand(bitStream, lengthOfText, HuffmanCode);
	    System.out.println("Expand text: " + expandText);
        }
}
