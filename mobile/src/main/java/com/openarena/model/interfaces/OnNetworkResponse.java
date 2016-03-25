package com.openarena.model.interfaces;

public interface OnNetworkResponse<S> extends OnResult<S>{
	void onError();
}
