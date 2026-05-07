public class MyHashSet<E>{
    private java.util.HashSet<E> set;
    
    public MyHashSet(){
      set = new java.util.HashSet<>();
    }
    public boolean contains(Object o){
      return set.contains(o);
    }
    public void clear(){
      set.clear();
    }
    public boolean add(E e){
      return set.add(e);
    }
    public boolean remove(Object o){
      return set.remove(o);
    }
    public E get(int index){
      int current = 0;
      for (E e : set) {
        if (current == index) {
          return e;
        }
        current++;
      }
      return null;
    }
    public int size(){ return set.size(); }
    
    public DLList<E> toLinkedList(){
      DLList<E> dLList = new DLList<E>();
      for(E e : set){
        dLList.add(e);
      }
      return dLList;
    }
  
    @Override
    public String toString(){
      StringBuilder string = new StringBuilder();
      for(E o : set){
        string.append(o.toString()).append("\n");
      }
      return string.toString();
    }
  } 
      
  
  