package com.example.kuba10.mypokemonplaces.ListFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuba10.mypokemonplaces.Adapters.ListAdapter;
import com.example.kuba10.mypokemonplaces.Adapters.OnStartDragListener;
import com.example.kuba10.mypokemonplaces.Adapters.SimpleItemTouchHelperCallback;
import com.example.kuba10.mypokemonplaces.Model.PokePlace;
import com.example.kuba10.mypokemonplaces.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements OnStartDragListener {

    ArrayList<PokePlace> placeList;
    private ItemTouchHelper mItemTouchHelper;


    @BindView(R.id.listRecycler)
    RecyclerView recyclerView;


    public static ListFragment newInstance(ArrayList<PokePlace> placeList) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", placeList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             placeList = getArguments().getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setHasFixedSize(true);
       ListAdapter adapter =  new ListAdapter(placeList);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);



//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);





        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    public interface ItemTouchHelperAdapter {

        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }


}
