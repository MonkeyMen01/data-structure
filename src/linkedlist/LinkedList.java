package linkedlist;

public class LinkedList {
    Node head;
    Node tail;
    int length;

    public LinkedList(int value) {
        Node node = new Node(value);
        this.head = node;
        this.tail = node;
        length = 1;
    }

    class Node{
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    void append(int value){
        Node node = new Node(value);
        this.tail.value=value;

    };

}
