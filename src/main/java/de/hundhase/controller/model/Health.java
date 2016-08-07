package de.hundhase.controller.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import de.hundhase.controller.model.ImmutableHealth;

@Value.Immutable
@JsonSerialize(as = ImmutableHealth.class)
public interface Health {

    String UP = "UP";

    String getStatus();
}
