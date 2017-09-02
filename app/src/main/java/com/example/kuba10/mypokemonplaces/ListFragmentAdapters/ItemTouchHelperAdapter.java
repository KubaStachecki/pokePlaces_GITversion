package com.example.kuba10.mypokemonplaces.ListFragmentAdapters;

 interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void clearView();

//    void onDrawOver();
}