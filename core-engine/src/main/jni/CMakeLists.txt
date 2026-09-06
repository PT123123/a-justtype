cmake_minimum_required(VERSION 3.22.1)
project("rime_jni")

add_library(rime_jni SHARED
    rime_jni.cpp
)

find_library(rime_lib rime)

target_link_libraries(rime_jni
    ${rime_lib}
    android
    log
)
