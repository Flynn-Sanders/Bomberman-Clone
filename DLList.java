import java.util.Iterator;

public class DLList<E> implements Iterable<E>{
    private Node<E> head;
    private Node<E> tail;
    private int size;
    
    public DLList(){
      head = new Node<E>(null);
      tail = new Node<E>(null);
      head.setNext(tail);
      tail.setPrev(head);
      size = 0;
    }
    
      public Node<E> getNode(int index){
      Node<E> current;
      int count;
      
      if(index < size/2){
        current = head.next();
        count = 0;
          while(current != tail){
          if(count == index){
            return current;
          }
            current = current.next();
          count++;
          }
      }
      else{
        current = tail.prev();
        count = size - 1;
        while(current != head){
          if(count == index){
            return current;
          }
          current = current.prev();
          count--;
        }
      }
      
      return null;
    }
    public E get(int index){
      return getNode(index).get();
    }
    
    public boolean add(E data){
      Node<E> newNode = new Node<E>(data);
      Node<E> before = tail.prev();
      
      before.setNext(newNode);
      newNode.setNext(tail);
      tail.setPrev(newNode);
      newNode.setPrev(before);
      
      size++;
      return true;
    }
    public void add(int index, E data){
      Node<E> newNode = new Node<E>(data);
      Node<E> before;
      Node<E> after;
      if(index == 0){
        after = head.next();
        head.setNext(newNode);
        newNode.setNext(after);
        newNode.setPrev(head);
        after.setPrev(newNode);  
        size++;
      }
      else{
        before = getNode(index).prev();
        after = getNode(index);
        
        before.setNext(newNode);
        newNode.setNext(after);
        newNode.setPrev(before);
        after.setPrev(newNode);
        size++;
      }
    }
  
    public E remove(int index) {
      if(size != 0){
        Node<E> current = getNode(index);
        Node<E> before = current.prev();
        Node<E> after = current.next();
        
        before.setNext(after);
        after.setPrev(before);
        size--;
          return current.get();
      }
      return null;
      
    }
  
    public boolean remove(Object o){
      Node<E> current = head.next();
      while(current != tail){
        if(current.get().equals(o)){
          current.prev().setNext(current.next());
          current.next().setPrev(current.prev());
          size--;
          return true;
        }
        else{
          current = current.next();
        }
      }
      return false;
    }
    
    public E set(int index, E data){
      E replaced = getNode(index).get();
      getNode(index).setData(data);
      return replaced;
    }
    
    public int size() {
      return size;
    }
  
    public boolean contains(Object o){
      for(int i = 0; i < size; i++){
        if(get(i).equals(o)){
          return true;
        }
      }
      return false;
    }
  
    public void clear(){
      while(size > 0){
        remove(size - 1);
      }
    }
  
    @Override
    public String toString(){
      String temp = "";
      for(int i = 0; i < size; i++){
        temp = temp + "\n" + (i + 1) + ". " + get(i);
      }
      return temp;
    }

    @Override
public Iterator<E> iterator() {
    return new Iterator<E>() {

        private Node<E> current = head.next();

        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public E next() {
            E data = current.get();
            current = current.next();
            return data;
        }
    };
}
  }
  