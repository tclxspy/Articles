//R向单词查找树
public class TrieST<Value> {
	private static int R = 256; //基数
	private Node root;
	private static class Node
	{
		private Object val;
		private Node[] next = new Node[R];
	}
	
	@SuppressWarnings("unchecked")
	public Value get(String key)
	{
		Node x = get(root, key, 0);
		if(x == null)
		{
			return null;
		}
		return (Value)x.val;
	}
	
	private Node get(Node x, String key, int d)
	{
		//返回以x作为根结点的字单词查找树中与key相关联的值
		if(x == null)
		{
			return null;
		}
		if(d == key.length())
		{
			return x;
		}
		char c = key.charAt(d);//找到第d个字符所对应的字单词查找树
		return get(x.next[c], key, d + 1);
	}
	
	public void put(String key, Value val)
	{
		root = put(root, key, val, 0);			
	}
	
	private Node put(Node x, String key, Value val, int d)
	{
		//如果key存在于以x为根结点的子单词查找树中则更新与它相关联的值
		if(x == null)
		{
			x = new Node();			
		}
		if(d == key.length())
		{
			x.val = val;
			return x;
		}
		char c = key.charAt(d);//找到第d个字符所对应的字单词查找树
		x.next[c] = put(x.next[c], key, val, d + 1);
		return x;
	}
	
	public void delete(String key)
	{  
	    root = delete(root, key, 0); 
	}  
	  
	private Node delete(Node x, String key, int d)
	{  
	    if(x == null) 
	    {
	    	return null;  
	    }
	    if(d == key.length())
	    {
	        x.val = null;  
	    }
	    else
	    {  
	        char c= key.charAt(d);  
	        x.next[c] = delete(x.next[c], key, d+1);  
	    }  
	    if(x.val != null) 
	    {
	    	return x;  
	    }
	      
	    for(char c = 0; c < R; c++) 
	    {
	        if(x.next[c] != null) 
	        {
	            return x; 
	        }
	    }
	    return null;  
	}  
	
	public static void main(String[] args) 
	 {
		TrieST<Integer> newST = new TrieST<Integer>();
    	String[] keys= {"Nicholas", "Nate", "Jenny", "Penny", "Cynthina", "Michael"};
    	for(int i = 0; i < keys.length; i++)
    	{
    		newST.put(keys[i], i);
    	}	    	
    	newST.delete("Penny");
    	for(int i = 0; i < keys.length; i++)
    	{
    		Object val = newST.get(keys[i]);
    		System.out.println(keys[i] + " " + val);
    	} 
	 }

}
