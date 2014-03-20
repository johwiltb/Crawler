import java.util.ArrayList;

/**
 * @author John Wiltberger
 *
 */

/*
 DLS(node, goal, depth) {
  if ( depth >= 0 ) {
    if ( node == goal )
      return node

    for each child in expand(node)
      DLS(child, goal, depth-1)
  }
}
 */
public class DepthFirst {
	private ArrayList<String>[] inlineURLs;
	private int depthLimit, currentDepth;
	
	public DepthFirst(ArrayList<String>[] url, int dLimit, int curDepth) {
		this.depthLimit = dLimit;
		this.currentDepth = curDepth;
		if ( curDepth >= 0)
		{
			if (url[dLimit] == null)
			{
				
			}
		}
		
		for (int i = 0; i < url.length; i++) {
			DepthFirst next = new DepthFirst(this.inlineURLs, depthLimit, currentDepth - 1);
		}
	}

}
