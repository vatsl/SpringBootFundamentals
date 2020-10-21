package ttl.larku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import ttl.larku.domain.StudentCreatedEvent;
import ttl.larku.service.StudentService;
import ttl.larku.service.props.ConnectionService;
import ttl.larku.service.props.ServiceThatWeDontOwn;

import java.util.List;
import java.util.Set;

@EnableAsync
@SpringBootApplication
public class SpringBootApp {
    public static void defaultmain(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);

    }

    public static void main(String [] args) {
        SpringApplication app = new SpringApplication(SpringBootApp.class);
        app.addInitializers(new MyInitializer());
        ConfigurableApplicationContext context = app.run(args);

//        ConnectionService cs = context.getBean("connectionService", ConnectionService.class);
//        int result = cs.makeConnection();
//
//        System.out.println("Result is " + result);
//
//        ServiceThatWeDontOwn stwdo = context.getBean("serviceThatWeDontOwn", ServiceThatWeDontOwn.class);
//        System.out.println("stwdo: " + stwdo);

//        context.close();
    }

    public static void mainWithBuilder(String [] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringBootApp.class)
            .initializers(new MyInitializer());
         builder.run(args);
    }

    /**
     * An example of main that you could use to run a Spring boot
     * application as a JavaSE application.  No Web Environment.
     * A CommandLineRunner is useful in this context to actually
     * get some work done.
     *
     * @param args
     */
    public static void cmdLineMain(String [] args) {
        ConfigurableApplicationContext ctx =
                new SpringApplicationBuilder(SpringBootApp.class)
                        .web(WebApplicationType.NONE)
                        .run(args);

        ctx.close();
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            System.out.println("Bean Runner Called");
        };
    }
}

//@Component
class ContextConfigDemo implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}

@Component
class ApplicationCommandLineRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationCommandLineRunner called:");
        List<String> noOptionArgs = args.getNonOptionArgs();
        System.out.println("NoOptionArgs");
        noOptionArgs.forEach(System.out::println);

        Set<String> optionNames = args.getOptionNames();
        System.out.println("optionNames");
        optionNames.forEach(System.out::println);

        List<String> optionValues = args.getOptionValues("verbose");
        System.out.println("debug optionValue");
        if(optionValues != null) optionValues.forEach(System.out::println);
    }
}

//@Component
class MyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Autowired
    private StudentService studentService;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("Initializer called");
    }
}

@Component
class GeneralEventListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener
    @Async
    public void handleStudentCreatedEvent(StudentCreatedEvent event) {
        logger.info("Student Created: " + event + " in " + Thread.currentThread());
    }

    @EventListener
    public void handleServletRequestEvent(ServletRequestHandledEvent event) {
        logger.info("ServletRequest: " + event);
    }
}

//@Component
class ContextRefreshedHandler implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Context refreshed");
    }
}

@Component
class OtherContextRefreshedHandler {
    @EventListener
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        System.out.println("ServletRequestHandled: " + event);
    }
}


//@Component
class AllEventListener {
    @EventListener
    public void listenToAllEvents(ApplicationEvent event) {
        System.out.println("Event happened: " + event);
        if(event instanceof SpringApplicationEvent) {
            System.out.println("SpringBoot event");
//            SpringApplicationEvent sae = (SpringApplicationEvent)event;
//            for(String arg: sae.getArgs()) {
//                System.out.println("arg: " + arg);
//            }
        }
        if(event instanceof AvailabilityChangeEvent) {
            AvailabilityChangeEvent ace = (AvailabilityChangeEvent) event;
            System.out.println("AvailabilityChanged new State: " + ace.getState());
        }

    }
}
