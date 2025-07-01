package com.playdata.schedulerservice.api.util;

import com.playdata.schedulerservice.api.entity.StrayAnimalEntity;

public class SafeEnumParser {

    // 성별 코드가 유효하지 않으면 Q(미상)으로 대체
    public static StrayAnimalEntity.SexCode parseSexCode(String code) {
        try {
            return StrayAnimalEntity.SexCode.valueOf(code);
        } catch (Exception e) {
            return StrayAnimalEntity.SexCode.Q;
        }
    }

    // 중성화 여부가 유효하지 않으면 U(미상)으로 대체
    public static StrayAnimalEntity.NeuterYn parseNeuterYn(String code) {
        try {
            return StrayAnimalEntity.NeuterYn.valueOf(code);
        } catch (Exception e) {
            return StrayAnimalEntity.NeuterYn.U;
        }
    }
}