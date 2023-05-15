// By phan0216 and ajayx006
public class ArrayList<T extends Comparable<T>> implements List<T> {
    private int size;
    private boolean isSorted;
    private T[] a;
    public ArrayList() {
        a = (T[]) new Comparable[2];
        this.isSorted = true;
        this.size = 0;
    }
    @Override
    public boolean add(T t) {
        if (t == null) {
            return false;
        }

        if (size == a.length) {
            resize();
        }

        a[size++] = t;
        if (size > 1 && a[size - 2].compareTo(a[size - 1]) > 0) { // simple sortedness check
            isSorted = false;
        }

        else if (size == 1) isSorted = true; // a one-item array is sorted

        return true;
    }
    @Override
    public boolean add(int index, T t) {
        if (t == null || index < 0 || index > size) {
            return false;
        }

        if (index == size) {
            add(t);
            return true;
        }

        if (size == a.length) {
            resize(); // see helper
        }

        System.arraycopy(a, index, a, index + 1, size - index); // array shifting

        a[index] = t;

        size++;

        if ((index == 0 && a[index+1].compareTo(a[index]) < 0)
                || ((index > 0) && (a[index-1].compareTo(a[index]) > 0) && (a[index+1].compareTo(a[index]) < 0))) {
            isSorted = false; // simple sortedness check
        }

        return true;
    }

    @Override
    public void clear() {
        a = (T[]) new Comparable[2];

        size = 0;

        isSorted = true; // empty array is sorted
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return a[index];
    }

    @Override
    public int indexOf(T t) {
        if (t == null) {
            return -1;
        }

        if (isSorted) {
            int low = 0;

            int high = size - 1;

            while (low <= high) {
                if (a[low].equals(t)) return low; // the lower index will eventually be the wanted element's

                int mid = (low + high) / 2;

                int compare = get(mid).compareTo(t);

                if (compare == 0) {
                    high = mid;

                } else if (compare < 0) {
                    low = mid + 1;    //
                                      // narrows down search range progressively
                } else {              //
                    high = mid - 1;   //
                }
            }
            return -1; // if not found

        } else {
            for (int i = 0; i < size; i++) { // linear search if not sorted
                if (get(i).equals(t)) {
                    return i;
                }
            }
            return -1;
        }
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void sort() { // insertion sort
        for (int i = 1; i < size; i++) {
            T key = a[i];

            int j = i - 1;

            while (j >= 0 && a[j].compareTo(key) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
        isSorted = true;
    }

    @Override
    public T remove(int index) { // removes the element at the given index
        if (index < 0 || index >= size) {
            return null;
        }

        T removedElement = a[index];
        for (int i = index; i < size - 1; i++) {
            a[i] = a[i + 1];
        } // shifts index

        a[size - 1] = null;
        size--;

        checkIfSorted(); // sortedness helper function
        return removedElement;
    }

    @Override
    public void equalTo(T t) {
        if (t == null) {
            return;
        }
        if (isSorted) {
            while (a[0].compareTo(t) != 0) remove(0);           // removes from each end of the
            while (a[size-1].compareTo(t) != 0) remove(size-1); // list
        } else {
            for (int i = 0; i < size; i++) {
                if (!get(i).equals(t)) {
                    remove(i);
                    i--;
                }
            }
        }
        isSorted = true;
    }
    @Override
    public void reverse() {
        if (size <= 1) return; // reversing does nothing if the list is less than two long.

        int left = 0;
        int right = size - 1;
        reverse_helper(left, right); // see reverse_helper()

        checkIfSorted();
    }

    @Override
    public void merge(List<T> otherList) {
        if (otherList == null) {
            return;
        }
        ArrayList<T> other = (ArrayList<T>) otherList;
        this.sort();    // sorts both lists
        other.sort();   //

        int i = 0, j = 0, k = 0;
        T[] merged_a = (T[]) new Comparable[this.size + other.size]; // new perfect-size array

        while (i < this.size && j < other.size) {
            if (this.a[i].compareTo(other.a[j]) <= 0) {
                merged_a[k++] = this.a[i++];
            } else {
                merged_a[k++] = other.a[j++];
            }
        } // takes care of the smallest list first

        while (j < other.size) {
            merged_a[k++] = other.a[j++];
        } // checks to see if there are any leftover items

        this.a = merged_a;
        this.size = k;
        this.isSorted = true;
    }
    @Override
    public boolean rotate(int n) {
        if (n <= 0 || size <= 1) {
            return false; // no need to rotate an empty or single-item list
        }
        int rotations = n % size; // handle rotations greater than size of a

        if (rotations == 0) {
            return false; // no need to rotate if n is a multiple of size
        }

        // reverse the entire list
        int left = 0;
        int right = size - 1;
        reverse_helper(left, right);

        // reverse the first half of the list up to n
        right = rotations - 1;
        reverse_helper(left, right);

        // reverse the remaining part of the list
        left = rotations;
        right = size - 1;
        reverse_helper(left, right);
        checkIfSorted();
        return true;
    }
    private void reverse_helper(int left, int right) { // cute helper method uwu
        /*
        Basically a mini reverse method that helps with rotation() and reverse. Swaps two items with each other from
        the head and tail of a list/list segment.
         */
        while (left < right) {
            T temp = a[left];
            a[left] = a[right];
            a[right] = temp;
            left++;
            right--;
        }
    }

    @Override
    public boolean isSorted() {
        return isSorted;
    }
    private void checkIfSorted() { // sortedness checker
        isSorted = true;
        for (int i = 0; i < size - 1; i++) {
            if (a[i].compareTo(a[i + 1]) > 0) { // compares two neighboring items; all pairs must be in ascending order
                isSorted = false;
                break; //...otherwise isSorted is false
            }
        }
    }

    private void resize() {
        T[] newElements = (T[]) new Comparable[a.length * 2 + 1]; // +1 accounts for if a has length 0 (unlikely)
        System.arraycopy(a,0,newElements,0,size);
        a = newElements;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (T t : a) {
            out.append(t).append("\n"); // each item on its own line
        }
        return out.toString();
    }
//    public static void main(String[] args) {
//        String[] test = new String[]{"John","Bill","Steve","Yara","Debbie","Liam","Bob", "Jack", "Rudolph"};
//        String[] test1 = new String[]{"Bob","Laith","Simmonds",null,"Yara","Debbie","Liam"};
//
//        List<String> list = new ArrayList<>();
//
//        List<String> list1 = new ArrayList<>();
//        for (String s : test) {
//            list.add(s);
//        }
//        for (String s1 : test1) {
//            list1.add(s1);
//        }
//        System.out.println("List: \n"+list);
//        System.out.println("List1: \n"+list1);
//
//        list.merge(list1);
//
//        System.out.println("List after merge: \n"+list);
//    }
}