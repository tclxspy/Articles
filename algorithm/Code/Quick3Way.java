
public class Quick3Way {

	/**
	 * the implement method of Quick3Way
	 * @param a which prepare for sorting
	 * @param lo the start index
	 * @param hi the end index
	 */
	public static void quick3Way(int[] a, int lo, int hi) {
		if(hi <= lo) {
			return;
		}
		
		int lt = lo, i = lo + 1, gt = hi;
		while(i <= gt) {
			if(a[i] < a[lt]) {
				swap(a, lt, i);
				lt++;
				i++;
			}
			else if(a[i] > a[lt]) {
				swap(a, gt, i);
				gt--;
			}
			else {
				i++;
			}
		}//now array a meet: a[lo..lt-1] < a[lt..gt] < a[gt+1..hi]
		//implement Quick3Way for a[lo..lt-1]
		quick3Way(a, lo, lt - 1);
		//implement Quick3Way for a[gt+1..hi]
		quick3Way(a, gt + 1, hi);
	}
	
	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void main(String[] args) {	
		int[] sort = new int[]{1, 0, 10, 20, 3, 5, 6, 4, 9, 8, 12, 17, 34, 11, 4, 9, 4, 12, 4};		
		quick3Way(sort, 0, sort.length - 1);
		for (int i = 0; i < sort.length; i++) {
			System.out.print(sort[i] + " ");
		}
	}
}
