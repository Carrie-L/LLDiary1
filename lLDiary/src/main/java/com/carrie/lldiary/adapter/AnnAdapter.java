package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.helper.ItemTouchHelperAdapter;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.view.IconTextView;

import java.util.ArrayList;
import java.util.Collections;

public class AnnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
	public ArrayList<Ann> entities;
	private LayoutInflater mInflater;
	private Context mContext;
	private boolean isMoved=false;
	private Resources resources;

	public AnnAdapter(ArrayList<Ann> entities, Context context) {
		super();
		this.entities = entities;
		this.mContext=context;
		mInflater=LayoutInflater.from(mContext);
		resources=context.getResources();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View convertView=mInflater.inflate(R.layout.list_item_anniversary, parent,false);
		return new AnnViewHolder(convertView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		AnnViewHolder annHolder= (AnnViewHolder) holder;
		Ann ann=entities.get(position);
		annHolder.itv_content.setText(ann.getContent());
		annHolder.itv_content.setTextSize(14);
		annHolder.itv_content.setImageBackground(AppUtils.getResourceIdentifier(resources,ann.getIcon(),"drawable"));

		annHolder.itv_content.setFloatTextVisible();
		annHolder.itv_content.setFloatText(ann.getDate());
		annHolder.itv_content.setFloatTextColor(R.color.primaryColor);

		annHolder.itv_person.setText(ann.getPerson());
		annHolder.itv_person.setImageBackground(R.mipmap.ic_person);

		annHolder.itv_durTime.setText(ann.durTime);
		annHolder.itv_durTime.setImageBackground(R.mipmap.ic_time);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return entities.size();
	}

	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		Collections.swap(entities,fromPosition,toPosition);
		notifyItemMoved(fromPosition,toPosition);
//		App.dbHelper.updateAnn(entities.get(toPosition));
		isMoved=true;
		return false;
	}



	@Override
	public void onItemDismiss(int position) {
		App.dbHelper.deleteAnnItem(entities.get(position));
		entities.remove(position);
		notifyItemRemoved(position);
	}



	class AnnViewHolder extends RecyclerView.ViewHolder  {//implements ItemTouchHelperViewHolder
		IconTextView itv_content,itv_person,itv_durTime;
		private final CardView cardView;

		public AnnViewHolder(View itemView) {
			super(itemView);

			cardView = (CardView) itemView.findViewById(R.id.card_view);
			itv_content=(IconTextView) itemView.findViewById(R.id.itv_ann_content);
			itv_person=(IconTextView) itemView.findViewById(R.id.itv_ann_person);
			itv_durTime=(IconTextView) itemView.findViewById(R.id.itv_ann_durTime);
		}

//		@Override
//		public void onItemSelected() {
//			LogUtil.i("onItemSelected","itemView.isClickable()="+itemView.isClickable());
//			LogUtil.i("onItemSelected","itemView.isLongClickable()="+itemView.isLongClickable());
//			LogUtil.i("onItemSelected","itemView.isInTouchMode()="+itemView.isInTouchMode());
//			LogUtil.i("onItemSelected","itemView.isFocusableInTouchMode()="+itemView.isFocusableInTouchMode());
////			if(){isFocusableInTouchMode
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//					cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.primaryColor_transparent, null));
//				}else{
//					cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.primaryColor_transparent));
//				}
////			}
//		}
//
//		@Override
//		public void onItemClear() {
//			cardView.setCardBackgroundColor(Color.WHITE);
//		}
	}

}
