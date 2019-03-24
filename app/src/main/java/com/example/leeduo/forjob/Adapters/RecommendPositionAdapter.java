package com.example.leeduo.forjob.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeduo.forjob.JsonBean.JsonRecommendHit;
import com.example.leeduo.forjob.R;
import com.squareup.picasso.Picasso;

/**
 * Created by LeeDuo on 2019/3/5.
 */

public class RecommendPositionAdapter extends RecyclerView.Adapter<RecommendPositionAdapter.ViewHolder> {

    private Context context;
    private JsonRecommendHit[] jsonRecommendHits;
    private View mView;
    private OnItemTouchListener onItemTouchListener;

    public RecommendPositionAdapter(Context context, JsonRecommendHit[] jsonRecommendHits){
        this.context = context;
        this.jsonRecommendHits = jsonRecommendHits;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_position_item,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.positionName.setText(jsonRecommendHits[position].get_source().getTitle());
        holder.company_name.setText(jsonRecommendHits[position].get_source().getCompany().getCompanyShortName());
        holder.city.setText(jsonRecommendHits[position].get_source().getCity());
        holder.location.setText("");
        holder.experience.setText(jsonRecommendHits[position].get_source().getExperience());
        holder.degree.setText(jsonRecommendHits[position].get_source().getDegree());
        holder.salary.setText(jsonRecommendHits[position].get_source().getMin_salary()+"k-"+jsonRecommendHits[position].get_source().getMax_salary()+"k");
        holder.date.setText(jsonRecommendHits[position].get_source().getCompany().getUpdateTime());
        holder.finance.setText(jsonRecommendHits[position].get_source().getCompany().getFinanceStage());
        holder.size.setText(jsonRecommendHits[position].get_source().getCompany().getCompanySize());
        holder.industry.setText(jsonRecommendHits[position].get_source().getCompany().getIndustryField());

        Picasso.with(context).
                load("http://"+jsonRecommendHits[position].get_source().getCompany().getCompanyLogo().toString()).
                placeholder(R.drawable.upload_pic_loadding).
                error(R.drawable.upload_pic_fail).
                into(holder.company_image);
        String[] labels = jsonRecommendHits[position].get_source().getCompany().getOtherLabel().split(",");
        switch (labels.length){
            case 0:
                holder.label1.setVisibility(View.GONE);
                holder.label2.setVisibility(View.GONE);
                holder.label3.setVisibility(View.GONE);
                holder.label4.setVisibility(View.GONE);
                break;
            case 1:
                holder.label1.setText(" "+labels[0]+" ");
                holder.label2.setVisibility(View.GONE);
                holder.label3.setVisibility(View.GONE);
                holder.label4.setVisibility(View.GONE);
                break;
            case 2:
                holder.label1.setText(" "+labels[0]+" ");
                holder.label2.setText(" "+labels[1]+" ");
                holder.label3.setVisibility(View.GONE);
                holder.label4.setVisibility(View.GONE);
                break;
            case 3:
                holder.label1.setText(" "+labels[0]+" ");
                holder.label2.setText(" "+labels[1]+" ");
                holder.label3.setText(" "+labels[2]+" ");
                holder.label4.setVisibility(View.GONE);
                break;
            default:
                holder.label1.setText(" "+labels[0]+" ");
                holder.label2.setText(" "+labels[1]+" ");
                holder.label3.setText(" "+labels[2]+" ");
                holder.label4.setText(" "+labels[3]+" ");
                break;

        }
        if(onItemTouchListener != null){
            holder.parentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onItemTouchListener.OnItemTouchListener(position,v,event);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        try{
            return jsonRecommendHits.length;
        }catch (Exception e){
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView positionName;
        public final TextView city;
        public final TextView location;
        public final TextView experience;
        public final TextView degree;
        public final TextView salary;
        public final TextView date;
        public final TextView company_name;
        public final TextView finance;
        public final TextView size;
        public final TextView industry;
        public final TextView label1;
        public final TextView label2;
        public final TextView label3;
        public final TextView label4;
        public final ImageView company_image;
        public final LinearLayout parentView;
        public ViewHolder(View itemView) {
            super(itemView);
            positionName = itemView.findViewById(R.id.position_name);
            city = itemView.findViewById(R.id.city);
            location = itemView.findViewById(R.id.location);
            experience = itemView.findViewById(R.id.experience);
            degree = itemView.findViewById(R.id.degree);
            salary = itemView.findViewById(R.id.salary);
            date = itemView.findViewById(R.id.date);
            company_name = itemView.findViewById(R.id.company_name);
            finance = itemView.findViewById(R.id.finance);
            size = itemView.findViewById(R.id.size);
            industry = itemView.findViewById(R.id.industry);
            label1 = itemView.findViewById(R.id.label1);
            label2 = itemView.findViewById(R.id.label2);
            label3 = itemView.findViewById(R.id.label3);
            label4 = itemView.findViewById(R.id.label4);
            company_image = itemView.findViewById(R.id.company_image);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

    public void setData(JsonRecommendHit[] jsonRecommendHits){
        this.jsonRecommendHits = jsonRecommendHits;
        notifyDataSetChanged();
    }

    public void setOnItemTouchListener(RecommendPositionAdapter.OnItemTouchListener onItemTouchListener){
        this.onItemTouchListener = onItemTouchListener;
    }

    public interface OnItemTouchListener{
        void OnItemTouchListener(int position,View view,MotionEvent event);
    }

    public JsonRecommendHit[] getJsonRecommendHits() {
        return jsonRecommendHits;
    }
}
