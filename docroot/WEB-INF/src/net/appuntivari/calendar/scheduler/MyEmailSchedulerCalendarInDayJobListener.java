package net.appuntivari.calendar.scheduler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


public class MyEmailSchedulerCalendarInDayJobListener implements MessageListener
{
	private static final Log log = LogFactoryUtil.getLog(MyEmailSchedulerCalendarInDayJobListener.class);
	
	
	public void receive(Message arg0) throws MessageListenerException {

			
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			log.info("Scheduler Agenda Email every 1 day before - "+c.getTime());
			/*to do - this is a stub*/

	}

}
