public class MyHashSet<E>{
    private Object[] hashArray;
    private int size;
    
    public MyHashSet(){
      size = 0;
      hashArray = new Object[676];
    }
    public boolean contains(Object o){
      int location = o.hashCode();
      if(hashArray[location] != null && o.equals(hashArray[location])){
        return true;
      }
      return false;
    }
    public void clear(){
      size = 0;
      hashArray = new Object[1000];
    }
    public boolean add(E e){
      if(contains(e)) return false;
      hashArray[e.hashCode()] = e;
      size++; return true;
    }
    public boolean remove(Object o){
      if(!contains(o)) return false;
        hashArray[o.hashCode()] = null;
      size--; return true;
    }
    @SuppressWarnings("unchecked")
    public E get(int index){
      return (E)hashArray[index];
    }
    public int size(){ return size; }
    
    
    @SuppressWarnings("unchecked")
    public DLList<E> toLinkedList(){
      DLList<E> dLList = new DLList<E>();
      for(Object o : hashArray){
        if(o!=null){
          dLList.add((E)o);
        }
      }
      return dLList;
    }
  
    @Override
    public String toString(){
      String string = "";
      for(Object o : hashArray){
        if(o!=null){;
          string = string + o.toString() + "\n";
        }
      }
      return string;
    }
  } 
      
  
  