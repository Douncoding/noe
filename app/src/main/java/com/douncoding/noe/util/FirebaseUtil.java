package com.douncoding.noe.util;

import android.content.Context;
import android.text.TextUtils;

import com.douncoding.noe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static String getPath(DatabaseReference ref) {
        return ref.toString().substring(getBaseRef().toString().length()+1);
    }

    public static DatabaseReference getPetsRef() {
        return getBaseRef().child("pets");
    }

    public static DatabaseReference getBabysRef() {
        return getBaseRef().child("babys");
    }

    public static DatabaseReference getCarsRef() {
        return getBaseRef().child("cars");
    }

    public static DatabaseReference getBeaconRef() {
        return getBaseRef().child("beacons");
    }

    public static DatabaseReference getBeaconPayRef() {
        return getBaseRef().child("beacons").child("pay");
    }

    public static DatabaseReference getBeacoBabyRef() {
        return getBaseRef().child("beacons").child("baby");
    }

    public static DatabaseReference getBeaconPetRef() {
        return getBaseRef().child("beacons").child("pet");
    }

    public static DatabaseReference getPeopleRef() {
        return getBaseRef().child("people");
    }

    public static DatabaseReference getPaysRef() {
        return getBaseRef().child("pays"); //등록된 카드 정보
    }

    public static DatabaseReference getPaymentRef() {
        return getBaseRef().child("payment");
    }

    public static DatabaseReference getCurrentUserHasPetRef() {
        String uid = getCurrentUserId();
        if (TextUtils.isEmpty(uid)) {
            return null;
        } else {
            return getPeopleRef().child(uid).child("hasPet");
        }
    }

    public static DatabaseReference getCurrentUserHasBabyRef() {
        String uid = getCurrentUserId();
        if (TextUtils.isEmpty(uid)) {
            return null;
        } else {
            return getPeopleRef().child(uid).child("hasBaby");
        }
    }

    public static DatabaseReference getCurrentUserHasPayRef() {
        String uid = getCurrentUserId();
        if (TextUtils.isEmpty(uid)) {
            return null;
        } else {
            return getPeopleRef().child(uid).child("hasPay");
        }
    }

    public static DatabaseReference getCurrentUserHasCarRef() {
        String uid = getCurrentUserId();
        if (TextUtils.isEmpty(uid)) {
            return null;
        } else {
            return getPeopleRef().child(uid).child("hasCar");
        }
    }

    /**
     * "full" 이라는 공간을 사용하며, 추후 썸네일이 필요한 경우를 대비하기 위함이다.
     * @return 현재 로그인된 사용자의 사진 저장소 레퍼런스 반환
     */
    public static StorageReference getPhotoStorageWithCurrentUserRef(Context context) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://" +
            context.getString(R.string.google_storage_bucket));

        String uid = FirebaseUtil.getCurrentUserId();
        if (!TextUtils.isEmpty(uid)) {
            return photoRef.child(uid).child("full");
        } else {
            return null;
        }
    }
}
