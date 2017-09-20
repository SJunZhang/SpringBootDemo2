package org.springboot.sample.Boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;


/**
 * 用于监听服务加载完毕之后，用命令来关闭Application
 * @author DELL
 *
 */
public abstract class ShutdownBootApp {

  private final static String text = "zz";

  private static void shutdown(ApplicationContext ctx) {
    /**
     * 加入加载监听器, 服务加载完毕后在此触发
     */
    Map<String, ApplicationListener> listeners =  ctx.getBeansOfType(ApplicationListener.class);
    if(listeners!=null) {
      listeners.forEach((key, listener)->listener.onApplicationEvent(null));
    }
    
   /* BeanContext.setBeanFactory(ctx);
    BeanContext.init();*/
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String info = String.format("Press ENTER `%s` to exit.", text);
    try {
      System.out.println(info);
      for (String line = in.readLine(); !text.equals(line); line = in.readLine()) {
        System.out.println(info);
      }
    } catch (IOException e) {

    }
    System.exit(SpringApplication.exit(ctx));
  }


  public static void run(Object source, String... args) {
    SpringApplication app = new SpringApplication(new Object[] {source});
    // app.setApplicationContextClass(AnnotationConfigEmbeddedWebApplicationContext.class);
    shutdown(app.run(args));
  }

}
