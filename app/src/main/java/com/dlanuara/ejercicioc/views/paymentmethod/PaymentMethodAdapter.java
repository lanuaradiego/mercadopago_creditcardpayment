package com.dlanuara.ejercicioc.views.paymentmethod;

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
import com.dlanuara.ejercicioc.models.paymentmethod.PaymentMethod;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadAsyncTask;
import com.dlanuara.ejercicioc.utilities.imagedownloadasynctask.ImageDownloadedListener;

import java.util.ArrayList;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder> {

    private ArrayList<PaymentMethod> methods;

    /**
     * Carta seleccionada. Es -1 si no hay ninguna selecionada
     */
    private int checkedPosition;

    public PaymentMethodAdapter() {
        this.methods = new ArrayList<>();
        this.checkedPosition = -1;
    }

    public void addPaymentMethod(@NonNull PaymentMethod method){
        this.methods.add(method);
        notifyItemChanged(this.methods.size()-1);
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_paymentmethod, viewGroup, false);
        return new PaymentMethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(methods.get(position));
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    class PaymentMethodViewHolder extends RecyclerView.ViewHolder {
        private @NonNull TextView txtInfo;
        private @NonNull ImageView imgLogo;
        private @NonNull ImageView imgTick;

        private @Nullable PaymentMethod loaded;

        PaymentMethodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            imgTick = itemView.findViewById(R.id.imgTick);
        }

        void bind(final PaymentMethod method) {
            synchronized (this) {                                                                   // Lock para evitar que se sobreescriba una imagen con otra
                loaded = method;
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
            txtInfo.setText(method.getName());
            if(method.getImageThumbnail() != null)                                                  // Si ya se habia descargado la imagen
                imgLogo.setImageBitmap(method.getImageThumbnail());                                 // Mostrar descargada previamente
            else {
                //Comenzar descarga de imagen
                synchronized (this) {                                                               // Lock para evitar que se sobreescriba una imagen con otra
                    imgLogo.setImageBitmap(null);                                                   // Quitar imagen
                    ImageDownloadAsyncTask task= new ImageDownloadAsyncTask(new ImageDownloadedListener() {
                        @Override
                        public void onImageDownloaded(@Nullable Bitmap image) {
                            synchronized (PaymentMethodViewHolder.this) {                           // Lock para evitar que se sobreescriba una imagen con otra
                                method.setImageThumbnail(image);                                    // Al descargarse la imagen asignar al metodo asi no se vuelve a descargar
                                if(loaded == method)                                                // Si la tarjeta cargada en el viewholder es la misma a la cual se le descargo la imagen
                                    imgLogo.setImageBitmap(image);                                  // Mostrar imagen descargada
                            }
                        }
                    });
                    task.execute(method.getThumbnail());                                    // Iniciar carga de la imagen
                }
            }
        }
    }

    public @Nullable PaymentMethod getSelected() {
        if (checkedPosition != -1)
            return methods.get(checkedPosition);
        return null;
    }
}