package com.example.databaseapiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.databaseapiapp.model.Jadwal;

import java.util.ArrayList;
import java.util.List;

public class ListJadwalAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Jadwal> listJadwal, filterd;

    public ListJadwalAdapter(Context mContext, List<Jadwal> listJadwal) {
        this.mContext = mContext;
        this.listJadwal = listJadwal;
        this.filterd = this.listJadwal;
    }

    @Override
    public int getCount() {
        return filterd.size();
    }

    @Override
    public Object getItem(int position) {
        return filterd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        TextView txt_nama = convertView.findViewById(R.id.txt_nama);
        TextView txt_harga = convertView.findViewById(R.id.txt_tempat);
        TextView txt_waktu = convertView.findViewById(R.id.txt_waktu);

        Jadwal jadwal = filterd.get(position);
        txt_nama.setText(jadwal.getNama());
        txt_harga.setText(jadwal.getTempat());
        txt_waktu.setText(jadwal.getWaktu().toString());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        JadwalFilter filter = new JadwalFilter();
        return null;
    }

    private class JadwalFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Jadwal> filteredData = new ArrayList<Jadwal>();
            FilterResults result = new FilterResults();
            String filterString = charSequence.toString().toLowerCase();
            for (Jadwal jadwal : listJadwal) {
                if (jadwal.getNama().toLowerCase().contains(filterString)) {
                    filteredData.add(jadwal);
                }
            }
            result.count = filteredData.size();
            result.values = filteredData;
            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            filterd = (List<Jadwal>) results.values;
            notifyDataSetChanged();
        }
    }
}
