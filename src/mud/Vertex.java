package mud;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.Map;

// Represents a location in the mud.MUD (a vertex in the graph).
class Vertex {

    private String name;				// mud.Vertex name
    private String description;			// Message about this location
    private Map<String,Edge> routes;	// Direction -> Path
	private List<String> players;		// Players in this location
	private List<String> things;    	// Items at this location

    Vertex(String nm) {
		this.name = nm;
		this.description = "";
		this.routes = new HashMap<>(); 		// Not synchronised
		this.things = new Vector<>();       // Synchronised
		this.players = new Vector<>();
    }

	String getName() { return this.name; }

	void setDescription(String msg) { this.description = msg; }

	void addRoute(String dir, Edge e) { this.routes.put(dir, e); }

	Edge getRoute(String dir) { return this.routes.get(dir); }

	void addPlayer(String name) { this.players.add(name); }

	void removePlayer(String name) { this.players.remove(name); }

	void addThing(String thing) { this.things.add(thing); }

	void removeThing(String thing) { this.things.remove(thing); }

	boolean hasThing(String thing) { return this.things.contains(thing); }

	private String getView(String name) { return ( this.routes.get(name) ).getView(); }

    public String toString() {
    	StringBuilder summary = new StringBuilder("\n").append(this.description);

    	// paths
		Iterator iter = this.routes.keySet().iterator();
		while (iter.hasNext()) {
			String direction = iter.next().toString();
			summary.append("\nTo the ").append(direction).append(" there is ")
					.append( this.getView(direction) );
		}

		// items
		iter = this.things.iterator();
		if (iter.hasNext()) {
			summary.append("\n\tItems around: ");
			do { summary.append(iter.next()).append(" "); }
			while (iter.hasNext());
		}

		// players
		iter = this.players.iterator();
		if (iter.hasNext()) {
			summary.append("\n\tPlayers around: ");
			do { summary.append(iter.next()).append(" "); }
			while (iter.hasNext());
		}
		return summary.append("\n\n").toString();
    }
}

