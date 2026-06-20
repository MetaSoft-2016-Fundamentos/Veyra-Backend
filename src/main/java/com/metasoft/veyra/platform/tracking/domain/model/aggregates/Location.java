package com.metasoft.veyra.platform.tracking.domain.model.aggregates;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableMongoAggregateRoot;
import com.metasoft.veyra.platform.tracking.domain.model.events.LocationRecordedEvent;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.Coordinates;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
@Document(collection ="locations")
@Getter
public class Location extends AuditableMongoAggregateRoot<Location> {
    private final Long deviceId;
    private final Coordinates coordinates;
    private final LocalDateTime recordedAt;
    public Location(Long deviceId, double latitude, double longitude) {
        this.deviceId = deviceId;
        this.coordinates = new Coordinates(latitude,longitude);
        this.recordedAt = LocalDateTime.now();
        this.addDomainEvent(new LocationRecordedEvent(this, deviceId, latitude, longitude, this.recordedAt));

    }
}