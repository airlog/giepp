package pl.pisz.airlog.giepp.desktop.panels;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable.TableModel;

public class ObservedPanel
        extends RatingsPanel {

    public ObservedPanel(TableModel tableModel,
            BuyStockDialog buyDialog,
            SellStockDialog sellDialog) {
        super(tableModel, buyDialog, sellDialog);
    }
    
}
