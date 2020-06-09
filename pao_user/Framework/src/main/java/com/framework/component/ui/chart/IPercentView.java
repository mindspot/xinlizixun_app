package com.framework.component.ui.chart;

/**
 * Created by User on 2017/6/20.
 */
public interface IPercentView {

    void setProgressNotInUiThread(int currProgress);

    void setProgressWithAnim(int progress);
}
