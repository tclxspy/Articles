
public class HeapSort {	
	
	/**
	 * the implement method of HeapSort
	 * @param data which prepare for sorting
	 */
	public static void heapSort(int[] data) {	
		//from the end index, build the Max-Heapify		
		int startIndex = getParentIndex(data.length - 1);
		for(int i = startIndex; i >= 0; i--) {
			maxHeapifyByIteration(data, data.length, i);
			//or
			//maxHeapifyByIteration(data, data.length, i);
		}
		
		//swap the head and end. then maintain the Max-Heapify
		for (int i = data.length - 1; i > 0; i--) {
			swap(data, 0, i);
			maxHeapifyByIteration(data, i, 0);
			//or
			//maxHeapifyByIteration(data, i, 0);
		}
	}
	
	/**
	 * build the Max-Heapify by recursion method
	 * @param data input data
	 * @param heapSize length of data
	 * @param index from the end index, build the Max-Heapify
	 */
	private static void maxHeapifyByRecursion(int[] data, int heapSize, int index) {		
		//get the left and right index
		int left = getChildLeftIndex(index);
		int right = getChildRightIndex(index);
		
		//get the max data's index
		int largest = index;
		if(left < heapSize && data[index] < data[left]) {
			largest = left;
		}
		
		if(right < heapSize && data[largest] < data[right]) {
			largest = right;
		}
		
		//swap the max data to parent node. and then maintain the Max-Heapify again
		if(largest != index) {
			swap(data, largest, index);
			maxHeapifyByRecursion(data, heapSize, largest);
		}		
	}
	
	/**
	 * build the Max-Heapify by iteration method
	 * @param data input data
	 * @param heapSize length of data
	 * @param index from the end index, build the Max-Heapify
	 */
	private static void maxHeapifyByIteration(int[] data, int heapSize, int index) {	
		while(true) {
			//get the left and right index
			int left = getChildLeftIndex(index);
			int right = getChildRightIndex(index);
			
			//get the max data's index
			int largest = index;
			if(left < heapSize && data[index] < data[left]) {
				largest = left;
			}
			
			if(right < heapSize && data[largest] < data[right]) {
				largest = right;
			}
			
			//swap the max data to parent node. and then maintain the Max-Heapify again
			if(largest != index) {
				swap(data, largest, index);
				index = largest;
			}	
			else {
				break;
			}
		}
	}
	
	/**
	 * swap data[i] and data[j]
	 * @param data
	 * @param i
	 * @param j
	 */
	private static void swap(int[] data, int i, int j) {
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
	
	private static int getParentIndex(int current) {
		return (current - 1) / 2;
	}
	
	private static int getChildLeftIndex(int current) {
		return 2 * current + 1;
	}
	
	private static int getChildRightIndex(int current) {
		return 2 * (current + 1);
	}	
	
	public static void main(String[] args) {	
		int[] sort = new int[]{1, 0, 10, 20, 3, 5, 6, 4, 9, 8, 12, 17, 34, 11};		
		heapSort(sort);
		for (int i = 0; i < sort.length; i++) {
			System.out.print(sort[i] + " ");
		}
	}
}
