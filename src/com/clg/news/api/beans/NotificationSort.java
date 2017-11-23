package com.clg.news.api.beans;

import java.util.Comparator;

import com.clg.news.api.model.Notification;

public class NotificationSort implements Comparator<Notification> {

	@Override
	public int compare(Notification o1, Notification o2) {

		long t1 = o1.getMsgDate().getTime();
		long t2 = o2.getMsgDate().getTime();
		if (t2 > t1)
			return 1;
		else if (t1 > t2)
			return -1;
		else
			return 0;
	}

}
