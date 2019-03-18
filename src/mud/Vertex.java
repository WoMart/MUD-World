package mud;
/***********************************************************************
 * cs3524.solutions.mud.mud.Vertex
 ***********************************************************************/

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

// Represents a location in the mud.MUD (a vertex in the graph).
class Vertex
{
    String _name;				// mud.Vertex name
    String _msg = "";			// Message about this location
    Map<String,Edge> _routes;	// Association between direction
								// (e.g. "north") and a path (mud.Edge)
    List<String> _things;    	// The things (e.g. players) at this location

    Vertex( String nm ) {
		_name = nm;
		_routes = new HashMap<>(); 		// Not synchronised
		_things = new Vector<>();       // Synchronised
    }

    public String toString()
    {
		String summary = "\n";
		summary += _msg + "\n";
		Iterator iter = _routes.keySet().iterator();
		String direction;
		while (iter.hasNext()) {
			direction = (String)iter.next();
			summary += "To the " + direction + " there is " + ((Edge)_routes.get( direction ))._view + "\n";
		}
		iter = _things.iterator();
		if (iter.hasNext()) {
			summary += "You can see: ";
			do {
			summary += iter.next() + " ";
			} while (iter.hasNext());
		}
		summary += "\n\n";
		return summary;
    }
}

