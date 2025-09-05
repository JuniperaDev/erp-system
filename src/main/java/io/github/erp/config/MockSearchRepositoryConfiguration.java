package io.github.erp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Optional;

// Import all SearchRepository interfaces
import io.github.erp.docmgmt.repository.search.DocumentSearchRepository;
import io.github.erp.repository.search.AcademicQualificationSearchRepository;
import io.github.erp.repository.search.AccountAttributeMetadataSearchRepository;
import io.github.erp.repository.search.AccountAttributeSearchRepository;
import io.github.erp.repository.search.AccountBalanceSearchRepository;
import io.github.erp.repository.search.AccountOwnershipTypeSearchRepository;
import io.github.erp.repository.search.AccountStatusTypeSearchRepository;
import io.github.erp.repository.search.AccountTypeSearchRepository;
import io.github.erp.repository.search.AcquiringIssuingFlagSearchRepository;
import io.github.erp.repository.search.AgencyNoticeSearchRepository;
import io.github.erp.repository.search.AgentBankingActivitySearchRepository;
import io.github.erp.repository.search.AgriculturalEnterpriseActivityTypeSearchRepository;
import io.github.erp.repository.search.AlgorithmSearchRepository;
import io.github.erp.repository.search.AmortizationPeriodSearchRepository;
import io.github.erp.repository.search.AmortizationPostingReportRequisitionSearchRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
import io.github.erp.repository.search.AmortizationRecurrenceSearchRepository;
import io.github.erp.repository.search.AmortizationSequenceSearchRepository;
import io.github.erp.repository.search.AnticipatedMaturityPerioodSearchRepository;
import io.github.erp.repository.search.ApplicationUserSearchRepository;
import io.github.erp.repository.search.AssetAccessorySearchRepository;
import io.github.erp.repository.search.AssetAdditionsReportItemSearchRepository;
import io.github.erp.repository.search.AssetAdditionsReportSearchRepository;
import io.github.erp.repository.search.AssetCategorySearchRepository;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.repository.search.AssetDocumentAssignmentSearchRepository;
import io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepository;
import io.github.erp.repository.search.AssetJobSheetAssignmentSearchRepository;
import io.github.erp.repository.search.AssetPaymentInvoiceAssignmentSearchRepository;
import io.github.erp.repository.search.AssetPurchaseOrderAssignmentSearchRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.repository.search.AssetRevaluationSearchRepository;
import io.github.erp.repository.search.AssetWarrantyAssignmentSearchRepository;
import io.github.erp.repository.search.AssetWarrantySearchRepository;
import io.github.erp.repository.search.AssetWriteOffSearchRepository;
import io.github.erp.repository.search.AutonomousReportSearchRepository;
import io.github.erp.repository.search.BankBranchCodeSearchRepository;
import io.github.erp.repository.search.BankTransactionTypeSearchRepository;
import io.github.erp.repository.search.BouncedChequeCategoriesSearchRepository;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.repository.search.BusinessSegmentTypesSearchRepository;
import io.github.erp.repository.search.BusinessStampSearchRepository;
import io.github.erp.repository.search.BusinessTeamSearchRepository;
import io.github.erp.repository.search.CardAcquiringTransactionSearchRepository;
import io.github.erp.repository.search.CardBrandTypeSearchRepository;
import io.github.erp.repository.search.CardCategoryTypeSearchRepository;
import io.github.erp.repository.search.CardChargesSearchRepository;
import io.github.erp.repository.search.CardClassTypeSearchRepository;
import io.github.erp.repository.search.CardFraudIncidentCategorySearchRepository;
import io.github.erp.repository.search.CardFraudInformationSearchRepository;
import io.github.erp.repository.search.CardIssuerChargesSearchRepository;
import io.github.erp.repository.search.CardPerformanceFlagSearchRepository;
import io.github.erp.repository.search.CardStateSearchRepository;
import io.github.erp.repository.search.CardStatusFlagSearchRepository;
import io.github.erp.repository.search.CardTypesSearchRepository;
import io.github.erp.repository.search.CardUsageInformationSearchRepository;
import io.github.erp.repository.search.CategoryOfSecuritySearchRepository;
import io.github.erp.repository.search.ChannelTypeSearchRepository;
import io.github.erp.repository.search.ChartOfAccountsCodeSearchRepository;
import io.github.erp.repository.search.CollateralInformationSearchRepository;
import io.github.erp.repository.search.CollateralTypeSearchRepository;
import io.github.erp.repository.search.CommitteeTypeSearchRepository;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.repository.search.ContractStatusSearchRepository;
import io.github.erp.repository.search.CounterPartyCategorySearchRepository;
import io.github.erp.repository.search.CounterPartyDealTypeSearchRepository;
import io.github.erp.repository.search.CounterpartyTypeSearchRepository;
import io.github.erp.repository.search.CountyCodeSearchRepository;
import io.github.erp.repository.search.CountySubCountyCodeSearchRepository;
import io.github.erp.repository.search.CrbAccountHolderTypeSearchRepository;
import io.github.erp.repository.search.CrbAccountStatusSearchRepository;
import io.github.erp.repository.search.CrbAgentServiceTypeSearchRepository;
import io.github.erp.repository.search.CrbAgingBandsSearchRepository;
import io.github.erp.repository.search.CrbAmountCategoryBandSearchRepository;
import io.github.erp.repository.search.CrbComplaintStatusTypeSearchRepository;
import io.github.erp.repository.search.CrbComplaintTypeSearchRepository;
import io.github.erp.repository.search.CrbCreditApplicationStatusSearchRepository;
import io.github.erp.repository.search.CrbCreditFacilityTypeSearchRepository;
import io.github.erp.repository.search.CrbCustomerTypeSearchRepository;
import io.github.erp.repository.search.CrbDataSubmittingInstitutionsSearchRepository;
import io.github.erp.repository.search.CrbFileTransmissionStatusSearchRepository;
import io.github.erp.repository.search.CrbGlCodeSearchRepository;
import io.github.erp.repository.search.CrbNatureOfInformationSearchRepository;
import io.github.erp.repository.search.CrbProductServiceFeeTypeSearchRepository;
import io.github.erp.repository.search.CrbRecordFileTypeSearchRepository;
import io.github.erp.repository.search.CrbReportRequestReasonsSearchRepository;
import io.github.erp.repository.search.CrbReportViewBandSearchRepository;
import io.github.erp.repository.search.CrbSourceOfInformationTypeSearchRepository;
import io.github.erp.repository.search.CrbSubmittingInstitutionCategorySearchRepository;
import io.github.erp.repository.search.CrbSubscriptionStatusTypeCodeSearchRepository;
import io.github.erp.repository.search.CreditCardFacilitySearchRepository;
import io.github.erp.repository.search.CreditCardOwnershipSearchRepository;
import io.github.erp.repository.search.CreditNoteSearchRepository;
import io.github.erp.repository.search.CurrencyAuthenticityFlagSearchRepository;
import io.github.erp.repository.search.CurrencyServiceabilityFlagSearchRepository;
import io.github.erp.repository.search.CustomerComplaintStatusTypeSearchRepository;
import io.github.erp.repository.search.CustomerIDDocumentTypeSearchRepository;
import io.github.erp.repository.search.CustomerTypeSearchRepository;
import io.github.erp.repository.search.DealerGroupSearchRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.repository.search.DeliveryNoteSearchRepository;
import io.github.erp.repository.search.DepartmentTypeSearchRepository;
import io.github.erp.repository.search.DepreciationBatchSequenceSearchRepository;
import io.github.erp.repository.search.DepreciationEntryReportItemSearchRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.repository.search.DepreciationJobNoticeSearchRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.repository.search.DepreciationMethodSearchRepository;
import io.github.erp.repository.search.DepreciationPeriodSearchRepository;
import io.github.erp.repository.search.DepreciationReportSearchRepository;
import io.github.erp.repository.search.DerivativeSubTypeSearchRepository;
import io.github.erp.repository.search.DerivativeUnderlyingAssetSearchRepository;
import io.github.erp.repository.search.DetailedLeaseContractSearchRepository;
import io.github.erp.repository.search.EmploymentTermsSearchRepository;
import io.github.erp.repository.search.ExcelReportExportSearchRepository;
import io.github.erp.repository.search.ExchangeRateSearchRepository;
import io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import io.github.erp.repository.search.FileUploadSearchRepository;
import io.github.erp.repository.search.FinancialDerivativeTypeCodeSearchRepository;
import io.github.erp.repository.search.FiscalMonthSearchRepository;
import io.github.erp.repository.search.FiscalQuarterSearchRepository;
import io.github.erp.repository.search.FiscalYearSearchRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.repository.search.FraudCategoryFlagSearchRepository;
import io.github.erp.repository.search.FraudTypeSearchRepository;
import io.github.erp.repository.search.FxCustomerTypeSearchRepository;
import io.github.erp.repository.search.FxRateTypeSearchRepository;
import io.github.erp.repository.search.FxReceiptPurposeTypeSearchRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.repository.search.FxTransactionRateTypeSearchRepository;
import io.github.erp.repository.search.FxTransactionTypeSearchRepository;
import io.github.erp.repository.search.GdiMasterDataIndexSearchRepository;
import io.github.erp.repository.search.GdiTransactionDataIndexSearchRepository;
import io.github.erp.repository.search.GenderTypeSearchRepository;
import io.github.erp.repository.search.GlMappingSearchRepository;
import io.github.erp.repository.search.InsiderCategoryTypesSearchRepository;
import io.github.erp.repository.search.InstitutionCodeSearchRepository;
import io.github.erp.repository.search.InstitutionContactDetailsSearchRepository;
import io.github.erp.repository.search.InstitutionStatusTypeSearchRepository;
import io.github.erp.repository.search.InterbankSectorCodeSearchRepository;
import io.github.erp.repository.search.InterestCalcMethodSearchRepository;
import io.github.erp.repository.search.InternalMemoSearchRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.repository.search.IsicEconomicActivitySearchRepository;
import io.github.erp.repository.search.IsoCountryCodeSearchRepository;
import io.github.erp.repository.search.IsoCurrencyCodeSearchRepository;
import io.github.erp.repository.search.IssuersOfSecuritiesSearchRepository;
import io.github.erp.repository.search.JobSheetSearchRepository;
import io.github.erp.repository.search.KenyanCurrencyDenominationSearchRepository;
import io.github.erp.repository.search.LeaseAmortizationCalculationSearchRepository;
import io.github.erp.repository.search.LeaseAmortizationScheduleSearchRepository;
import io.github.erp.repository.search.LeaseContractSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityReportItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityReportSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportSearchRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.repository.search.LeaseModelMetadataSearchRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import io.github.erp.repository.search.LeasePeriodSearchRepository;
import io.github.erp.repository.search.LeaseRepaymentPeriodSearchRepository;
import io.github.erp.repository.search.LegalStatusSearchRepository;
import io.github.erp.repository.search.LoanAccountCategorySearchRepository;
import io.github.erp.repository.search.LoanApplicationStatusSearchRepository;
import io.github.erp.repository.search.LoanApplicationTypeSearchRepository;
import io.github.erp.repository.search.LoanDeclineReasonSearchRepository;
import io.github.erp.repository.search.LoanPerformanceClassificationSearchRepository;
import io.github.erp.repository.search.LoanProductTypeSearchRepository;
import io.github.erp.repository.search.LoanRepaymentFrequencySearchRepository;
import io.github.erp.repository.search.LoanRestructureFlagSearchRepository;
import io.github.erp.repository.search.LoanRestructureItemSearchRepository;
import io.github.erp.repository.search.ManagementMemberTypeSearchRepository;
import io.github.erp.repository.search.MemoActionSearchRepository;
import io.github.erp.repository.search.MerchantTypeSearchRepository;
import io.github.erp.repository.search.MessageTokenSearchRepository;
import io.github.erp.repository.search.MfbBranchCodeSearchRepository;
import io.github.erp.repository.search.MonthlyPrepaymentOutstandingReportItemSearchRepository;
import io.github.erp.repository.search.MonthlyPrepaymentReportRequisitionSearchRepository;
import io.github.erp.repository.search.MoratoriumItemSearchRepository;
import io.github.erp.repository.search.NatureOfCustomerComplaintsSearchRepository;
import io.github.erp.repository.search.NbvCompilationBatchSearchRepository;
import io.github.erp.repository.search.NbvCompilationJobSearchRepository;
import io.github.erp.repository.search.NbvCompilationReportItemSearchRepository;
import io.github.erp.repository.search.NbvReportItemSearchRepository;
import io.github.erp.repository.search.NbvReportSearchRepository;
import io.github.erp.repository.search.NetBookValueEntrySearchRepository;
import io.github.erp.repository.search.OutletStatusSearchRepository;
import io.github.erp.repository.search.OutletTypeSearchRepository;
import io.github.erp.repository.search.ParticularsOfOutletSearchRepository;
import io.github.erp.repository.search.PartyRelationTypeSearchRepository;
import io.github.erp.repository.search.PaymentCalculationSearchRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.repository.search.PaymentInvoiceSearchRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.repository.search.PdfReportRequisitionSearchRepository;
import io.github.erp.repository.search.PerformanceOfForeignSubsidiariesSearchRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.repository.search.PrepaymentAccountReportRequisitionSearchRepository;
import io.github.erp.repository.search.PrepaymentAccountReportSearchRepository;
import io.github.erp.repository.search.PrepaymentAccountSearchRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.repository.search.PrepaymentByAccountReportRequisitionSearchRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.repository.search.PrepaymentMarshallingSearchRepository;
import io.github.erp.repository.search.PrepaymentOutstandingOverviewReportSearchRepository;
import io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepository;
import io.github.erp.repository.search.PrepaymentReportSearchRepository;
import io.github.erp.repository.search.ProcessStatusSearchRepository;
import io.github.erp.repository.search.ProductTypeSearchRepository;
import io.github.erp.repository.search.ProfessionalQualificationSearchRepository;
import io.github.erp.repository.search.PurchaseOrderSearchRepository;
import io.github.erp.repository.search.QuestionBaseSearchRepository;
import io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepository;
import io.github.erp.repository.search.RelatedPartyRelationshipSearchRepository;
import io.github.erp.repository.search.RemittanceFlagSearchRepository;
import io.github.erp.repository.search.ReportContentTypeSearchRepository;
import io.github.erp.repository.search.ReportDesignSearchRepository;
import io.github.erp.repository.search.ReportRequisitionSearchRepository;
import io.github.erp.repository.search.ReportStatusSearchRepository;
import io.github.erp.repository.search.ReportTemplateSearchRepository;
import io.github.erp.repository.search.ReportingEntitySearchRepository;
import io.github.erp.repository.search.RouAccountBalanceReportItemSearchRepository;
import io.github.erp.repository.search.RouAccountBalanceReportSearchRepository;
import io.github.erp.repository.search.RouAssetListReportItemSearchRepository;
import io.github.erp.repository.search.RouAssetListReportSearchRepository;
import io.github.erp.repository.search.RouAssetNBVReportItemSearchRepository;
import io.github.erp.repository.search.RouAssetNBVReportSearchRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportSearchRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportSearchRepository;
import io.github.erp.repository.search.RouDepreciationRequestSearchRepository;
import io.github.erp.repository.search.RouInitialDirectCostSearchRepository;
import io.github.erp.repository.search.RouModelMetadataSearchRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportItemSearchRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepository;
import io.github.erp.repository.search.SecurityClassificationTypeSearchRepository;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.repository.search.SecurityTenureSearchRepository;
import io.github.erp.repository.search.SecurityTypeSearchRepository;
import io.github.erp.repository.search.ServiceOutletSearchRepository;
import io.github.erp.repository.search.SettlementCurrencySearchRepository;
import io.github.erp.repository.search.SettlementGroupSearchRepository;
import io.github.erp.repository.search.SettlementRequisitionSearchRepository;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.repository.search.ShareHoldingFlagSearchRepository;
import io.github.erp.repository.search.ShareholderTypeSearchRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.repository.search.SnaSectorCodeSearchRepository;
import io.github.erp.repository.search.SourceRemittancePurposeTypeSearchRepository;
import io.github.erp.repository.search.SourcesOfFundsTypeCodeSearchRepository;
import io.github.erp.repository.search.StaffCurrentEmploymentStatusSearchRepository;
import io.github.erp.repository.search.StaffRoleTypeSearchRepository;
import io.github.erp.repository.search.SubCountyCodeSearchRepository;
import io.github.erp.repository.search.SystemContentTypeSearchRepository;
import io.github.erp.repository.search.SystemModuleSearchRepository;
import io.github.erp.repository.search.TAAmortizationRuleSearchRepository;
import io.github.erp.repository.search.TACompilationRequestSearchRepository;
import io.github.erp.repository.search.TAInterestPaidTransferRuleSearchRepository;
import io.github.erp.repository.search.TALeaseInterestAccrualRuleSearchRepository;
import io.github.erp.repository.search.TALeaseRecognitionRuleSearchRepository;
import io.github.erp.repository.search.TALeaseRepaymentRuleSearchRepository;
import io.github.erp.repository.search.TARecognitionROURuleSearchRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.repository.search.TaxRuleSearchRepository;
import io.github.erp.repository.search.TerminalFunctionsSearchRepository;
import io.github.erp.repository.search.TerminalTypesSearchRepository;
import io.github.erp.repository.search.TerminalsAndPOSSearchRepository;
import io.github.erp.repository.search.TransactionAccountCategorySearchRepository;
import io.github.erp.repository.search.TransactionAccountLedgerSearchRepository;
import io.github.erp.repository.search.TransactionAccountPostingProcessTypeSearchRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.repository.search.TransactionAccountPostingRunSearchRepository;
import io.github.erp.repository.search.TransactionAccountReportItemSearchRepository;
import io.github.erp.repository.search.TransactionAccountSearchRepository;
import io.github.erp.repository.search.TransactionDetailsSearchRepository;
import io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepository;
import io.github.erp.repository.search.UltimateBeneficiaryTypesSearchRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.repository.search.UserSearchRepository;
import io.github.erp.repository.search.WIPListItemSearchRepository;
import io.github.erp.repository.search.WIPListReportSearchRepository;
import io.github.erp.repository.search.WIPTransferListItemSearchRepository;
import io.github.erp.repository.search.WIPTransferListReportSearchRepository;
import io.github.erp.repository.search.WeeklyCashHoldingSearchRepository;
import io.github.erp.repository.search.WeeklyCounterfeitHoldingSearchRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepository;
import io.github.erp.repository.search.WorkInProgressOverviewSearchRepository;
import io.github.erp.repository.search.WorkInProgressRegistrationSearchRepository;
import io.github.erp.repository.search.WorkInProgressReportSearchRepository;
import io.github.erp.repository.search.WorkInProgressTransferSearchRepository;
import io.github.erp.repository.search.WorkProjectRegisterSearchRepository;
import io.github.erp.repository.search.XlsxReportRequisitionSearchRepository;

/**
 * Configuration class that provides no-op implementations for SearchRepository interfaces
 * when Elasticsearch is not available. This allows the application to start gracefully
 * without Elasticsearch while maintaining all existing service patterns.
 */
@Configuration
@ConditionalOnMissingBean(ElasticsearchRestTemplate.class)
public class MockSearchRepositoryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MockSearchRepositoryConfiguration.class);

    public MockSearchRepositoryConfiguration() {
        log.info("MockSearchRepositoryConfiguration loaded - configuring no-op SearchRepository beans for graceful Elasticsearch degradation");
    }

    @Bean
    @Primary
    public DocumentSearchRepository noOpDocumentSearchRepository() {
        return createNoOpSearchRepository(DocumentSearchRepository.class);
    }
    @Bean
    @Primary
    public ParticularsOfOutletSearchRepository noOpParticularsOfOutletSearchRepository() {
        return createNoOpSearchRepository(ParticularsOfOutletSearchRepository.class);
    }
    @Bean
    @Primary
    public MessageTokenSearchRepository noOpMessageTokenSearchRepository() {
        return createNoOpSearchRepository(MessageTokenSearchRepository.class);
    }
    @Bean
    @Primary
    public GdiMasterDataIndexSearchRepository noOpGdiMasterDataIndexSearchRepository() {
        return createNoOpSearchRepository(GdiMasterDataIndexSearchRepository.class);
    }
    @Bean
    @Primary
    public UserSearchRepository noOpUserSearchRepository() {
        return createNoOpSearchRepository(UserSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbGlCodeSearchRepository noOpCrbGlCodeSearchRepository() {
        return createNoOpSearchRepository(CrbGlCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressRegistrationSearchRepository noOpWorkInProgressRegistrationSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressRegistrationSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilitySearchRepository noOpLeaseLiabilitySearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilitySearchRepository.class);
    }
    @Bean
    @Primary
    public MerchantTypeSearchRepository noOpMerchantTypeSearchRepository() {
        return createNoOpSearchRepository(MerchantTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CardFraudInformationSearchRepository noOpCardFraudInformationSearchRepository() {
        return createNoOpSearchRepository(CardFraudInformationSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationReportSearchRepository noOpDepreciationReportSearchRepository() {
        return createNoOpSearchRepository(DepreciationReportSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationEntrySearchRepository noOpDepreciationEntrySearchRepository() {
        return createNoOpSearchRepository(DepreciationEntrySearchRepository.class);
    }
    @Bean
    @Primary
    public LoanDeclineReasonSearchRepository noOpLoanDeclineReasonSearchRepository() {
        return createNoOpSearchRepository(LoanDeclineReasonSearchRepository.class);
    }
    @Bean
    @Primary
    public IsoCountryCodeSearchRepository noOpIsoCountryCodeSearchRepository() {
        return createNoOpSearchRepository(IsoCountryCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public CounterPartyCategorySearchRepository noOpCounterPartyCategorySearchRepository() {
        return createNoOpSearchRepository(CounterPartyCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationPeriodSearchRepository noOpDepreciationPeriodSearchRepository() {
        return createNoOpSearchRepository(DepreciationPeriodSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbDataSubmittingInstitutionsSearchRepository noOpCrbDataSubmittingInstitutionsSearchRepository() {
        return createNoOpSearchRepository(CrbDataSubmittingInstitutionsSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetRevaluationSearchRepository noOpAssetRevaluationSearchRepository() {
        return createNoOpSearchRepository(AssetRevaluationSearchRepository.class);
    }
    @Bean
    @Primary
    public BusinessSegmentTypesSearchRepository noOpBusinessSegmentTypesSearchRepository() {
        return createNoOpSearchRepository(BusinessSegmentTypesSearchRepository.class);
    }
    @Bean
    @Primary
    public InstitutionContactDetailsSearchRepository noOpInstitutionContactDetailsSearchRepository() {
        return createNoOpSearchRepository(InstitutionContactDetailsSearchRepository.class);
    }
    @Bean
    @Primary
    public InterestCalcMethodSearchRepository noOpInterestCalcMethodSearchRepository() {
        return createNoOpSearchRepository(InterestCalcMethodSearchRepository.class);
    }
    @Bean
    @Primary
    public OutletStatusSearchRepository noOpOutletStatusSearchRepository() {
        return createNoOpSearchRepository(OutletStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public CreditNoteSearchRepository noOpCreditNoteSearchRepository() {
        return createNoOpSearchRepository(CreditNoteSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentCalculationSearchRepository noOpPaymentCalculationSearchRepository() {
        return createNoOpSearchRepository(PaymentCalculationSearchRepository.class);
    }
    @Bean
    @Primary
    public PlaceholderSearchRepository noOpPlaceholderSearchRepository() {
        return createNoOpSearchRepository(PlaceholderSearchRepository.class);
    }
    @Bean
    @Primary
    public ExchangeRateSearchRepository noOpExchangeRateSearchRepository() {
        return createNoOpSearchRepository(ExchangeRateSearchRepository.class);
    }
    @Bean
    @Primary
    public CurrencyAuthenticityFlagSearchRepository noOpCurrencyAuthenticityFlagSearchRepository() {
        return createNoOpSearchRepository(CurrencyAuthenticityFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public SecurityClassificationTypeSearchRepository noOpSecurityClassificationTypeSearchRepository() {
        return createNoOpSearchRepository(SecurityClassificationTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public PdfReportRequisitionSearchRepository noOpPdfReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(PdfReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbSubscriptionStatusTypeCodeSearchRepository noOpCrbSubscriptionStatusTypeCodeSearchRepository() {
        return createNoOpSearchRepository(CrbSubscriptionStatusTypeCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public BusinessStampSearchRepository noOpBusinessStampSearchRepository() {
        return createNoOpSearchRepository(BusinessStampSearchRepository.class);
    }
    @Bean
    @Primary
    public TALeaseRepaymentRuleSearchRepository noOpTALeaseRepaymentRuleSearchRepository() {
        return createNoOpSearchRepository(TALeaseRepaymentRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public AccountAttributeSearchRepository noOpAccountAttributeSearchRepository() {
        return createNoOpSearchRepository(AccountAttributeSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanRestructureFlagSearchRepository noOpLoanRestructureFlagSearchRepository() {
        return createNoOpSearchRepository(LoanRestructureFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public BouncedChequeCategoriesSearchRepository noOpBouncedChequeCategoriesSearchRepository() {
        return createNoOpSearchRepository(BouncedChequeCategoriesSearchRepository.class);
    }
    @Bean
    @Primary
    public NatureOfCustomerComplaintsSearchRepository noOpNatureOfCustomerComplaintsSearchRepository() {
        return createNoOpSearchRepository(NatureOfCustomerComplaintsSearchRepository.class);
    }
    @Bean
    @Primary
    public CardUsageInformationSearchRepository noOpCardUsageInformationSearchRepository() {
        return createNoOpSearchRepository(CardUsageInformationSearchRepository.class);
    }
    @Bean
    @Primary
    public BankTransactionTypeSearchRepository noOpBankTransactionTypeSearchRepository() {
        return createNoOpSearchRepository(BankTransactionTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ContractStatusSearchRepository noOpContractStatusSearchRepository() {
        return createNoOpSearchRepository(ContractStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public EmploymentTermsSearchRepository noOpEmploymentTermsSearchRepository() {
        return createNoOpSearchRepository(EmploymentTermsSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationJobNoticeSearchRepository noOpDepreciationJobNoticeSearchRepository() {
        return createNoOpSearchRepository(DepreciationJobNoticeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetDocumentAssignmentSearchRepository noOpAssetDocumentAssignmentSearchRepository() {
        return createNoOpSearchRepository(AssetDocumentAssignmentSearchRepository.class);
    }
    @Bean
    @Primary
    public CardClassTypeSearchRepository noOpCardClassTypeSearchRepository() {
        return createNoOpSearchRepository(CardClassTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public RouInitialDirectCostSearchRepository noOpRouInitialDirectCostSearchRepository() {
        return createNoOpSearchRepository(RouInitialDirectCostSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationBatchSequenceSearchRepository noOpDepreciationBatchSequenceSearchRepository() {
        return createNoOpSearchRepository(DepreciationBatchSequenceSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetPurchaseOrderAssignmentSearchRepository noOpAssetPurchaseOrderAssignmentSearchRepository() {
        return createNoOpSearchRepository(AssetPurchaseOrderAssignmentSearchRepository.class);
    }
    @Bean
    @Primary
    public RouModelMetadataSearchRepository noOpRouModelMetadataSearchRepository() {
        return createNoOpSearchRepository(RouModelMetadataSearchRepository.class);
    }
    @Bean
    @Primary
    public UltimateBeneficiaryCategorySearchRepository noOpUltimateBeneficiaryCategorySearchRepository() {
        return createNoOpSearchRepository(UltimateBeneficiaryCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public FiscalYearSearchRepository noOpFiscalYearSearchRepository() {
        return createNoOpSearchRepository(FiscalYearSearchRepository.class);
    }
    @Bean
    @Primary
    public AccountAttributeMetadataSearchRepository noOpAccountAttributeMetadataSearchRepository() {
        return createNoOpSearchRepository(AccountAttributeMetadataSearchRepository.class);
    }
    @Bean
    @Primary
    public CountySubCountyCodeSearchRepository noOpCountySubCountyCodeSearchRepository() {
        return createNoOpSearchRepository(CountySubCountyCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public WIPListReportSearchRepository noOpWIPListReportSearchRepository() {
        return createNoOpSearchRepository(WIPListReportSearchRepository.class);
    }
    @Bean
    @Primary
    public PurchaseOrderSearchRepository noOpPurchaseOrderSearchRepository() {
        return createNoOpSearchRepository(PurchaseOrderSearchRepository.class);
    }
    @Bean
    @Primary
    public AcquiringIssuingFlagSearchRepository noOpAcquiringIssuingFlagSearchRepository() {
        return createNoOpSearchRepository(AcquiringIssuingFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public TAInterestPaidTransferRuleSearchRepository noOpTAInterestPaidTransferRuleSearchRepository() {
        return createNoOpSearchRepository(TAInterestPaidTransferRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public DealerSearchRepository noOpDealerSearchRepository() {
        return createNoOpSearchRepository(DealerSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationJobSearchRepository noOpDepreciationJobSearchRepository() {
        return createNoOpSearchRepository(DepreciationJobSearchRepository.class);
    }
    @Bean
    @Primary
    public RouAccountBalanceReportItemSearchRepository noOpRouAccountBalanceReportItemSearchRepository() {
        return createNoOpSearchRepository(RouAccountBalanceReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountLedgerSearchRepository noOpTransactionAccountLedgerSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountLedgerSearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationPostingReportItemSearchRepository noOpRouDepreciationPostingReportItemSearchRepository() {
        return createNoOpSearchRepository(RouDepreciationPostingReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbSubmittingInstitutionCategorySearchRepository noOpCrbSubmittingInstitutionCategorySearchRepository() {
        return createNoOpSearchRepository(CrbSubmittingInstitutionCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public RouAssetListReportSearchRepository noOpRouAssetListReportSearchRepository() {
        return createNoOpSearchRepository(RouAssetListReportSearchRepository.class);
    }
    @Bean
    @Primary
    public InternalMemoSearchRepository noOpInternalMemoSearchRepository() {
        return createNoOpSearchRepository(InternalMemoSearchRepository.class);
    }
    @Bean
    @Primary
    public CommitteeTypeSearchRepository noOpCommitteeTypeSearchRepository() {
        return createNoOpSearchRepository(CommitteeTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public DeliveryNoteSearchRepository noOpDeliveryNoteSearchRepository() {
        return createNoOpSearchRepository(DeliveryNoteSearchRepository.class);
    }
    @Bean
    @Primary
    public CustomerComplaintStatusTypeSearchRepository noOpCustomerComplaintStatusTypeSearchRepository() {
        return createNoOpSearchRepository(CustomerComplaintStatusTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanPerformanceClassificationSearchRepository noOpLoanPerformanceClassificationSearchRepository() {
        return createNoOpSearchRepository(LoanPerformanceClassificationSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentAccountSearchRepository noOpPrepaymentAccountSearchRepository() {
        return createNoOpSearchRepository(PrepaymentAccountSearchRepository.class);
    }
    @Bean
    @Primary
    public SourcesOfFundsTypeCodeSearchRepository noOpSourcesOfFundsTypeCodeSearchRepository() {
        return createNoOpSearchRepository(SourcesOfFundsTypeCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public CardBrandTypeSearchRepository noOpCardBrandTypeSearchRepository() {
        return createNoOpSearchRepository(CardBrandTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentLabelSearchRepository noOpPaymentLabelSearchRepository() {
        return createNoOpSearchRepository(PaymentLabelSearchRepository.class);
    }
    @Bean
    @Primary
    public IssuersOfSecuritiesSearchRepository noOpIssuersOfSecuritiesSearchRepository() {
        return createNoOpSearchRepository(IssuersOfSecuritiesSearchRepository.class);
    }
    @Bean
    @Primary
    public AccountTypeSearchRepository noOpAccountTypeSearchRepository() {
        return createNoOpSearchRepository(AccountTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbSourceOfInformationTypeSearchRepository noOpCrbSourceOfInformationTypeSearchRepository() {
        return createNoOpSearchRepository(CrbSourceOfInformationTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetRegistrationSearchRepository noOpAssetRegistrationSearchRepository() {
        return createNoOpSearchRepository(AssetRegistrationSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountPostingProcessTypeSearchRepository noOpTransactionAccountPostingProcessTypeSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountPostingProcessTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public DealerGroupSearchRepository noOpDealerGroupSearchRepository() {
        return createNoOpSearchRepository(DealerGroupSearchRepository.class);
    }
    @Bean
    @Primary
    public NetBookValueEntrySearchRepository noOpNetBookValueEntrySearchRepository() {
        return createNoOpSearchRepository(NetBookValueEntrySearchRepository.class);
    }
    @Bean
    @Primary
    public CardCategoryTypeSearchRepository noOpCardCategoryTypeSearchRepository() {
        return createNoOpSearchRepository(CardCategoryTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public UniversallyUniqueMappingSearchRepository noOpUniversallyUniqueMappingSearchRepository() {
        return createNoOpSearchRepository(UniversallyUniqueMappingSearchRepository.class);
    }
    @Bean
    @Primary
    public CountyCodeSearchRepository noOpCountyCodeSearchRepository() {
        return createNoOpSearchRepository(CountyCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public AcademicQualificationSearchRepository noOpAcademicQualificationSearchRepository() {
        return createNoOpSearchRepository(AcademicQualificationSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbComplaintTypeSearchRepository noOpCrbComplaintTypeSearchRepository() {
        return createNoOpSearchRepository(CrbComplaintTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SettlementCurrencySearchRepository noOpSettlementCurrencySearchRepository() {
        return createNoOpSearchRepository(SettlementCurrencySearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountReportItemSearchRepository noOpTransactionAccountReportItemSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetWriteOffSearchRepository noOpAssetWriteOffSearchRepository() {
        return createNoOpSearchRepository(AssetWriteOffSearchRepository.class);
    }
    @Bean
    @Primary
    public AlgorithmSearchRepository noOpAlgorithmSearchRepository() {
        return createNoOpSearchRepository(AlgorithmSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentInvoiceSearchRepository noOpPaymentInvoiceSearchRepository() {
        return createNoOpSearchRepository(PaymentInvoiceSearchRepository.class);
    }
    @Bean
    @Primary
    public FixedAssetDepreciationSearchRepository noOpFixedAssetDepreciationSearchRepository() {
        return createNoOpSearchRepository(FixedAssetDepreciationSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportDesignSearchRepository noOpReportDesignSearchRepository() {
        return createNoOpSearchRepository(ReportDesignSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentMarshallingSearchRepository noOpPrepaymentMarshallingSearchRepository() {
        return createNoOpSearchRepository(PrepaymentMarshallingSearchRepository.class);
    }
    @Bean
    @Primary
    public CardAcquiringTransactionSearchRepository noOpCardAcquiringTransactionSearchRepository() {
        return createNoOpSearchRepository(CardAcquiringTransactionSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentCompilationRequestSearchRepository noOpPrepaymentCompilationRequestSearchRepository() {
        return createNoOpSearchRepository(PrepaymentCompilationRequestSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressOverviewSearchRepository noOpWorkInProgressOverviewSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressOverviewSearchRepository.class);
    }
    @Bean
    @Primary
    public WIPTransferListReportSearchRepository noOpWIPTransferListReportSearchRepository() {
        return createNoOpSearchRepository(WIPTransferListReportSearchRepository.class);
    }
    @Bean
    @Primary
    public CreditCardOwnershipSearchRepository noOpCreditCardOwnershipSearchRepository() {
        return createNoOpSearchRepository(CreditCardOwnershipSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentByAccountReportRequisitionSearchRepository noOpPrepaymentByAccountReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(PrepaymentByAccountReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public TALeaseRecognitionRuleSearchRepository noOpTALeaseRecognitionRuleSearchRepository() {
        return createNoOpSearchRepository(TALeaseRecognitionRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public OutletTypeSearchRepository noOpOutletTypeSearchRepository() {
        return createNoOpSearchRepository(OutletTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public RouAccountBalanceReportSearchRepository noOpRouAccountBalanceReportSearchRepository() {
        return createNoOpSearchRepository(RouAccountBalanceReportSearchRepository.class);
    }
    @Bean
    @Primary
    public ShareholderTypeSearchRepository noOpShareholderTypeSearchRepository() {
        return createNoOpSearchRepository(ShareholderTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbAgentServiceTypeSearchRepository noOpCrbAgentServiceTypeSearchRepository() {
        return createNoOpSearchRepository(CrbAgentServiceTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public NbvCompilationBatchSearchRepository noOpNbvCompilationBatchSearchRepository() {
        return createNoOpSearchRepository(NbvCompilationBatchSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanApplicationStatusSearchRepository noOpLoanApplicationStatusSearchRepository() {
        return createNoOpSearchRepository(LoanApplicationStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public MoratoriumItemSearchRepository noOpMoratoriumItemSearchRepository() {
        return createNoOpSearchRepository(MoratoriumItemSearchRepository.class);
    }
    @Bean
    @Primary
    public StaffRoleTypeSearchRepository noOpStaffRoleTypeSearchRepository() {
        return createNoOpSearchRepository(StaffRoleTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SecurityClearanceSearchRepository noOpSecurityClearanceSearchRepository() {
        return createNoOpSearchRepository(SecurityClearanceSearchRepository.class);
    }
    @Bean
    @Primary
    public InsiderCategoryTypesSearchRepository noOpInsiderCategoryTypesSearchRepository() {
        return createNoOpSearchRepository(InsiderCategoryTypesSearchRepository.class);
    }
    @Bean
    @Primary
    public NbvReportSearchRepository noOpNbvReportSearchRepository() {
        return createNoOpSearchRepository(NbvReportSearchRepository.class);
    }
    @Bean
    @Primary
    public DetailedLeaseContractSearchRepository noOpDetailedLeaseContractSearchRepository() {
        return createNoOpSearchRepository(DetailedLeaseContractSearchRepository.class);
    }
    @Bean
    @Primary
    public RelatedPartyRelationshipSearchRepository noOpRelatedPartyRelationshipSearchRepository() {
        return createNoOpSearchRepository(RelatedPartyRelationshipSearchRepository.class);
    }
    @Bean
    @Primary
    public CreditCardFacilitySearchRepository noOpCreditCardFacilitySearchRepository() {
        return createNoOpSearchRepository(CreditCardFacilitySearchRepository.class);
    }
    @Bean
    @Primary
    public LeasePaymentSearchRepository noOpLeasePaymentSearchRepository() {
        return createNoOpSearchRepository(LeasePaymentSearchRepository.class);
    }
    @Bean
    @Primary
    public QuestionBaseSearchRepository noOpQuestionBaseSearchRepository() {
        return createNoOpSearchRepository(QuestionBaseSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbAccountHolderTypeSearchRepository noOpCrbAccountHolderTypeSearchRepository() {
        return createNoOpSearchRepository(CrbAccountHolderTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentAccountReportSearchRepository noOpPrepaymentAccountReportSearchRepository() {
        return createNoOpSearchRepository(PrepaymentAccountReportSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentCategorySearchRepository noOpPaymentCategorySearchRepository() {
        return createNoOpSearchRepository(PaymentCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public TerminalsAndPOSSearchRepository noOpTerminalsAndPOSSearchRepository() {
        return createNoOpSearchRepository(TerminalsAndPOSSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressOutstandingReportRequisitionSearchRepository noOpWorkInProgressOutstandingReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressOutstandingReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentOutstandingOverviewReportSearchRepository noOpPrepaymentOutstandingOverviewReportSearchRepository() {
        return createNoOpSearchRepository(PrepaymentOutstandingOverviewReportSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityReportItemSearchRepository noOpLeaseLiabilityReportItemSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbComplaintStatusTypeSearchRepository noOpCrbComplaintStatusTypeSearchRepository() {
        return createNoOpSearchRepository(CrbComplaintStatusTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionDetailsSearchRepository noOpTransactionDetailsSearchRepository() {
        return createNoOpSearchRepository(TransactionDetailsSearchRepository.class);
    }
    @Bean
    @Primary
    public TAAmortizationRuleSearchRepository noOpTAAmortizationRuleSearchRepository() {
        return createNoOpSearchRepository(TAAmortizationRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public RouAssetNBVReportSearchRepository noOpRouAssetNBVReportSearchRepository() {
        return createNoOpSearchRepository(RouAssetNBVReportSearchRepository.class);
    }
    @Bean
    @Primary
    public BankBranchCodeSearchRepository noOpBankBranchCodeSearchRepository() {
        return createNoOpSearchRepository(BankBranchCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public CustomerIDDocumentTypeSearchRepository noOpCustomerIDDocumentTypeSearchRepository() {
        return createNoOpSearchRepository(CustomerIDDocumentTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public InvoiceSearchRepository noOpInvoiceSearchRepository() {
        return createNoOpSearchRepository(InvoiceSearchRepository.class);
    }
    @Bean
    @Primary
    public CardChargesSearchRepository noOpCardChargesSearchRepository() {
        return createNoOpSearchRepository(CardChargesSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetJobSheetAssignmentSearchRepository noOpAssetJobSheetAssignmentSearchRepository() {
        return createNoOpSearchRepository(AssetJobSheetAssignmentSearchRepository.class);
    }
    @Bean
    @Primary
    public AccountBalanceSearchRepository noOpAccountBalanceSearchRepository() {
        return createNoOpSearchRepository(AccountBalanceSearchRepository.class);
    }
    @Bean
    @Primary
    public AmortizationPostingReportSearchRepository noOpAmortizationPostingReportSearchRepository() {
        return createNoOpSearchRepository(AmortizationPostingReportSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanApplicationTypeSearchRepository noOpLoanApplicationTypeSearchRepository() {
        return createNoOpSearchRepository(LoanApplicationTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public WeeklyCashHoldingSearchRepository noOpWeeklyCashHoldingSearchRepository() {
        return createNoOpSearchRepository(WeeklyCashHoldingSearchRepository.class);
    }
    @Bean
    @Primary
    public AgencyNoticeSearchRepository noOpAgencyNoticeSearchRepository() {
        return createNoOpSearchRepository(AgencyNoticeSearchRepository.class);
    }
    @Bean
    @Primary
    public CardTypesSearchRepository noOpCardTypesSearchRepository() {
        return createNoOpSearchRepository(CardTypesSearchRepository.class);
    }
    @Bean
    @Primary
    public CounterPartyDealTypeSearchRepository noOpCounterPartyDealTypeSearchRepository() {
        return createNoOpSearchRepository(CounterPartyDealTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentSearchRepository noOpPaymentSearchRepository() {
        return createNoOpSearchRepository(PaymentSearchRepository.class);
    }
    @Bean
    @Primary
    public RouAssetNBVReportItemSearchRepository noOpRouAssetNBVReportItemSearchRepository() {
        return createNoOpSearchRepository(RouAssetNBVReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public AgentBankingActivitySearchRepository noOpAgentBankingActivitySearchRepository() {
        return createNoOpSearchRepository(AgentBankingActivitySearchRepository.class);
    }
    @Bean
    @Primary
    public MonthlyPrepaymentOutstandingReportItemSearchRepository noOpMonthlyPrepaymentOutstandingReportItemSearchRepository() {
        return createNoOpSearchRepository(MonthlyPrepaymentOutstandingReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbCustomerTypeSearchRepository noOpCrbCustomerTypeSearchRepository() {
        return createNoOpSearchRepository(CrbCustomerTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TaxRuleSearchRepository noOpTaxRuleSearchRepository() {
        return createNoOpSearchRepository(TaxRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public ShareHoldingFlagSearchRepository noOpShareHoldingFlagSearchRepository() {
        return createNoOpSearchRepository(ShareHoldingFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public FraudTypeSearchRepository noOpFraudTypeSearchRepository() {
        return createNoOpSearchRepository(FraudTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TaxReferenceSearchRepository noOpTaxReferenceSearchRepository() {
        return createNoOpSearchRepository(TaxReferenceSearchRepository.class);
    }
    @Bean
    @Primary
    public FileUploadSearchRepository noOpFileUploadSearchRepository() {
        return createNoOpSearchRepository(FileUploadSearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationEntrySearchRepository noOpRouDepreciationEntrySearchRepository() {
        return createNoOpSearchRepository(RouDepreciationEntrySearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseModelMetadataSearchRepository noOpLeaseModelMetadataSearchRepository() {
        return createNoOpSearchRepository(LeaseModelMetadataSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountSearchRepository noOpTransactionAccountSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountSearchRepository.class);
    }
    @Bean
    @Primary
    public CurrencyServiceabilityFlagSearchRepository noOpCurrencyServiceabilityFlagSearchRepository() {
        return createNoOpSearchRepository(CurrencyServiceabilityFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseAmortizationScheduleSearchRepository noOpLeaseAmortizationScheduleSearchRepository() {
        return createNoOpSearchRepository(LeaseAmortizationScheduleSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportContentTypeSearchRepository noOpReportContentTypeSearchRepository() {
        return createNoOpSearchRepository(ReportContentTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public RemittanceFlagSearchRepository noOpRemittanceFlagSearchRepository() {
        return createNoOpSearchRepository(RemittanceFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public LegalStatusSearchRepository noOpLegalStatusSearchRepository() {
        return createNoOpSearchRepository(LegalStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public SystemContentTypeSearchRepository noOpSystemContentTypeSearchRepository() {
        return createNoOpSearchRepository(SystemContentTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SourceRemittancePurposeTypeSearchRepository noOpSourceRemittancePurposeTypeSearchRepository() {
        return createNoOpSearchRepository(SourceRemittancePurposeTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportTemplateSearchRepository noOpReportTemplateSearchRepository() {
        return createNoOpSearchRepository(ReportTemplateSearchRepository.class);
    }
    @Bean
    @Primary
    public FxTransactionRateTypeSearchRepository noOpFxTransactionRateTypeSearchRepository() {
        return createNoOpSearchRepository(FxTransactionRateTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SecurityTypeSearchRepository noOpSecurityTypeSearchRepository() {
        return createNoOpSearchRepository(SecurityTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkProjectRegisterSearchRepository noOpWorkProjectRegisterSearchRepository() {
        return createNoOpSearchRepository(WorkProjectRegisterSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbNatureOfInformationSearchRepository noOpCrbNatureOfInformationSearchRepository() {
        return createNoOpSearchRepository(CrbNatureOfInformationSearchRepository.class);
    }
    @Bean
    @Primary
    public PartyRelationTypeSearchRepository noOpPartyRelationTypeSearchRepository() {
        return createNoOpSearchRepository(PartyRelationTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AmortizationRecurrenceSearchRepository noOpAmortizationRecurrenceSearchRepository() {
        return createNoOpSearchRepository(AmortizationRecurrenceSearchRepository.class);
    }
    @Bean
    @Primary
    public CardIssuerChargesSearchRepository noOpCardIssuerChargesSearchRepository() {
        return createNoOpSearchRepository(CardIssuerChargesSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressOutstandingReportSearchRepository noOpWorkInProgressOutstandingReportSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressOutstandingReportSearchRepository.class);
    }
    @Bean
    @Primary
    public FixedAssetAcquisitionSearchRepository noOpFixedAssetAcquisitionSearchRepository() {
        return createNoOpSearchRepository(FixedAssetAcquisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public WIPTransferListItemSearchRepository noOpWIPTransferListItemSearchRepository() {
        return createNoOpSearchRepository(WIPTransferListItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbProductServiceFeeTypeSearchRepository noOpCrbProductServiceFeeTypeSearchRepository() {
        return createNoOpSearchRepository(CrbProductServiceFeeTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ChartOfAccountsCodeSearchRepository noOpChartOfAccountsCodeSearchRepository() {
        return createNoOpSearchRepository(ChartOfAccountsCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public FxTransactionChannelTypeSearchRepository noOpFxTransactionChannelTypeSearchRepository() {
        return createNoOpSearchRepository(FxTransactionChannelTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetGeneralAdjustmentSearchRepository noOpAssetGeneralAdjustmentSearchRepository() {
        return createNoOpSearchRepository(AssetGeneralAdjustmentSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetPaymentInvoiceAssignmentSearchRepository noOpAssetPaymentInvoiceAssignmentSearchRepository() {
        return createNoOpSearchRepository(AssetPaymentInvoiceAssignmentSearchRepository.class);
    }
    @Bean
    @Primary
    public SecurityTenureSearchRepository noOpSecurityTenureSearchRepository() {
        return createNoOpSearchRepository(SecurityTenureSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetAdditionsReportSearchRepository noOpAssetAdditionsReportSearchRepository() {
        return createNoOpSearchRepository(AssetAdditionsReportSearchRepository.class);
    }
    @Bean
    @Primary
    public DerivativeSubTypeSearchRepository noOpDerivativeSubTypeSearchRepository() {
        return createNoOpSearchRepository(DerivativeSubTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public GdiTransactionDataIndexSearchRepository noOpGdiTransactionDataIndexSearchRepository() {
        return createNoOpSearchRepository(GdiTransactionDataIndexSearchRepository.class);
    }
    @Bean
    @Primary
    public GlMappingSearchRepository noOpGlMappingSearchRepository() {
        return createNoOpSearchRepository(GlMappingSearchRepository.class);
    }
    @Bean
    @Primary
    public CounterpartyTypeSearchRepository noOpCounterpartyTypeSearchRepository() {
        return createNoOpSearchRepository(CounterpartyTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ProfessionalQualificationSearchRepository noOpProfessionalQualificationSearchRepository() {
        return createNoOpSearchRepository(ProfessionalQualificationSearchRepository.class);
    }
    @Bean
    @Primary
    public AutonomousReportSearchRepository noOpAutonomousReportSearchRepository() {
        return createNoOpSearchRepository(AutonomousReportSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationEntryReportItemSearchRepository noOpDepreciationEntryReportItemSearchRepository() {
        return createNoOpSearchRepository(DepreciationEntryReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public FiscalQuarterSearchRepository noOpFiscalQuarterSearchRepository() {
        return createNoOpSearchRepository(FiscalQuarterSearchRepository.class);
    }
    @Bean
    @Primary
    public FxReceiptPurposeTypeSearchRepository noOpFxReceiptPurposeTypeSearchRepository() {
        return createNoOpSearchRepository(FxReceiptPurposeTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanAccountCategorySearchRepository noOpLoanAccountCategorySearchRepository() {
        return createNoOpSearchRepository(LoanAccountCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public ApplicationUserSearchRepository noOpApplicationUserSearchRepository() {
        return createNoOpSearchRepository(ApplicationUserSearchRepository.class);
    }
    @Bean
    @Primary
    public JobSheetSearchRepository noOpJobSheetSearchRepository() {
        return createNoOpSearchRepository(JobSheetSearchRepository.class);
    }
    @Bean
    @Primary
    public RouAssetListReportItemSearchRepository noOpRouAssetListReportItemSearchRepository() {
        return createNoOpSearchRepository(RouAssetListReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbReportViewBandSearchRepository noOpCrbReportViewBandSearchRepository() {
        return createNoOpSearchRepository(CrbReportViewBandSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressTransferSearchRepository noOpWorkInProgressTransferSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressTransferSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityPostingReportSearchRepository noOpLeaseLiabilityPostingReportSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityPostingReportSearchRepository.class);
    }
    @Bean
    @Primary
    public AmortizationSequenceSearchRepository noOpAmortizationSequenceSearchRepository() {
        return createNoOpSearchRepository(AmortizationSequenceSearchRepository.class);
    }
    @Bean
    @Primary
    public ManagementMemberTypeSearchRepository noOpManagementMemberTypeSearchRepository() {
        return createNoOpSearchRepository(ManagementMemberTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public FiscalMonthSearchRepository noOpFiscalMonthSearchRepository() {
        return createNoOpSearchRepository(FiscalMonthSearchRepository.class);
    }
    @Bean
    @Primary
    public CollateralTypeSearchRepository noOpCollateralTypeSearchRepository() {
        return createNoOpSearchRepository(CollateralTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseAmortizationCalculationSearchRepository noOpLeaseAmortizationCalculationSearchRepository() {
        return createNoOpSearchRepository(LeaseAmortizationCalculationSearchRepository.class);
    }
    @Bean
    @Primary
    public SettlementSearchRepository noOpSettlementSearchRepository() {
        return createNoOpSearchRepository(SettlementSearchRepository.class);
    }
    @Bean
    @Primary
    public InstitutionStatusTypeSearchRepository noOpInstitutionStatusTypeSearchRepository() {
        return createNoOpSearchRepository(InstitutionStatusTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountPostingRuleSearchRepository noOpTransactionAccountPostingRuleSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountPostingRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanProductTypeSearchRepository noOpLoanProductTypeSearchRepository() {
        return createNoOpSearchRepository(LoanProductTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TARecognitionROURuleSearchRepository noOpTARecognitionROURuleSearchRepository() {
        return createNoOpSearchRepository(TARecognitionROURuleSearchRepository.class);
    }
    @Bean
    @Primary
    public CollateralInformationSearchRepository noOpCollateralInformationSearchRepository() {
        return createNoOpSearchRepository(CollateralInformationSearchRepository.class);
    }
    @Bean
    @Primary
    public ProcessStatusSearchRepository noOpProcessStatusSearchRepository() {
        return createNoOpSearchRepository(ProcessStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityReportSearchRepository noOpLeaseLiabilityReportSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityReportSearchRepository.class);
    }
    @Bean
    @Primary
    public BusinessDocumentSearchRepository noOpBusinessDocumentSearchRepository() {
        return createNoOpSearchRepository(BusinessDocumentSearchRepository.class);
    }
    @Bean
    @Primary
    public AgriculturalEnterpriseActivityTypeSearchRepository noOpAgriculturalEnterpriseActivityTypeSearchRepository() {
        return createNoOpSearchRepository(AgriculturalEnterpriseActivityTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SnaSectorCodeSearchRepository noOpSnaSectorCodeSearchRepository() {
        return createNoOpSearchRepository(SnaSectorCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public FileTypeSearchRepository noOpFileTypeSearchRepository() {
        return createNoOpSearchRepository(FileTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetWarrantyAssignmentSearchRepository noOpAssetWarrantyAssignmentSearchRepository() {
        return createNoOpSearchRepository(AssetWarrantyAssignmentSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentReportSearchRepository noOpPrepaymentReportSearchRepository() {
        return createNoOpSearchRepository(PrepaymentReportSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityPostingReportItemSearchRepository noOpLeaseLiabilityPostingReportItemSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityPostingReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public ExecutiveCategoryTypeSearchRepository noOpExecutiveCategoryTypeSearchRepository() {
        return createNoOpSearchRepository(ExecutiveCategoryTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ChannelTypeSearchRepository noOpChannelTypeSearchRepository() {
        return createNoOpSearchRepository(ChannelTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public TACompilationRequestSearchRepository noOpTACompilationRequestSearchRepository() {
        return createNoOpSearchRepository(TACompilationRequestSearchRepository.class);
    }
    @Bean
    @Primary
    public DerivativeUnderlyingAssetSearchRepository noOpDerivativeUnderlyingAssetSearchRepository() {
        return createNoOpSearchRepository(DerivativeUnderlyingAssetSearchRepository.class);
    }
    @Bean
    @Primary
    public RouMonthlyDepreciationReportSearchRepository noOpRouMonthlyDepreciationReportSearchRepository() {
        return createNoOpSearchRepository(RouMonthlyDepreciationReportSearchRepository.class);
    }
    @Bean
    @Primary
    public ExcelReportExportSearchRepository noOpExcelReportExportSearchRepository() {
        return createNoOpSearchRepository(ExcelReportExportSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentReportRequisitionSearchRepository noOpPrepaymentReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(PrepaymentReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationEntryReportSearchRepository noOpRouDepreciationEntryReportSearchRepository() {
        return createNoOpSearchRepository(RouDepreciationEntryReportSearchRepository.class);
    }
    @Bean
    @Primary
    public ProductTypeSearchRepository noOpProductTypeSearchRepository() {
        return createNoOpSearchRepository(ProductTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AnticipatedMaturityPerioodSearchRepository noOpAnticipatedMaturityPerioodSearchRepository() {
        return createNoOpSearchRepository(AnticipatedMaturityPerioodSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityScheduleReportSearchRepository noOpLeaseLiabilityScheduleReportSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityScheduleReportSearchRepository.class);
    }
    @Bean
    @Primary
    public PaymentRequisitionSearchRepository noOpPaymentRequisitionSearchRepository() {
        return createNoOpSearchRepository(PaymentRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportStatusSearchRepository noOpReportStatusSearchRepository() {
        return createNoOpSearchRepository(ReportStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentMappingSearchRepository noOpPrepaymentMappingSearchRepository() {
        return createNoOpSearchRepository(PrepaymentMappingSearchRepository.class);
    }
    @Bean
    @Primary
    public InterbankSectorCodeSearchRepository noOpInterbankSectorCodeSearchRepository() {
        return createNoOpSearchRepository(InterbankSectorCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetAccessorySearchRepository noOpAssetAccessorySearchRepository() {
        return createNoOpSearchRepository(AssetAccessorySearchRepository.class);
    }
    @Bean
    @Primary
    public AmortizationPeriodSearchRepository noOpAmortizationPeriodSearchRepository() {
        return createNoOpSearchRepository(AmortizationPeriodSearchRepository.class);
    }
    @Bean
    @Primary
    public IsicEconomicActivitySearchRepository noOpIsicEconomicActivitySearchRepository() {
        return createNoOpSearchRepository(IsicEconomicActivitySearchRepository.class);
    }
    @Bean
    @Primary
    public AccountStatusTypeSearchRepository noOpAccountStatusTypeSearchRepository() {
        return createNoOpSearchRepository(AccountStatusTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public FxCustomerTypeSearchRepository noOpFxCustomerTypeSearchRepository() {
        return createNoOpSearchRepository(FxCustomerTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbAgingBandsSearchRepository noOpCrbAgingBandsSearchRepository() {
        return createNoOpSearchRepository(CrbAgingBandsSearchRepository.class);
    }
    @Bean
    @Primary
    public MfbBranchCodeSearchRepository noOpMfbBranchCodeSearchRepository() {
        return createNoOpSearchRepository(MfbBranchCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public CategoryOfSecuritySearchRepository noOpCategoryOfSecuritySearchRepository() {
        return createNoOpSearchRepository(CategoryOfSecuritySearchRepository.class);
    }
    @Bean
    @Primary
    public BusinessTeamSearchRepository noOpBusinessTeamSearchRepository() {
        return createNoOpSearchRepository(BusinessTeamSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityByAccountReportItemSearchRepository noOpLeaseLiabilityByAccountReportItemSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityByAccountReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseContractSearchRepository noOpLeaseContractSearchRepository() {
        return createNoOpSearchRepository(LeaseContractSearchRepository.class);
    }
    @Bean
    @Primary
    public FxRateTypeSearchRepository noOpFxRateTypeSearchRepository() {
        return createNoOpSearchRepository(FxRateTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetDisposalSearchRepository noOpAssetDisposalSearchRepository() {
        return createNoOpSearchRepository(AssetDisposalSearchRepository.class);
    }
    @Bean
    @Primary
    public FraudCategoryFlagSearchRepository noOpFraudCategoryFlagSearchRepository() {
        return createNoOpSearchRepository(FraudCategoryFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationEntryReportItemSearchRepository noOpRouDepreciationEntryReportItemSearchRepository() {
        return createNoOpSearchRepository(RouDepreciationEntryReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbRecordFileTypeSearchRepository noOpCrbRecordFileTypeSearchRepository() {
        return createNoOpSearchRepository(CrbRecordFileTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SettlementRequisitionSearchRepository noOpSettlementRequisitionSearchRepository() {
        return createNoOpSearchRepository(SettlementRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportingEntitySearchRepository noOpReportingEntitySearchRepository() {
        return createNoOpSearchRepository(ReportingEntitySearchRepository.class);
    }
    @Bean
    @Primary
    public AmortizationPostingReportRequisitionSearchRepository noOpAmortizationPostingReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(AmortizationPostingReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public FinancialDerivativeTypeCodeSearchRepository noOpFinancialDerivativeTypeCodeSearchRepository() {
        return createNoOpSearchRepository(FinancialDerivativeTypeCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public FxTransactionTypeSearchRepository noOpFxTransactionTypeSearchRepository() {
        return createNoOpSearchRepository(FxTransactionTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public ReasonsForBouncedChequeSearchRepository noOpReasonsForBouncedChequeSearchRepository() {
        return createNoOpSearchRepository(ReasonsForBouncedChequeSearchRepository.class);
    }
    @Bean
    @Primary
    public ContractMetadataSearchRepository noOpContractMetadataSearchRepository() {
        return createNoOpSearchRepository(ContractMetadataSearchRepository.class);
    }
    @Bean
    @Primary
    public InstitutionCodeSearchRepository noOpInstitutionCodeSearchRepository() {
        return createNoOpSearchRepository(InstitutionCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public WeeklyCounterfeitHoldingSearchRepository noOpWeeklyCounterfeitHoldingSearchRepository() {
        return createNoOpSearchRepository(WeeklyCounterfeitHoldingSearchRepository.class);
    }
    @Bean
    @Primary
    public WIPListItemSearchRepository noOpWIPListItemSearchRepository() {
        return createNoOpSearchRepository(WIPListItemSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanRepaymentFrequencySearchRepository noOpLoanRepaymentFrequencySearchRepository() {
        return createNoOpSearchRepository(LoanRepaymentFrequencySearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationRequestSearchRepository noOpRouDepreciationRequestSearchRepository() {
        return createNoOpSearchRepository(RouDepreciationRequestSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentAccountReportRequisitionSearchRepository noOpPrepaymentAccountReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(PrepaymentAccountReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public FixedAssetNetBookValueSearchRepository noOpFixedAssetNetBookValueSearchRepository() {
        return createNoOpSearchRepository(FixedAssetNetBookValueSearchRepository.class);
    }
    @Bean
    @Primary
    public CardFraudIncidentCategorySearchRepository noOpCardFraudIncidentCategorySearchRepository() {
        return createNoOpSearchRepository(CardFraudIncidentCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityScheduleItemSearchRepository noOpLeaseLiabilityScheduleItemSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityScheduleItemSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityByAccountReportSearchRepository noOpLeaseLiabilityByAccountReportSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityByAccountReportSearchRepository.class);
    }
    @Bean
    @Primary
    public LeasePeriodSearchRepository noOpLeasePeriodSearchRepository() {
        return createNoOpSearchRepository(LeasePeriodSearchRepository.class);
    }
    @Bean
    @Primary
    public KenyanCurrencyDenominationSearchRepository noOpKenyanCurrencyDenominationSearchRepository() {
        return createNoOpSearchRepository(KenyanCurrencyDenominationSearchRepository.class);
    }
    @Bean
    @Primary
    public DepreciationMethodSearchRepository noOpDepreciationMethodSearchRepository() {
        return createNoOpSearchRepository(DepreciationMethodSearchRepository.class);
    }
    @Bean
    @Primary
    public NbvCompilationReportItemSearchRepository noOpNbvCompilationReportItemSearchRepository() {
        return createNoOpSearchRepository(NbvCompilationReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public AccountOwnershipTypeSearchRepository noOpAccountOwnershipTypeSearchRepository() {
        return createNoOpSearchRepository(AccountOwnershipTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbAccountStatusSearchRepository noOpCrbAccountStatusSearchRepository() {
        return createNoOpSearchRepository(CrbAccountStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public PrepaymentAmortizationSearchRepository noOpPrepaymentAmortizationSearchRepository() {
        return createNoOpSearchRepository(PrepaymentAmortizationSearchRepository.class);
    }
    @Bean
    @Primary
    public CardStatusFlagSearchRepository noOpCardStatusFlagSearchRepository() {
        return createNoOpSearchRepository(CardStatusFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public XlsxReportRequisitionSearchRepository noOpXlsxReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(XlsxReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public RouMonthlyDepreciationReportItemSearchRepository noOpRouMonthlyDepreciationReportItemSearchRepository() {
        return createNoOpSearchRepository(RouMonthlyDepreciationReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public SignedPaymentSearchRepository noOpSignedPaymentSearchRepository() {
        return createNoOpSearchRepository(SignedPaymentSearchRepository.class);
    }
    @Bean
    @Primary
    public MonthlyPrepaymentReportRequisitionSearchRepository noOpMonthlyPrepaymentReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(MonthlyPrepaymentReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetAdditionsReportItemSearchRepository noOpAssetAdditionsReportItemSearchRepository() {
        return createNoOpSearchRepository(AssetAdditionsReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityCompilationSearchRepository noOpLeaseLiabilityCompilationSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityCompilationSearchRepository.class);
    }
    @Bean
    @Primary
    public CardStateSearchRepository noOpCardStateSearchRepository() {
        return createNoOpSearchRepository(CardStateSearchRepository.class);
    }
    @Bean
    @Primary
    public DepartmentTypeSearchRepository noOpDepartmentTypeSearchRepository() {
        return createNoOpSearchRepository(DepartmentTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SystemModuleSearchRepository noOpSystemModuleSearchRepository() {
        return createNoOpSearchRepository(SystemModuleSearchRepository.class);
    }
    @Bean
    @Primary
    public ServiceOutletSearchRepository noOpServiceOutletSearchRepository() {
        return createNoOpSearchRepository(ServiceOutletSearchRepository.class);
    }
    @Bean
    @Primary
    public CardPerformanceFlagSearchRepository noOpCardPerformanceFlagSearchRepository() {
        return createNoOpSearchRepository(CardPerformanceFlagSearchRepository.class);
    }
    @Bean
    @Primary
    public NbvCompilationJobSearchRepository noOpNbvCompilationJobSearchRepository() {
        return createNoOpSearchRepository(NbvCompilationJobSearchRepository.class);
    }
    @Bean
    @Primary
    public SubCountyCodeSearchRepository noOpSubCountyCodeSearchRepository() {
        return createNoOpSearchRepository(SubCountyCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetWarrantySearchRepository noOpAssetWarrantySearchRepository() {
        return createNoOpSearchRepository(AssetWarrantySearchRepository.class);
    }
    @Bean
    @Primary
    public PerformanceOfForeignSubsidiariesSearchRepository noOpPerformanceOfForeignSubsidiariesSearchRepository() {
        return createNoOpSearchRepository(PerformanceOfForeignSubsidiariesSearchRepository.class);
    }
    @Bean
    @Primary
    public AssetCategorySearchRepository noOpAssetCategorySearchRepository() {
        return createNoOpSearchRepository(AssetCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public CustomerTypeSearchRepository noOpCustomerTypeSearchRepository() {
        return createNoOpSearchRepository(CustomerTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbCreditApplicationStatusSearchRepository noOpCrbCreditApplicationStatusSearchRepository() {
        return createNoOpSearchRepository(CrbCreditApplicationStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbFileTransmissionStatusSearchRepository noOpCrbFileTransmissionStatusSearchRepository() {
        return createNoOpSearchRepository(CrbFileTransmissionStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbCreditFacilityTypeSearchRepository noOpCrbCreditFacilityTypeSearchRepository() {
        return createNoOpSearchRepository(CrbCreditFacilityTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public SettlementGroupSearchRepository noOpSettlementGroupSearchRepository() {
        return createNoOpSearchRepository(SettlementGroupSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountCategorySearchRepository noOpTransactionAccountCategorySearchRepository() {
        return createNoOpSearchRepository(TransactionAccountCategorySearchRepository.class);
    }
    @Bean
    @Primary
    public MemoActionSearchRepository noOpMemoActionSearchRepository() {
        return createNoOpSearchRepository(MemoActionSearchRepository.class);
    }
    @Bean
    @Primary
    public TransactionAccountPostingRunSearchRepository noOpTransactionAccountPostingRunSearchRepository() {
        return createNoOpSearchRepository(TransactionAccountPostingRunSearchRepository.class);
    }
    @Bean
    @Primary
    public TerminalFunctionsSearchRepository noOpTerminalFunctionsSearchRepository() {
        return createNoOpSearchRepository(TerminalFunctionsSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseLiabilityScheduleReportItemSearchRepository noOpLeaseLiabilityScheduleReportItemSearchRepository() {
        return createNoOpSearchRepository(LeaseLiabilityScheduleReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public TerminalTypesSearchRepository noOpTerminalTypesSearchRepository() {
        return createNoOpSearchRepository(TerminalTypesSearchRepository.class);
    }
    @Bean
    @Primary
    public StaffCurrentEmploymentStatusSearchRepository noOpStaffCurrentEmploymentStatusSearchRepository() {
        return createNoOpSearchRepository(StaffCurrentEmploymentStatusSearchRepository.class);
    }
    @Bean
    @Primary
    public NbvReportItemSearchRepository noOpNbvReportItemSearchRepository() {
        return createNoOpSearchRepository(NbvReportItemSearchRepository.class);
    }
    @Bean
    @Primary
    public RouDepreciationPostingReportSearchRepository noOpRouDepreciationPostingReportSearchRepository() {
        return createNoOpSearchRepository(RouDepreciationPostingReportSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbAmountCategoryBandSearchRepository noOpCrbAmountCategoryBandSearchRepository() {
        return createNoOpSearchRepository(CrbAmountCategoryBandSearchRepository.class);
    }
    @Bean
    @Primary
    public LeaseRepaymentPeriodSearchRepository noOpLeaseRepaymentPeriodSearchRepository() {
        return createNoOpSearchRepository(LeaseRepaymentPeriodSearchRepository.class);
    }
    @Bean
    @Primary
    public GenderTypeSearchRepository noOpGenderTypeSearchRepository() {
        return createNoOpSearchRepository(GenderTypeSearchRepository.class);
    }
    @Bean
    @Primary
    public IsoCurrencyCodeSearchRepository noOpIsoCurrencyCodeSearchRepository() {
        return createNoOpSearchRepository(IsoCurrencyCodeSearchRepository.class);
    }
    @Bean
    @Primary
    public TALeaseInterestAccrualRuleSearchRepository noOpTALeaseInterestAccrualRuleSearchRepository() {
        return createNoOpSearchRepository(TALeaseInterestAccrualRuleSearchRepository.class);
    }
    @Bean
    @Primary
    public WorkInProgressReportSearchRepository noOpWorkInProgressReportSearchRepository() {
        return createNoOpSearchRepository(WorkInProgressReportSearchRepository.class);
    }
    @Bean
    @Primary
    public ReportRequisitionSearchRepository noOpReportRequisitionSearchRepository() {
        return createNoOpSearchRepository(ReportRequisitionSearchRepository.class);
    }
    @Bean
    @Primary
    public LoanRestructureItemSearchRepository noOpLoanRestructureItemSearchRepository() {
        return createNoOpSearchRepository(LoanRestructureItemSearchRepository.class);
    }
    @Bean
    @Primary
    public UltimateBeneficiaryTypesSearchRepository noOpUltimateBeneficiaryTypesSearchRepository() {
        return createNoOpSearchRepository(UltimateBeneficiaryTypesSearchRepository.class);
    }
    @Bean
    @Primary
    public CrbReportRequestReasonsSearchRepository noOpCrbReportRequestReasonsSearchRepository() {
        return createNoOpSearchRepository(CrbReportRequestReasonsSearchRepository.class);
    }

    /**
     * Creates a no-op implementation of any SearchRepository interface using reflection.
     * This method returns a proxy that implements all methods to return empty results.
     */
    @SuppressWarnings("unchecked")
    private <T> T createNoOpSearchRepository(Class<T> repositoryInterface) {
        return (T) Proxy.newProxyInstance(
            repositoryInterface.getClassLoader(),
            new Class[]{repositoryInterface},
            new NoOpSearchRepositoryInvocationHandler()
        );
    }

    /**
     * InvocationHandler that provides no-op implementations for all SearchRepository methods.
     * Returns appropriate empty results based on the method return type.
     */
    private static class NoOpSearchRepositoryInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log.debug("No-op SearchRepository method called: {} - returning empty result", method.getName());
            
            Class<?> returnType = method.getReturnType();
            
            // Handle Object methods first
            if ("hashCode".equals(method.getName()) && method.getParameterCount() == 0) {
                return System.identityHashCode(proxy);
            } else if ("equals".equals(method.getName()) && method.getParameterCount() == 1) {
                return proxy == args[0];
            } else if ("toString".equals(method.getName()) && method.getParameterCount() == 0) {
                return "NoOpSearchRepository@" + Integer.toHexString(System.identityHashCode(proxy));
            }
            
            // Handle different return types appropriately
            if (returnType == Page.class) {
                Pageable pageable = null;
                if (args != null) {
                    for (Object arg : args) {
                        if (arg instanceof Pageable) {
                            pageable = (Pageable) arg;
                            break;
                        }
                    }
                }
                if (pageable == null) {
                    pageable = Pageable.unpaged();
                }
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            } else if (returnType == Optional.class) {
                return Optional.empty();
            } else if (returnType == Iterable.class || returnType.isAssignableFrom(java.util.List.class)) {
                return Collections.emptyList();
            } else if (returnType == long.class || returnType == Long.class) {
                return 0L;
            } else if (returnType == int.class || returnType == Integer.class) {
                return 0;
            } else if (returnType == boolean.class || returnType == Boolean.class) {
                return false;
            } else if (returnType == void.class || returnType == Void.class) {
                return null;
            } else {
                // For entity types and other objects, return null
                return null;
            }
        }
    }
}
