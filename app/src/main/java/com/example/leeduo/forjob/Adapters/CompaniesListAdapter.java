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

import com.example.leeduo.forjob.JsonBean.JsonCompaniesBean;
import com.example.leeduo.forjob.JsonBean.JsonCompaniesContent;
import com.example.leeduo.forjob.R;
import com.squareup.picasso.Picasso;


/**
 * Created by LeeDuo on 2019/2/4.
 */
//公司适配器
public class CompaniesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private View dataView,footView;
    private Context context;
    private JsonCompaniesContent[] jsonCompaniesContents;
    private String[] labels;
    private final int FOOT_TYPE = 1;
    private final int DATA_TYPE = 0;
    private OnItemTouchListener onItemTouchListener;
    private boolean footerViewIsVisible = false;

    public CompaniesListAdapter(JsonCompaniesBean jsonCompaniesBean, Context context){
        this.context = context;
        jsonCompaniesContents = jsonCompaniesBean.getContent();

    }

    public void setFooterViewIsVisible(boolean footerViewIsVisible) {
        this.footerViewIsVisible = footerViewIsVisible;
    }

    public boolean isFooterViewIsVisible() {
        return footerViewIsVisible;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == DATA_TYPE){
            dataView = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_list_item,parent,false);
            return new DateViewHolder(dataView);
        }else{
            footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_list_footer,parent,false);

            if(footerViewIsVisible){
                footView.setVisibility(View.VISIBLE);
            }else{
                footView.setVisibility(View.INVISIBLE);
            }
            return new FootViewHolder(footView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof DateViewHolder){
            if(onItemTouchListener != null){
                ((DateViewHolder) holder).parentView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        onItemTouchListener.OnItemTouchListener(position,v,event);
                        return true;
                    }
                });
            }
            Picasso.with(context).
                    load("http://"+jsonCompaniesContents[position].getCompanyLogo().toString()).
                    placeholder(R.drawable.upload_pic_loadding).
                    error(R.drawable.upload_pic_fail).
                    into(((DateViewHolder) holder).companyLogo);
            ((DateViewHolder)holder).companyName.setText(jsonCompaniesContents[position].getCompanyShortName());
            ((DateViewHolder)holder).companyPositionNum.setText(""+jsonCompaniesContents[position].getPositionNumber());
            ((DateViewHolder)holder).companyLocation.setText(jsonCompaniesContents[position].getCity());
            ((DateViewHolder)holder).companyFinanceStage.setText(jsonCompaniesContents[position].getFinanceStage());
            ((DateViewHolder)holder).companySize.setText(jsonCompaniesContents[position].getCompanySize());
            ((DateViewHolder)holder).companyIndustryField.setText(jsonCompaniesContents[position].getIndustryField());
            labels = jsonCompaniesContents[position].getOtherLabel().split(",");
            switch (labels.length){
                case 0:
                    ((DateViewHolder)holder).companyLabel1.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel2.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel3.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel4.setVisibility(View.GONE);
                    break;
                case 1:
                    ((DateViewHolder)holder).companyLabel1.setText(" "+labels[0]+" ");
                    ((DateViewHolder)holder).companyLabel2.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel3.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel4.setVisibility(View.GONE);
                    break;
                case 2:
                    ((DateViewHolder)holder).companyLabel1.setText(" "+labels[0]+" ");
                    ((DateViewHolder)holder).companyLabel2.setText(" "+labels[1]+" ");
                    ((DateViewHolder)holder).companyLabel3.setVisibility(View.GONE);
                    ((DateViewHolder)holder).companyLabel4.setVisibility(View.GONE);
                    break;
                case 3:
                    ((DateViewHolder)holder).companyLabel1.setText(" "+labels[0]+" ");
                    ((DateViewHolder)holder).companyLabel2.setText(" "+labels[1]+" ");
                    ((DateViewHolder)holder).companyLabel3.setText(" "+labels[2]+" ");
                    ((DateViewHolder)holder).companyLabel4.setVisibility(View.GONE);
                    break;
                default:
                    ((DateViewHolder)holder).companyLabel1.setText(" "+labels[0]+" ");
                    ((DateViewHolder)holder).companyLabel2.setText(" "+labels[1]+" ");
                    ((DateViewHolder)holder).companyLabel3.setText(" "+labels[2]+" ");
                    ((DateViewHolder)holder).companyLabel4.setText(" "+labels[3]+" ");
                    break;
            }
        }else {}
    }

    @Override
    public int getItemCount() {
        if(jsonCompaniesContents == null){
            return 0;
        }else{
            return (jsonCompaniesContents.length+1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1){
            return FOOT_TYPE;
        }
        return DATA_TYPE;
    }


    public static class FootViewHolder extends RecyclerView.ViewHolder{

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder{

        public final ImageView companyLogo;
        public final TextView companyName;
        public final TextView companyPositionNum;
        public final TextView companyLocation;
        public final TextView companyFinanceStage;
        public final TextView companySize;
        public final TextView companyIndustryField;
        public final TextView companyLabel1,companyLabel2,companyLabel3,companyLabel4;
        public final LinearLayout parentView;

        public DateViewHolder(View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.company_logo);
            companyName = itemView.findViewById(R.id.company_name);
            companyPositionNum = itemView.findViewById(R.id.company_position_number);
            companyLocation = itemView.findViewById(R.id.company_location);
            companyFinanceStage = itemView.findViewById(R.id.company_finance_stage);
            companySize = itemView.findViewById(R.id.company_size);
            companyIndustryField = itemView.findViewById(R.id.company_industry_field);
            companyLabel1 = itemView.findViewById(R.id.company_label_1);
            companyLabel2 = itemView.findViewById(R.id.company_label_2);
            companyLabel3 = itemView.findViewById(R.id.company_label_3);
            companyLabel4 = itemView.findViewById(R.id.company_label_4);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

    public void setData(JsonCompaniesBean jsonCompaniesBean) {
        jsonCompaniesContents = jsonCompaniesBean.getContent();
        notifyDataSetChanged();
        try{
            if(footerViewIsVisible){
                footView.setVisibility(View.VISIBLE);
            }else{
                footView.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){}

    }
    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener){
        this.onItemTouchListener = onItemTouchListener;
    }

    public interface OnItemTouchListener{
        void OnItemTouchListener(int position,View view,MotionEvent event);
    }
    public int getCompanyId(int position){
        return jsonCompaniesContents[position].getCompanyId();
    }

}
