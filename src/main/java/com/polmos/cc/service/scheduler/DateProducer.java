package com.polmos.cc.service.scheduler;

import java.util.Date;

import javax.enterprise.inject.Produces;

/**
 *
 * @author RobicToNieMaKomu
 */
public class DateProducer {

    @Produces
    @Now
    public Date now() {
        return new Date();
    }
}
