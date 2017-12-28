package zi.dpapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zi.dpapp.MainActivity;
import zi.dpapp.R;
import zi.dpapp.models.Evento;
import zi.dpapp.models.Giorni;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.CustomViewHolder> {
    private ArrayList<Giorni> arrayListGiorni;
    private Context mContext;
    private EventiAdapter eventiAdapter;
    private String role;

    public interface Agenda {
        public void openTimePickerAlert(String data);
    }

    public AgendaAdapter(Context context, ArrayList<Giorni> arrayListGiorni, String role) {
        this.arrayListGiorni = arrayListGiorni;
        this.mContext = context;
        this.role = role;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cardview_agenda, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        if (role.equals("Streamer") || role.equals("Admin")) {
            customViewHolder.imageViewAdd.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.imageViewAdd.setVisibility(View.GONE);
        }
        final Giorni giorni = arrayListGiorni.get(i);
        customViewHolder.textViewGiorno.setText(giorni.getGiorno().substring(0, 1).toUpperCase() + giorni.getGiorno().substring(1));
        customViewHolder.textViewData.setText(giorni.getData());
        customViewHolder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).openTimePickerAlert(giorni.getData());
                }
            }
        });
        JSONArray arr = null;
        ArrayList<Evento> arrayListEvento = new ArrayList<>();
        try {
            arr = new JSONArray(giorni.getEventi());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int z = 0; z < arr.length(); z++) {
            try {
                JSONObject jObj = arr.getJSONObject(z);
                Date data = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(jObj.getString("data"));
                String stringData = new SimpleDateFormat("dd/MM/yyyy").format(data);
                String stringOra = new SimpleDateFormat("HH:mm").format(data);
                Log.d("data", stringData + " " + giorni.getData());
                if (stringData.equals(giorni.getData())) {
                    customViewHolder.textViewNessunEvento.setVisibility(View.GONE);
                    arrayListEvento.add(new Evento(jObj.getString("canale"), stringOra));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        customViewHolder.recyclerViewAgenda.setLayoutManager(layoutManager);
        eventiAdapter = new EventiAdapter(mContext, arrayListEvento);
        customViewHolder.recyclerViewAgenda.setAdapter(eventiAdapter);
    }

    @Override
    public int getItemCount() {
        return (null != arrayListGiorni ? arrayListGiorni.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGiorno, textViewData, textViewNessunEvento;
        ImageView imageViewAdd;
        RecyclerView recyclerViewAgenda;

        public CustomViewHolder(View view) {
            super(view);
            this.textViewGiorno = (TextView) view.findViewById(R.id.textViewNomeGiorno);
            this.textViewData = (TextView) view.findViewById(R.id.textViewData);
            this.imageViewAdd = (ImageView) view.findViewById(R.id.imageAdd);
            this.recyclerViewAgenda = (RecyclerView) view.findViewById(R.id.recyclerViewAgenda);
            this.textViewNessunEvento = (TextView) view.findViewById(R.id.textViewNessunEvento);
        }
    }
}
