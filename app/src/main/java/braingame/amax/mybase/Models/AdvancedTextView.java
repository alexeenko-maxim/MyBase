package braingame.amax.mybase.Models;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

public class AdvancedTextView extends androidx.appcompat.widget.AppCompatTextView {
    // Максимальное значение шкалы
    private int mMaxValue = 100;

    // Конструкторы
    public AdvancedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AdvancedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedTextView(Context context) {
        super(context);
    }

    // Установка максимального значения
    public void setMaxValue(int maxValue){
        mMaxValue = maxValue;
    }

    // Установка значения
    public synchronized void setValue(int value) {
        // Установка новой надписи
        this.setText(String.valueOf(value));

        // Drawable, отвечающий за фон
        LayerDrawable background = (LayerDrawable) this.getBackground();

        // Достаём Clip, отвечающий за шкалу, по индексу 1
        ClipDrawable barValue = (ClipDrawable) background.getDrawable(1);

        // Устанавливаем уровень шкалы
        int newClipLevel = value * 10000 / mMaxValue;
        barValue.setLevel(newClipLevel);

        // Уведомляем об изменении Drawable
        drawableStateChanged();
    }
}
