package io.github.erp.erp.startUp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.erp.startUp.index.engine_v1.AbstractStartupRegisteredIndexService;
import io.github.erp.erp.startUp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.TransactionAccountLedgerSearchRepository;
import io.github.erp.service.TransactionAccountLedgerService;
import io.github.erp.service.mapper.TransactionAccountLedgerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class TransactionAccountLedgerIndexingService extends AbstractStartupRegisteredIndexService {

    private static final String TAG = "AccountLedgerIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final TransactionAccountLedgerService service;
    private final TransactionAccountLedgerMapper mapper;
    private final TransactionAccountLedgerSearchRepository searchRepository;

    public TransactionAccountLedgerIndexingService(IndexProperties indexProperties, TransactionAccountLedgerService service, TransactionAccountLedgerMapper mapper, TransactionAccountLedgerSearchRepository searchRepository) {
        super(indexProperties, indexProperties.getRebuild());
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    /**
     * This method is called to register a service which is to respond to the callback
     */
    @Override
    public void register() {

        log.info("Registering {} Service", TAG);

        IndexingServiceChainSingleton.getInstance().registerService(this);
    }

    private static final Lock reindexLock = new ReentrantLock();

    @Async
    public void index() {
        try {
            reindexLock.lockInterruptibly();

            indexerSequence();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reindexLock.unlock();
        }
    }

    private void indexerSequence() {
        log.info("Initiating {} build sequence", TAG);
        long startup = System.currentTimeMillis();
        this.searchRepository.saveAll(
            service.findAll(Pageable.unpaged())
                .stream()
                .map(mapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
        log.trace("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
    }

    @Override
    public void tearDown() {

        if (reindexLock.tryLock()) {
            this.searchRepository.deleteAll();
        } else {
            log.trace("{} ReIndexer: Concurrent reindexing attempt", TAG);
        }
    }
}
