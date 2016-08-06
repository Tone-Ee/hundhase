package de.hundhase.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "pictures")
public class Picture {

    @Id
    @Column(name = "id")
    @JsonView(Views.Full.class)
    private long id;

    @Column(name = "path")
    @JsonView(Views.Min.class)
    private String path;

    @Column(name = "fileName")
    @JsonView(Views.Full.class)
    private String fileName;

    @Column(name = "caption")
    @JsonView(Views.Full.class)
    private String caption;

    @Column(name = "date_added")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonView(Views.Full.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private DateTime dateAdded;

    @Column(name = "exif_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonView(Views.Extended.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private DateTime exifDate;

    @Column(name = "scope")
    @JsonView(Views.Extended.class)
    private String scope;

    public static class Views {
        public interface Min {
        }

        public interface Extended extends Min {
        }

        public interface Full extends Extended {
        }
    }
}