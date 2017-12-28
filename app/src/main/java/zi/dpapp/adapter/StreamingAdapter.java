package zi.dpapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import zi.dpapp.R;
import zi.dpapp.models.Streamer;
import zi.dpapp.tools.BlurTransform;


public class StreamingAdapter extends RecyclerView.Adapter<StreamingAdapter.ViewHolder>{
    private ArrayList<Streamer> mDataSet;
    private Context mContext;

    public StreamingAdapter(Context context, ArrayList<Streamer> DataSet){
        mDataSet = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNome;
        public CardView cardView;
        public RelativeLayout layoutLive;
        public ImageView logo;
        public ViewHolder(View v){
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardView);
            textViewNome = (TextView) v.findViewById(R.id.textViewNome);
            layoutLive = (RelativeLayout) v.findViewById(R.id.layoutLive);
            logo = (ImageView) v.findViewById(R.id.imageViewLogo);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.cell_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final ViewHolder finalHolder = holder;
        final Streamer streamer = mDataSet.get(position);
        Picasso.with(mContext).load(streamer.getLogo()).transform(new BlurTransform(mContext)).into(holder.logo, new Callback() {
            @Override
            public void onSuccess() {
                finalHolder.textViewNome.setText(streamer.getNome());
            }

            @Override
            public void onError() {

            }
        });
        if (streamer.isLive()) {
            holder.layoutLive.setVisibility(View.VISIBLE);
        } else {
            holder.layoutLive.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }
}
