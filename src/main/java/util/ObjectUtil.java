package util;

/**
 * @Author:jianyang
 * @since 2021-12-24 01:32
 */
public class ObjectUtil {
	public static <T> T checkNotNull(T arg, String text){
		if (arg == null) {
			throw new NullPointerException(text);
		}
		return arg;
	}
}
