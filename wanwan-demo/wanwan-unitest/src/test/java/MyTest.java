import com.google.common.base.CharMatcher;
import org.junit.Test;

/**
 * @author wanwan 2022/3/9
 */
public class MyTest {

	@Test
	public void test1() {
		boolean isText = !CharMatcher.anyOf("x×*").matchesAnyOf("2个字-100x10px");
	}
}
