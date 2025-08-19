package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

public class PaymentInvoiceDTO implements Serializable {
    private Long id;
    @NotNull
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal invoiceAmount;
    private String fileUploadToken;
    private String compilationToken;
    @Lob
    private String remarks;
    private Set<PurchaseOrderDTO> purchaseOrders = new HashSet<>();
    private Set<PlaceholderDTO> placeholders = new HashSet<>();
    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();
    private SettlementCurrencyDTO settlementCurrency;
    private DealerDTO biller;
    private Set<DeliveryNoteDTO> deliveryNotes = new HashSet<>();
    private Set<JobSheetDTO> jobSheets = new HashSet<>();
    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();
}
