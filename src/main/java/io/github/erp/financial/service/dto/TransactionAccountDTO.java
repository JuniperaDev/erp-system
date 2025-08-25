package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

public class TransactionAccountDTO implements Serializable {
    private Long id;
    @NotNull
    private String accountNumber;
    @NotNull
    private String accountName;
    @Lob
    private byte[] notes;
    private String notesContentType;
    @NotNull
    private Object accountType;
    @NotNull
    private Object accountSubType;
    private Boolean dummyAccount;
    private TransactionAccountLedgerDTO accountLedger;
    private TransactionAccountCategoryDTO accountCategory;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();
    private ServiceOutletDTO serviceOutlet;
    private SettlementCurrencyDTO settlementCurrency;
    private ReportingEntityDTO institution;
}
