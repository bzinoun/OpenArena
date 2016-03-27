package com.openarena.model.interfaces;

import com.openarena.model.objects.EventData;

public interface EventListener {
	void onEvent(EventData event);
}
