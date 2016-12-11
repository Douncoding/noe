package com.douncoding.noe.ui.car_action.register;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Car;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.service.BeaconService;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.babys_action.register.BabyRegisterContract;
import com.douncoding.noe.util.FirebaseUtil;
import com.douncoding.noe.util.ImageUtil;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.douncoding.noe.Constants.CARMERA_PARAMS;
import static com.douncoding.noe.Constants.FULL_SIZE_MAX_DIMENSION;
import static com.douncoding.noe.Constants.RC_CAMERA_PERMISSIONS;
import static com.douncoding.noe.Constants.TC_PICK_IMAGE;

public class CarRegisterPresenter extends BasePresenter<CarRegisterContract.View> implements  CarRegisterContract.Presenter {

    private Bitmap resizeBitmap;

    @Override
    public void onResumeFromContext() {

    }

    @Override
    public void onPauseFromContext() {

    }

    @Override
    public void onDestroyFromContext() {

    }

    @Override
    public void onSubmit(Car car) {
        // 이미지 등록 확인
        if (resizeBitmap == null) {
            mView.showRequestInputField(0);
            return ;
        }

        // 기본정보확인
        if (car.getName().isEmpty() || car.getBirthday().isEmpty() || car.getLicenseplate().isEmpty()) {
            mView.showRequestInputField(1);
            return ;
        }

        // 비콘정보 확인
        Beacon beacon = car.getBeacon();
        if (beacon.getUuid().isEmpty() || beacon.getMajor().isEmpty() || beacon.getMinor().isEmpty()) {
            mView.showRequestInputField(3);
            return ;
        }

        mView.showProgressDialog();
        StorageReference photoRef = FirebaseUtil.getPhotoStorageWithCurrentUserRef(getContext());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

        Long timestamp = System.currentTimeMillis();
        // 사진 업로드 (UploadTask)
        photoRef.child(timestamp + ".jpg").putBytes(outputStream.toByteArray()).addOnSuccessListener(taskSnapshot -> {
            car.setPictureUrl(taskSnapshot.getDownloadUrl().toString());

            DatabaseReference carsRef = FirebaseUtil.getCarsRef();
            DatabaseReference beaconRef = FirebaseUtil.getBeaconRef();
            DatabaseReference peopleRef = FirebaseUtil.getCurrentUserHasCarRef();

            Map<String, Object> updateUserData = new HashMap<>();
            final String carKey = carsRef.push().getKey();
            updateUserData.put(FirebaseUtil.getPath(carsRef) + "/" + carKey, car);
            updateUserData.put(FirebaseUtil.getPath(beaconRef) + "/" + beaconRef.push().getKey(), beacon);
            updateUserData.put(FirebaseUtil.getPath(peopleRef) + "/" + carKey, true);

            // 데이터베이스 기록
            FirebaseUtil.getBaseRef().updateChildren(updateUserData, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    FirebaseCrash.report(databaseError.toException());
                    mView.showRegisterFailure();
                } else {
                    mView.showRegisterSuccess();
                }

                mView.hideProgressDialog();
            });
        });
    }

    private Uri mFileUri; // 저장될 위치를 이미 선출하기 위함
    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    @Override
    public void onShowImagePicker() {
        if (!EasyPermissions.hasPermissions(getActivity(), CARMERA_PARAMS)) {
            EasyPermissions.requestPermissions(getActivity(), "This sample will upload a picture from your Camera",
                    RC_CAMERA_PERMISSIONS, CARMERA_PARAMS);
            return;
        }

        // Choose file storage location
        File file = new File(getActivity().getExternalCacheDir(), UUID.randomUUID().toString());
        mFileUri = Uri.fromFile(file);

        // 카메라 갭처 인텐트 생성
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam){
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            cameraIntents.add(intent);
        }

        // 갤러리 인테트 생성
        Intent pickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(pickerIntent, getContext().getString(R.string.picture_chooser_title));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        getActivity().startActivityForResult(chooserIntent, TC_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final boolean isCamera = MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());
            if (!isCamera) {
                mFileUri = data.getData();
            }

            // 이미지 리사이즈 처리 및 사진 현시
            handleRegisterPicture(mFileUri);
        }
    }


    /**
     * {@link this#onActivityResult(int, int, Intent)} 에 의해 호출되며, 카메라 또는 갤러리로 부터 전송된 이미지를
     * 서버에 등록하기 적절한 이미지의 크기로 리사이즈 한다.
     * 썸네일 이미지는 별도로 생성하지 않으며, Glide 와 같은 이미지 로딩 라이브러리에서 제공하는 Thumbnail 을 이용하여 출력한다.
     * @param from 등록할 이미지의 URI (content://)
     */
    private void handleRegisterPicture(Uri from) {
        new AsyncTask<Uri, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(Uri... params) {
                Uri uri = params[0];

                try {
                    return ImageUtil.decodeSampledBitmapFromUri(
                            getContext(), uri, FULL_SIZE_MAX_DIMENSION, FULL_SIZE_MAX_DIMENSION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    resizeBitmap = bitmap;
                    mView.renderFacePicture(bitmap);
                }
            }
        }.execute(from);
    }

    // 최초 한번만 수행하기 위함
    @Override
    public void searchAroundBeacon() {
        List<org.ccbeacon.beacon.Beacon> beacons = BeaconService.instance.getCurrentBeaconList();
        if (beacons.size() <= 0) {
            Logger.i("비콘 감지 실패: 수동입력 필요");
            mView.showSearchBeaconFailure();
        } else if (beacons.size() == 1){
            Logger.i("비콘 한개 감지: 자동입력");
            org.ccbeacon.beacon.Beacon item = beacons.get(0);
            Beacon result = new Beacon(item.getId1().toString(),
                    item.getId2().toString(), item.getId3().toString());
            mView.renderBeaconData(result);
        } else {
            Logger.i("비콘 %s개 감지: 선택필요", beacons.size());
            List<Beacon> result = new ArrayList<>();
            for (org.ccbeacon.beacon.Beacon item : beacons) {
                result.add(new Beacon(item.getId1().toString(), item.getId2().toString(),
                        item.getId3().toString()));
            }

            mView.renderChooserBeaconDialog(result);
        }
    }
}
