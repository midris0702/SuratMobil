package net.netne.afahzis.appmobil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.netne.afahzis.appmobil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Reshape by Iwan on 14/12/17.
 */

public class AdapterInbox extends ArrayAdapter {
    private Context c;

    List<String> JUDUL = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    List<String> BACA = new ArrayList<String>();

    public AdapterInbox(Context c, List<String> JUDUL, List<String> ISI, List<String> TANGGAL,List<String> BACA) {
        super(c, R.layout.row_inbox, JUDUL);
        this.c = c;
        this.JUDUL = JUDUL;
        this.ISI = ISI;
        this.TANGGAL = TANGGAL;
        this.BACA = BACA;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View rowView;
        if(BACA.get(position).equals("1")){
            rowView = inflater.inflate(R.layout.row_inbox, null, true);
        }else{
            rowView = inflater.inflate(R.layout.row_inbox_baca, null, true);
        }
        TextView txtJudul = (TextView) rowView.findViewById(R.id.txtJudul);
        TextView txtIsi = (TextView) rowView.findViewById(R.id.txtPesan);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.txtTanggal);

        txtJudul.setText(JUDUL.get(position));
        txtTanggal.setText(TANGGAL.get(position));
        txtIsi.setText(ISI.get(position));

        return rowView;
    }
}
