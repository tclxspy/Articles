//基于三向单词查找树的符号表
public class TST<Value> {
	private Node root;
	private class Node
	{
		char c;
		Node left, mid, right;
		Value val;
	}
	
	public Value get(String key)
	{
		Node x = get(root, key, 0);
		if(x == null)
		{
			return null;
		}
		return x.val;
	}
	
	private Node get(Node x, String key, int d)
	{
		if(x == null)
		{
			return null;
		}
		char c = key.charAt(d);
		if(c < x.c)
		{
			return get(x.left, key, d);
		}
		else if(c > x.c)
		{
			return get(x.right, key, d);
		}
		else if(d < key.length() - 1)
		{
			return get(x.mid, key, d + 1);
		}
		else
		{
			return x;
		}
	}
	
	public void put(String key, Value val)
	{
		root = put(root, key, val, 0);
	}
	
	private Node put(Node x, String key, Value val, int d)
	{
		char c = key.charAt(d);
		if(x == null)
		{
			x = new Node();
			x.c = c;
		}
		if(c < x.c)
		{
			x.left = put(x.left, key, val, d);
		}
		else if(c > x.c)
		{
			x.right = put(x.right, key, val, d);
		}
		else if(d < key.length() - 1)
		{
			x.mid = put(x.mid, key, val, d + 1);
		}
		else
		{
			x.val = val;
		}
		return x;
	}
	
	 public static void main(String[] args) 
	 {
	    	TST<Integer> newTST = new TST<Integer>();
	    	String[] keys= {"Nicholas", "Nate", "Jenny", "Penny", "Cynthina", "Michael"};
	    	for(int i = 0; i < keys.length; i++)
	    	{
	    		newTST.put(keys[i], i);
	    	}	    	
	    	
	    	for(int i = 0; i < keys.length; i++)
	    	{
	    		int val = newTST.get(keys[i]);
	    		System.out.println(keys[i] + " " + val);
	    	} 
	 }
}
