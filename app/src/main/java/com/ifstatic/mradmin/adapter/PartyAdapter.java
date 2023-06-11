package com.ifstatic.mradmin.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ItemPartyLayoutBinding;
import com.ifstatic.mradmin.models.PartyModel;

import java.util.ArrayList;
import java.util.List;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.PartyViewHolder> {

    private Context context;
    private List<PartyModel> partyModelList = new ArrayList<>();
    private PartyItemClickListener partyItemClickListener;

    public PartyAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyListIsChanged(List<PartyModel> partyModelList) {
        this.partyModelList = partyModelList;
        notifyDataSetChanged();
    }

    public void initItemClickListener(PartyItemClickListener partyItemClickListener) {
        this.partyItemClickListener = partyItemClickListener;
    }

    @NonNull
    @Override
    public PartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPartyLayoutBinding itemPartyLayoutBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_party_layout,parent,false);
        return new PartyViewHolder(itemPartyLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyViewHolder holder, int position) {
        PartyModel model=partyModelList.get(position);
        holder.itemPartyLayoutBinding.setParty(model);
        holder.itemPartyLayoutBinding.getRoot().setOnClickListener(v->{
            partyItemClickListener.onClickItem(position,model);
        });
    }

    @Override
    public int getItemCount() {
        return partyModelList.size();
    }

    public class PartyViewHolder extends RecyclerView.ViewHolder {

        ItemPartyLayoutBinding itemPartyLayoutBinding;

        public PartyViewHolder(@NonNull ItemPartyLayoutBinding itemPartyLayoutBinding) {
            super(itemPartyLayoutBinding.getRoot());
            this.itemPartyLayoutBinding=itemPartyLayoutBinding;
        }
    }

    public interface PartyItemClickListener {
        void onClickItem(int position, PartyModel model);
    }
}
