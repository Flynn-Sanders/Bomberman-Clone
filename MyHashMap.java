
public class MyHashMap<K, V> {

    //internal node class to store pairs
    private static class Pair<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private DLList<Pair<K, V>>[] table;
    private int size;
    private int capacity = 100;
    private MyHashSet<K> keySet;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new DLList[capacity];
        this.size = 0;
        this.keySet = new MyHashSet<>();
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public int size() {
        return size;
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public V put(K key, V value) {
        int index = hash(key);
        
        if (table[index] == null) {
            table[index] = new DLList<>();
        }

        for (Pair<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                V old = entry.value;
                entry.value = value;
                return old;
            }
        }

        table[index].add(new Pair<>(key, value));
        keySet.add(key);
        size++;

        return null;
    }

    public V get(Object key) {
        int index = hash(key);
        DLList<Pair<K, V>> bucket = table[index];

        if (bucket == null) return null;

        for (Pair<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public V remove(Object key) {
        int index = hash(key);
        DLList<Pair<K, V>> bucket = table[index];

        if (bucket == null) return null;

        for (Pair<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V toReturn = entry.value;
                bucket.remove(entry);
                keySet.remove(key);
                size--;
                return toReturn;
            }
        }
        return null;
    }
}