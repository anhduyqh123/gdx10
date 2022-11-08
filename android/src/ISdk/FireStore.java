package ISdk;

import GameGDX.GDX;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class FireStore {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FireStore()
    {
        Update();
        Read();
    }
    private void Update()
    {
        db.collection("users").document("JhvgwRH8OmPWciepZ6ZD")
                .update(
                        "born", 1999
                );
    }
    private void Read()
    {
        db.collection("users").get().addOnCompleteListener(task->{
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot i : task.getResult())
                    GDX.Log(i.getId()+" =>"+i.getData());
            }
            else {
                GDX.Log("Error getting documents. "+task.getException());
            }
        });
    }
    private void Add()
    {
        GDX.Log("add user");
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> GDX.Log("DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> GDX.Log("Error adding document "+e.getLocalizedMessage()));
    }
}
