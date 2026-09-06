// JNI bindings for librime
// This file bridges Kotlin/Java calls to the native librime C++ library

#include <jni.h>
#include <rime_api.h>
#include <string>
#include <vector>

// Helper to throw Java exceptions
static void throwException(JNIEnv* env, const char* className, const char* msg) {
    jclass exClass = env->FindClass(className);
    if (exClass != nullptr) {
        env->ThrowNew(exClass, msg);
    }
}

// Helper to get RimeSessionId from Java
static RimeSessionId getSessionId(JNIEnv* env, jobject obj) {
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "sessionId", "J");
    return env->GetLongField(obj, fid);
}

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeInitialize(
    JNIEnv* env, jobject obj, jstring configDir, jstring userDir) {
    
    const char* config = env->GetStringUTFChars(configDir, nullptr);
    const char* user = env->GetStringUTFChars(userDir, nullptr);
    
    RimeTraits traits;
    traits.shared_data_dir = config;
    traits.user_data_dir = user;
    traits.app_name = "shell-keyboard";
    
    jboolean result = RimeInitialize(&traits) ? JNI_TRUE : JNI_FALSE;
    
    env->ReleaseStringUTFChars(configDir, config);
    env->ReleaseStringUTFChars(userDir, user);
    
    return result;
}

JNIEXPORT void JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeFinalize(JNIEnv* env, jobject obj) {
    RimeFinalize();
}

JNIEXPORT jboolean JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeProcessKey(
    JNIEnv* env, jobject obj, jlong sessionId, jint keyCode, jint modifiers) {
    
    jboolean result = RimeProcessKey((RimeSessionId)sessionId, (int)keyCode, (int)modifiers) 
        ? JNI_TRUE : JNI_FALSE;
    return result;
}

JNIEXPORT jstring JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetCommit(
    JNIEnv* env, jobject obj, jlong sessionId) {
    
    RimeCommit commit;
    if (RimeGetCommit((RimeSessionId)sessionId, &commit)) {
        jstring result = env->NewStringUTF(commit.text);
        RimeFreeCommit(&commit);
        return result;
    }
    return nullptr;
}

JNIEXPORT jobject JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetContext(
    JNIEnv* env, jobject obj, jlong sessionId) {
    
    RimeContext context;
    if (!RimeGetContext((RimeSessionId)sessionId, &context)) {
        return nullptr;
    }
    
    // Build RimeContext Java object
    jclass ctxClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$RimeContext");
    jmethodID ctor = env->GetMethodID(ctxClass, "<init>", "(Ljava/lang/String;Lcom/justtype/shellkeyboard/core/Rime$Composition;Lcom/justtype/shellkeyboard/core/Rime$Menu;Ljava/lang/String;)V");
    
    // Build Composition
    jclass compClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$Composition");
    jmethodID compCtor = env->GetMethodID(compClass, "<init>", "(IIIILjava/lang/String;)V");
    jobject composition = env->NewObject(compClass, compCtor,
        (jint)context.composition.length,
        (jint)context.composition.cursor_pos,
        (jint)context.composition.sel_start,
        (jint)context.composition.sel_end,
        env->NewStringUTF(context.composition.preedit));
    
    // Build Menu with candidates
    jclass menuClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$Menu");
    jmethodID menuCtor = env->GetMethodID(menuClass, "<init>", "(IIZII[Lcom/justtype/shellkeyboard/core/Rime$Candidate;)V");
    
    // Build candidates array
    jclass candClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$Candidate");
    jmethodID candCtor = env->GetMethodID(candClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
    jobjectArray candidates = env->NewObjectArray(context.menu.num_candidates, candClass, nullptr);
    
    for (int i = 0; i < context.menu.num_candidates; i++) {
        jobject cand = env->NewObject(candClass, candCtor,
            env->NewStringUTF(context.menu.candidates[i].text),
            context.menu.candidates[i].comment ? env->NewStringUTF(context.menu.candidates[i].comment) : nullptr);
        env->SetObjectArrayElement(candidates, i, cand);
        env->DeleteLocalRef(cand);
    }
    
    jobject menu = env->NewObject(menuClass, menuCtor,
        (jint)context.menu.page_size,
        (jint)context.menu.page_no,
        context.menu.is_last_page ? JNI_TRUE : JNI_FALSE,
        (jint)context.menu.highlighted_candidate_index,
        (jint)context.menu.num_candidates,
        candidates);
    
    jobject result = env->NewObject(ctxClass, ctor,
        env->NewStringUTF(context.composition.preedit),
        composition,
        menu,
        nullptr);
    
    RimeFreeContext(&context);
    return result;
}

JNIEXPORT jobject JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetStatus(
    JNIEnv* env, jobject obj, jlong sessionId) {
    
    RimeStatus status;
    if (!RimeGetStatus((RimeSessionId)sessionId, &status)) {
        return nullptr;
    }
    
    jclass statusClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$RimeStatus");
    jmethodID ctor = env->GetMethodID(statusClass, "<init>", "(Ljava/lang/String;ZZZZZZZ)V");
    
    jobject result = env->NewObject(statusClass, ctor,
        env->NewStringUTF(status.schema_id),
        status.is_disabled ? JNI_TRUE : JNI_FALSE,
        status.is_composing ? JNI_TRUE : JNI_FALSE,
        status.is_ascii_mode ? JNI_TRUE : JNI_FALSE,
        status.is_full_shape ? JNI_TRUE : JNI_FALSE,
        status.is_simplified ? JNI_TRUE : JNI_FALSE,
        status.is_traditional ? JNI_TRUE : JNI_FALSE,
        status.is_ascii_punct ? JNI_TRUE : JNI_FALSE);
    
    RimeFreeStatus(&status);
    return result;
}

JNIEXPORT void JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeSetOption(
    JNIEnv* env, jobject obj, jlong sessionId, jstring option, jboolean value) {
    
    const char* opt = env->GetStringUTFChars(option, nullptr);
    RimeSetOption((RimeSessionId)sessionId, opt, (bool)value);
    env->ReleaseStringUTFChars(option, opt);
}

JNIEXPORT jboolean JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetOption(
    JNIEnv* env, jobject obj, jlong sessionId, jstring option) {
    
    const char* opt = env->GetStringUTFChars(option, nullptr);
    jboolean result = RimeGetOption((RimeSessionId)sessionId, opt, nullptr) ? JNI_TRUE : JNI_FALSE;
    env->ReleaseStringUTFChars(option, opt);
    return result;
}

JNIEXPORT jstring JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetCurrentSchema(
    JNIEnv* env, jobject obj, jlong sessionId) {
    
    char schemaId[256];
    if (RimeGetCurrentSchema((RimeSessionId)sessionId, schemaId, sizeof(schemaId))) {
        return env->NewStringUTF(schemaId);
    }
    return nullptr;
}

JNIEXPORT jboolean JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeSetSchema(
    JNIEnv* env, jobject obj, jlong sessionId, jstring schemaId) {
    
    const char* schema = env->GetStringUTFChars(schemaId, nullptr);
    jboolean result = RimeSetSchema((RimeSessionId)sessionId, schema) ? JNI_TRUE : JNI_FALSE;
    env->ReleaseStringUTFChars(schemaId, schema);
    return result;
}

JNIEXPORT jobject JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeGetSchemaList(JNIEnv* env, jobject obj) {
    RimeSchemaList list;
    if (!RimeGetSchemaList(&list)) {
        return nullptr;
    }
    
    jclass infoClass = env->FindClass("com/justtype/shellkeyboard/core/Rime$SchemaInfo");
    jmethodID ctor = env->GetMethodID(infoClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    
    jobjectArray result = env->NewObjectArray(list.size, infoClass, nullptr);
    for (size_t i = 0; i < list.size; i++) {
        jobject info = env->NewObject(infoClass, ctor,
            env->NewStringUTF(list.list[i].schema_id),
            env->NewStringUTF(list.list[i].name),
            list.list[i].version ? env->NewStringUTF(list.list[i].version) : nullptr,
            list.list[i].author ? env->NewStringUTF(list.list[i].author) : nullptr);
        env->SetObjectArrayElement(result, i, info);
        env->DeleteLocalRef(info);
    }
    
    RimeFreeSchemaList(&list);
    return result;
}

JNIEXPORT jlong JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeCreateSession(JNIEnv* env, jobject obj) {
    return (jlong)RimeCreateSession();
}

JNIEXPORT void JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeDestroySession(
    JNIEnv* env, jobject obj, jlong sessionId) {
    RimeDestroySession((RimeSessionId)sessionId);
}

JNIEXPORT jboolean JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeSimulateKeySequence(
    JNIEnv* env, jobject obj, jlong sessionId, jstring sequence) {
    
    const char* seq = env->GetStringUTFChars(sequence, nullptr);
    jboolean result = RimeSimulateKeySequence((RimeSessionId)sessionId, seq) ? JNI_TRUE : JNI_FALSE;
    env->ReleaseStringUTFChars(sequence, seq);
    return result;
}

JNIEXPORT jstring JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeCommit(JNIEnv* env, jobject obj) {
    RimeCommit commit;
    if (RimeGetCommit(0, &commit)) {
        jstring result = env->NewStringUTF(commit.text);
        RimeFreeCommit(&commit);
        return result;
    }
    return nullptr;
}

JNIEXPORT void JNICALL
Java_com_justtype_shellkeyboard_core_Rime_rimeCleanup(JNIEnv* env, jobject obj) {
    RimeCleanupAllSessions();
}

} // extern "C"
