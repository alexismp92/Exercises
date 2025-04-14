package src.exercise.stack;


/*
    * This is a simple stack (Fist In Last Out) created with java.
*/

interface Stack {
    void push(int element);
    void pop();
    int peek();
    boolean isEmpty();
}

class StackImp implements Stack {

    private final int[] stack;
    private int position;

    public StackImp(int size) {
        stack = new int[size];
    }

    @Override
    public void push(int element) {
        if (position == stack.length) {
            System.out.println("Stack is full");
        } else {
            stack[position] = element;
            System.out.println("Pushed element: " + stack[position]);
            position++;
        }
    }

    @Override
    public void pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
        } else {
            System.out.println("Popped element: " + stack[position - 1]);
            position--;
        }
    }

    @Override
    public int peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return -1;
        } else {
            System.out.println("Peeked element: " + stack[position - 1]);
            return stack[position - 1];
        }
    }

    @Override
    public boolean isEmpty() {
        boolean isEmpty = true;
        if (position == 0) {
            System.out.println("Stack is empty");
        }else{
            isEmpty = false;
        }
        return isEmpty;
    }
}


public class StackMain {
    public static void main(String[] args) {
        Stack stack = new StackImp(5);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        stack.peek();
        stack.peek();
        stack.isEmpty();
    }
}