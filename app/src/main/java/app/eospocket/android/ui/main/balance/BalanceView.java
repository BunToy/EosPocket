package app.eospocket.android.ui.main.balance;

import app.eospocket.android.common.mvp.IView;
import app.eospocket.android.eos.model.coinmarketcap.CoinMarketCap;

public interface BalanceView extends IView {

    void showTokens();

    void setEosBalance(Double balance);

    void setMarketPrice(CoinMarketCap coinMarketCapData);

    void noMarketPrice();

    void noTransferItem();

    void showTransferItem();

    void getTransferError();
}
