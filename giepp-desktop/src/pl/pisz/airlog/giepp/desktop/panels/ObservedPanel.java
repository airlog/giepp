package pl.pisz.airlog.giepp.desktop.panels;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable.TableModel;

/** Panel widoku obserwowanych.
 * @author Rafal
 */
public class ObservedPanel extends RatingsPanel {

    /** Tworzy nowy obiekt.
     * @param tableModel    model tabeli widoku
     * @param buyDialog     okno dialogowe kupna
     * @param sellDialog    okno dialogowe sprzedaży
     */
    public ObservedPanel(TableModel tableModel,
            BuyStockDialog buyDialog,
            SellStockDialog sellDialog) {
        super(tableModel, buyDialog, sellDialog);
    }
    
}
