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
        int firstAvailableIndex = -1;
        int start = index;

        while (keys[index] != null) {
            if (keys[index] == DELETED) {
                // remember if key isnt found
                if (firstAvailableIndex == -1) firstAvailableIndex = index;
            } else if (keys[index].equals(key)) {
                // update existing key and return new
                V old = (V) values[index];
                values[index] = value;
                return old;
            }

            index = (index + 1) % capacity;
            
            // prevents infinite loop if the map is 100% full (no nulls)
            if (index == start) break;
        }

        // use first deleted, if non use first null
        int insertionIndex = (firstAvailableIndex != -1) ? firstAvailableIndex : index;

        //  is map is truly full? (no nulls + no deleteds)
        if (keys[insertionIndex] != null && keys[insertionIndex] != DELETED) {
            resize();
            return put(key, value); // try again after resizing
        }

        size++;
        keySet.add(key);
        keys[insertionIndex] = key;
        values[insertionIndex] = value;
        return null;
    }

    public void resize(){
        int newCapacity = capacity * 2;
        Object[] oldKeys = keys;
        Object[] oldValues = values;

        keys = new Object[newCapacity];
        values = new Object[newCapacity];
        capacity = newCapacity;
        size = 0;
        keySet = new MyHashSet<>();

        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null && oldKeys[i] != DELETED) {
                put((K) oldKeys[i], (V) oldValues[i]);
            }
        }
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