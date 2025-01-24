package agus.prasetyo.backend.system.utils;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static DataManager instance;

    private final Map<String, Object> dataStore;

    private DataManager() {
        this.dataStore = new HashMap<>();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void set(String key, Object value) {
        dataStore.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = dataStore.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Key '" + key + "' not found");
        }
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }
        return (T) value;
    }

    public void remove(String key) {
        dataStore.remove(key);
    }

    public boolean contains(String key) {
        return dataStore.containsKey(key);
    }

    public void clear() {
        dataStore.clear();
    }
}


