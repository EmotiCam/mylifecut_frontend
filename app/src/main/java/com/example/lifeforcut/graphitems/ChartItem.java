package com.example.lifecut.graphitems;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * Base class of the Chart ListView items
 *
 */

public abstract class ChartItem {

    static final int TYPE_BARCHART = 0;
    static final int TYPE_LINECHART = 1;
    static final int TYPE_RADARCHART = 2;

    ChartData<?> mChartData;

    ChartItem(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context c);




}
