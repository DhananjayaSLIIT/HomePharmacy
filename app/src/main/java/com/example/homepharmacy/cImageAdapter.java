package com.example.homepharmacy;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class cImageAdapter extends RecyclerView.Adapter<cImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<cUpload> mUploads;
    private onItemClickListener mListener;

    public cImageAdapter(Context context, List<cUpload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.medic_items,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder,int position) {
        cUpload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText("Product Name: "+uploadCurrent.getName());
        holder.textViewPrice.setText(String.valueOf("Price : Rs "+uploadCurrent.getmPrice()));
        holder.textViewQuantity.setText(String.valueOf("Quantity : "+uploadCurrent.getmQuantity()));
        Picasso.get().load(uploadCurrent.getImageUrl())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewPrice;
        public TextView textViewQuantity;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice =  itemView.findViewById(R.id.text_view_price);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            imageView = itemView.findViewById(R.id.image_view_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem update = contextMenu.add(Menu.NONE,1,1,"Update");
            MenuItem delete = contextMenu.add(Menu.NONE,2,2,"Delete");


            update.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onUpdateClick(position);
                            return true;
                            //break;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface onItemClickListener{
        void onItemClick(int position);

        void onUpdateClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }
}
