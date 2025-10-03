package com.example.lyricsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyricsapp.R;
import com.example.lyricsapp.entities.SongResult;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private List<SongResult> results;
    private HistoryClickListener listener;

    public HistoryAdapter(List<SongResult> results, HistoryClickListener listener) {
        this.results = results;
        this.listener = listener;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_result, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        SongResult result = results.get(position);
        holder.historySongTitleTV.setText(result.getTitle());
        holder.historySongAlbumTV.setText(result.getAlbum());
        holder.historySongArtistTV.setText(result.getArtist());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(List<SongResult> newResults){
        this.results.clear();
        this.results.addAll(newResults);
        notifyDataSetChanged();
    }


    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView historySongTitleTV;
        TextView historySongAlbumTV;
        TextView historySongArtistTV;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historySongTitleTV = itemView.findViewById(R.id.historySongTitleTV);
            historySongAlbumTV = itemView.findViewById(R.id.historySongAlbumTV);
            historySongArtistTV = itemView.findViewById(R.id.historySongArtistTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position >= 0){
                SongResult clickedItem = results.get(position);
                listener.onItemClick(clickedItem, v);
            }
        }
    }

    public interface HistoryClickListener{
        void onItemClick(SongResult item, View v);
    }
}
