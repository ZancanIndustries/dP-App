package zi.dpapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zi.dpapp.R;
import zi.dpapp.models.Evento;

/**
 * Created by giacomo.zancan on 27/12/2017.
 */

public class EventiAdapter extends RecyclerView.Adapter<EventiAdapter.ViewHolder> {
    private ArrayList<Evento> mDataSet;
    private Context mContext;

    public EventiAdapter(Context context, ArrayList<Evento> DataSet) {
        mDataSet = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCanale, textViewOra;

        public ViewHolder(View v) {
            super(v);
            textViewCanale = (TextView) v.findViewById(R.id.textViewCanale);
            textViewOra = (TextView) v.findViewById(R.id.textViewOra);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.cell_listaeventi, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Evento evento = mDataSet.get(position);
        holder.textViewCanale.setText("-  " + evento.getCanale());
        holder.textViewOra.setText(evento.getOra());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}