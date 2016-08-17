import java.util.Iterator;
import java.util.Scanner;

public class Stack<Item> implements Iterable<Item>  
{  
    private Node first; // 栈顶（最近添加的元素） 
    
    private int N; // 元素数量
    
    private class Node  
    { // 定义了结点的镶嵌类
        Item item;  
        Node next;  
    }  
    
    public boolean isEmpty() 
    { 
    	return first == null; // Or: N == 0. 
    } 
    
    public int size() 
    { 
    	return N; 
    } 
    
    public void push(Item item)  
    { //从栈顶添加元素 
        Node oldfirst = first;  
        first = new Node();  
        first.item = item;  
        first.next = oldfirst;  
        N++;  
    }  
    
    public Item pop()  
    { // 从栈顶删除元素
        Item item = first.item;  
        first = first.next;  
        N--;  
        return item;  
    }     
    
    public Iterator<Item> iterator()  
    {
    	return new ListIterator(); 
    }
    
    private class ListIterator implements Iterator<Item>  //实现迭代器
    {  
        private Node current = first; 
        
        public boolean hasNext()  
        { 
        	return current != null;
        }  
        public void remove() 
        { 
        	//null
        }  
        public Item next()  
        {  
	        Item item = current.item;  
	        current = current.next;  
	        return item;  
        }
    }
    
    public static void main(String[] args)  
    {  
//		Stack<Double> stack = new Stack<Double>(); 
//		String data = "10.0 20.0 30.0 40.0";
//	    Scanner sc = new Scanner(data); 
//	    while (sc.hasNext())  
//	    {	    	
//	    	stack.push(sc.nextDouble());
//	    }
//	    sc.close();
//	    
//	    for (Double value : stack) 
//	    {
//	    	System.out.println(value);
//	    }
    	
    	Stack<String> ops = new	 Stack<String>();
    	Stack<Double> vals = new Stack<Double>();
    	String expression = "( 3 + ( 4 * 5 ) * 10 )";
	    Scanner sc = new Scanner(expression); 
	    while (sc.hasNext())  
	    {	    	
	    	String s = sc.next();
	    	switch(s)
	    	{
		    	case "(":
		    		break;
		    	case "+":
		    		ops.push(s);
		    		break;
		    	case "-":
		    		ops.push(s);
		    		break;
		    	case "*":
		    		ops.push(s);
		    		break;
		    	case "/":
		    		ops.push(s);
		    		break;
		    	case "sqrt":
		    		ops.push(s);
		    		break;
		    	case ")":
		    		String op = ops.pop();
		    		double v = vals.pop();
		    		if(op.equals("+"))
		    		{
		    			v = vals.pop() + v;
		    		}
		    		else if(op.equals("-"))
		    		{
		    			v = vals.pop() - v;
		    		}
		    		else if(op.equals("*"))
		    		{
		    			v = vals.pop() * v;
		    		}
		    		else if(op.equals("/"))
		    		{
		    			v = vals.pop() / v;
		    		}
		    		else if(op.equals("sqrt"))
		    		{
		    			v = Math.sqrt(v);
		    		}		    		
		    		vals.push(v);
		    		break;
		    	default:
		    		vals.push(Double.parseDouble(s));
	    	}
	    }
	    sc.close();
	    System.out.println(vals.pop());     	
    } 
}
