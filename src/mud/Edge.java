package mud;


// Represents an path in the mud.MUD (an edge in a graph).
class Edge {

    private Vertex dest;   // Your destination if you walk down this path
    private String view;   // What you see if you look down this path
    
    Edge(Vertex d, String v) {
        this.dest = d;
	    this.view = v;
    }

    Vertex getDest() { return this.dest; }
    String getView() { return this.view; }
}

