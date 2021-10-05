package eterna.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eterna.R;
import eterna.data.schema.QuotesTable;
import eterna.data.schema.VideosTable;
import eterna.ui.activity.DemoMain;
import eterna.ui.adpter.QuoteAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FragmentQuotes extends Fragment {

    private SwipeRefreshLayout srl;
    private String Category;
    private AlertDialog.Builder catBuilder;
    private Dialog catDialog;
    private Button addCat;
    private QuotesTable QouRow;
    private TextInputLayout CatLayout;
    private RecyclerView rcvQt;
    private ArrayList<QuotesTable> qtList;
    private QuoteAdapter qta;
    private DatabaseReference dbref,getDbref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_quote, container, false);
        srl = contentView.findViewById(R.id.swipeRefreshLt);
        rcvQt = contentView.findViewById(R.id.recycleQt);
        FloatingActionButton uploadQt = contentView.findViewById(R.id.uploadQt);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        qtList = new ArrayList<>();
        dbref = FirebaseDatabase.getInstance().getReference().child("Songs").child(DemoMain.Emotion);
        getDbref = FirebaseDatabase.getInstance().getReference().child("Quotes").child(DemoMain.Emotion);

        rcvQt.setLayoutManager(llm);
        srl.setOnRefreshListener(() -> {
            srl.setColorSchemeColors(Color.BLACK, Color.rgb(171, 77, 77), Color.rgb(205,13,93));
            getQuotes();
        });
        uploadQt.setVisibility(View.GONE);
        uploadQt.setOnClickListener(v -> uploadQote());
        getQuotes();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (qtList!=null){
            Collections.shuffle(qtList);
        }
    }

    private void uploadQote() {
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    catBuilder = new AlertDialog.Builder(requireContext());
                    catBuilder.setView(R.layout.dialog_add_new);
                    catBuilder.setTitle("Add New"+ DemoMain.Emotion+" Song");
                    catDialog = catBuilder.create();
                    catDialog.show();
                    final String key = dbref.push().getKey();
                    CatLayout = catDialog.findViewById(R.id.cat_et);
                    addCat = catDialog.findViewById(R.id.add_category);
                    addCat.setOnClickListener(v -> {
                        Category = Objects.requireNonNull(CatLayout.getEditText()).getText().toString().trim();
                        if (Category.isEmpty()){
                            CatLayout.setError("This Field Can't Be Empty");
                        }else {
                            VideosTable st = new VideosTable("0",Category,"");
                            if (key != null) {
                                dbref.child(key).setValue(st);
                                catDialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), "Can not add song", Toast.LENGTH_SHORT).show();
                            }
                            catDialog.hide();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void getQuotes() {
        System.out.println("getting Quotes");
        getDbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Got : "+ DemoMain.Emotion+" results");
                qtList.clear();
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    QouRow = postSnapshot.getValue(QuotesTable.class);
                    qtList.add(QouRow);
                }
                Collections.shuffle(qtList);
                qta = new QuoteAdapter(qtList,getContext());
                rcvQt.setAdapter(qta);
                srl.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
