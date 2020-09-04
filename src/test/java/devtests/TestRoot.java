/**
 * 
 */
package devtests;

import org.topicquests.os.asr.pd.ParserDispatcherEnvironment;
import org.topicquests.os.asr.pd.api.IDispatcher;

/**
 * @author jackpark
 *
 */
public class TestRoot {
	protected ParserDispatcherEnvironment environment;
	protected IDispatcher dispatcher;

	/**
	 * 
	 */
	public TestRoot() {
		environment = new ParserDispatcherEnvironment();
		dispatcher = environment.getDispatcher();
	}

}
