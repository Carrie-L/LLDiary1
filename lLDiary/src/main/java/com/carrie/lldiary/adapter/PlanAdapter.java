package com.carrie.lldiary.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Plan;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PlanAdapter";
    public ArrayList<Plan> entities;
    private int size;

    private static final int TYPE_TOP=0;
    private static final int TYPE_CONTENT=1;
    private PlanContentViewHolder contentViewHolder;
    private PlanTopViewHolder topViewHolder;

    public PlanAdapter(ArrayList<Plan> entities) {
        this.entities = entities;
        size=entities.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_TOP){
            View topView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_top, parent, false);
            PlanTopViewHolder topViewHolder=new PlanTopViewHolder(topView);
            return topViewHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_plan, parent, false);
            PlanContentViewHolder holder=new PlanContentViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Plan plan=entities.get(position);

        if(holder.getItemViewType()==TYPE_TOP){
            topViewHolder = (PlanTopViewHolder) holder;
            showTop(topViewHolder,plan);
        }else if(holder.getItemViewType()==TYPE_CONTENT){
            contentViewHolder = (PlanContentViewHolder) holder;
            showContent(contentViewHolder,plan);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_TOP;
        }else if(entities.get(position).getDate().equals(entities.get(position-1).getDate())){
            return TYPE_CONTENT;
        }else{
            return TYPE_TOP;
        }
    }

    private void showContent(final PlanContentViewHolder contentViewHolder,final Plan plan){
        int icon=plan.getIcon();
        contentViewHolder.icon.setImageResource(icon);
//        if(plan.getRemind()){
//            contentViewHolder.clock.setImageResource(R.drawable.plan_clock);
//        }else{
//            contentViewHolder.clock.setVisibility(View.INVISIBLE);
//        }
        contentViewHolder.duringTime.setText(plan.getStartTime());//+" - "+plan.getEndTime()
        contentViewHolder.content.setText(plan.getContent());

        changeStateIcon(contentViewHolder,plan.getFinish());
        contentViewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(TAG,"点击了状态图标");

                changeStateIcon(contentViewHolder,plan.getFinish());
            }
        });
    }

    private void changeStateIcon(PlanContentViewHolder holder,boolean bool){
        if(bool){
            holder.state.setImageResource(R.drawable.node_circle_finish);//plan_finish
        }else{
            holder.state.setImageResource(R.drawable.node_circle);//plan_unfinish
        }
    }

    private void showTop(PlanTopViewHolder holder, Plan plan){
        showContent(holder,plan);
        holder.date.setText(DateUtil.friendlyDate(plan.getDate()));
        holder.total.setText("已完成: " + plan.sum + "个");
    }


    @Override
    public int getItemCount() {
        return entities.size();
    }

    public class PlanContentViewHolder extends RecyclerView.ViewHolder {
        TextView content, duringTime;
        private ImageView state,icon;

        public PlanContentViewHolder(View itemView) {
            super(itemView);

            content= (TextView) itemView.findViewById(R.id.tv_item_plan_content);
            duringTime= (TextView) itemView.findViewById(R.id.tv_item_plan_time);
            icon= (ImageView) itemView.findViewById(R.id.iv_item_plan_icon);
            state= (ImageView) itemView.findViewById(R.id.iv_item_plan_state);
        }

        public String getStartTime(String time){
            return time.split("-")[0];
        }

        public String getEndTime(String time){
            return time.split("-")[1];
        }
    }

    public class PlanTopViewHolder extends PlanContentViewHolder{
       private TextView  date,total;

        public PlanTopViewHolder(View itemView) {
            super(itemView);
            date= (TextView) itemView.findViewById(R.id.tv_item_date);
            total= (TextView) itemView.findViewById(R.id.tv_item_total);
        }


    }


}
