package heapsort;

import java.util.Arrays;
import java.util.Scanner;

public class HeapSort {
    public void heapSort(int[] arr) {
        int n = arr.length;

        //1. Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        //2.One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            System.out.println("before array : " + Arrays.toString(arr));
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            //3. Call max heapify on the reduced heap
            System.out.println("after array : " + Arrays.toString(arr));
            heapify(arr, i, 0);

        }
    }

    void heapify(int[] arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }

    private static int[] inputScanner() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please enter Array (separated by,):");
            String[] inputArray = scanner.nextLine().split(",");
            int[] arr = new int[inputArray.length];

            for (int i = 0; i < inputArray.length; i++) {
                try {
                    arr[i] = Integer.parseInt(inputArray[i]);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "The input contains a non-integer value. Please re-execute the program and enter a valid " +
                                    "integer.");
                    return new int[0];
                }
            }
            System.out.println("Input Array: " + Arrays.toString(arr));
            return arr;
        }
    }

    public static void main(String[] args) {
        HeapSort hs = new HeapSort();
        int[] arr = inputScanner();
        hs.heapSort(arr);
        System.out.println("Sorted array is");
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
