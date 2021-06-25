package partA;

import java.util.Collection;
import java.util.LinkedList;

public interface Node {
    public Collection<Node> getCollction(Class<? extends HasUUID> aClass);

}
