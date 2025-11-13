package frameworks_drivers.firebase;

import entity.User;
import interface_adapter.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Firebase implementation of UserRepository.
 */
public class FirebaseUserRepositoryImpl implements UserRepository {
    private final FirestoreService firestoreService;
    
    public FirebaseUserRepositoryImpl(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }
    
    @Override
    public User getById(String userId) {
        // TODO: Fetch user document from Firestore and map to User entity
        Map<String, Object> data = firestoreService.getDocument("users", userId);
        // Convert to User entity
        return null;
    }
    
    @Override
    public void save(User user) {
        // TODO: Convert User entity to Map and save to Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("createdAt", user.getCreatedAt().toString());
        
        firestoreService.saveDocument("users", user.getId(), data);
    }
    
    @Override
    public void update(User user) {
        // TODO: Update user document in Firestore
        save(user);
    }
    
    @Override
    public void delete(String userId) {
        firestoreService.deleteDocument("users", userId);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        // TODO: Query Firestore for user with email
        return false;
    }
}

