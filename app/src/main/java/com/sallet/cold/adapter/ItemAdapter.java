package com.sallet.cold.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sallet.cold.R;
import com.sallet.cold.bean.WordCheckBean;
import com.sallet.cold.utils.azlist.AZBaseAdapter;
import com.sallet.cold.utils.azlist.AZItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends AZBaseAdapter<WordCheckBean, ItemAdapter.ItemHolder> implements Filterable {
	List<AZItemEntity<WordCheckBean>> mDatas;
	List<AZItemEntity<WordCheckBean>> filterDatas;

	@Override
	public Filter getFilter() {
		return new Filter() {
			//perform filtering
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();
				if (charString.isEmpty()) {
					//If there is no filtered content, the source data is used
					filterDatas = mDatas;
				} else {
					List<AZItemEntity<WordCheckBean>> filteredList = new ArrayList<>();


					for (int i = 0; i < mDatas.size(); i++) {

						if (mDatas.get(i).getValue().getWords().toUpperCase().startsWith(charString.toUpperCase())) {
							filteredList.add(mDatas.get(i));
						}
					}

					filterDatas = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = filterDatas;
				return filterResults;
			}

			//return the filtered value
			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				filterDatas = (List<AZItemEntity<WordCheckBean>>) filterResults.values;
				setDataList(filterDatas);
				notifyDataSetChanged();
				onBack.onBack();

			}
		};
	}
	public interface OnBack {
		void onBack();
	};
	public interface OnClick {
		void onclick(int position);
	};
	OnClick onClick;
	OnBack onBack;
	public ItemAdapter(List<AZItemEntity<WordCheckBean>> dataList,OnClick onClick,OnBack onBack) {
		super(dataList);
		mDatas=dataList;
		filterDatas=dataList;
		this.onClick=onClick;
		this.onBack=onBack;
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false));
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		holder.mTextName.setText(mDataList.get(position).getValue().getWords());
		if(mDataList.get(position).getValue().isCheck()){
			holder.ivCheck.setVisibility(View.VISIBLE);
		}else {
			holder.ivCheck.setVisibility(View.GONE);
		}
		holder.itemView.setOnClickListener(v -> onClick.onclick(position));
	}

	static class ItemHolder extends RecyclerView.ViewHolder {

		TextView mTextName;
		ImageView ivCheck;

		ItemHolder(View itemView) {
			super(itemView);
			mTextName = itemView.findViewById(R.id.text_item_name);
			ivCheck = itemView.findViewById(R.id.iv_check);
		}
	}
}
