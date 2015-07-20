package net.appuntivari.calendar.scheduler;


import java.util.Calendar;
import java.util.Date;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class MySmsSchedulerCalendarInDayJobListener implements MessageListener
{
	private static final Log log = LogFactoryUtil.getLog(MySmsSchedulerCalendarInDayJobListener.class);

	public void receive(Message arg0) throws MessageListenerException {

		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		log.info("Scheduler Agenda SMS every 5 minute before - "+c.getTime());
		/*to do - this is a stub*/

	}

}
