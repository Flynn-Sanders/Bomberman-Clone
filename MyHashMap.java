public class MyHashMap<K,V>{
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap(){
        size=0;
        hashArray = new Object[676];
        keySet = new MyHashSet<>();
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value){
        if (!keySet.contains(key)) size++;
        V toReturn = (V)hashArray[key.hashCode()];
        hashArray[key.hashCode()] = value;
        keySet.add(key);
        
        return toReturn;
        
    }
    
    @SuppressWarnings("unchecked")
    public V get(Object o){
        return (V) hashArray[o.hashCode()];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object o){
        V toReturn = (V)hashArray[o.hashCode()];
        hashArray[o.hashCode()] = null;
        keySet.remove(o);
        size--;
        return toReturn;
    }

    public int size() { return size; }
    public MyHashSet<K> keySet() { return keySet; }

    public String toString() {
        String text = "";
        DLList<K> list = keySet.toLinkedList();
        for (int i = 0; i < list.size(); i++) {
            K key = list.get(i); 
            int index = key.hashCode();
            text += key + " : " + hashArray[index] + "\n";
        }   
        return text;
    }
    public String keyString(){
        String text = "";
        DLList<K> list = keySet.toLinkedList();
        for (int i = 0; i < list.size(); i++) {
            K key = list.get(i); 
            text += key.toString()+"\n";
        }   
        return text;
    }
}