package com.study.android.ex09_alert;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.android.ex09_alert.R;

public class CustomProgressDialog extends Dialog {


    public static CustomProgressDialog show(Context context, CharSequence title,
                                            CharSequence message) {
        return show(context, title, message, false);
    }

    public static CustomProgressDialog show(Context context, CharSequence title,
                                            CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static CustomProgressDialog show(Context context, CharSequence title,
                                            CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    // AndroidManifest.xml의 android:theme 세팅 : @android:style/Theme.Light.NoTitleBar
    // drawable에 이미지 복사
    // style.xml에 스타일 추가
    public static CustomProgressDialog show(Context context, CharSequence title,
                                            CharSequence message, boolean indeterminate,
                                            boolean cancelable, OnCancelListener cancelListener) {

        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);

        /* The next line will add the ProgressBar to the dialog. */
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(lparam);

        ll.setBackgroundResource(R.drawable.loading_background);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);


        ProgressBar pbar = new ProgressBar(context);
        pbar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ll.addView(pbar);

        if (!message.equals("")) {
            TextView pText = new TextView(context);
            pText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            pText.setGravity(Gravity.CENTER_HORIZONTAL);
            pText.setText(message);
            pText.setTextSize(16.0f);
            ll.addView(pText);
        }

        dialog.addContentView(ll, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public CustomProgressDialog(Context context) {
        super(context, R.style.NewDialog);
    }
}