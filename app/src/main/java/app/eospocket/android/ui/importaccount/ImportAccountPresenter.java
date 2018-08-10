package app.eospocket.android.ui.importaccount;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Calendar;

import app.eospocket.android.common.Constants;
import app.eospocket.android.common.mvp.BasePresenter;
import app.eospocket.android.eos.EosManager;
import app.eospocket.android.eos.request.AccountRequest;
import app.eospocket.android.eos.request.KeyAccountsRequest;
import app.eospocket.android.security.keystore.KeyStore;
import app.eospocket.android.utils.EncryptUtil;
import app.eospocket.android.wallet.db.model.EosAccountModel;
import app.eospocket.android.wallet.repository.EosAccountRepository;
import io.mithrilcoin.eos.crypto.ec.EosPrivateKey;
import io.mithrilcoin.eos.crypto.ec.EosPublicKey;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportAccountPresenter extends BasePresenter<ImportAccountView> {

    private EosManager mEosManager;

    private EncryptUtil mEncryptUtil;

    private KeyStore mKeyStore;

    private EosAccountRepository mEosAccountRepository;

    public ImportAccountPresenter(ImportAccountView view, EosManager eosManager, EncryptUtil encryptUtil,
            KeyStore keyStore, EosAccountRepository eosAccountRepository) {
        super(view);

        this.mEosManager = eosManager;
        this.mEncryptUtil = encryptUtil;
        this.mKeyStore = keyStore;
        this.mEosAccountRepository = eosAccountRepository;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void findAccount(@Nullable String privateKey) {
        Single.fromCallable(() -> {
            EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
            EosPublicKey eosPublicKey = eosPrivateKey.getPublicKey();
            return eosPublicKey.toString();
        })
        .flatMap(publicKey -> {
            KeyAccountsRequest request = new KeyAccountsRequest();
            request.publicKey = publicKey;
            return mEosManager.findAccountByPublicKey(request);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result != null && !result.accounts.isEmpty()) {
                mView.getAccount(result.accounts.get(0));
            }
        }, e -> {
            e.printStackTrace();
        });
    }

    public void findAccountName(String accountName) {
        Single.fromCallable(() -> {
            AccountRequest request = new AccountRequest();
            request.accountName = accountName;

            return request;
        })
        .flatMap(request -> mEosManager.findAccountByName(request))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            mView.foundAccount(result);
        }, e -> {
            e.printStackTrace();
            mView.noAccount();
        });
    }

    public void importAccount(@NonNull String accountName) {
        importAccount(accountName, null, null);
    }

    public void importAccount(@NonNull String accountName, @Nullable String privateKey, @Nullable String password) {
        mEosAccountRepository.findAccount(accountName)
        .map(accounts -> {
            if (accounts.isEmpty()) {
                EosAccountModel eosAccountModel = EosAccountModel
                        .builder()
                        .name(accountName)
                        .created(Calendar.getInstance().getTime())
                        .build();
                if (!TextUtils.isEmpty(privateKey)) {
                    if (!TextUtils.isEmpty(password)) {
                        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
                        EosPublicKey eosPublicKey = eosPrivateKey.getPublicKey();

                        String enc = mEncryptUtil.getEncryptString(privateKey, password);
                        String encPrivKey = mKeyStore.encryptString(enc, Constants.KEYSTORE_PRIV_KEY_ALIAS);
                        eosAccountModel.setPrivateKey(encPrivKey);
                        eosAccountModel.setPublicKey(eosPublicKey.toString());
                    } else {
                        throw new IllegalArgumentException();
                    }
                }

                mEosAccountRepository.insert(eosAccountModel);

                return true;
            }

            return false;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result) {
                mView.successImport();
            } else {
                mView.existAccount();
            }
        }, e -> {
            e.printStackTrace();
        });
    }
}