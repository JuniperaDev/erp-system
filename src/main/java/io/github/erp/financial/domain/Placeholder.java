package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Placeholder entity for the financial context.
 */
@Entity
@Table(name = "placeholder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Placeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Placeholder containingPlaceholder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Placeholder getContainingPlaceholder() {
        return containingPlaceholder;
    }

    public void setContainingPlaceholder(Placeholder containingPlaceholder) {
        this.containingPlaceholder = containingPlaceholder;
    }
}
