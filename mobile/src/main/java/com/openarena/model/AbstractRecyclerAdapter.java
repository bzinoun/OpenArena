package com.openarena.model;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.openarena.util.L;

import java.util.ArrayList;

/**
 *
 * @param <OL> object of list
 * @param <VH> ViewHolder
 */
public abstract class AbstractRecyclerAdapter<OL,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	protected ArrayList<OL> mList;

	public AbstractRecyclerAdapter(ArrayList<OL> list) {
		mList = list;
	}

	@Override
	abstract public VH onCreateViewHolder(ViewGroup parent, int viewType);

	@Override
	abstract public void onBindViewHolder(VH holder, int position);

	@Override
	public int getItemCount() {
		if (mList != null) return mList.size();
		else {
			L.e(AbstractRecyclerAdapter.class, "mList == null");
			return 0;
		}
	}

	public void addItem(int index, OL item) {
		if (mList != null) {
			mList.add(index, item);
			notifyItemInserted(index);
		}
	}

	public void replaceItem(int index, OL item) {
		if (mList != null) {
			mList.set(index, item);
			notifyItemChanged(index);
		}
	}

	public void removeItem(int index) {
		if (mList != null) {
			mList.remove(index);
			notifyItemRemoved(index);
		}
	}

	public void changeData(ArrayList<OL> list) {
		if (list != null) {
			mList = list;
			notifyDataSetChanged();
		}
	}

}
