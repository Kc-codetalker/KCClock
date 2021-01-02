//
// Created by kace on 21/12/20.
//

#include <jni.h>

#include <string>

extern "C" JNIEXPORT jstring JNICALL

Java_id_ac_ui_cs_mobileprogramming_kace_kcclock_alarm_TimeBasedAlarmFragment_getAlarmDescTextFromJNI( JNIEnv *env, jobject /* this */, jboolean isSun, jboolean isMon, jboolean isTue, jboolean isWed, jboolean isThu, jboolean isFri, jboolean isSat, jstring nextTime, jstring everyDay, jstring every, jstring sun, jstring mon, jstring tue, jstring wed, jstring thu, jstring fri, jstring sat) {

    jboolean isCopy;
    if (isSun && isMon && isTue && isWed && isThu && isFri && isSat) {
        std::string everyDayStr = env->GetStringUTFChars(everyDay, &isCopy);
        return env->NewStringUTF(everyDayStr.c_str());
    } else if (isSun || isMon || isTue || isWed || isThu || isFri || isSat) {
        std::string everyStr = env->GetStringUTFChars(every, &isCopy);
        if (isSun) {
            std::string day = env->GetStringUTFChars(sun, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isMon) {
            std::string day = env->GetStringUTFChars(mon, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isTue) {
            std::string day = env->GetStringUTFChars(tue, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isWed) {
            std::string day = env->GetStringUTFChars(wed, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isThu) {
            std::string day = env->GetStringUTFChars(thu, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isFri) {
            std::string day = env->GetStringUTFChars(fri, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        if (isSat) {
            std::string day = env->GetStringUTFChars(sat, &isCopy);
            everyStr = everyStr.append(" ").append(day).append(",");
        }
        everyStr.erase(everyStr.size()-1);
        return env->NewStringUTF(everyStr.c_str());
    } else {
        std::string nextTimeStr = env->GetStringUTFChars(nextTime, &isCopy);
        return env->NewStringUTF(nextTimeStr.c_str());
    }
}