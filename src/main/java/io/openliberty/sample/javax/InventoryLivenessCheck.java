package io.openliberty.sample.javax;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class InventoryLivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        long memUsed = memBean.getHeapMemoryUsage().getUsed();
        long memMax = memBean.getHeapMemoryUsage().getMax();

        return HealthCheckResponse.named(InventoryResource.class.getSimpleName() + " Liveness Check")
                .withData("memory used", memUsed).withData("memory max", memMax).status(memUsed < memMax * 0.9).build();
    }
}