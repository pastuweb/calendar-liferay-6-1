package net.appuntivari.calendar.scheduler;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class MySchedulerCalendarInDayJobListener implements MessageListener
{
	private static final Log log = LogFactoryUtil.getLog(MySchedulerCalendarInDayJobListener.class);
	 
	public void receive(Message arg0) throws MessageListenerException {
	 
		log.info("Start Scheduler: check TODAY Calendar Events");
			//write your logic.

	}

}
