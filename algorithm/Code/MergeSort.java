
public class MergeSort {

	/**
	 * the implement method of MergeSort
	 * @param arr which prepare for sorting
	 */
	public static void mergeSort(int[] arr) {
		int len = arr.length;
		int[] result = new int[len];
		
		for(int block = 1; block < len; block *= 2) {
			for(int start = 0; start < len; start += 2 * block) {
				int low = start;
				int mid = (start + block) < len ? (start + block) : len;
				int high = (start + 2 * block) < len ? (start + 2 * block) : len;
				//the start and end index of two array
				int start_one = low, end_one = mid;
				int start_two = mid, end_two = high;
				//combine the two array
				while(start_one < end_one && start_two < end_two) {
					result[low++] = arr[start_one] < arr[start_two] ? arr[start_one++] : arr[start_two++];					
				}				
				while(start_one < end_one) {
					result[low++] = arr[start_one++];
				}				
				while(start_two < end_two) {
					result[low++] = arr[start_two++];
				}
			}
			int[] temp = arr;
			arr = result;
			result = temp;
		}
		result = arr;
	}
	
	public static void main(String[] args) {	
		int[] sort = new int[]{1, 0, 10, 20, 3, 5, 6, 4, 9, 8, 12, 17, 34, 11};		
		mergeSort(sort);
		for (int i = 0; i < sort.length; i++) {
			System.out.print(sort[i] + " ");
		}
	}
}
