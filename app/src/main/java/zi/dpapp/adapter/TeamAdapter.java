package zi.dpapp.adapter;

import android.content.Context;
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
import zi.dpapp.models.Team;
import zi.dpapp.tools.BlurTransform;

/**
 * Created by giacomo.zancan on 21/12/2017.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder>{
    private ArrayList<Team> mDataSet;
    private Context mContext;

    public TeamAdapter(Context context, ArrayList<Team> DataSet){
        mDataSet = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNome, textViewGioco;
        public CardView cardView;
        public RelativeLayout layoutLive;
        public ImageView logo;
        public ViewHolder(View v){
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardView);
            textViewNome = (TextView) v.findViewById(R.id.textViewNome);
            textViewGioco = (TextView) v.findViewById(R.id.textViewGioco);
            layoutLive = (RelativeLayout) v.findViewById(R.id.layoutLive);
            logo = (ImageView) v.findViewById(R.id.imageViewLogo);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.cell_cardview_team, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final ViewHolder finalHolder = holder;
        final Team team = mDataSet.get(position);
        Picasso.with(mContext).load(team.getLogo()).transform(new BlurTransform(mContext)).into(holder.logo, new Callback() {
            @Override
            public void onSuccess() {
                finalHolder.textViewNome.setText(team.getNome());
                finalHolder.textViewGioco.setText(team.getGioco());
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }
}
