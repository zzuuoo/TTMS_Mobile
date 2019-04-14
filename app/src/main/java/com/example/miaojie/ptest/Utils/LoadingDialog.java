package com.example.miaojie.ptest.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.View.LVCircularRing;

public class LoadingDialog {
    LVCircularRing mLoadingView;
    Dialog mLoadingDialog;

    public LoadingDialog(Context context, String msg) {
        // ���ȵõ�����View
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view, null);
        // ��ȡ��������
        LinearLayout layout = view.findViewById(R.id.dialog_view);
        // ҳ���е�LoadingView
        mLoadingView = view.findViewById(R.id.lv_circularring);
        // ҳ������ʾ�ı�
        TextView loadingText = view.findViewById(R.id.loading_text);
        // ��ʾ�ı�
        loadingText.setText(msg);
        // �����Զ�����ʽ��Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        // ���÷��ؼ���Ч
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void show(){
        mLoadingDialog.show();
        mLoadingView.startAnim();
    }

    public void close(){
        if (mLoadingDialog!=null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
            mLoadingDialog=null;
        }
    }
}