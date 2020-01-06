package de.drazil.homeautomation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;

@Configuration
public class ApplicationConfig {

	@Bean
	public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
		AutoJsonRpcServiceImplExporter exp = new AutoJsonRpcServiceImplExporter();
		return exp;
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("myScheduler-");
		scheduler.setPoolSize(100);
		// block spring context stopping to allow SI pollers to complete
		// (to graceful shutdown still running tasks, without destroying beans used in
		// these tasks)
		// scheduler.setWaitForTasksToCompleteOnShutdown(true);
		// scheduler.setAwaitTerminationSeconds(20);
		return scheduler;
	}

}
