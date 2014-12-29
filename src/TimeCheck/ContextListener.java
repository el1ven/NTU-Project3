package TimeCheck;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;

/**
 * Created by el1ven on 27/12/14.
 */
public class ContextListener implements ServletContextListener {

    private Timer timer =null;

    @Override
    public void contextInitialized(ServletContextEvent event) {

        timer = new Timer(true);
        event.getServletContext().log("定时器已经启动");
        timer.schedule(new Mytask(event.getServletContext()), 0, 60*60*1000);
        event.getServletContext().log("已经添加任务调度表");

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        timer.cancel();
        event.getServletContext().log("定时器销毁");

    }
}
