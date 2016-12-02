package com.hyphenate.chatuidemo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.adapter.PickGridAdapter;
import com.hyphenate.chatuidemo.utils.GlideLoader;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 16-12-2.
 */
public class NewPostActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 1000;
    private EditText contentEdit;
    private EaseTitleBar titleBar;
    private GridView gridview;
    private PickGridAdapter gridAdapter;

    private ArrayList<String> path = new ArrayList<>();
    private int photoRemain = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_post);
        initView();
    }

    private void initView() {
        titleBar = (EaseTitleBar)findViewById(R.id.title_bar);
        contentEdit = (EditText)findViewById(R.id.content_edit);
        titleBar.setRightLayoutClickListener(this);
        titleBar.setRightImageResource(R.drawable.send_plane);
        gridview = (GridView)findViewById(R.id.gridview);
        gridAdapter = new PickGridAdapter(this);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(gridAdapter.getItemViewType(i)==gridAdapter.ADD){
                    updatePhoto();
                }else {
                    Intent intent = new Intent(NewPostActivity.this, PhotoDetailActivity.class);
                    intent.putExtra("url",gridAdapter.getItem(i));
                    intent.putExtra("mode",2);
                    startActivityForResult(intent,PhotoDetailActivity.DELETE_CODE);
                }
            }
        });
    }


    public void updatePhoto(){

        if (photoRemain<=0)
            return;

        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.common_top_bar_blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.common_top_bar_blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                //.crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(photoRemain)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/grape/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open(NewPostActivity.this, imageConfig);   // 开启图片选择器
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            photoRemain =9 - pathList.size();
            gridAdapter.setData(path);
        }
        if (requestCode == PhotoDetailActivity.DELETE_CODE && resultCode == RESULT_OK && data !=null){
            String url = data.getStringExtra("url");
            if(url!=null && path!=null && path.contains(url)){
                path.remove(url);
                gridAdapter.setData(path);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.right_image:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Whether_to_give_up_this_post);
        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }
}
