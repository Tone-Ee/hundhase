package de.hundhase.frontend;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hundhase.models.CustomDateSerializer;
import org.immutables.value.Value;
import org.joda.time.DateTime;


@Value.Immutable
@JsonSerialize(as = de.hundhase.frontend.ImmutableFEPicture.class)
@JsonDeserialize(as = de.hundhase.frontend.ImmutableFEPicture.class)
public interface FEPicture {

    @JsonView(Views.Full.class)
    long getId();

    @JsonView(Views.Min.class)
    String getPath();

    @JsonView(Views.Full.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    DateTime getDateAdded();

    @JsonView(Views.Extended.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    DateTime getExifDate();

    @JsonView(Views.Extended.class)
    String getScope();

    interface Views {
        interface Min {
        }

        interface Extended extends Min {
        }

        interface Full extends Extended {
        }
    }

}
