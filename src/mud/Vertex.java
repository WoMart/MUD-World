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
	List<String> players;

    Vertex( String nm ) {
		_name = nm;
		_routes = new HashMap<>(); 		// Not synchronised
		_things = new Vector<>();       // Synchronised
		players = new Vector<>();
    }

    public String getView(String name) {
    	return this._routes.get(name)._view;
	}

	public void addPlayer(String name) {
    	players.add(name);
	}

	public void removePlayer(String name) {
    	players.remove(name);
	}

    public String toString()
    {
    	StringBuilder summary = new StringBuilder("\n").append(_msg);
		Iterator iter = _routes.keySet().iterator();
		while (iter.hasNext()) {
			String direction = iter.next().toString();
			summary.append("\nTo the ").append(direction).append(" there is ")
					.append( this.getView(direction) );
		}
		iter = _things.iterator();
		if (iter.hasNext()) {
			summary.append("\n\tItems around: ");
			do { summary.append(iter.next()).append(" "); }
			while (iter.hasNext());
		}
		// no need to check if there are players in location, because person calling it is there
		iter = players.iterator();
		summary.append("\n\tPlayers around: ");
		do { summary.append(iter.next()).append(" "); }
		while (iter.hasNext());
		return summary.append("\n\n").toString();
    }
}

