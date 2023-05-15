// By phan0216 and ajayx006
public class LinkedList<T extends Comparable<T>> implements List<T> {
    private Node<T> head;
    private int size;
    private boolean isSorted;

    public LinkedList() {
        head = null;
        size = 0;
        isSorted = true;
    }

    @Override
    public boolean add(T element) {
        if (element == null) {
            return false;
        }

        if (head == null) {
            head = new Node<>(element);
            size++;
            return true; // adding to empty list
        }

        Node<T> current = head;

        while (current.getNext() != null) {
            current = current.getNext();
        }

        current.setNext(new Node<>(element));

        size++;

        checkIfSorted();

        return true;
    }

    @Override
    public boolean add(int index, T element) {
        if (index < 0 || index >= size) {
            return false;
        }

        Node<T> newNode = new Node<>(element);

        if (index == 0) {
            newNode.setNext(head);
            head = newNode;

        } else {
            Node<T> current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            } // iterates through the list to the item before desired index

            newNode.setNext(current.getNext()); // then adds in the item
            current.setNext(newNode);
        }

        checkIfSorted();
        size++;

        return true;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
        isSorted = true; // empty list is sorted
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        } // iterates until element found

        return current.getData(); // and returns it
    }

    @Override
    public int indexOf(T element) { // element at index
        if (element == null) {
            return -1;
        }

        Node<T> current = head;
        int index = 0;
        if (isSorted) { // binary search
            while (current != null) {
                int cmp = element.compareTo(current.getData());

                if (cmp == 0) {
                    return index;

                } else if (cmp < 0) {
                    return -1;
                }

                current = current.getNext();
                index++;
            }

        } else { // linear search if not sorted
            while (current != null) {
                if (element.equals(current.getData())) {
                    return index;
                }

                current = current.getNext();
                index++;
            }
        }
        return -1;
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
        if (isSorted) {
            return;
        }

        Node<T> newHead = null;

        Node<T> current = head;

        while (current != null) {
            Node<T> next = current.getNext();

            if (newHead == null || current.getData().compareTo(newHead.getData()) < 0) {
                current.setNext(newHead);
                newHead = current;

            } else {
                Node<T> previous = newHead;

                while (previous.getNext() != null && current.getData().compareTo(previous.getNext().getData()) > 0) {
                    previous = previous.getNext();
                }
                current.setNext(previous.getNext());

                previous.setNext(current);
            }

            current = next;
        }
        head = newHead;

        isSorted = true; // after sorting
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node<T> removedNode;

        if (index == 0) {
            removedNode = head;

            head = head.getNext(); // removal at beginning

        } else {
            Node<T> current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            } // iterate to index right before

            removedNode = current.getNext();

            current.setNext(removedNode.getNext()); // moves pointers
        }
        size--;

        checkIfSorted();

        return removedNode.getData();
    }

    @Override
    public void equalTo(T element) {
        Node<T> current = head;
        Node<T> previous = null;

        if (isSorted) { // best case
            while (head.getData().compareTo(element) < 0) {
                remove(0);
            } // removes stuff at the beginning that don't match

            while (get(size - 1).compareTo(element) > 0) {
                remove(size - 1); // remove stuff at the rear that don't match
            }

        } else { // when not sorted
            while (current != null) { // moves pointers around so the non-matching items aren't pointed to
                if (current.getData().compareTo(element) == 0) {
                    previous = current;

                } else {
                    if (previous == null) {
                        head = current.getNext();

                    } else {
                        previous.setNext(current.getNext());
                    }

                    size--;
                }

                current = current.getNext();
            }
        }

        isSorted = true; // same items so sorted
    }

    @Override
    public void reverse() {
        Node<T> current = head;

        Node<T> previous = null;

        Node<T> next;

        while (current != null) { // swaps two items with each other one at a time
            next = current.getNext();

            current.setNext(previous);

            previous = current;

            current = next;
        }

        head = previous;

        checkIfSorted();
    }

    @Override
    public void merge(List<T> otherList) {
        if (otherList == null) {
            return;
        }

        LinkedList<T> other = (LinkedList<T>) otherList;

        sort();

        other.sort();

        Node<T> current = head;

        Node<T> otherCurrent = other.head;

        Node<T> previous = null;

        while (current != null && otherCurrent != null) {
            if (current.getData().compareTo(otherCurrent.getData()) < 0) {
                previous = current;

                current = current.getNext();

            } else {
                Node<T> temp = otherCurrent.getNext();

                otherCurrent.setNext(current);

                if (previous == null) {
                    this.head = otherCurrent;

                } else {
                    previous.setNext(otherCurrent);
                }

                previous = otherCurrent;

                otherCurrent = temp;

                size++;
            }
        }

        if (otherCurrent != null) {
            if (previous == null) {
                this.head = otherCurrent;
            } else {

                previous.setNext(otherCurrent);
            }

            while (otherCurrent != null) {
                size++;

                otherCurrent = otherCurrent.getNext();
            }
        }
        isSorted = true;
    }

    @Override
    public boolean rotate(int n) {
        if (head == null || n <= 0) {
            return false;
        }

        int length = size;

        n %= length;

        if (n == 0) {
            return false;
        }

        Node<T> current = head;

        for (int i = 0; i < length - n - 1; i++) {
            current = current.getNext();
        }

        Node<T> newHead = current.getNext();

        current.setNext(null);

        current = newHead;

        while (current.getNext() != null) {
            current = current.getNext();
        }

        current.setNext(head);

        head = newHead;

        checkIfSorted();

        return true;
    }

    private void checkIfSorted() { // helper method to check for sortedness, iterates up two items at a time
        if (head == null || head.getNext() == null) {
            isSorted = true;

            return;
        }

        Node<T> current = head;

        while (current != null && current.getNext() != null) {
            if (current.getData().compareTo(current.getNext().getData()) <= 0) {

                current = current.getNext();

            } else {
                isSorted = false;

                return; // leaves loop the moment a pair is out of order...
            }
        }
        isSorted = true; // or if not, then it's sorted
    }

    @Override
    public boolean isSorted() {
        return isSorted;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            out.append(current.getData()).append("\n");
            current = current.getNext();
        }
        return out.toString();
    }
//    public static void main(String[] args) {
//        String[] test = new String[]{"John","Bill","Steve","Yara","Debbie","Liam","Bob", "Jack", "Rudolph","Yara"};
//        String[] test1 = new String[]{"Bob","Laith","Simmonds",null,"Yara","Debbie","Liam"};
//
//        List<String> list = new LinkedList<>();
//
//        List<String> list1 = new LinkedList<>();
//        for (String s : test) {
//            list.add(s);
//        }
//        for (String s1 : test1) {
//            list1.add(s1);
//        }
//        System.out.println("List: \n"+list);
//        System.out.println("List1: \n"+list1);


//        list.rotate(3);
//        System.out.println("List after rotate: "+list);

//        list.reverse();
//        System.out.println("List after reverse: "+list);

//        list.sort();
//        System.out.println("List after sort: "+list);
//
//        System.out.println("isSorted: "+list.isSorted());

//        list.equalTo("Bill");
//        System.out.println("List after equalTo(\"Bill\"): "+list);
//        System.out.println("isSorted: "+list.isSorted());
//        System.out.println("size: "+list.size());

//        System.out.println("indexOf \"Yara\": "+list.indexOf("Yara"));
//        list.sort();
//        System.out.println("indexOf \"Yara\" after sort: "+list.indexOf("Yara"));

//        list.merge(list1);
//
//        System.out.println("List after merge: "+list);
//}
}
