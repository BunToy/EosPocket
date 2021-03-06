package app.eospocket.android.ui.importaccount;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.eospocket.android.common.rxjava.RxJavaSchedulers;
import app.eospocket.android.eos.EosManager;
import app.eospocket.android.security.keystore.KeyStore;
import app.eospocket.android.utils.EncryptUtil;
import app.eospocket.android.wallet.PocketAppManager;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

public class ImportAccountPresenterTest {

    private static final String TESTABLE_PRIVATE_KEY = "5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3";

    @Mock
    private ImportAccountView view;

    @Mock
    private EosManager eosManager;

    @Mock
    private EncryptUtil encryptUtil;

    @Mock
    private KeyStore keyStore;

    @Mock
    private PocketAppManager pocketAppManager;

    private TestScheduler testScheduler;

    private ImportAccountPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testScheduler = new TestScheduler();

        presenter = new ImportAccountPresenter(view, eosManager, encryptUtil, keyStore,
                pocketAppManager, new RxJavaSchedulers() {
            @Override
            public Scheduler getNewThread() {
                return testScheduler;
            }

            @Override
            public Scheduler getComputation() {
                return testScheduler;
            }

            @Override
            public Scheduler getIo() {
                return testScheduler;
            }

            @Override
            public Scheduler getMainThread() {
                return testScheduler;
            }
        });
    }

    @Test
    public void findAccountName() {
    }

    @Test
    public void importAccount() {
    }

    @Test
    public void importAccount1() {
    }
}