package com.app.ScoringApi.algorithm;

public class RemoveDuplicatesNDArray {
    // Custom hash-table for integers (open addressing, linear probing)
    static class IntHashTable {
        private int[] table;
        private boolean[] occupied;

        public IntHashTable(int size) {
            table = new int[size];
            occupied = new boolean[size];
        }

        // Insert returns true if number was inserted (not duplicate)
        // Returns false if number already exists
        public boolean insert(int value) {
            int index = Math.abs(value) % table.length;

            while (occupied[index]) {
                if (table[index] == value) {
                    return false; // duplicate found
                }
                index = (index + 1) % table.length; // linear probing
            }

            table[index] = value;
            occupied[index] = true;
            return true; // inserted successfully
        }
    }

    // Removes duplicates from each row
    public static int[][] removeDuplicates(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {

            int[] row = arr[i];

            // Create custom hash table sized to the row
            IntHashTable seen = new IntHashTable(row.length * 2);

            for (int j = 0; j < row.length; j++) {
                int value = row[j];

                if (seen.insert(value)) {
                    // First time seeing this value → keep it
                    continue;
                } else {
                    // Duplicate → replace with 0
                    row[j] = 0;
                }
            }
        }
        return arr;
    }

    // Simple test
    public static void main(String[] args) {
        int[][] input = {
                {1, 3, 1, 2, 3, 4, 4, 3, 5},
                {1, 1, 1, 1, 1, 1, 1}
        };

        int[][] output = removeDuplicates(input);

        // Print result
        for (int[] row : output) {
            System.out.print("{");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) System.out.print(", ");
            }
            System.out.println("}");
        }
    }


    // brief explanation
//    For each row:
//
//    Build a custom hash table (open addressing + linear probing).
//
//    For each element:
//
//    If inserting into the hash table succeeds, keep it.
//
//    If inserting fails (value already exists), set the current element to 0.
//
//    Continue until the row is processed.
//
//    This avoids any Java built-ins like contains, HashSet, HashMap, etc.



//
//   Time Complexity
//
//    Let:
//
//    n = number of rows
//
//            mᵢ = number of elements in row i
//
//    For each row, insertion into our hash table is O(1) average.
//
//    Total time =
//
//            O(m₁ + m₂ + ... + mₙ)
//
//    which simplifies to:
//
//    O(total elements) → linear time
//
//    Space complexity per row: O(m) for the hash table.
}
