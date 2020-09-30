package com.example.coinhunt.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coinhunt.R;
import com.example.coinhunt.models.User;

import java.util.List;


public class MyUserRankingFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRankingFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRankingFragmentRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position+1;
       holder.position.setText(pos+ "-");
       holder.coin.setText(String.valueOf(mValues.get(position).getDuck()));
       holder.nick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView position;
        public final TextView coin;
        public final TextView nick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            position = (TextView) view.findViewById(R.id.textViewPosition);
            nick = (TextView) view.findViewById(R.id.textViewNick);
            coin= (TextView) view.findViewById(R.id.textViewCoin);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + coin.getText() + "'";
        }
    }
}