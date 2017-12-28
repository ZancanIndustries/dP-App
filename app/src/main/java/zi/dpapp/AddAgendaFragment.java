package zi.dpapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAgendaFragment extends DialogFragment {

    RadioGroup radioGroup;
    Button radioButton;
    RadioButton radioStreamer;
    String canale;

    public interface addAgenda {
        public void addAgendaEvents(String canale);
    }

    static AddAgendaFragment newInstance(int num) {
        AddAgendaFragment f = new AddAgendaFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_addagenda, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioStreamer = (RadioButton) view.findViewById(R.id.radioStreamer);
        radioStreamer.setText(getArguments().getString("twitch"));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) view.findViewById(i);
                radioGroup.check(R.id.radioStreamer);
                if (i == R.id.radioStreamer) {
                    canale = radioButton.getText().toString();
                } else if (i == R.id.radiodP) {
                    canale = radioButton.getText().toString();
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Seleziona canale Twitch")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(getActivity() instanceof MainActivity){
                            ((MainActivity)getActivity()).addAgendaEvents(canale);
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }).show();
    }
}
