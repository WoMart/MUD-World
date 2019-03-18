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
    private String name;					// mud.Vertex name
    private String msg;				// Message about this location
    private Map<String,Edge> routes;		// Association between direction
									// (e.g. "north") and a path (mud.Edge)
    private List<String> things;    // The things (e.g. players) at this location
	private List<String> players;

    Vertex( String nm ) {
		this.name = nm;
		this.msg = "";
		this.routes = new HashMap<>(); 		// Not synchronised
		this.things = new Vector<>();       // Synchronised
		this.players = new Vector<>();
    }

    private String getView(String name) {
    	return this.routes.get(name)._view;
	}

	public String getName() { return this.name; }

	public void setMessage(String msg) { this.msg = msg; }

	public void addRoute(String dir, Edge e) { this.routes.put(dir, e); }

	public Edge getRoute(String dir) { return this.routes.get(dir); }

	public void addThing(String thing) { this.things.add(thing); }

	public void removeThing(String thing) { this.things.remove(thing); }

	public boolean hasThing(String thing) { return this.things.contains(thing); }

	public void addPlayer(String name) {
    	players.add(name);
	}

	public void removePlayer(String name) {
    	players.remove(name);
	}

    public String toString()
    {
    	StringBuilder summary = new StringBuilder("\n").append(msg);
		Iterator iter = routes.keySet().iterator();
		while (iter.hasNext()) {
			String direction = iter.next().toString();
			summary.append("\nTo the ").append(direction).append(" there is ")
					.append( this.getView(direction) );
		}
		iter = things.iterator();
		if (iter.hasNext()) {
			summary.append("\n\tItems around: ");
			do { summary.append(iter.next()).append(" "); }
			while (iter.hasNext());
		}
		iter = players.iterator();
		if (iter.hasNext()) {
			summary.append("\n\tPlayers around: ");
			do { summary.append(iter.next()).append(" "); }
			while (iter.hasNext());
		}
		return summary.append("\n\n").toString();
    }
}

