package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

public class DealerDTO implements Serializable {
    private Long id;
    @NotNull
    private String dealerName;
    private String taxNumber;
    private String identificationDocumentNumber;
    private String organizationName;
    private String department;
    private String position;
    private String postalAddress;
    private String physicalAddress;
    private String accountName;
    private String accountNumber;
    private String bankersName;
    private String bankersBranch;
    private String bankersSwiftCode;
    private String fileUploadToken;
    private String compilationToken;
    @Lob
    private String remarks;
    private String otherNames;
    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();
    private DealerGroupDTO dealerGroup;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();
}
