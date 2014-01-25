package pl.pisz.airlog.giepp.desktop.util;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.Comparator;

import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;

import pl.pisz.airlog.giepp.game.Game;

/** Zawiera rzadko wykorzystywane metody.
 * @author Rafal
 */
public class HelperTools {

    private static final String APPLICATION_DIR_NAME = ".giepp";
    
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");
    
    private static File getApplicationPath() {
        String home = HelperTools.getUserHomePath();
        String sep = HelperTools.getFileSeparator();
        
        return new File(HelperTools.getUserHomePath() + sep + APPLICATION_DIR_NAME);
    }
    
    /** Korzystając z właściwości Java określa ścieżke do katalogu domowego użytkownika.
     * @return  ścieżka do katalogu domowego użytkownika
     * @see System#getProperty(String)
     */
    protected static String getUserHomePath() {
        return System.getProperty("user.home");
    }
    
    /** Korzystając z właściwości Java określa separator plików wykorzystywany w systemie.
     * @return  separator
     * @see System#getProperty(String)
     */
    protected static String getFileSeparator() {
        return System.getProperty("file.separator");
    }
    
    /** Rozpoczyna nową grę i tworzy odpowiednie pliki przechowujące stan gry.
     * Ta metoda powinna być wywoływana jedynie podczas uruchomienia i nie należy wykorzystywać
     * jej do restartowania stanu gry (od tego jest dedykowana metoda).
     * 
     * @return  nowy obiekt {@link Game}
     */
    public static Game newGame() {
        File dir = HelperTools.getApplicationPath();
        if (!dir.exists()) dir.mkdirs();
        
        LocalStorage localStorage = null;        
        try {
            File files[] = new File[] {
                    new File(dir, "owned.xml"),
                    new File(dir, "archive.xml"),
                    new File(dir, "observed.xml"),
                    new File(dir, "stats.xml"),
                };
            for (File file : files) {
                if (!file.exists()) file.createNewFile();
            }
            
            localStorage = LocalStorage.newInstance(files[0], files[1], files[2], files[3]);
        } catch (IOException e) {
            System.err.println(e);
        }
        return new Game(new GPWDataSource(), new GPWDataParser(), localStorage);
    }
    
    /** Odwraca działanie danego {@link Comparator}.
     * @param comparator    komparator do odwrócenia
     * @return  nowy komparator działający odwrotnie
     */
    public static <T> Comparator<T> getReverseComparator(Comparator<T> comparator) {
        final Comparator<T> comp = comparator;
        return new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return -comp.compare(a, b); 
            }
        };
    }
    
    /** Format cen wykorzystywany w aplikacji.
     * @return  obowiązujący format cen
     */
    public static DecimalFormat getPriceFormat() {
        return PRICE_FORMAT;
    }
    
}
