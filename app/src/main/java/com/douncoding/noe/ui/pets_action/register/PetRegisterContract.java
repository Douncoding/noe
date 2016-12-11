package com.douncoding.noe.ui.pets_action.register;

import android.content.Intent;
import android.graphics.Bitmap;

import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseContract;

import java.util.List;

public interface PetRegisterContract {
    interface View extends BaseContract.View {
        // 이미지 출력
        void renderFacePicture(Bitmap bitmap);

        // 검색된 비콘정보 기록
        void renderBeaconData(Beacon beacon);
        // 검색된 비콘 중 선택 다이얼로그 현시
        void renderChooserBeaconDialog(List<Beacon> beaconList);
        // 자동 검색실패 -> 사용자 수동입력 유도
        void showSearchBeaconFailure();

        // 처리 중 다이얼로그 출력
        void showProgressDialog(String message);
        // 현재 출려된 다이얼로그 삭제
        void hideProgressDialog();

        // 재대로 입력하지 않은 경우 발생메시지
        // loc 은 어떤 위치가 잘못되었는지를 알려줌 ..
        void showRequestInputField(int loc);

        // 등록 성공
        void showRegisterSuccess();
        // 등록 실패
        void showRegisterFailure();
    }

    interface Presenter extends BaseContract.Presenter {
        // 등록을 원하는 반려동물 정보
        void onSubmit(Pet pet);

        // 이미지 선택 요청
        void onShowImagePicker();

        // 이미지 선택 요청애 대한 응답 처리
        void onActivityResult(int requestCode, int resultCode, Intent data);

        // 주변 비콘 자동검색 요청
        void searchAroundBeacon();
    }
}
