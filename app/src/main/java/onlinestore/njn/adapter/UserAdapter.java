package onlinestore.njn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.R;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.OrderModel;
import onlinestore.njn.model.UserModel;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<UserModel> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    Context context;
    String layout_style = "0";
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){this.mOnItemClickListener=mItemClickListener;}
    public interface OnItemClickListener{
        void onItemClick(View view, UserModel obj, int pos);
    }

    public UserAdapter(List<UserModel> items, Context context, String layout_style){
        this.items = items;
        this.context = context;
        this.layout_style = layout_style;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView customer_name;
        public TextView customer_address;
        public TextView total_price;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            customer_name = (TextView) v.findViewById(R.id.cust_name);
            customer_address = (TextView) v.findViewById(R.id.cust_address);
            total_price = (TextView) v.findViewById(R.id.total_price);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (layout_style.equals("0")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        }else if(layout_style.equals("1")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        }



        vh = new OriginalViewHolder(v);
        return vh;
    }
    int total = 0;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final UserModel u = items.get(position);
            view.customer_name.setText(u.first_name + " " +u.last_name);
            view.customer_address.setText(u.address);
            view.total_price.setText(u.reg_date);



        if(u.profile_photo.isEmpty()){
            u.profile_photo = "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80";
            Glide.with(context)
                    .load(u.profile_photo)

                    .into(view.image);
        }


            Glide.with(context)
                    .load(u.profile_photo)

                    .into(view.image);


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
