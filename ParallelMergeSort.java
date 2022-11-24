import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
	
	private static Random rand=new Random();
	
	public static void main(String[] args) {

	
		int[] array1=new int[30000000];
		
		for(int i=0; i<array1.length; i++) {
			array1[i]=rand.nextInt();
		}
		Date start1=new Date();
		mergeSort(array1);
		Date end1=new Date();
		System.out.println(end1.getTime()-start1.getTime());
		
        int[] array2=new int[30000000];
		
		for(int i=0; i<array2.length; i++) {
			array2[i]=rand.nextInt();
		}
		Date start2=new Date();
		parallelMergeSort(array2);
		Date end2=new Date();
		System.out.println(end2.getTime()-start2.getTime());
		
		
        int[] array3=new int[30000000];
		
		for(int i=0; i<array3.length; i++) {
			array3[i]=rand.nextInt();
		}
		Date start3=new Date();
		Arrays.sort(array3);
		Date end3=new Date();
		System.out.println(end3.getTime()-start3.getTime());
		
	}
	
	private static void mergeSort(int[] array){

        if(array.length>1){

            int firstHalfSize=array.length/2;
            int[] firstHalf=new int[firstHalfSize];
            for(int i=0; i<firstHalfSize; i++){
                firstHalf[i]=array[i];
            }
            mergeSort(firstHalf);

            int secondHalfSize=array.length-firstHalfSize;
            int half=array.length/2;
            int[] secondHalf=new int[secondHalfSize];
            for(int i=0; i<secondHalfSize; i++){
                secondHalf[i]=array[half+i];
            }
            mergeSort(secondHalf);

            merge(firstHalf, secondHalf, array);
        }
    }
	
	private static void merge(int[] arr1, int[] arr2, int[] array){

        int curr1=0;
        int curr2=0;
        int curr3=0;

        while(curr1<arr1.length && curr2<arr2.length){
            if(arr1[curr1]<arr2[curr2]){
                array[curr3]=arr1[curr1];
                curr3++;
                curr1++;
            } else {
                array[curr3]=arr2[curr2];
                curr3++;
                curr2++;
            }
        }

        while(curr1<arr1.length){
             array[curr3]=arr1[curr1];
             curr3++;
             curr1++;
        }

        while(curr2<arr2.length){
            array[curr3]=arr2[curr2];
            curr3++;
            curr2++;
        }

    }
	
	public static void parallelMergeSort(int[] array) {
		RecursiveAction task=new ParallelSortTask(array);
		ForkJoinPool pool=new ForkJoinPool();
		pool.invoke(task);
	}
	
	
	private static class ParallelSortTask extends RecursiveAction{
		
		private int[] array;
		private final int limit=500;
		
		public ParallelSortTask(int[] array) {
			this.array=array;		
		}

		@Override
		protected void compute() {
			if(array.length<limit) {
			     Arrays.sort(array);
			} else {
			int firstHalfSize=array.length/2;
            int[] firstHalf=new int[firstHalfSize];
            for(int i=0; i<firstHalfSize; i++){
                firstHalf[i]=array[i];
            }
            int secondHalfSize=array.length-firstHalfSize;
            int half=array.length/2;
            int[] secondHalf=new int[secondHalfSize];
            for(int i=0; i<secondHalfSize; i++){
                secondHalf[i]=array[half+i];
            }
            
            invokeAll(new ParallelSortTask(firstHalf),
            		new ParallelSortTask(secondHalf));	
            
            merge(firstHalf, secondHalf, array);
		}
		}	
	
		
	}


}
