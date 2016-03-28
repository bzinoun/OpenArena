package com.openarena.model.interfaces;

public interface OnItemTouchAdapter {
	boolean onItemMove(int fromPosition, int toPosition);
	void onItemDismiss(int position);
}
