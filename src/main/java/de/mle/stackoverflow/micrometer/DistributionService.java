package de.mle.stackoverflow.micrometer;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class DistributionService {
    @Autowired
    private MeterRegistry registry;

    private DistributionSummary distributionSummary;

    @PostConstruct
    public void initMeter() {
        distributionSummary = DistributionSummary
                .builder("attributes.size")
                .description("Counter for the attributes per offer")
                .minimumExpectedValue(1.)
                .maximumExpectedValue(30.)
                .percentilePrecision(0)
                .publishPercentileHistogram()
                .baseUnit("attributes")
                .register(registry);
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void record() {
        distributionSummary.record(new Random().nextInt(30));
    }
}
