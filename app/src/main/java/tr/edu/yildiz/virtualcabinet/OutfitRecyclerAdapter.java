package tr.edu.yildiz.virtualcabinet;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OutfitRecyclerAdapter extends RecyclerView.Adapter<OutfitRecyclerAdapter.MyViewHolder> {

    private List<Outfit> outfitList = new ArrayList<Outfit>();
    private OnDeleteClickListener deleteClickListener;
    private OnEditClickListener editClickListener;
    private final boolean selectionMode;
    private Menu menu;
    Outfit selectedOutfit;
    MyViewHolder selectedHolder;

    public OutfitRecyclerAdapter(boolean selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.outfit_item, viewGroup, false);

        return new MyViewHolder(itemView, deleteClickListener, editClickListener);
    }

    @Override
    public int getItemCount() {
        return outfitList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView priceTextView, dateTextView, catTextView, colorTextView, patTextView;
        CardView cardView;
        ImageView imageView;
        Button editBtn, deleteBtn;
        RelativeLayout cardLayout;

        int position;
        public MyViewHolder(@NonNull View view, OnDeleteClickListener deleteListener, OnEditClickListener editListener){
            super(view);

            priceTextView = (TextView) view.findViewById(R.id.price_textView);
            dateTextView = (TextView) view.findViewById(R.id.buy_date_textView);
            catTextView = (TextView) view.findViewById(R.id.category_textView);
            colorTextView = (TextView) view.findViewById(R.id.color_textView);
            patTextView = (TextView) view.findViewById(R.id.pattern_textView);
            imageView = (ImageView) view.findViewById(R.id.outfit_item_pic);
            editBtn = (Button) view.findViewById(R.id.edit_outfit_button);
            deleteBtn = (Button) view.findViewById(R.id.delete_outfit_button);
            cardView = (CardView) view.findViewById(R.id.outfit_cardView);
            cardLayout = (RelativeLayout) view.findViewById(R.id.outfit_relativeLayout);

            if(selectionMode){
                editBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
            }
            else {
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editListener.onEditClick(position);
                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteListener.onDeleteClick(position);
                    }
                });
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.position = position;
        Outfit outfit = outfitList.get(position);
        holder.priceTextView.setText(String.valueOf(outfit.getPrice()));
        holder.catTextView.setText(outfit.getCategory());
        holder.colorTextView.setText(outfit.getColor());
        holder.patTextView.setText(outfit.getPattern());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String date = format.format(outfit.getBuyDate().getTime());
        holder.dateTextView.setText(date);

        holder.imageView.setImageURI(Uri.parse(outfit.getPhoto()));

        if(selectionMode){
            holder.cardView.setClickable(true);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuItem done = menu.findItem(R.id.action_selection_done);
                    if(selectedOutfit == null){
                        done.setVisible(true);
                        holder.cardLayout
                                .setBackgroundColor(ResourcesCompat
                                        .getColor(v.getResources(), R.color.selected, null));
                        selectedOutfit = outfitList.get(position);
                        selectedHolder = holder;
                    }
                    else if(selectedOutfit != null && selectedOutfit.getId() == outfitList.get(position).getId()){
                        done.setVisible(false);
                        selectedOutfit = null;
                        holder.cardLayout.setBackgroundColor(Color.TRANSPARENT);
                        selectedHolder = null;
                    }
                    else{
                        done.setVisible(true);
                        selectedOutfit = outfitList.get(position);
                        holder.cardLayout
                                .setBackgroundColor(ResourcesCompat
                                        .getColor(v.getResources(), R.color.selected, null));
                        selectedHolder.cardLayout.setBackgroundColor(Color.TRANSPARENT);
                        selectedHolder = holder;
                    }
                }
            });
        }
    }

    public Outfit getSelectedOutfit() {
        return selectedOutfit;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public void setOutfitList(List<Outfit> l){
        this.outfitList = l;
    }

    public List<Outfit> getOutfitList(){
        return outfitList;
    }
    public void setOnEditClick(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void setOnDeleteClick(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void removeItem(int position){
        outfitList.remove(position);
        notifyDataSetChanged();
    }
}
