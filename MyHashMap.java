public class MyHashMap<K, V> {
    private Object[] keys;
    private Object[] values;
    private int size;
    private MyHashSet<K> keySet;
    private int capacity = 100;

    private static final Object DELETED = new Object();

    public MyHashMap() {
        size = 0;
        keys = new Object[capacity];
        values = new Object[capacity];
        keySet = new MyHashSet<>();
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        int index = hash(key);
        
        while (keys[index] != null && !keys[index].equals(key)) {
            index = (index + 1) % capacity;
        }

        if (keys[index] == null) {
            size++;
            keySet.add(key);
        }

        V old = (V) values[index];
        keys[index] = key;
        values[index] = value;
        return old;
    }

    public V remove(Object key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                V toReturn = (V) values[index];
                keys[index] = DELETED; 
                values[index] = null;
                keySet.remove((K) key);
                size--;
                return toReturn;
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    public V get(Object key) {
        int index = hash(key);
        int start = index;
        while (keys[index] != null) {
            if (keys[index] != DELETED && keys[index].equals(key)) 
                return (V) values[index];
            index = (index + 1) % capacity;
            if (index == start) break; 
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public int size() { return size; }
    public MyHashSet<K> keySet() { return keySet; }
}