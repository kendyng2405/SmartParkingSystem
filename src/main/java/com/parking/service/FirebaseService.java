package com.parking.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory fallback storage when Firebase is not available
    private final Map<String, Map<String, Map<String, Object>>> inMemoryStore = new HashMap<>();

    private boolean isFirestoreAvailable() {
        return firestore != null;
    }

    public String save(String collection, Map<String, Object> data) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            DocumentReference docRef = firestore.collection(collection).document();
            docRef.set(data).get();
            return docRef.getId();
        } else {
            String id = UUID.randomUUID().toString();
            inMemoryStore.computeIfAbsent(collection, k -> new LinkedHashMap<>()).put(id, new HashMap<>(data));
            return id;
        }
    }

    public String saveWithId(String collection, String id, Map<String, Object> data) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            firestore.collection(collection).document(id).set(data).get();
            return id;
        } else {
            inMemoryStore.computeIfAbsent(collection, k -> new LinkedHashMap<>()).put(id, new HashMap<>(data));
            return id;
        }
    }

    public Map<String, Object> getById(String collection, String id) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            DocumentSnapshot doc = firestore.collection(collection).document(id).get().get();
            if (doc.exists()) {
                Map<String, Object> data = new HashMap<>(doc.getData());
                data.put("id", doc.getId());
                return data;
            }
            return null;
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            if (col != null && col.containsKey(id)) {
                Map<String, Object> data = new HashMap<>(col.get(id));
                data.put("id", id);
                return data;
            }
            return null;
        }
    }

    public List<Map<String, Object>> getAll(String collection) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> results = new ArrayList<>();
        if (isFirestoreAvailable()) {
            ApiFuture<QuerySnapshot> future = firestore.collection(collection).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Map<String, Object> data = new HashMap<>(doc.getData());
                data.put("id", doc.getId());
                results.add(data);
            }
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            if (col != null) {
                for (Map.Entry<String, Map<String, Object>> entry : col.entrySet()) {
                    Map<String, Object> data = new HashMap<>(entry.getValue());
                    data.put("id", entry.getKey());
                    results.add(data);
                }
            }
        }
        return results;
    }

    public List<Map<String, Object>> getByField(String collection, String field, Object value) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> results = new ArrayList<>();
        if (isFirestoreAvailable()) {
            ApiFuture<QuerySnapshot> future = firestore.collection(collection).whereEqualTo(field, value).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Map<String, Object> data = new HashMap<>(doc.getData());
                data.put("id", doc.getId());
                results.add(data);
            }
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            if (col != null) {
                for (Map.Entry<String, Map<String, Object>> entry : col.entrySet()) {
                    if (value.equals(entry.getValue().get(field))) {
                        Map<String, Object> data = new HashMap<>(entry.getValue());
                        data.put("id", entry.getKey());
                        results.add(data);
                    }
                }
            }
        }
        return results;
    }

    public void update(String collection, String id, Map<String, Object> data) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            firestore.collection(collection).document(id).update(data).get();
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            if (col != null && col.containsKey(id)) {
                col.get(id).putAll(data);
            }
        }
    }

    public void delete(String collection, String id) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            firestore.collection(collection).document(id).delete().get();
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            if (col != null) {
                col.remove(id);
            }
        }
    }

    public long count(String collection) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            return firestore.collection(collection).get().get().size();
        } else {
            Map<String, Map<String, Object>> col = inMemoryStore.get(collection);
            return col != null ? col.size() : 0;
        }
    }

    public long countByField(String collection, String field, Object value) throws ExecutionException, InterruptedException {
        if (isFirestoreAvailable()) {
            return firestore.collection(collection).whereEqualTo(field, value).get().get().size();
        } else {
            return getByField(collection, field, value).size();
        }
    }

    // Initialize default data
    public void initializeDefaultData() {
        try {
            // Create default roles if not exist
            List<Map<String, Object>> roles = getAll("roles");
            if (roles.isEmpty()) {
                Map<String, Object> admin = new HashMap<>();
                admin.put("roleName", "ADMIN");
                admin.put("description", "System Administrator");
                save("roles", admin);

                Map<String, Object> manager = new HashMap<>();
                manager.put("roleName", "MANAGER");
                manager.put("description", "Parking Manager");
                save("roles", manager);

                Map<String, Object> staff = new HashMap<>();
                staff.put("roleName", "STAFF");
                staff.put("description", "Parking Staff");
                save("roles", staff);

                Map<String, Object> driver = new HashMap<>();
                driver.put("roleName", "DRIVER");
                driver.put("description", "Driver / Parking User");
                save("roles", driver);
            }

            // Create default vehicle types
            List<Map<String, Object>> vTypes = getAll("vehicleTypes");
            if (vTypes.isEmpty()) {
                String[][] types = {
                    {"Motorcycle", "Two-wheeled motor vehicle"},
                    {"Car", "Four-wheeled passenger vehicle"},
                    {"SUV/Truck", "Sport utility vehicle or truck"},
                    {"Electric Vehicle", "Electric powered vehicle with charging needs"}
                };
                for (String[] type : types) {
                    Map<String, Object> vt = new HashMap<>();
                    vt.put("typeName", type[0]);
                    vt.put("description", type[1]);
                    save("vehicleTypes", vt);
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
        }
    }
}
