package eterna.data.schema;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideosTable {

    public int id;
    public String likes;
    public String name;
    public String link;
    public String description;
    public String title;

    public VideosTable() {

    }

    public VideosTable(String likes, String name, String link) {
        this.id = id;
        this.likes = likes;
        this.name = name;
        this.link = link;
    }

    public static DatabaseReference geReference(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        return dbRef.child("Videos");
    }

}
