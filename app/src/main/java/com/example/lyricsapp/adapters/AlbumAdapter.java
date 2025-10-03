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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{

    private List<Album> results;
    private AlbumClickListener listener;

    public AlbumAdapter(List<Album> results, AlbumClickListener listener) {
        this.results = results;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_album_result, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album result = results.get(position);
        holder.albumInArtistTV.setText(result.getTitle());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(List<Album> newResults){
        this.results.clear();
        this.results.addAll(newResults);
        notifyDataSetChanged();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView albumInArtistTV;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumInArtistTV = itemView.findViewById(R.id.albumInArtistTV);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position >= 0){
                Album clickedItem = results.get(position);
                listener.onItemClick(clickedItem, v);
            }
        }
    }

    public interface AlbumClickListener{
        void onItemClick(Album item, View v);
    }
}
