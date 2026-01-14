package com.example.datasantri; // <-- SAMAKAN dengan tulisan package di MainActivity.java kamu

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class SantriAdapter extends ArrayAdapter<Santri> {
    public SantriAdapter(Context context, ArrayList<Santri> santri) {
        super(context, 0, santri);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Santri s = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_santri, parent, false);
        }

        TextView tvNama = convertView.findViewById(R.id.tvNama);
        TextView tvKelas = convertView.findViewById(R.id.tvKelas);
        TextView tvAsal = convertView.findViewById(R.id.tvAsal);
        TextView tvNoHp = convertView.findViewById(R.id.tvNoHp);

        tvNama.setText(s.getNama());
        tvKelas.setText("Kelas: " + s.getKelas());
        tvAsal.setText("Asal: " + s.getAsal());
        tvNoHp.setText("No. HP: " + s.getNoHp());

        return convertView;
    }
}