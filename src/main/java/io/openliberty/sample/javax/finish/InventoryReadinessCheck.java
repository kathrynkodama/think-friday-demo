package io.openliberty.sample.javax.finish;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {

    private static final String readinessCheck = InventoryResource.class.getSimpleName() + " Readiness Check";

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named(InventoryReadinessCheck.class.getSimpleName()).withData("ready", true).up()
                .build();
    }
}