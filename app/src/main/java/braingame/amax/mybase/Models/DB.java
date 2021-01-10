package braingame.amax.mybase.Models;

public class DB {

    public final static String QUERY_SELECT_MIN_EN = "SELECT * FROM words WHERE priority != \"new\" AND stat <= (SELECT MIN(stat) FROM words) ORDER BY random() LIMIT 1";
    public final static String QUERY_SELECT_NEW_WORD = "SELECT * FROM words WHERE priority = \"new\" ORDER BY random() LIMIT 1";
    public final static String QUERY_SELECT_MORE_ANSWER = "SELECT en,trans FROM words WHERE ru LIKE ?";
    public final static String QUERY_UPDATE_COUNTUP = "UPDATE words SET countup = countup + 1 WHERE en = \"";
    public final static String QUERY_UPDATE_COUNTDOWN = "UPDATE words SET countup = countup + 1 WHERE en = \"";

    public static final String USERNAME = "name";


    public static final String DATABASE_NAMES_TABLE = "words";
    public static final String COLLUMN_NAMES_EN = "en";
    public static final String COLLUMN_NAMES_TRANS = "trans";
    public static final String COLLUMN_NAMES_RU = "ru";

}
