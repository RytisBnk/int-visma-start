package lt.visma.starter.service.impl;

import lt.visma.starter.service.ServerTimeService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@Service
public class ServerTimeServiceImpl implements ServerTimeService {
    @Override
    public String getCurrentServerTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
