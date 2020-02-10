package com.nextsol.digitalcompass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextsol.digitalcompass.R;
import com.nextsol.digitalcompass.model.Forecast;

import java.util.List;

public class ForeCastAdapter extends RecyclerView.Adapter<ForeCastAdapter.MyViewHolder> {
    Context mctx;
    int layout;
    List<Forecast> forecasts;

    public ForeCastAdapter(Context context, int resource, List<Forecast> object) {
        this.mctx = context;
        this.layout = resource;
        this.forecasts = object;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mctx).inflate(R.layout.item_forecast,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewDay.setText(""+forecasts.get(position).getTimeStamp());
        holder.textViewStatus.setText(""+forecasts.get(position).getStatus());
        holder.textViewHumidity.setText(""+forecasts.get(position).getHumidity());
        holder.textViewWind.setText(""+forecasts.get(position).getSpeedWind());
        holder.textViewPressure.setText(""+forecasts.get(position).getPressure());
        holder.textViewTemp.setText(""+forecasts.get(position).getTemp());

    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewDay, textViewStatus, textViewTemp, textViewWind, textViewHumidity, textViewPressure;
        LinearLayout linearLayoutDetail;

        public MyViewHolder( View itemView) {

            super(itemView);
            imageViewIcon=(ImageView) itemView.findViewById(R.id.imageviewIcon_ItermForeCast);
            textViewTemp=(TextView) itemView.findViewById(R.id.textviewTemp_ItermForeCast);
            textViewDay=(TextView) itemView.findViewById(R.id.textviewDay_ItermForeCast);
            textViewWind=(TextView) itemView.findViewById(R.id.textviewWind_ItermForeCast);
            textViewHumidity=(TextView) itemView.findViewById(R.id.textviewHumidity_ItermForeCast);
            textViewPressure=(TextView) itemView.findViewById(R.id.textviewPressure_ItermForeCast);
            textViewStatus=(TextView) itemView.findViewById(R.id.textviewStatus_ItermForeCast);
        }
    }
}


