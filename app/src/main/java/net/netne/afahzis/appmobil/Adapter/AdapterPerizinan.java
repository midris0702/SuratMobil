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

public class AdapterPerizinan extends ArrayAdapter {
    private Context c;

    List<String> NO_POLISI = new ArrayList<String>();
    List<String> KETERANGAN = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    List<String> ACC = new ArrayList<String>();

    public AdapterPerizinan(Context c, List<String> NO_POLISI, List<String> KETERANGAN, List<String> TANGGAL,List<String> ACC) {
        super(c, R.layout.row_perizinan, NO_POLISI);
        this.c = c;
        this.NO_POLISI = NO_POLISI;
        this.KETERANGAN = KETERANGAN;
        this.TANGGAL = TANGGAL;
        this.ACC = ACC;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View rowView = inflater.inflate(R.layout.row_perizinan, null, true);
        TextView txtNo_Poll = (TextView) rowView.findViewById(R.id.txtNoPoll);
        TextView txtPerizinan = (TextView) rowView.findViewById(R.id.txtPerizinan);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.txtTanggal);
        TextView txtStatus = (TextView)rowView.findViewById(R.id.txtStatus);

        txtNo_Poll.setText(NO_POLISI.get(position));
        txtTanggal.setText(TANGGAL.get(position));
        txtStatus.setText(ACC.get(position));
        txtPerizinan.setText(KETERANGAN.get(position));

        return rowView;
    }
}
