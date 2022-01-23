package com.example.gestionstock.adapters;

import java.util.List;
import com.example.gestionstock.R;
import com.example.gestionstock.beans.ProduitRowItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProduitCustomListViewAdapter extends ArrayAdapter<ProduitRowItem> {

    Context context;

    public ProduitCustomListViewAdapter(Context context, int resourceId,
                                        List<ProduitRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtLibelle;
        TextView txtQuantite;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ProduitRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.produit_list_item, null);
            holder = new ViewHolder();
            holder.txtQuantite = (TextView) convertView.findViewById(R.id.quantite);
            holder.txtLibelle = (TextView) convertView.findViewById(R.id.libelle);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtQuantite.setText(rowItem.getQuantite());
        holder.txtLibelle.setText(rowItem.getLibelle());
        holder.imageView.setImageDrawable(rowItem.getImage());

        return convertView;
    }
}
