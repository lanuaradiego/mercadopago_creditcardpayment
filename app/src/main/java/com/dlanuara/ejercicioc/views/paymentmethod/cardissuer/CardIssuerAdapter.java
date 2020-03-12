package com.dlanuara.ejercicioc.views.paymentmethod.cardissuer;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.models.paymentmethod.cardissuer.CardIssuer;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadAsyncTask;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadedListener;

import java.util.ArrayList;

public class CardIssuerAdapter extends RecyclerView.Adapter<CardIssuerAdapter.CardIssuerViewHolder> {

    private ArrayList<CardIssuer> cardIssuers;

    /**
     * Carta seleccionada. Es -1 si no hay ninguna selecionada
     */
    private int checkedPosition;

    public CardIssuerAdapter() {
        this.cardIssuers = new ArrayList<>();
        this.checkedPosition = -1;
    }

    public void addCardIssuer(@NonNull CardIssuer cardIssuer){
        this.cardIssuers.add(cardIssuer);
        notifyItemChanged(this.cardIssuers.size()-1);
    }

    @NonNull
    @Override
    public CardIssuerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_cardissuer, viewGroup, false);
        return new CardIssuerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardIssuerViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(cardIssuers.get(position));
    }

    @Override
    public int getItemCount() {
        return cardIssuers.size();
    }

    class CardIssuerViewHolder extends RecyclerView.ViewHolder {
        private @NonNull TextView txtInfo;
        private @NonNull ImageView imgLogo;
        private @NonNull ImageView imgTick;

        private @Nullable CardIssuer loaded;

        CardIssuerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            imgTick = itemView.findViewById(R.id.imgTick);
        }

        void bind(final CardIssuer cardIssuer) {
            synchronized (this) {                                                                   // Lock para evitar que se sobreescriba una imagen con otra
                loaded = cardIssuer;
            }
            //Definir si esta tildado o no
            if (checkedPosition == -1) {
                imgTick.setVisibility(View.GONE);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    imgTick.setVisibility(View.VISIBLE);
                } else {
                    imgTick.setVisibility(View.GONE);
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgTick.setVisibility(View.VISIBLE);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
            txtInfo.setText(cardIssuer.getName());
            if(cardIssuer.getImageThumbnail() != null)                                                  // Si ya se habia descargado la imagen
                imgLogo.setImageBitmap(cardIssuer.getImageThumbnail());                                 // Mostrar descargada previamente
            else {
                //Comenzar descarga de imagen
                synchronized (this) {                                                               // Lock para evitar que se sobreescriba una imagen con otra
                    imgLogo.setImageBitmap(null);                                                   // Quitar imagen
                    ImageDownloadAsyncTask task= new ImageDownloadAsyncTask(new ImageDownloadedListener() {
                        @Override
                        public void onImageDownloaded(@Nullable Bitmap image) {
                            synchronized (CardIssuerViewHolder.this) {                           // Lock para evitar que se sobreescriba una imagen con otra
                                cardIssuer.setImageThumbnail(image);                                    // Al descargarse la imagen asignar al metodo asi no se vuelve a descargar
                                if(loaded == cardIssuer)                                                // Si la tarjeta cargada en el viewholder es la misma a la cual se le descargo la imagen
                                    imgLogo.setImageBitmap(image);                                  // Mostrar imagen descargada
                            }
                        }
                    });
                    task.execute(cardIssuer.getThumbnail());                                    // Iniciar carga de la imagen
                }
            }
        }
    }

    public @Nullable CardIssuer getSelected() {
        if (checkedPosition != -1)
            return cardIssuers.get(checkedPosition);
        return null;
    }
}