package braingame.amax.mybase.Models;

public class DatabaseQuery {

    public final static String QUERY_SELECT_MIN_EN = "SELECT * FROM words WHERE priority != \"new\" AND stat <= (SELECT MIN(stat) FROM words) ORDER BY random() LIMIT 1";
    public final static String QUERY_SELECT_NEW_WORD = "SELECT * FROM words WHERE priority = \"new\" ORDER BY random() LIMIT 1";
    public final static String QUERY_GET_TOTAL_WORDS = "SELECT COUNT(_id) FROM words";
    public final static String QUERY_GET_TOTAL_YOUR_WORDS = "SELECT COUNT(_id) FROM words WHERE tag = \"custom\"";
    public final static String QUERY_GET_TOTAL_TODAY_WORDS = "SELECT COUNT(_id) FROM words WHERE dt <= ? AND dt!= \"\"";
    public final static String QUERY_SELECT_MORE_ANSWER = "SELECT en,trans FROM words WHERE ru LIKE ?";
    public final static String QUERY_UPDATE_COUNTUP = "UPDATE words SET countup = countup + 1 WHERE en = \"";
    public final static String QUERY_UPDATE_COUNTDOWN = "UPDATE words SET countup = countup + 1 WHERE en = \"";

    public static final String USERNAME = "name";
    public static final String TABLE_NAME = "words";
    public static final String COLLUMN_NAMES_EN = "en";
    public static final String COLLUMN_NAMES_TRANS = "trans";
    public static final String COLLUMN_NAMES_RU = "ru";
    public static final String COLLUMN_NAMES_PART_OF_SPEECH = "part_of_speech";
    public static final String COLLUMN_NAMES_STAT = "stat";
    public static final String COLLUMN_NAMES_DT = "dt";
    public static final String COLLUMN_NAMES_COUNTUP = "countup";
    public static final String COLLUMN_NAMES_COUNTDOWN = "countdown";
    public static final String COLLUMN_NAMES_PRIORITY = "priority";

}
