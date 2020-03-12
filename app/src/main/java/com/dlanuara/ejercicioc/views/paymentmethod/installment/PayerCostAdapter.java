package com.dlanuara.ejercicioc.views.paymentmethod.installment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dlanuara.ejercicioc.R;
import com.dlanuara.ejercicioc.models.paymentmethod.installment.PayerCost;

import java.util.ArrayList;

public class PayerCostAdapter extends RecyclerView.Adapter<PayerCostAdapter.PayerCostViewHolder> {

    private ArrayList<PayerCost> payerCosts;

    /**
     * Carta seleccionada. Es -1 si no hay ninguna selecionada
     */
    private int checkedPosition;

    public PayerCostAdapter() {
        this.payerCosts = new ArrayList<>();
        this.checkedPosition = -1;
    }

    public void addPayerCost(@NonNull PayerCost payerIssuer){
        this.payerCosts.add(payerIssuer);
        notifyItemChanged(this.payerCosts.size()-1);
    }

    @NonNull
    @Override
    public PayerCostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_payercost, viewGroup, false);
        return new PayerCostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayerCostViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(payerCosts.get(position));
    }

    @Override
    public int getItemCount() {
        return payerCosts.size();
    }

    class PayerCostViewHolder extends RecyclerView.ViewHolder {
        private @NonNull TextView txtInfo;
        private @NonNull TextView txtRate;
        private @NonNull ImageView imgTick;

        PayerCostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtRate = itemView.findViewById(R.id.txtRate);
            imgTick = itemView.findViewById(R.id.imgTick);
        }

        void bind(final PayerCost payerCost) {
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
            txtInfo.setText(payerCost.getRecommendedMessage());
            txtRate.setText(payerCost.getLabel(0).replace('_',' ').replace("|", ", "));
        }
    }

    public @Nullable PayerCost getSelected() {
        if (checkedPosition != -1)
            return payerCosts.get(checkedPosition);
        return null;
    }
}