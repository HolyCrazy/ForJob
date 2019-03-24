package com.example.leeduo.forjob.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeduo.forjob.JsonBean.JsonPositionsBean;
import com.example.leeduo.forjob.JsonBean.JsonPositionsContent;
import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/2/9.
 */
//公司职位展示适配器
public class SingleCompanyPositionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int FOOT_TYPE = 1;
    private final int DATA_TYPE = 0;
    private View dataView,footView;
    private JsonPositionsContent[] jsonPositionsContents;
    private Context context;
    private boolean footViewVisible = true;
    private OnItemClickListener onItemClickListener;


    public void setData(JsonPositionsBean jsonPositionsBean) {
        jsonPositionsContents = jsonPositionsBean.getContent();
        notifyDataSetChanged();
        try{
            if(footViewVisible){
                footView.setVisibility(View.VISIBLE);
            }else {
                footView.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){}
    }

    public boolean isFootViewVisible() {
        return footViewVisible;
    }

    public void setFootViewVisible(boolean footViewVisible) {
        this.footViewVisible = footViewVisible;
    }


    public SingleCompanyPositionsAdapter(JsonPositionsBean jsonPositionsBean, Context context ){
        jsonPositionsContents = jsonPositionsBean.getContent();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == DATA_TYPE){
            dataView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_position_item,parent,false);
            return new SingleCompanyPositionsAdapter.DataViewHolder(dataView);
        }else{
            footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_list_footer,parent,false);
            if(footViewVisible){
                footView.setVisibility(View.VISIBLE);
            }else {
                footView.setVisibility(View.INVISIBLE);
            }
            return new SingleCompanyPositionsAdapter.FootViewHolder(footView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof DataViewHolder){
            if(onItemClickListener != null){
                ((DataViewHolder)holder) .parentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(position,v);

                    }
                });
            }
            ((DataViewHolder)holder).positionName.setText(jsonPositionsContents[position].getTitle());
            ((DataViewHolder)holder).positionLocExpDeg.setText(jsonPositionsContents[position].getCity()+"/"+jsonPositionsContents[position].getExperience()+"/"+jsonPositionsContents[position].getDegree());
            ((DataViewHolder)holder).positionSalary.setText(jsonPositionsContents[position].getMinSalary()+"k"+"-"+jsonPositionsContents[position].getMaxSalary()+"k");
            ((DataViewHolder)holder).positionCreateTime.setText(jsonPositionsContents[position].getCreateTime());
        }else{

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1){
            return FOOT_TYPE;
        }
        return DATA_TYPE;
    }

    @Override
    public int getItemCount() {
        if(jsonPositionsContents == null){
            return 0;
        }else{
            return (jsonPositionsContents.length+1);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder{

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private final TextView positionName,positionLocExpDeg,positionSalary,positionCreateTime;
        private LinearLayout parentView;
        public DataViewHolder(View itemView) {
            super(itemView);
            positionName = itemView.findViewById(R.id.position_name);
            positionLocExpDeg = itemView.findViewById(R.id.position_loc_exp_deg);
            positionSalary = itemView.findViewById(R.id.position_salary);
            positionCreateTime = itemView.findViewById(R.id.position_create_time);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, View view);
    }

    public int getPositionId(int position){
        return jsonPositionsContents[position].getJobId();
    }

    public int getCompanyId(int position){
        return jsonPositionsContents[position].getCompanyId();
    }

}
