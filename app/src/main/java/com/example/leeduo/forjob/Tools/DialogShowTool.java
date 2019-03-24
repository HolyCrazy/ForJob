package com.example.leeduo.forjob.Tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/2/11.
 */
//加载框工具
public class DialogShowTool {

    private View dialogView;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    private AlertDialog alertDialog;
    private Activity activity;
    private boolean isDismiss;

    public DialogShowTool(Activity activity){
        this.activity = activity;
        isDismiss = false;
        dialogView = activity.getLayoutInflater().inflate(R.layout.waiting_dialog_view,null);
        imageView = dialogView.findViewById(R.id.dialog_image);
        imageView.setBackgroundResource(R.drawable.waiting_dialog_anim);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        alertDialog = new AlertDialog.Builder(activity,R.style.dialogStyle)
                .setView(dialogView)
                .setCancelable(false)
                .create();


    }

    public void show(){
        animationDrawable.start();
        alertDialog.show();

        //改变大小和背景
        alertDialog.getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.dialog_background));
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = 300;
        layoutParams.height = 300;
        alertDialog.getWindow().setAttributes(layoutParams);
    }
    public void dismiss(){
        if(!isDismiss){
            animationDrawable.stop();
            alertDialog.dismiss();
            isDismiss = true;
        }

    }
}
