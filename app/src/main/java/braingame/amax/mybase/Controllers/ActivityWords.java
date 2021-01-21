package braingame.amax.mybase.Controllers;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.R;

public class ActivityWords extends AppCompatActivity {
    SharedPreferences sPref = null;
    TextView mTextViewMenu, mTextViewExit, mUserName;
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initDataBaseHelper();

        sPref = getSharedPreferences("name", MODE_PRIVATE);
        String userName = sPref.getString("name", "");



        final Dialog selectSessionEnRu = new Dialog(this);
        selectSessionEnRu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectSessionEnRu.setContentView(R.layout.activity_words_enru_select_session);
        Objects.requireNonNull(selectSessionEnRu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button mBtnSelectEasySessionEnRu = selectSessionEnRu.findViewById(R.id.btn_easy_session);
        Button mBtnSelectNormalSessionEnRu = selectSessionEnRu.findViewById(R.id.btn_normal_session);
        Button mBtnSelectHardSessionEnRu = selectSessionEnRu.findViewById(R.id.btn_hard_session);

        final Dialog selectSessionRuEn = new Dialog(this);
        selectSessionRuEn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectSessionRuEn.setContentView(R.layout.activity_words_enru_select_session);
        Objects.requireNonNull(selectSessionEnRu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button mBtnSelectEasySessionRuEn = selectSessionRuEn.findViewById(R.id.btn_easy_session);
        Button mBtnSelectNormalSessionRuEn = selectSessionRuEn.findViewById(R.id.btn_normal_session);
        Button mBtnSelectHardSessionRuEn = selectSessionRuEn.findViewById(R.id.btn_hard_session);

        Button mBtnGoLearnEnRu = findViewById(R.id.btn_go_learnEnRu);
        Button mBtnGoLearnRuEn = findViewById(R.id.btn_go_learnRuEn);
        Button mBtnGoAddWord = findViewById(R.id.btn_go_add_word);

        mBtnGoLearnEnRu.setOnClickListener(v -> selectSessionEnRu.show());
        mBtnGoLearnRuEn.setOnClickListener(v -> selectSessionRuEn.show());
        mBtnGoAddWord.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityWords.this, ActivityWordsAdd.class);
            startActivity(intent);
        });
        mBtnSelectEasySessionEnRu.setOnClickListener(v -> goToActivityWordsEnruEasy());
        mBtnSelectNormalSessionEnRu.setOnClickListener(v -> goToActivityWordsEnruNormal());
        mBtnSelectHardSessionEnRu.setOnClickListener(v -> goToActivityWordsEnruHard());
        mBtnSelectEasySessionRuEn.setOnClickListener(v -> goToActivityWordsRuenEasy());
        mBtnSelectNormalSessionRuEn.setOnClickListener(v -> goToActivityWordsRuenNormal());
        mBtnSelectHardSessionRuEn.setOnClickListener(v -> goToActivityWordsRuenHard());

        pieChart = findViewById(R.id.chart1);

        createChart();

        mTextViewMenu = findViewById(R.id.back_to_menu);
        mTextViewExit = findViewById(R.id.exit);
        mUserName = findViewById(R.id.user_name);
        mUserName.setText(userName);

        mTextViewExit.setOnClickListener(v -> {
            finishAffinity();
        });
        mTextViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityWords.this, ActivityMenu.class);
            startActivity(intent);
        });
    }

    private void createChart() {
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<>();

        AddValuesToPIEENTRY();
        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData(PieEntryLabels, pieDataSet);
        Legend legend = pieChart.getLegend();

        pieChart.setData(pieData);
        pieChart.animateY(2000);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Прогресс");
        pieChart.setDescription("");
        pieChart.setDrawSliceText(false);
        pieChart.setCenterTextSizePixels(40);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        legend.setTextSize(8f);

        pieData.setValueTextSize(10);
        pieChart.getLegend();
    }
    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));
        entries.add(new BarEntry(7f, 4));
    }
    public void AddValuesToPieEntryLabels(){
        PieEntryLabels.add("Перевод EN > RU");
        PieEntryLabels.add("Перевод RU > EN");
        PieEntryLabels.add("Перевод предложений");
        PieEntryLabels.add("Фразовые глаголы");
        PieEntryLabels.add("Грамматика");
    }
    private void goToActivityWordsEnruEasy() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 5);
        startActivity(intent);
    }
    private void goToActivityWordsEnruNormal() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 10);
        startActivity(intent);
    }
    private void goToActivityWordsEnruHard() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 25);
        startActivity(intent);
    }
    private void goToActivityWordsRuenEasy() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 5);
        startActivity(intent);
    }
    private void goToActivityWordsRuenNormal() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 10);
        startActivity(intent);
    }
    private void goToActivityWordsRuenHard() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 25);
        startActivity(intent);
    }
    private void initDataBaseHelper() {
        System.out.println("--- Вызван метод initDataBaseHelper()");
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
    }
}
