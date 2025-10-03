package com.example.lyricsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyricsapp.R;
import com.example.lyricsapp.entities.FavoriteResult;
import com.example.lyricsapp.entities.UserData;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoriteResult> favorites = new ArrayList<>();
    private OnFavoriteInteractionListener listener;
    private UserData userData;

    public FavoriteAdapter(List<FavoriteResult> favorites, OnFavoriteInteractionListener listener, UserData userData) {
        this.favorites = favorites;
        this.listener = listener;
        this.userData = userData;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_result, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteResult favoriteResult = favorites.get(position);
        holder.favSongTitleTV.setText(favoriteResult.getSongTitle());
        holder.favSongAlbumTV.setText(favoriteResult.getAlbumName());
        holder.favSongArtistTV.setText(favoriteResult.getArtistName());

        holder.favoriteItemCard.setOnClickListener(v -> {
            if(listener != null){
                listener.onSongResultClick(favoriteResult);
            }
        });

        holder.removeFavBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveFavClick(favoriteResult, userData, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updateResutls(List<FavoriteResult> newFavorites){
        this.favorites.clear();
        this.favorites.addAll(newFavorites);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        favorites.remove(position);
        notifyItemRemoved(position);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{

        MaterialCardView favoriteItemCard;
        TextView favSongTitleTV;
        TextView favSongAlbumTV;
        TextView favSongArtistTV;
        ImageView removeFavBtn;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteItemCard = itemView.findViewById(R.id.favoriteItemCard);
            favSongTitleTV = itemView.findViewById(R.id.favSongTitleTV);
            favSongAlbumTV = itemView.findViewById(R.id.favSongAlbumTV);
            favSongArtistTV = itemView.findViewById(R.id.favSongArtistTV);
            removeFavBtn = itemView.findViewById(R.id.removeFavBtn);
        }

    }

    public interface OnFavoriteInteractionListener{
        void onSongResultClick(FavoriteResult favoriteResult);
        void onRemoveFavClick(FavoriteResult favoriteResult, UserData userData, int position);
    }
}
