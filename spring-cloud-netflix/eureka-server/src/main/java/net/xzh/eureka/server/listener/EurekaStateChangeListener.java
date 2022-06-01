package net.xzh.eureka.server.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;

/**
 * 监听eureka服务
 */
@Component
public class EurekaStateChangeListener {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(EurekaStateChangeListener.class);

	@EventListener
	public void listen(EurekaInstanceCanceledEvent event) {
		LOGGER.info("服务下线，{}",event.getAppName());
	}

	@EventListener
	public void listen(EurekaInstanceRegisteredEvent event) {
		InstanceInfo instanceInfo = event.getInstanceInfo();
		LOGGER.info("服务注册，{}",instanceInfo.getInstanceId());
	}

	@EventListener
	public void listen(EurekaInstanceRenewedEvent event) {
		InstanceInfo instanceInfo = event.getInstanceInfo();
		LOGGER.info("服务续约，{}",instanceInfo.getInstanceId());
	}
}
