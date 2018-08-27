package com.example.xiewujie.chattingdemo.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;
import com.example.xiewujie.chattingdemo.service.MyService;
import com.example.xiewujie.chattingdemo.view.activity.EditDataActivity;
import com.example.xiewujie.chattingdemo.view.event.OwnerDataInitEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.FileNotFoundException;
import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerFragment extends Fragment implements View.OnClickListener {

    private ImageView backgroudImage;
    private TextView nickNameText;
    private TextView locationText;
    private TextView ageText;
    private TextView genderText;
    private Button changeBac;
    private Button changeHeader;
    private Button changeData;
    private Button settingBtn;
    private CircleImageView avatarView;
    private Context mContext;
    private static final int PIC_HEADER = 0;
    private static final int PIC_BACKBROUND = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_data_layout, container, false);
        nickNameText = (TextView) view.findViewById(R.id.personal_user_nic_name_text);
        ageText = (TextView) view.findViewById(R.id.personal_age_text);
        locationText = (TextView) view.findViewById(R.id.personal_position_text);
        genderText = (TextView) view.findViewById(R.id.personal_gender_text);
        changeBac = (Button) view.findViewById(R.id.change_background);
        changeData = (Button) view.findViewById(R.id.change_data);
        changeHeader = (Button) view.findViewById(R.id.chang_header);
        settingBtn = (Button) view.findViewById(R.id.setting);
        avatarView = (CircleImageView) view.findViewById(R.id.personal_user_nic_name_view);
        backgroudImage = (ImageView) view.findViewById(R.id.owner_background_image);
        mContext = getContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewData();
    }

    private void initViewData() {
        Owner owner = OwnerManager.UserManagerHelper.getInstance().getOwner();
        genderText.setText(owner.getGender());
        ageText.setText(String.valueOf(owner.getAge()));
        locationText.setText(owner.getLocation());
        nickNameText.setText(owner.getName());

        Glide.with(this).
                load(owner.getBackgroundUrl()).
                error(R.drawable.personal_default_background)
                .into(backgroudImage);
        Glide.with(mContext)
                .load(owner.getAvatarUrl())
                .error(R.drawable.header_default_icon)
                .into(avatarView);
        changeHeader.setOnClickListener(this);
        changeData.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        changeBac.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_background:
                dispatchPictureIntent(PIC_BACKBROUND);
                break;
            case R.id.chang_header:
               dispatchPictureIntent(PIC_HEADER);
                break;
            case R.id.setting:
                Intent intent = new Intent(mContext, EditDataActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void dispatchPictureIntent(int type) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, null);
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(photoPickerIntent, type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case PIC_HEADER:
                    updateImage(data.getData(), PIC_HEADER);
                    break;
                case PIC_BACKBROUND:
                    updateImage(data.getData(), PIC_BACKBROUND);
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateImage(Uri uri, final int type) {
        String path = MyService.getRealPathFromURI(mContext, uri);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath("leanCloud.png", path);
            if (file != null) {
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null) {
                            String url = file.getUrl();
                            if (type == PIC_HEADER) {
                                OwnerManager.UserManagerHelper.getInstance().updateOwner(new String[]{"user_header_image"}, new String[]{url});
                            } else {
                                OwnerManager.UserManagerHelper.getInstance().updateOwner(new String[]{"user_background_image"}, new String[]{url});
                            }
                        }
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (type == PIC_HEADER) {
            avatarView.setImageBitmap(bitmap);
        } else {
            backgroudImage.setImageBitmap(bitmap);
        }
        initViewData();
    }
}
