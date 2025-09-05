/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.erp.config;

import io.github.erp.repository.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

@TestConfiguration
@Profile("testcontainers")
@ConditionalOnClass(ElasticsearchRestTemplate.class)
public class TestElasticsearchConfiguration {

    @Bean
    @Primary
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        RestHighLevelClient mockClient = Mockito.mock(RestHighLevelClient.class);
        ElasticsearchConverter converter = new MappingElasticsearchConverter(new SimpleElasticsearchMappingContext());
        return new ElasticsearchRestTemplate(mockClient, converter);
    }

    @Bean("elasticsearchTemplate")
    @Primary
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return elasticsearchRestTemplate();
    }


    @Bean
    @Primary
    public AcademicQualificationSearchRepository academicQualificationSearchRepository() {
        return Mockito.mock(AcademicQualificationSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountAttributeMetadataSearchRepository accountAttributeMetadataSearchRepository() {
        return Mockito.mock(AccountAttributeMetadataSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountAttributeSearchRepository accountAttributeSearchRepository() {
        return Mockito.mock(AccountAttributeSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountBalanceSearchRepository accountBalanceSearchRepository() {
        return Mockito.mock(AccountBalanceSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountOwnershipTypeSearchRepository accountOwnershipTypeSearchRepository() {
        return Mockito.mock(AccountOwnershipTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountStatusTypeSearchRepository accountStatusTypeSearchRepository() {
        return Mockito.mock(AccountStatusTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public AccountTypeSearchRepository accountTypeSearchRepository() {
        return Mockito.mock(AccountTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public AcquiringIssuingFlagSearchRepository acquiringIssuingFlagSearchRepository() {
        return Mockito.mock(AcquiringIssuingFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public AgencyNoticeSearchRepository agencyNoticeSearchRepository() {
        return Mockito.mock(AgencyNoticeSearchRepository.class);
    }

    @Bean
    @Primary
    public AgentBankingActivitySearchRepository agentBankingActivitySearchRepository() {
        return Mockito.mock(AgentBankingActivitySearchRepository.class);
    }

    @Bean
    @Primary
    public AgriculturalEnterpriseActivityTypeSearchRepository agriculturalEnterpriseActivityTypeSearchRepository() {
        return Mockito.mock(AgriculturalEnterpriseActivityTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public AlgorithmSearchRepository algorithmSearchRepository() {
        return Mockito.mock(AlgorithmSearchRepository.class);
    }

    @Bean
    @Primary
    public AmortizationPeriodSearchRepository amortizationPeriodSearchRepository() {
        return Mockito.mock(AmortizationPeriodSearchRepository.class);
    }

    @Bean
    @Primary
    public AmortizationPostingReportRequisitionSearchRepository amortizationPostingReportRequisitionSearchRepository() {
        return Mockito.mock(AmortizationPostingReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository() {
        return Mockito.mock(AmortizationPostingReportSearchRepository.class);
    }

    @Bean
    @Primary
    public AmortizationRecurrenceSearchRepository amortizationRecurrenceSearchRepository() {
        return Mockito.mock(AmortizationRecurrenceSearchRepository.class);
    }

    @Bean
    @Primary
    public AmortizationSequenceSearchRepository amortizationSequenceSearchRepository() {
        return Mockito.mock(AmortizationSequenceSearchRepository.class);
    }

    @Bean
    @Primary
    public AnticipatedMaturityPerioodSearchRepository anticipatedMaturityPerioodSearchRepository() {
        return Mockito.mock(AnticipatedMaturityPerioodSearchRepository.class);
    }

    @Bean
    @Primary
    public ApplicationUserSearchRepository applicationUserSearchRepository() {
        return Mockito.mock(ApplicationUserSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetAccessorySearchRepository assetAccessorySearchRepository() {
        return Mockito.mock(AssetAccessorySearchRepository.class);
    }

    @Bean
    @Primary
    public AssetAdditionsReportItemSearchRepository assetAdditionsReportItemSearchRepository() {
        return Mockito.mock(AssetAdditionsReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetAdditionsReportSearchRepository assetAdditionsReportSearchRepository() {
        return Mockito.mock(AssetAdditionsReportSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetCategorySearchRepository assetCategorySearchRepository() {
        return Mockito.mock(AssetCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public AssetDisposalSearchRepository assetDisposalSearchRepository() {
        return Mockito.mock(AssetDisposalSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetDocumentAssignmentSearchRepository assetDocumentAssignmentSearchRepository() {
        return Mockito.mock(AssetDocumentAssignmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository() {
        return Mockito.mock(AssetGeneralAdjustmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetJobSheetAssignmentSearchRepository assetJobSheetAssignmentSearchRepository() {
        return Mockito.mock(AssetJobSheetAssignmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetPaymentInvoiceAssignmentSearchRepository assetPaymentInvoiceAssignmentSearchRepository() {
        return Mockito.mock(AssetPaymentInvoiceAssignmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetPurchaseOrderAssignmentSearchRepository assetPurchaseOrderAssignmentSearchRepository() {
        return Mockito.mock(AssetPurchaseOrderAssignmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetRegistrationSearchRepository assetRegistrationSearchRepository() {
        return Mockito.mock(AssetRegistrationSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetRevaluationSearchRepository assetRevaluationSearchRepository() {
        return Mockito.mock(AssetRevaluationSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetWarrantyAssignmentSearchRepository assetWarrantyAssignmentSearchRepository() {
        return Mockito.mock(AssetWarrantyAssignmentSearchRepository.class);
    }

    @Bean
    @Primary
    public AssetWarrantySearchRepository assetWarrantySearchRepository() {
        return Mockito.mock(AssetWarrantySearchRepository.class);
    }

    @Bean
    @Primary
    public AssetWriteOffSearchRepository assetWriteOffSearchRepository() {
        return Mockito.mock(AssetWriteOffSearchRepository.class);
    }

    @Bean
    @Primary
    public AutonomousReportSearchRepository autonomousReportSearchRepository() {
        return Mockito.mock(AutonomousReportSearchRepository.class);
    }

    @Bean
    @Primary
    public BankBranchCodeSearchRepository bankBranchCodeSearchRepository() {
        return Mockito.mock(BankBranchCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public BankTransactionTypeSearchRepository bankTransactionTypeSearchRepository() {
        return Mockito.mock(BankTransactionTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public BouncedChequeCategoriesSearchRepository bouncedChequeCategoriesSearchRepository() {
        return Mockito.mock(BouncedChequeCategoriesSearchRepository.class);
    }

    @Bean
    @Primary
    public BusinessDocumentSearchRepository businessDocumentSearchRepository() {
        return Mockito.mock(BusinessDocumentSearchRepository.class);
    }

    @Bean
    @Primary
    public BusinessSegmentTypesSearchRepository businessSegmentTypesSearchRepository() {
        return Mockito.mock(BusinessSegmentTypesSearchRepository.class);
    }

    @Bean
    @Primary
    public BusinessStampSearchRepository businessStampSearchRepository() {
        return Mockito.mock(BusinessStampSearchRepository.class);
    }

    @Bean
    @Primary
    public BusinessTeamSearchRepository businessTeamSearchRepository() {
        return Mockito.mock(BusinessTeamSearchRepository.class);
    }

    @Bean
    @Primary
    public CardAcquiringTransactionSearchRepository cardAcquiringTransactionSearchRepository() {
        return Mockito.mock(CardAcquiringTransactionSearchRepository.class);
    }

    @Bean
    @Primary
    public CardBrandTypeSearchRepository cardBrandTypeSearchRepository() {
        return Mockito.mock(CardBrandTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CardCategoryTypeSearchRepository cardCategoryTypeSearchRepository() {
        return Mockito.mock(CardCategoryTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CardChargesSearchRepository cardChargesSearchRepository() {
        return Mockito.mock(CardChargesSearchRepository.class);
    }

    @Bean
    @Primary
    public CardClassTypeSearchRepository cardClassTypeSearchRepository() {
        return Mockito.mock(CardClassTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CardFraudIncidentCategorySearchRepository cardFraudIncidentCategorySearchRepository() {
        return Mockito.mock(CardFraudIncidentCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public CardFraudInformationSearchRepository cardFraudInformationSearchRepository() {
        return Mockito.mock(CardFraudInformationSearchRepository.class);
    }

    @Bean
    @Primary
    public CardIssuerChargesSearchRepository cardIssuerChargesSearchRepository() {
        return Mockito.mock(CardIssuerChargesSearchRepository.class);
    }

    @Bean
    @Primary
    public CardPerformanceFlagSearchRepository cardPerformanceFlagSearchRepository() {
        return Mockito.mock(CardPerformanceFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public CardStateSearchRepository cardStateSearchRepository() {
        return Mockito.mock(CardStateSearchRepository.class);
    }

    @Bean
    @Primary
    public CardStatusFlagSearchRepository cardStatusFlagSearchRepository() {
        return Mockito.mock(CardStatusFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public CardTypesSearchRepository cardTypesSearchRepository() {
        return Mockito.mock(CardTypesSearchRepository.class);
    }

    @Bean
    @Primary
    public CardUsageInformationSearchRepository cardUsageInformationSearchRepository() {
        return Mockito.mock(CardUsageInformationSearchRepository.class);
    }

    @Bean
    @Primary
    public CategoryOfSecuritySearchRepository categoryOfSecuritySearchRepository() {
        return Mockito.mock(CategoryOfSecuritySearchRepository.class);
    }

    @Bean
    @Primary
    public ChannelTypeSearchRepository channelTypeSearchRepository() {
        return Mockito.mock(ChannelTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ChartOfAccountsCodeSearchRepository chartOfAccountsCodeSearchRepository() {
        return Mockito.mock(ChartOfAccountsCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public CollateralInformationSearchRepository collateralInformationSearchRepository() {
        return Mockito.mock(CollateralInformationSearchRepository.class);
    }

    @Bean
    @Primary
    public CollateralTypeSearchRepository collateralTypeSearchRepository() {
        return Mockito.mock(CollateralTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CommitteeTypeSearchRepository committeeTypeSearchRepository() {
        return Mockito.mock(CommitteeTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ContractMetadataSearchRepository contractMetadataSearchRepository() {
        return Mockito.mock(ContractMetadataSearchRepository.class);
    }

    @Bean
    @Primary
    public ContractStatusSearchRepository contractStatusSearchRepository() {
        return Mockito.mock(ContractStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public CounterPartyDealTypeSearchRepository counterPartyDealTypeSearchRepository() {
        return Mockito.mock(CounterPartyDealTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CounterPartyCategorySearchRepository counterPartyCategorySearchRepository() {
        return Mockito.mock(CounterPartyCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public CounterpartyTypeSearchRepository counterpartyTypeSearchRepository() {
        return Mockito.mock(CounterpartyTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CountyCodeSearchRepository countyCodeSearchRepository() {
        return Mockito.mock(CountyCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public CountySubCountyCodeSearchRepository countySubCountyCodeSearchRepository() {
        return Mockito.mock(CountySubCountyCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbAccountHolderTypeSearchRepository crbAccountHolderTypeSearchRepository() {
        return Mockito.mock(CrbAccountHolderTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbAccountStatusSearchRepository crbAccountStatusSearchRepository() {
        return Mockito.mock(CrbAccountStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbAgentServiceTypeSearchRepository crbAgentServiceTypeSearchRepository() {
        return Mockito.mock(CrbAgentServiceTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbAgingBandsSearchRepository crbAgingBandsSearchRepository() {
        return Mockito.mock(CrbAgingBandsSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbAmountCategoryBandSearchRepository crbAmountCategoryBandSearchRepository() {
        return Mockito.mock(CrbAmountCategoryBandSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbComplaintStatusTypeSearchRepository crbComplaintStatusTypeSearchRepository() {
        return Mockito.mock(CrbComplaintStatusTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbComplaintTypeSearchRepository crbComplaintTypeSearchRepository() {
        return Mockito.mock(CrbComplaintTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbCreditApplicationStatusSearchRepository crbCreditApplicationStatusSearchRepository() {
        return Mockito.mock(CrbCreditApplicationStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbCreditFacilityTypeSearchRepository crbCreditFacilityTypeSearchRepository() {
        return Mockito.mock(CrbCreditFacilityTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbCustomerTypeSearchRepository crbCustomerTypeSearchRepository() {
        return Mockito.mock(CrbCustomerTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbDataSubmittingInstitutionsSearchRepository crbDataSubmittingInstitutionsSearchRepository() {
        return Mockito.mock(CrbDataSubmittingInstitutionsSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbFileTransmissionStatusSearchRepository crbFileTransmissionStatusSearchRepository() {
        return Mockito.mock(CrbFileTransmissionStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbGlCodeSearchRepository crbGlCodeSearchRepository() {
        return Mockito.mock(CrbGlCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbNatureOfInformationSearchRepository crbNatureOfInformationSearchRepository() {
        return Mockito.mock(CrbNatureOfInformationSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbProductServiceFeeTypeSearchRepository crbProductServiceFeeTypeSearchRepository() {
        return Mockito.mock(CrbProductServiceFeeTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbRecordFileTypeSearchRepository crbRecordFileTypeSearchRepository() {
        return Mockito.mock(CrbRecordFileTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbReportRequestReasonsSearchRepository crbReportRequestReasonsSearchRepository() {
        return Mockito.mock(CrbReportRequestReasonsSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbReportViewBandSearchRepository crbReportViewBandSearchRepository() {
        return Mockito.mock(CrbReportViewBandSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbSourceOfInformationTypeSearchRepository crbSourceOfInformationTypeSearchRepository() {
        return Mockito.mock(CrbSourceOfInformationTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CrbSubmittingInstitutionCategorySearchRepository crbSubmittingInstitutionCategorySearchRepository() {
        return Mockito.mock(CrbSubmittingInstitutionCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public CrbSubscriptionStatusTypeCodeSearchRepository crbSubscriptionStatusTypeCodeSearchRepository() {
        return Mockito.mock(CrbSubscriptionStatusTypeCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public CreditCardFacilitySearchRepository creditCardFacilitySearchRepository() {
        return Mockito.mock(CreditCardFacilitySearchRepository.class);
    }

    @Bean
    @Primary
    public CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository() {
        return Mockito.mock(CreditCardOwnershipSearchRepository.class);
    }

    @Bean
    @Primary
    public CreditNoteSearchRepository creditNoteSearchRepository() {
        return Mockito.mock(CreditNoteSearchRepository.class);
    }

    @Bean
    @Primary
    public CurrencyAuthenticityFlagSearchRepository currencyAuthenticityFlagSearchRepository() {
        return Mockito.mock(CurrencyAuthenticityFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public CurrencyServiceabilityFlagSearchRepository currencyServiceabilityFlagSearchRepository() {
        return Mockito.mock(CurrencyServiceabilityFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public CustomerComplaintStatusTypeSearchRepository customerComplaintStatusTypeSearchRepository() {
        return Mockito.mock(CustomerComplaintStatusTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CustomerIDDocumentTypeSearchRepository customerIDDocumentTypeSearchRepository() {
        return Mockito.mock(CustomerIDDocumentTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public CustomerTypeSearchRepository customerTypeSearchRepository() {
        return Mockito.mock(CustomerTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public DealerGroupSearchRepository dealerGroupSearchRepository() {
        return Mockito.mock(DealerGroupSearchRepository.class);
    }

    @Bean
    @Primary
    public DealerSearchRepository dealerSearchRepository() {
        return Mockito.mock(DealerSearchRepository.class);
    }

    @Bean
    @Primary
    public DeliveryNoteSearchRepository deliveryNoteSearchRepository() {
        return Mockito.mock(DeliveryNoteSearchRepository.class);
    }

    @Bean
    @Primary
    public DepartmentTypeSearchRepository departmentTypeSearchRepository() {
        return Mockito.mock(DepartmentTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationBatchSequenceSearchRepository depreciationBatchSequenceSearchRepository() {
        return Mockito.mock(DepreciationBatchSequenceSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationEntryReportItemSearchRepository depreciationEntryReportItemSearchRepository() {
        return Mockito.mock(DepreciationEntryReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationEntrySearchRepository depreciationEntrySearchRepository() {
        return Mockito.mock(DepreciationEntrySearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationJobNoticeSearchRepository depreciationJobNoticeSearchRepository() {
        return Mockito.mock(DepreciationJobNoticeSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationJobSearchRepository depreciationJobSearchRepository() {
        return Mockito.mock(DepreciationJobSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationMethodSearchRepository depreciationMethodSearchRepository() {
        return Mockito.mock(DepreciationMethodSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationPeriodSearchRepository depreciationPeriodSearchRepository() {
        return Mockito.mock(DepreciationPeriodSearchRepository.class);
    }

    @Bean
    @Primary
    public DepreciationReportSearchRepository depreciationReportSearchRepository() {
        return Mockito.mock(DepreciationReportSearchRepository.class);
    }

    @Bean
    @Primary
    public DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository() {
        return Mockito.mock(DerivativeSubTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public DerivativeUnderlyingAssetSearchRepository derivativeUnderlyingAssetSearchRepository() {
        return Mockito.mock(DerivativeUnderlyingAssetSearchRepository.class);
    }

    @Bean
    @Primary
    public DetailedLeaseContractSearchRepository detailedLeaseContractSearchRepository() {
        return Mockito.mock(DetailedLeaseContractSearchRepository.class);
    }

    @Bean
    @Primary
    public EmploymentTermsSearchRepository employmentTermsSearchRepository() {
        return Mockito.mock(EmploymentTermsSearchRepository.class);
    }

    @Bean
    @Primary
    public ExcelReportExportSearchRepository excelReportExportSearchRepository() {
        return Mockito.mock(ExcelReportExportSearchRepository.class);
    }

    @Bean
    @Primary
    public ExchangeRateSearchRepository exchangeRateSearchRepository() {
        return Mockito.mock(ExchangeRateSearchRepository.class);
    }

    @Bean
    @Primary
    public ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository() {
        return Mockito.mock(ExecutiveCategoryTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FileTypeSearchRepository fileTypeSearchRepository() {
        return Mockito.mock(FileTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FileUploadSearchRepository fileUploadSearchRepository() {
        return Mockito.mock(FileUploadSearchRepository.class);
    }

    @Bean
    @Primary
    public FinancialDerivativeTypeCodeSearchRepository financialDerivativeTypeCodeSearchRepository() {
        return Mockito.mock(FinancialDerivativeTypeCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public FiscalMonthSearchRepository fiscalMonthSearchRepository() {
        return Mockito.mock(FiscalMonthSearchRepository.class);
    }

    @Bean
    @Primary
    public FiscalQuarterSearchRepository fiscalQuarterSearchRepository() {
        return Mockito.mock(FiscalQuarterSearchRepository.class);
    }

    @Bean
    @Primary
    public FiscalYearSearchRepository fiscalYearSearchRepository() {
        return Mockito.mock(FiscalYearSearchRepository.class);
    }

    @Bean
    @Primary
    public FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository() {
        return Mockito.mock(FixedAssetAcquisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public FixedAssetDepreciationSearchRepository fixedAssetDepreciationSearchRepository() {
        return Mockito.mock(FixedAssetDepreciationSearchRepository.class);
    }

    @Bean
    @Primary
    public FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository() {
        return Mockito.mock(FixedAssetNetBookValueSearchRepository.class);
    }

    @Bean
    @Primary
    public FraudCategoryFlagSearchRepository fraudCategoryFlagSearchRepository() {
        return Mockito.mock(FraudCategoryFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public FraudTypeSearchRepository fraudTypeSearchRepository() {
        return Mockito.mock(FraudTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxCustomerTypeSearchRepository fxCustomerTypeSearchRepository() {
        return Mockito.mock(FxCustomerTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxRateTypeSearchRepository fxRateTypeSearchRepository() {
        return Mockito.mock(FxRateTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxReceiptPurposeTypeSearchRepository fxReceiptPurposeTypeSearchRepository() {
        return Mockito.mock(FxReceiptPurposeTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository() {
        return Mockito.mock(FxTransactionChannelTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxTransactionRateTypeSearchRepository fxTransactionRateTypeSearchRepository() {
        return Mockito.mock(FxTransactionRateTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public FxTransactionTypeSearchRepository fxTransactionTypeSearchRepository() {
        return Mockito.mock(FxTransactionTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public GdiMasterDataIndexSearchRepository gdiMasterDataIndexSearchRepository() {
        return Mockito.mock(GdiMasterDataIndexSearchRepository.class);
    }

    @Bean
    @Primary
    public GdiTransactionDataIndexSearchRepository gdiTransactionDataIndexSearchRepository() {
        return Mockito.mock(GdiTransactionDataIndexSearchRepository.class);
    }

    @Bean
    @Primary
    public GenderTypeSearchRepository genderTypeSearchRepository() {
        return Mockito.mock(GenderTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public GlMappingSearchRepository glMappingSearchRepository() {
        return Mockito.mock(GlMappingSearchRepository.class);
    }

    @Bean
    @Primary
    public InsiderCategoryTypesSearchRepository insiderCategoryTypesSearchRepository() {
        return Mockito.mock(InsiderCategoryTypesSearchRepository.class);
    }

    @Bean
    @Primary
    public InstitutionCodeSearchRepository institutionCodeSearchRepository() {
        return Mockito.mock(InstitutionCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public InstitutionContactDetailsSearchRepository institutionContactDetailsSearchRepository() {
        return Mockito.mock(InstitutionContactDetailsSearchRepository.class);
    }

    @Bean
    @Primary
    public InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository() {
        return Mockito.mock(InstitutionStatusTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public InterbankSectorCodeSearchRepository interbankSectorCodeSearchRepository() {
        return Mockito.mock(InterbankSectorCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public InterestCalcMethodSearchRepository interestCalcMethodSearchRepository() {
        return Mockito.mock(InterestCalcMethodSearchRepository.class);
    }

    @Bean
    @Primary
    public InternalMemoSearchRepository internalMemoSearchRepository() {
        return Mockito.mock(InternalMemoSearchRepository.class);
    }

    @Bean
    @Primary
    public InvoiceSearchRepository invoiceSearchRepository() {
        return Mockito.mock(InvoiceSearchRepository.class);
    }

    @Bean
    @Primary
    public IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository() {
        return Mockito.mock(IsicEconomicActivitySearchRepository.class);
    }

    @Bean
    @Primary
    public IsoCountryCodeSearchRepository isoCountryCodeSearchRepository() {
        return Mockito.mock(IsoCountryCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository() {
        return Mockito.mock(IsoCurrencyCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public IssuersOfSecuritiesSearchRepository issuersOfSecuritiesSearchRepository() {
        return Mockito.mock(IssuersOfSecuritiesSearchRepository.class);
    }

    @Bean
    @Primary
    public JobSheetSearchRepository jobSheetSearchRepository() {
        return Mockito.mock(JobSheetSearchRepository.class);
    }

    @Bean
    @Primary
    public KenyanCurrencyDenominationSearchRepository kenyanCurrencyDenominationSearchRepository() {
        return Mockito.mock(KenyanCurrencyDenominationSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseAmortizationCalculationSearchRepository leaseAmortizationCalculationSearchRepository() {
        return Mockito.mock(LeaseAmortizationCalculationSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseAmortizationScheduleSearchRepository leaseAmortizationScheduleSearchRepository() {
        return Mockito.mock(LeaseAmortizationScheduleSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseContractSearchRepository leaseContractSearchRepository() {
        return Mockito.mock(LeaseContractSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityByAccountReportItemSearchRepository leaseLiabilityByAccountReportItemSearchRepository() {
        return Mockito.mock(LeaseLiabilityByAccountReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityByAccountReportSearchRepository leaseLiabilityByAccountReportSearchRepository() {
        return Mockito.mock(LeaseLiabilityByAccountReportSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityCompilationSearchRepository leaseLiabilityCompilationSearchRepository() {
        return Mockito.mock(LeaseLiabilityCompilationSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityPostingReportItemSearchRepository leaseLiabilityPostingReportItemSearchRepository() {
        return Mockito.mock(LeaseLiabilityPostingReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityPostingReportSearchRepository leaseLiabilityPostingReportSearchRepository() {
        return Mockito.mock(LeaseLiabilityPostingReportSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityReportItemSearchRepository leaseLiabilityReportItemSearchRepository() {
        return Mockito.mock(LeaseLiabilityReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityReportSearchRepository leaseLiabilityReportSearchRepository() {
        return Mockito.mock(LeaseLiabilityReportSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository() {
        return Mockito.mock(LeaseLiabilityScheduleItemSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository() {
        return Mockito.mock(LeaseLiabilityScheduleReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilityScheduleReportSearchRepository leaseLiabilityScheduleReportSearchRepository() {
        return Mockito.mock(LeaseLiabilityScheduleReportSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseLiabilitySearchRepository leaseLiabilitySearchRepository() {
        return Mockito.mock(LeaseLiabilitySearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseModelMetadataSearchRepository leaseModelMetadataSearchRepository() {
        return Mockito.mock(LeaseModelMetadataSearchRepository.class);
    }

    @Bean
    @Primary
    public LeasePaymentSearchRepository leasePaymentSearchRepository() {
        return Mockito.mock(LeasePaymentSearchRepository.class);
    }

    @Bean
    @Primary
    public LeasePeriodSearchRepository leasePeriodSearchRepository() {
        return Mockito.mock(LeasePeriodSearchRepository.class);
    }

    @Bean
    @Primary
    public LeaseRepaymentPeriodSearchRepository leaseRepaymentPeriodSearchRepository() {
        return Mockito.mock(LeaseRepaymentPeriodSearchRepository.class);
    }

    @Bean
    @Primary
    public LegalStatusSearchRepository legalStatusSearchRepository() {
        return Mockito.mock(LegalStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanAccountCategorySearchRepository loanAccountCategorySearchRepository() {
        return Mockito.mock(LoanAccountCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public LoanApplicationStatusSearchRepository loanApplicationStatusSearchRepository() {
        return Mockito.mock(LoanApplicationStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanApplicationTypeSearchRepository loanApplicationTypeSearchRepository() {
        return Mockito.mock(LoanApplicationTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanDeclineReasonSearchRepository loanDeclineReasonSearchRepository() {
        return Mockito.mock(LoanDeclineReasonSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanPerformanceClassificationSearchRepository loanPerformanceClassificationSearchRepository() {
        return Mockito.mock(LoanPerformanceClassificationSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanProductTypeSearchRepository loanProductTypeSearchRepository() {
        return Mockito.mock(LoanProductTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanRepaymentFrequencySearchRepository loanRepaymentFrequencySearchRepository() {
        return Mockito.mock(LoanRepaymentFrequencySearchRepository.class);
    }

    @Bean
    @Primary
    public LoanRestructureFlagSearchRepository loanRestructureFlagSearchRepository() {
        return Mockito.mock(LoanRestructureFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public LoanRestructureItemSearchRepository loanRestructureItemSearchRepository() {
        return Mockito.mock(LoanRestructureItemSearchRepository.class);
    }

    @Bean
    @Primary
    public ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository() {
        return Mockito.mock(ManagementMemberTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public MemoActionSearchRepository memoActionSearchRepository() {
        return Mockito.mock(MemoActionSearchRepository.class);
    }

    @Bean
    @Primary
    public MerchantTypeSearchRepository merchantTypeSearchRepository() {
        return Mockito.mock(MerchantTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public MessageTokenSearchRepository messageTokenSearchRepository() {
        return Mockito.mock(MessageTokenSearchRepository.class);
    }

    @Bean
    @Primary
    public MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository() {
        return Mockito.mock(MfbBranchCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public MonthlyPrepaymentOutstandingReportItemSearchRepository monthlyPrepaymentOutstandingReportItemSearchRepository() {
        return Mockito.mock(MonthlyPrepaymentOutstandingReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public MonthlyPrepaymentReportRequisitionSearchRepository monthlyPrepaymentReportRequisitionSearchRepository() {
        return Mockito.mock(MonthlyPrepaymentReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public MoratoriumItemSearchRepository moratoriumItemSearchRepository() {
        return Mockito.mock(MoratoriumItemSearchRepository.class);
    }

    @Bean
    @Primary
    public NatureOfCustomerComplaintsSearchRepository natureOfCustomerComplaintsSearchRepository() {
        return Mockito.mock(NatureOfCustomerComplaintsSearchRepository.class);
    }

    @Bean
    @Primary
    public NbvCompilationBatchSearchRepository nbvCompilationBatchSearchRepository() {
        return Mockito.mock(NbvCompilationBatchSearchRepository.class);
    }

    @Bean
    @Primary
    public NbvCompilationJobSearchRepository nbvCompilationJobSearchRepository() {
        return Mockito.mock(NbvCompilationJobSearchRepository.class);
    }

    @Bean
    @Primary
    public NbvReportSearchRepository nbvReportSearchRepository() {
        return Mockito.mock(NbvReportSearchRepository.class);
    }

    @Bean
    @Primary
    public NetBookValueEntrySearchRepository netBookValueEntrySearchRepository() {
        return Mockito.mock(NetBookValueEntrySearchRepository.class);
    }

    @Bean
    @Primary
    public OutletStatusSearchRepository outletStatusSearchRepository() {
        return Mockito.mock(OutletStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public OutletTypeSearchRepository outletTypeSearchRepository() {
        return Mockito.mock(OutletTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ParticularsOfOutletSearchRepository particularsOfOutletSearchRepository() {
        return Mockito.mock(ParticularsOfOutletSearchRepository.class);
    }

    @Bean
    @Primary
    public PartyRelationTypeSearchRepository partyRelationTypeSearchRepository() {
        return Mockito.mock(PartyRelationTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentCalculationSearchRepository paymentCalculationSearchRepository() {
        return Mockito.mock(PaymentCalculationSearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentCategorySearchRepository paymentCategorySearchRepository() {
        return Mockito.mock(PaymentCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentInvoiceSearchRepository paymentInvoiceSearchRepository() {
        return Mockito.mock(PaymentInvoiceSearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentLabelSearchRepository paymentLabelSearchRepository() {
        return Mockito.mock(PaymentLabelSearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentRequisitionSearchRepository paymentRequisitionSearchRepository() {
        return Mockito.mock(PaymentRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public PaymentSearchRepository paymentSearchRepository() {
        return Mockito.mock(PaymentSearchRepository.class);
    }

    @Bean
    @Primary
    public PdfReportRequisitionSearchRepository pdfReportRequisitionSearchRepository() {
        return Mockito.mock(PdfReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public PerformanceOfForeignSubsidiariesSearchRepository performanceOfForeignSubsidiariesSearchRepository() {
        return Mockito.mock(PerformanceOfForeignSubsidiariesSearchRepository.class);
    }

    @Bean
    @Primary
    public PlaceholderSearchRepository placeholderSearchRepository() {
        return Mockito.mock(PlaceholderSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentAccountReportRequisitionSearchRepository prepaymentAccountReportRequisitionSearchRepository() {
        return Mockito.mock(PrepaymentAccountReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository() {
        return Mockito.mock(PrepaymentAccountReportSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentAccountSearchRepository prepaymentAccountSearchRepository() {
        return Mockito.mock(PrepaymentAccountSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository() {
        return Mockito.mock(PrepaymentAmortizationSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentByAccountReportRequisitionSearchRepository prepaymentByAccountReportRequisitionSearchRepository() {
        return Mockito.mock(PrepaymentByAccountReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository() {
        return Mockito.mock(PrepaymentCompilationRequestSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentMappingSearchRepository prepaymentMappingSearchRepository() {
        return Mockito.mock(PrepaymentMappingSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentMarshallingSearchRepository prepaymentMarshallingSearchRepository() {
        return Mockito.mock(PrepaymentMarshallingSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository() {
        return Mockito.mock(PrepaymentReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public PrepaymentReportSearchRepository prepaymentReportSearchRepository() {
        return Mockito.mock(PrepaymentReportSearchRepository.class);
    }

    @Bean
    @Primary
    public ProcessStatusSearchRepository processStatusSearchRepository() {
        return Mockito.mock(ProcessStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public ProductTypeSearchRepository productTypeSearchRepository() {
        return Mockito.mock(ProductTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ProfessionalQualificationSearchRepository professionalQualificationSearchRepository() {
        return Mockito.mock(ProfessionalQualificationSearchRepository.class);
    }

    @Bean
    @Primary
    public PurchaseOrderSearchRepository purchaseOrderSearchRepository() {
        return Mockito.mock(PurchaseOrderSearchRepository.class);
    }

    @Bean
    @Primary
    public QuestionBaseSearchRepository questionBaseSearchRepository() {
        return Mockito.mock(QuestionBaseSearchRepository.class);
    }

    @Bean
    @Primary
    public ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository() {
        return Mockito.mock(ReasonsForBouncedChequeSearchRepository.class);
    }

    @Bean
    @Primary
    public RelatedPartyRelationshipSearchRepository relatedPartyRelationshipSearchRepository() {
        return Mockito.mock(RelatedPartyRelationshipSearchRepository.class);
    }

    @Bean
    @Primary
    public RemittanceFlagSearchRepository remittanceFlagSearchRepository() {
        return Mockito.mock(RemittanceFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public ReportContentTypeSearchRepository reportContentTypeSearchRepository() {
        return Mockito.mock(ReportContentTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ReportDesignSearchRepository reportDesignSearchRepository() {
        return Mockito.mock(ReportDesignSearchRepository.class);
    }

    @Bean
    @Primary
    public ReportingEntitySearchRepository reportingEntitySearchRepository() {
        return Mockito.mock(ReportingEntitySearchRepository.class);
    }

    @Bean
    @Primary
    public ReportRequisitionSearchRepository reportRequisitionSearchRepository() {
        return Mockito.mock(ReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public ReportStatusSearchRepository reportStatusSearchRepository() {
        return Mockito.mock(ReportStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public ReportTemplateSearchRepository reportTemplateSearchRepository() {
        return Mockito.mock(ReportTemplateSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAccountBalanceReportItemSearchRepository rouAccountBalanceReportItemSearchRepository() {
        return Mockito.mock(RouAccountBalanceReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAccountBalanceReportSearchRepository rouAccountBalanceReportSearchRepository() {
        return Mockito.mock(RouAccountBalanceReportSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAssetListReportItemSearchRepository rouAssetListReportItemSearchRepository() {
        return Mockito.mock(RouAssetListReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAssetListReportSearchRepository rouAssetListReportSearchRepository() {
        return Mockito.mock(RouAssetListReportSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository() {
        return Mockito.mock(RouAssetNBVReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouAssetNBVReportSearchRepository rouAssetNBVReportSearchRepository() {
        return Mockito.mock(RouAssetNBVReportSearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository() {
        return Mockito.mock(RouDepreciationEntryReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationEntryReportSearchRepository rouDepreciationEntryReportSearchRepository() {
        return Mockito.mock(RouDepreciationEntryReportSearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository() {
        return Mockito.mock(RouDepreciationEntrySearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository() {
        return Mockito.mock(RouDepreciationPostingReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationPostingReportSearchRepository rouDepreciationPostingReportSearchRepository() {
        return Mockito.mock(RouDepreciationPostingReportSearchRepository.class);
    }

    @Bean
    @Primary
    public RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository() {
        return Mockito.mock(RouDepreciationRequestSearchRepository.class);
    }

    @Bean
    @Primary
    public RouInitialDirectCostSearchRepository rouInitialDirectCostSearchRepository() {
        return Mockito.mock(RouInitialDirectCostSearchRepository.class);
    }

    @Bean
    @Primary
    public RouModelMetadataSearchRepository rouModelMetadataSearchRepository() {
        return Mockito.mock(RouModelMetadataSearchRepository.class);
    }

    @Bean
    @Primary
    public RouMonthlyDepreciationReportItemSearchRepository rouMonthlyDepreciationReportItemSearchRepository() {
        return Mockito.mock(RouMonthlyDepreciationReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public RouMonthlyDepreciationReportSearchRepository rouMonthlyDepreciationReportSearchRepository() {
        return Mockito.mock(RouMonthlyDepreciationReportSearchRepository.class);
    }

    @Bean
    @Primary
    public SecurityClassificationTypeSearchRepository securityClassificationTypeSearchRepository() {
        return Mockito.mock(SecurityClassificationTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public SecurityClearanceSearchRepository securityClearanceSearchRepository() {
        return Mockito.mock(SecurityClearanceSearchRepository.class);
    }

    @Bean
    @Primary
    public SecurityTenureSearchRepository securityTenureSearchRepository() {
        return Mockito.mock(SecurityTenureSearchRepository.class);
    }

    @Bean
    @Primary
    public SecurityTypeSearchRepository securityTypeSearchRepository() {
        return Mockito.mock(SecurityTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public ServiceOutletSearchRepository serviceOutletSearchRepository() {
        return Mockito.mock(ServiceOutletSearchRepository.class);
    }

    @Bean
    @Primary
    public SettlementCurrencySearchRepository settlementCurrencySearchRepository() {
        return Mockito.mock(SettlementCurrencySearchRepository.class);
    }

    @Bean
    @Primary
    public SettlementGroupSearchRepository settlementGroupSearchRepository() {
        return Mockito.mock(SettlementGroupSearchRepository.class);
    }

    @Bean
    @Primary
    public SettlementRequisitionSearchRepository settlementRequisitionSearchRepository() {
        return Mockito.mock(SettlementRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public SettlementSearchRepository settlementSearchRepository() {
        return Mockito.mock(SettlementSearchRepository.class);
    }

    @Bean
    @Primary
    public ShareHoldingFlagSearchRepository shareHoldingFlagSearchRepository() {
        return Mockito.mock(ShareHoldingFlagSearchRepository.class);
    }

    @Bean
    @Primary
    public ShareholderTypeSearchRepository shareholderTypeSearchRepository() {
        return Mockito.mock(ShareholderTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public SignedPaymentSearchRepository signedPaymentSearchRepository() {
        return Mockito.mock(SignedPaymentSearchRepository.class);
    }

    @Bean
    @Primary
    public SnaSectorCodeSearchRepository snaSectorCodeSearchRepository() {
        return Mockito.mock(SnaSectorCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public SourceRemittancePurposeTypeSearchRepository sourceRemittancePurposeTypeSearchRepository() {
        return Mockito.mock(SourceRemittancePurposeTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public SourcesOfFundsTypeCodeSearchRepository sourcesOfFundsTypeCodeSearchRepository() {
        return Mockito.mock(SourcesOfFundsTypeCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public StaffCurrentEmploymentStatusSearchRepository staffCurrentEmploymentStatusSearchRepository() {
        return Mockito.mock(StaffCurrentEmploymentStatusSearchRepository.class);
    }

    @Bean
    @Primary
    public StaffRoleTypeSearchRepository staffRoleTypeSearchRepository() {
        return Mockito.mock(StaffRoleTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public SubCountyCodeSearchRepository subCountyCodeSearchRepository() {
        return Mockito.mock(SubCountyCodeSearchRepository.class);
    }

    @Bean
    @Primary
    public SystemContentTypeSearchRepository systemContentTypeSearchRepository() {
        return Mockito.mock(SystemContentTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public SystemModuleSearchRepository systemModuleSearchRepository() {
        return Mockito.mock(SystemModuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TAAmortizationRuleSearchRepository tAAmortizationRuleSearchRepository() {
        return Mockito.mock(TAAmortizationRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TACompilationRequestSearchRepository tACompilationRequestSearchRepository() {
        return Mockito.mock(TACompilationRequestSearchRepository.class);
    }

    @Bean
    @Primary
    public TAInterestPaidTransferRuleSearchRepository tAInterestPaidTransferRuleSearchRepository() {
        return Mockito.mock(TAInterestPaidTransferRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TALeaseInterestAccrualRuleSearchRepository tALeaseInterestAccrualRuleSearchRepository() {
        return Mockito.mock(TALeaseInterestAccrualRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TALeaseRecognitionRuleSearchRepository tALeaseRecognitionRuleSearchRepository() {
        return Mockito.mock(TALeaseRecognitionRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TALeaseRepaymentRuleSearchRepository tALeaseRepaymentRuleSearchRepository() {
        return Mockito.mock(TALeaseRepaymentRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TARecognitionROURuleSearchRepository tARecognitionROURuleSearchRepository() {
        return Mockito.mock(TARecognitionROURuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TaxReferenceSearchRepository taxReferenceSearchRepository() {
        return Mockito.mock(TaxReferenceSearchRepository.class);
    }

    @Bean
    @Primary
    public TaxRuleSearchRepository taxRuleSearchRepository() {
        return Mockito.mock(TaxRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TerminalFunctionsSearchRepository terminalFunctionsSearchRepository() {
        return Mockito.mock(TerminalFunctionsSearchRepository.class);
    }

    @Bean
    @Primary
    public TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository() {
        return Mockito.mock(TerminalsAndPOSSearchRepository.class);
    }

    @Bean
    @Primary
    public TerminalTypesSearchRepository terminalTypesSearchRepository() {
        return Mockito.mock(TerminalTypesSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountCategorySearchRepository transactionAccountCategorySearchRepository() {
        return Mockito.mock(TransactionAccountCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountLedgerSearchRepository transactionAccountLedgerSearchRepository() {
        return Mockito.mock(TransactionAccountLedgerSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountPostingProcessTypeSearchRepository transactionAccountPostingProcessTypeSearchRepository() {
        return Mockito.mock(TransactionAccountPostingProcessTypeSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountPostingRuleSearchRepository transactionAccountPostingRuleSearchRepository() {
        return Mockito.mock(TransactionAccountPostingRuleSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountPostingRunSearchRepository transactionAccountPostingRunSearchRepository() {
        return Mockito.mock(TransactionAccountPostingRunSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountReportItemSearchRepository transactionAccountReportItemSearchRepository() {
        return Mockito.mock(TransactionAccountReportItemSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionAccountSearchRepository transactionAccountSearchRepository() {
        return Mockito.mock(TransactionAccountSearchRepository.class);
    }

    @Bean
    @Primary
    public TransactionDetailsSearchRepository transactionDetailsSearchRepository() {
        return Mockito.mock(TransactionDetailsSearchRepository.class);
    }

    @Bean
    @Primary
    public UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository() {
        return Mockito.mock(UltimateBeneficiaryCategorySearchRepository.class);
    }

    @Bean
    @Primary
    public UltimateBeneficiaryTypesSearchRepository ultimateBeneficiaryTypesSearchRepository() {
        return Mockito.mock(UltimateBeneficiaryTypesSearchRepository.class);
    }

    @Bean
    @Primary
    public UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository() {
        return Mockito.mock(UniversallyUniqueMappingSearchRepository.class);
    }

    @Bean
    @Primary
    public WeeklyCashHoldingSearchRepository weeklyCashHoldingSearchRepository() {
        return Mockito.mock(WeeklyCashHoldingSearchRepository.class);
    }

    @Bean
    @Primary
    public WeeklyCounterfeitHoldingSearchRepository weeklyCounterfeitHoldingSearchRepository() {
        return Mockito.mock(WeeklyCounterfeitHoldingSearchRepository.class);
    }

    @Bean
    @Primary
    public WIPListItemSearchRepository wIPListItemSearchRepository() {
        return Mockito.mock(WIPListItemSearchRepository.class);
    }

    @Bean
    @Primary
    public WIPListReportSearchRepository wIPListReportSearchRepository() {
        return Mockito.mock(WIPListReportSearchRepository.class);
    }

    @Bean
    @Primary
    public WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository() {
        return Mockito.mock(WIPTransferListItemSearchRepository.class);
    }

    @Bean
    @Primary
    public WIPTransferListReportSearchRepository wIPTransferListReportSearchRepository() {
        return Mockito.mock(WIPTransferListReportSearchRepository.class);
    }

    @Bean
    @Primary
    public WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository() {
        return Mockito.mock(WorkInProgressOutstandingReportRequisitionSearchRepository.class);
    }

    @Bean
    @Primary
    public WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository() {
        return Mockito.mock(WorkInProgressOutstandingReportSearchRepository.class);
    }

    @Bean
    @Primary
    public WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository() {
        return Mockito.mock(WorkInProgressRegistrationSearchRepository.class);
    }

    @Bean
    @Primary
    public WorkInProgressTransferSearchRepository workInProgressTransferSearchRepository() {
        return Mockito.mock(WorkInProgressTransferSearchRepository.class);
    }

    @Bean
    @Primary
    public WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository() {
        return Mockito.mock(WorkProjectRegisterSearchRepository.class);
    }

    @Bean
    @Primary
    public XlsxReportRequisitionSearchRepository xlsxReportRequisitionSearchRepository() {
        return Mockito.mock(XlsxReportRequisitionSearchRepository.class);
    }
}
