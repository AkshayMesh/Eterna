package eterna.data.schema;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserTable {
    public String id;
    public String name;
    public String email;
    public String dpUrl;
    public String dateLogin;
    public String status;
    public String loginType;
    public String dateCreated;
    public float rating;

    public UserTable() {
    }

    public UserTable(String id, String name, String email, String dpUrl, String dateLogin
            , String status, String loginType, String dateCreated) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dpUrl = dpUrl;
        this.dateLogin = dateLogin;
        this.status = status;
        this.loginType = loginType;
        this.dateCreated = dateCreated;
    }

    public UserTable(String name, String email, String dpUrl, String dateLogin, String status
            , String loginType) {
        this.name = name;
        this.email = email;
        this.dpUrl = dpUrl;
        this.dateLogin = dateLogin;
        this.status = status;
        this.loginType = loginType;
    }

    public static DatabaseReference getReference(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        return dbRef.child("Users");
    }
}
