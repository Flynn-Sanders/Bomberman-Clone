public class MyHashMap<K,V>{
    private java.util.HashMap<K,V> map;

    public MyHashMap(){
        map = new java.util.HashMap<>();
    }

    public V put(K key, V value){
        return map.put(key, value);
    }
    
    public V get(Object o){
        return map.get(o);
    }

    public V remove(Object o){
        return map.remove(o);
    }

    public int size() { return map.size(); }
    public MyHashSet<K> keySet() {
        MyHashSet<K> set = new MyHashSet<>();
        for (K key : map.keySet()) {
            set.add(key);
        }
        return set;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (java.util.Map.Entry<K,V> entry : map.entrySet()) {
            text.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return text.toString();
    }

    public String keyString(){
        StringBuilder text = new StringBuilder();
        for (K key : map.keySet()) {
            text.append(key.toString()).append("\n");
        }
        return text.toString();
    }

    public boolean containsKey(K key){ return map.containsKey(key); }
}