import java.util.Iterator;
import java.util.Scanner;  

public class Bag<Item> implements Iterable<Item>  
{  
    private Node first; // 第一结点
    
    private int N;
    
    private class Node  
    {  
        Item item;  
        Node next;  
    }  
    
    public void add(Item item)  
    {
        Node oldfirst = first;  
        first = new Node();  
        first.item = item;  
        first.next = oldfirst;  
        N++;
    }  
    
    public boolean isEmpty() 
    { 
    	return first == null; // Or: N == 0. 
    }
    
    public int size()
    {
    	return N;
    }
    
    public Iterator<Item> iterator()  {
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
	    Bag<Double> numbers = new Bag<Double>();
	    String data = "10.0 20.0 30.0 40.0";
	    Scanner sc = new Scanner(data);
	    while (sc.hasNext())  
	    {	    	
	    	numbers.add(sc.nextDouble());
	    }
	    sc.close();	    

	    int N = numbers.size();  
	    double sum = 0.0;  
	    for (double x : numbers) 
	    {
	    	sum += x;  
	    }
	    
	    double mean = sum / N;  
	    sum = 0.0;  
	    
	    for (double x : numbers) 
	    {
	    	sum += (x - mean)*(x - mean);  
	    }
	    
	    double std = Math.sqrt(sum/(N-1));  
	    System.out.printf("Mean: %.2f\n", mean);  
	    System.out.printf("Std dev: %.2f\n", std);  
    }
} 