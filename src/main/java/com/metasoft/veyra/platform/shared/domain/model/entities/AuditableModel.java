/**
 * Base class for auditable JPA entities.
 *
 * <p>This class provides common auditing fields such as {@code createdAt} and
 * {@code updatedAt}, which are automatically managed by Spring Data JPA through
 * the {@link AuditingEntityListener}. It is designed to be extended by entities
 * that require automatic timestamp tracking.</p>
 *
 * @summary Provides auditing fields (creation and update timestamps) for JPA entities.
 */
package com.metasoft.veyra.platform.shared.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditableModel {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Getter
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;
}
