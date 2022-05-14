package onlinestore.njn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.activity.CheckOutActivity;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.OrderModel;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ItemModel> items = new ArrayList<>();
    private List<CartModel> cart = CartModel.listAll(CartModel.class);

    private OnItemClickListener mOnItemClickListener;
    Context context;
    String layout_style = "0";
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){this.mOnItemClickListener=mItemClickListener;}

    public interface OnItemClickListener{
        void onItemClick(View view, ItemModel obj, int pos);
    }



    public ProductAdapter(List<ItemModel> items, Context context, String layout_style){
        this.items = items;
        this.context = context;
        this.layout_style = layout_style;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView delete;
        public TextView title;
        public TextView price;
        public TextView pd_qty;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            delete = (ImageView) v.findViewById(R.id.delete);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            pd_qty = (TextView) v.findViewById(R.id.pd_qty);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (layout_style.equals("0")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
        }else if(layout_style.equals("1")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_1, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_1, parent, false);
        }



        vh = new OriginalViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final ItemModel p = items.get(position);


            view.title.setText(p.product_name + "");
            view.price.setText("Rs "+p.price + "");
            view.pd_qty.setText("Qty: "+p.quantity + "");
            view.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartModel cm = new CartModel();
                    CartModel.deleteAll(CartModel.class, "PRODUCTID = ?", p.product_id);
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                }
            });


            Glide.with(context)
                    .load(p.photo)

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
