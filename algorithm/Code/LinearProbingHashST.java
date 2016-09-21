//基于线性探测的符号表
public class LinearProbingHashST<Key,Value> 
{
	private static final int INIT_CAPACITY = 16;
	
	private int n;// 键值对数量
	private int m;// 散列表的大小
	private Key[] keys;// 保存键的数组
	private Value[] vals;// 保存值的数组
	
	public LinearProbingHashST() 
	{
		this(INIT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public LinearProbingHashST(int capacity) 
	{
		this.m = capacity;
		keys = (Key[]) new Object[capacity];
		vals = (Value[]) new Object[capacity];
	}
	
	public int hash(Key key)
	{
		return (key.hashCode() & 0x7fffffff) % m;
	}
	
	public void put(Key key, Value val)
	{
		if(key == null) 
		{
			throw new NullPointerException("key is null");
		}
		
		if(val == null)
		{
			delete(key);
			return;
		}
		
		// TODO扩容
		if(n >= m/2)
		{
			resize(2*m);
		}
		
		int i = hash(key);
		for (; keys[i] != null; i = (i + 1) % m) 
		{
			if(key.equals(keys[i]))
			{
				vals[i] = val;
				return;
			}
		}
		
		keys[i] = key;
		vals[i] = val;
		n++;
	}

	public void delete(Key key)
	{
		if(key == null) 
		{
			throw new NullPointerException("key is null");
		}
		
		if (!contains(key)) 
		{
			return;
		}
		
		// 找到删除的位置
		int i = hash(key);
	    while (!key.equals(keys[i])) 
	    {
	    	i = (i + 1) % m;
	    }

	    keys[i] = null;
		vals[i] = null;
		
		// 将删除位置后面的值重新散列
		i = (i + 1) % m;
		for (; keys[i] != null; i = (i + 1) % m) 
		{
			Key keyToRehash = keys[i];
			Value valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			n--;
			put(keyToRehash, valToRehash);
		}
		
		n--;
		// TODO缩容
		if(n>0 && n == m/8)
		{
			resize(m/2);
		}
	}
	
	public Value get(Key key)
	{
		if(key == null) 
		{
			throw new NullPointerException("key is null");
		}
		
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m) 
		{
			if(key.equals(keys[i]))
			{
				return vals[i];
			}
		}
		return null;
	}
	
	public boolean contains(Key key)
	{
		if(key == null) 
		{
			throw new NullPointerException("key is null");
		}
		
		return get(key) != null;
	}	
	
	private void resize(int cap)
	{
		LinearProbingHashST<Key,Value> t;
		t = new LinearProbingHashST<Key,Value>(cap);
		for(int i = 0; i < m; i++)
		{
			if(keys[i] != null)
			{
				t.put(keys[i], vals[i]);
			}
		}
		keys = t.keys;
		vals = t.vals;
		m = t.m;
	}
	
	 public static void main(String[] args) 
	 { 
		 LinearProbingHashST<String, String> st = new LinearProbingHashST<String, String>();
         		String[] data = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "m"};
         		String[] val = new String[]{"aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh", "mmm"};
         		for (int i = 0; i < data.length; i++)
         		{            
			st.put(data[i], val[i]);
         		} 

         		for (int i = 0; i < data.length; i++)
         		{
             		System.out.println(data[i] + " " + st.get(data[i]));
         		}     
     	}
}

