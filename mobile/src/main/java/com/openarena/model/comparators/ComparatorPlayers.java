package com.openarena.model.comparators;

import com.openarena.model.objects.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComparatorPlayers {

	public static void sortByJerseyNumber(ArrayList<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			@Override
			public int compare(Player lhs, Player rhs) {
				int first = lhs.getJerseyNumber();
				int next = rhs.getJerseyNumber();
				if (first > next) return 1;
				else if (first < next) return -1;
				else return 0;
			}
		});
	}

}
