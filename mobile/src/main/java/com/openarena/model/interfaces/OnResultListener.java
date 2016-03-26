package com.openarena.model.interfaces;

public interface OnResultListener<S> extends OnResult<S>{
	void onError(int code);
}
