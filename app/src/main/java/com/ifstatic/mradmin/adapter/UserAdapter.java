package com.ifstatic.mradmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ItemUsersLayoutBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userlist=new ArrayList<>();
    private Context context;
    private UserItemClickListener userItemClickListener;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void initItemClickListener(UserItemClickListener userItemClickListener) {
        this.userItemClickListener = userItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUsersLayoutBinding itemUsersLayoutBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_users_layout,parent,false);
        return new UserViewHolder(itemUsersLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel userModel =userlist.get(position);
        holder.itemUsersLayoutBinding.setUser(userModel);
        holder.itemUsersLayoutBinding.getRoot().setOnClickListener(v->{
            userItemClickListener.onClickItem(position,userModel);
        });

    }


    @Override
    public int getItemCount() {
        return userlist.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyListItemChanged(List<UserModel> userlist){
        this.userlist = userlist;
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        ItemUsersLayoutBinding itemUsersLayoutBinding;
        public UserViewHolder(@NonNull ItemUsersLayoutBinding itemUsersLayoutBinding) {
            super(itemUsersLayoutBinding.getRoot());
            this.itemUsersLayoutBinding=itemUsersLayoutBinding;
        }
    }

    public interface UserItemClickListener {
        void onClickItem(int position, UserModel model);
    }
}
