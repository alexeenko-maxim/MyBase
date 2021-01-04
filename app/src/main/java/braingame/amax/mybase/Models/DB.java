package braingame.amax.mybase.Models;

public class DB {

    public final static String QUERY_SELECT_MIN_EN = "SELECT * FROM words WHERE priority != \"new\" AND stat <= (SELECT MIN(stat) FROM words) ORDER BY random() LIMIT 1";
    public final static String QUERY_SELECT_NEW_WORD = "SELECT * FROM words WHERE priority = \"new\" ORDER BY random() LIMIT 1";
    public final static String QUERY_UPDATE_COUNTUP = "UPDATE words SET countup = countup + 1 WHERE en = \"";
    public final static String QUERY_UPDATE_COUNTDOWN = "UPDATE words SET countup = countup + 1 WHERE en = \"";




}
