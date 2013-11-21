package com.aifull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionAPI {

	public static Object invoke(Object invokeObject, String callMethod,
			Object[] argObjects) throws InvocationTargetException,
			IllegalAccessException, NoSuchMethodException {
		Method method = findMethod(invokeObject, callMethod, argObjects);
		return method.invoke(invokeObject, argObjects);
	}

	public static Method findMethod(Object invokeObject, String callMethod,
				Object[] argObjects) throws NoSuchMethodException {
		Class[] paramClasses = null;
		Method[] methods = invokeObject.getClass().getMethods();
		top: for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(callMethod)) {
				if (argObjects == null
						&& methods[i].getParameterTypes().length == 0) {
					return methods[i];
				}
				if (argObjects == null) {
					continue;
				}
				paramClasses = methods[i].getParameterTypes();
				if (paramClasses.length == argObjects.length) {
					// 全てのパラメーターリストの型と、引数の型の検証
					for (int j = 0; j < paramClasses.length; j++) {
						Class paramClass = paramClasses[j];
						Object argObj = argObjects[j];
						// 引数の型がプリミティブの場合、引数のオブジェクト
						// がnullでなくプリミティブ
						// もしくわ、NumberのサブクラスならＯＫとする。
						if (argObj == null) {
							continue;
						}
						if (paramClass.isPrimitive()
								&& (argObj instanceof Number || argObj
										.getClass().isPrimitive())) {
							continue;
						}
						if (!paramClass.isInstance(argObj)) {
							// 型に暗黙変換の互換性が無い時点で、次のメソッドへ
							continue top;
						}
					}
					return methods[i];
				}
			}
		}
		String paramLength = (paramClasses != null) ? Integer.toString(paramClasses.length) : "";
				String errorMes = "メソッドはありません。";
				throw new NoSuchMethodException(errorMes);
	}


}