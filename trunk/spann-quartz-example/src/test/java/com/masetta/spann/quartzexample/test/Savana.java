package com.masetta.spann.quartzexample.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.masetta.spann.quartzexample.annotations.Cron;
import com.masetta.spann.quartzexample.annotations.Interval;
import com.masetta.spann.quartzexample.annotations.QuartzJob;
import com.masetta.spann.spring.core.annotations.VisitMethods;

@Component
@VisitMethods
public class Savana {
	
	private Log log = LogFactory.getLog( Savana.class );
	
	private AtomicInteger newElemphants = new AtomicInteger();
	
	private AtomicInteger newZebras = new AtomicInteger();
	
	// a zebra is born every 5th of a second.
	@QuartzJob(name="zebraBorn",group="savana")
	@Interval(repeatInterval=200)
	public void zebraBorn() {
		log.debug("new zebra born.");
		newZebras.incrementAndGet();
	}

	// an elephant is born every second second
	@QuartzJob(name="elephantBorn",group="savana")
	@Cron(cronExpression="0/2 * * ? * * *")
	public void elephantBorn() {
		log.debug("new elephant born.");
		newElemphants.incrementAndGet();
	}
	
	public int getNewZebras() {
		return newZebras.get();
	}
	
	public int getNewElephants() {
		return newElemphants.get();
	}

}
