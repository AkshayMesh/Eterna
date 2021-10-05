package eterna.ui.adpter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import eterna.R;
import eterna.data.schema.QuotesTable;

import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteHolder> {

    private ArrayList<QuotesTable> Qtlist;
    private Context mContext;

    public QuoteAdapter(ArrayList<QuotesTable> qtlist, Context mContext) {
        Qtlist = qtlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public QuoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(mContext).inflate(R.layout.card_quote,parent,false);
        return new QuoteHolder(child);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteHolder holder, int position) {
        holder.setQuoteData(Qtlist.get(position));
    }

    @Override
    public int getItemCount() {
        return Qtlist.size();
    }

    public class QuoteHolder extends RecyclerView.ViewHolder {
        private TextView qtText, copy, share;
        public QuoteHolder(@NonNull View itemView) {
            super(itemView);
            qtText = itemView.findViewById(R.id.qt_txt);
            copy = itemView.findViewById(R.id.copy);
            share = itemView.findViewById(R.id.share);
        }

        private void setQuoteData(QuotesTable qTable){
            qtText.setText(qTable.quoteText);
            copy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Quote", qTable.quoteText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
            });
            share.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, qTable.quoteText);
                Intent shareIntent = Intent.createChooser(sendIntent, "Share Quote");
                mContext.startActivity(shareIntent);
            });
        }
    }
}
