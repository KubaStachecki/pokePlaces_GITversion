package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}