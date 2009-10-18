#include <windows.h>
#include "ampt_core_time_NativeWinClock.h"

JNIEXPORT jlong JNICALL Java_ampt_core_time_NativeWinClock_getFrequency(JNIEnv *, jobject)
{
	LARGE_INTEGER freq;
	QueryPerformanceFrequency(&freq);
	return freq.QuadPart;
}

JNIEXPORT jlong JNICALL Java_ampt_core_time_NativeWinClock_getCounter(JNIEnv *, jobject)
{
	LARGE_INTEGER counter;
	QueryPerformanceCounter(&counter);
	return counter.QuadPart;
}
