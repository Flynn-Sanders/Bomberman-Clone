import java.util.ArrayList;

public class MinHeap<E extends Comparable<E>>{
    private ArrayList<E> heap;

    public MinHeap(){
        heap = new ArrayList<>();
    }
    public void add(E data){
        
        int index = heap.size();
        heap.add(data);
        while(index>0){
            int parent = (index-1) / 2;
            E p = heap.get(parent);
            E c = heap.get(index);
            if(p.compareTo(c) > 0){
                heap.set(parent, c);
                heap.set(index, p);
                index = parent;
            }
            else break;
        }
    }
    public E poll(){
        E last = heap.remove(heap.size()-1);
        if(heap.size()==0) return last;
        E toReturn = heap.set(0, last);
        
        int parent = 0;
        while(parent<heap.size()){
            int left = parent*2+1; int right = parent*2+2;
            int swap = -1; int size = heap.size();

            if(right<size){
                if(heap.get(left).compareTo(heap.get(right))<0) swap = left;
                else swap = right;
            }
            else if (left<size && right>=size) swap = left;

            if(swap!=-1 && heap.get(swap).compareTo(heap.get(parent))<0){
                E temp = heap.get(swap);
                heap.set(swap, heap.get(parent));
                heap.set(parent, temp);
                parent = swap;
            }
            else break;
        }
        return toReturn;
    }
    public E peek(){
        if(heap.isEmpty()) return null;
        return heap.get( 0);
    }
    public boolean isEmpty(){ return heap.isEmpty(); }
} 