package com.example.lyricsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyricsapp.R;
import com.example.lyricsapp.entities.Album;
import com.example.lyricsapp.entities.SongResult;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private List<SongResult> results;
    private SongClickListener listener;

    public SongAdapter(List<SongResult> results, SongClickListener listener) {
        this.results = results;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_song_result, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongResult result = results.get(position);
        holder.songInAlbumResultTV.setText(result.getTitle());
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

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView songInAlbumResultTV;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songInAlbumResultTV = itemView.findViewById(R.id.songInAlbumResultTV);

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

    public interface SongClickListener{
        void onItemClick(SongResult item, View v);
    }
}
