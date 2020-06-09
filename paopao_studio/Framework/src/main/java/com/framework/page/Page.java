package com.framework.page;

/**
 * @author Administrator
 * @date 2018/1/15
 */
public interface Page extends ComponentPage {

    /**
     * Activity的页面
     */
    interface ActivityPage extends Page {

        boolean hasWindowFocus();

        void setOnIWindowFocus(IWindowFocus windowFocus);

        interface IWindowFocus {
            void focused();
        }
    }

    /**
     * fragment的页面
     */
    interface FragmentPage extends Page {

        void onShow();
    }

}
