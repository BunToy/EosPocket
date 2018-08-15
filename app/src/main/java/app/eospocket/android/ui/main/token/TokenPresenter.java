package app.eospocket.android.ui.main.token;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.eospocket.android.common.Constants;
import app.eospocket.android.common.mvp.BasePresenter;
import app.eospocket.android.eos.EosManager;
import app.eospocket.android.eos.model.action.Action;
import app.eospocket.android.eos.model.action.ActionList;
import app.eospocket.android.eos.request.AccountRequest;
import app.eospocket.android.eos.request.ActionRequest;
import app.eospocket.android.wallet.PocketAppManager;
import app.eospocket.android.wallet.db.model.EosAccountTokenModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokenPresenter extends BasePresenter<TokenView> {

    private EosManager mEosManager;

    private PocketAppManager mPocketAppManager;

    public TokenPresenter(TokenView view, EosManager eosManager, PocketAppManager pocketAppManager) {
        super(view);
        this.mEosManager = eosManager;
        this.mPocketAppManager = pocketAppManager;
    }

    @Override
    public void onCreate() {

    }

    private void getTokens(@NonNull String accountName) {
    Single.fromCallable(() -> {
            ActionRequest request = new ActionRequest();
            request.accountName = accountName;
            request.pos = -1;
            request.offset = -1;
            return request;
        })
        .flatMap((request) -> mEosManager.getAccountActions(request))
        .flatMap(actionList -> {
            return Single.fromCallable(() -> {
                if (!actionList.actions.isEmpty()) {
                    long totalActions = actionList.actions.get(0).accountActionSeq;

                    int totalPage = (int) (totalActions / Constants.ACTIONS_PER_PAGE);

                    if (totalActions % Constants.ACTIONS_PER_PAGE != 0) {
                        totalPage++;
                    }

                    for (int i = 0; i < totalPage; i++) {
                        long pos = i * Constants.ACTIONS_PER_PAGE;
                        long offset = Constants.ACTIONS_PER_PAGE - 1;

                        ActionRequest request = new ActionRequest();
                        request.accountName = accountName;
                        request.pos = pos;
                        request.offset = offset;

                        ActionList actions = mEosManager.getAccountActions(request).blockingGet();

                        List<EosAccountTokenModel> eosAccountTokenModels = new ArrayList<>();
                        Map<String, Boolean> tokens = new HashMap<>();

                        for (Action action : actions.actions) {
                            if ("transfer".equalsIgnoreCase(action.actionTrace.act.name)
                                    && accountName.equalsIgnoreCase(action.actionTrace.act.data.to)) {
                                if (!tokens.containsKey(action.actionTrace.act.account)) {
                                    tokens.put(action.actionTrace.act.account, true);

                                    EosAccountTokenModel eosAccountTokenModel = new EosAccountTokenModel();
                                    eosAccountTokenModel.setName(action.actionTrace.act.data.quantity.split(" ")[1]);
                                    eosAccountTokenModel.setSymbol(eosAccountTokenModel.getName());
                                    eosAccountTokenModel.setContract(action.actionTrace.act.account);

                                    eosAccountTokenModels.add(eosAccountTokenModel);
                                }
                            }
                        }

                        return eosAccountTokenModels;
                    }
                }

                return new ArrayList<EosAccountTokenModel>();
            });
        })
        .flatMap(eosAccountTokenModels -> {
            return Single.fromCallable(() -> {
                if (!eosAccountTokenModels.isEmpty()) {
                    mPocketAppManager.insertAllTokens(eosAccountTokenModels);
                }
                return true;
            });
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            mView.showTokens();
        }, e -> {
            e.printStackTrace();
        });
    }

    public void getAccount(@NonNull String accountName) {
        Single.fromCallable(() -> {
            AccountRequest request = new AccountRequest();
            request.accountName = accountName;
            return request;
        })
        .flatMap((request) -> mEosManager.findAccountByName(request))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(account -> {

        }, e -> {
            e.printStackTrace();
        });
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
}