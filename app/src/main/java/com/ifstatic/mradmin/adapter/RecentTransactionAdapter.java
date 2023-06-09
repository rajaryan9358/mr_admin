package com.ifstatic.mradmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ItemRecentTransactionLayoutBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.Transactions.TransactionsActivity;
import com.ifstatic.mradmin.view.UserDetails.UserDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionAdapter.RecentTransactionViewHolder> {

    private Context context;
    private List<RecentTransactionModel> transactionMOdellist=new ArrayList<>();
    private TransactionItemClickListner transactionItemClickListner;

    public RecentTransactionAdapter(Context context) {
        this.context=context;
    }
    public void initItemClickListener(TransactionItemClickListner transactionItemClickListner){
        this.transactionItemClickListner=transactionItemClickListner;
    }

    @NonNull
    @Override
    public RecentTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentTransactionLayoutBinding itemRecentTransactionLayoutBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recent_transaction_layout,parent,false);
        return new RecentTransactionViewHolder(itemRecentTransactionLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTransactionViewHolder holder, int position) {
            RecentTransactionModel transactionModel=transactionMOdellist.get(position);
            if (position%2!=0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemRecentTransactionLayoutBinding.holderlayout.setBackgroundColor(context.getColor(R.color.light_color_primary));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemRecentTransactionLayoutBinding.holderlayout.setBackgroundColor(context.getColor(R.color.white));
                }
            }

            holder.itemRecentTransactionLayoutBinding.setTransactionmodel(transactionModel);
            // hide mrno view for userDetail Activity
        if (context instanceof UserDetailActivity){
            holder.itemRecentTransactionLayoutBinding.mrNoTextView.setVisibility(View.GONE);
        }

        holder.itemRecentTransactionLayoutBinding.getRoot().setOnClickListener(v->{
            transactionItemClickListner.onItemClicked(transactionModel,position);
        });
    }

    @Override
    public int getItemCount() {
        return transactionMOdellist.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyListItemChanged(List<RecentTransactionModel> transactionMOdellist){
        this.transactionMOdellist = transactionMOdellist;
        notifyDataSetChanged();
    }

    public static class RecentTransactionViewHolder extends RecyclerView.ViewHolder{
        ItemRecentTransactionLayoutBinding itemRecentTransactionLayoutBinding;
        public RecentTransactionViewHolder(@NonNull ItemRecentTransactionLayoutBinding itemRecentTransactionLayoutBinding) {
            super(itemRecentTransactionLayoutBinding.getRoot());
            this.itemRecentTransactionLayoutBinding=itemRecentTransactionLayoutBinding;
        }
    }

public interface TransactionItemClickListner {
    void onItemClicked(RecentTransactionModel transactionModel,int posittion);
}
}