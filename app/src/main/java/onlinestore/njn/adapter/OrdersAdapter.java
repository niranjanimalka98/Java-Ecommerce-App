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

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.R;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.OrderModel;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<OrderModel> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    Context context;
    String layout_style = "0";
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){this.mOnItemClickListener=mItemClickListener;}
    public interface OnItemClickListener{
        void onItemClick(View view, OrderModel obj, int pos);
    }

    public OrdersAdapter(List<OrderModel> items, Context context, String layout_style){
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        }else if(layout_style.equals("1")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        }



        vh = new OriginalViewHolder(v);
        return vh;
    }
    int total = 0;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final OrderModel o = items.get(position);
            view.customer_name.setText(o.customer.first_name + " " +o.customer.last_name);
            view.customer_address.setText(o.customer.address);


            total = 0;
            for(CartModel c:o.cart){
                total+= c.product_price;
            }

            view.total_price.setText("Rs: " + total);



//            Glide.with(context)
//                    .load(o.customer.profile_photo)
//
//                    .into(view.image);


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
