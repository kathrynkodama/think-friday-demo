package io.openliberty.sample.jakarta;

import java.util.List;

import io.openliberty.sample.jakarta.model.Alert;
import io.openliberty.sample.jakarta.model.Vaccine;
import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("")
@ApplicationScoped
public class InventoryResource {

}