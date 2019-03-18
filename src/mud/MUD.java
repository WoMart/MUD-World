package mud;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileReader;

import java.util.*;

/**
 * A class that can be used to represent a mud.MUD; essenially, this is a
 * graph.
 */
public class MUD implements Serializable
{
	// Map of all locations: name -> location( vertex )
	private Map<String,Vertex> vertexMap = new HashMap<>();
	private List<String> players = new ArrayList<>();
	private String startLocation = "";

    // Add a new path( edge ) between two locations
    private void addEdge( String source, String destination, String direction, String view )
    {
        Vertex v = getOrCreateVertex( source );
        Vertex w = getOrCreateVertex( destination );
        v.addRoute( direction, new Edge( w, view ) );
    }

    // Create a new item in a location
    private void createThing( String loc, String thing )
    {
		Vertex v = getOrCreateVertex( loc );
		v.addThing( thing );
    }

    // Change the the location's description( view )
    private void changeMessage( String loc, String msg )
    {
		Vertex v = getOrCreateVertex( loc );
		v.setMessage(msg);
    }

    // Return given location. Create a new one if it does not exist yet
    private Vertex getOrCreateVertex( String vertexName )
    {
        Vertex v = getVertex( vertexName );
        if (v == null) {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    // Return given location
    private Vertex getVertex( String vertexName )
    {
		return vertexMap.get( vertexName );
    }

    /**
     * Records a map based on data from a given file ( e.g "mymud.edg" )
     * Format of each line: source direction destination view
     */
    private void createEdges( String edgesfile )
    {
		try {
			FileReader fin = new FileReader( edgesfile );
			BufferedReader edges = new BufferedReader( fin );
			String line;
			while((line = edges.readLine()) != null) {
				StringTokenizer st = new StringTokenizer( line );
				if( st.countTokens( ) < 3 ) {
					System.err.println( "Skipping ill-formatted line " + line );
					continue;
				}
				String source = st.nextToken();
				String dir    = st.nextToken();
				String dest   = st.nextToken();
				String msg = "";
				while (st.hasMoreTokens())
					msg = msg + st.nextToken() + " ";

				addEdge( source, dest, dir, msg );
			}
		}
		catch( IOException e )
		{ System.err.println( "Graph.createEdges( String " + edgesfile + ")\n" + e.getMessage() ); }
    }

    /**
     * Records locations' descriptions based on a given file ( e.g "mymud.msg" )
     * The first location given becomes the starting location of the map
	 * Format of each line: location message
	 */
    private void recordDescriptions(String messagesfile )
    {
		try {
			FileReader fin = new FileReader( messagesfile );
			BufferedReader messages = new BufferedReader( fin );
			String line;
			boolean first = true; // For recording the start location.
			while((line = messages.readLine()) != null) {
				StringTokenizer st = new StringTokenizer( line );
				if( st.countTokens( ) < 2 ) {
					System.err.println( "Skipping ill-formatted line " + line );
					continue;
				}
				String loc = st.nextToken();
				String msg = "";
				while (st.hasMoreTokens())
					msg = msg + st.nextToken() + " ";

				changeMessage( loc, msg );
				if (first) {				// Record the start location.
					startLocation = loc;
					first = false;
				}
			}
		}
		catch( IOException e ) {
			System.err.println( "Graph.recordDescriptions( String " +
					messagesfile + ")\n" + e.getMessage() );
		}
    }

    /**
     * Records items in each location
     * Format: location thing1 thing2 ...
     */
    private void recordThings( String thingsfile )
    {
		try {
			FileReader fin = new FileReader( thingsfile );
			BufferedReader things = new BufferedReader( fin );
			String line;
			while((line = things.readLine()) != null) {
				StringTokenizer st = new StringTokenizer( line );
				if( st.countTokens( ) < 2 ) {
					System.err.println( "Skipping ill-formatted line " + line );
					continue;
				}
				String loc = st.nextToken();
				while (st.hasMoreTokens())
					addThing( loc, st.nextToken());
			}
		}
		catch( IOException e )
		{ System.err.println( "Graph.recordThings( String " + thingsfile + ")\n" + e.getMessage() ); }
    }

    MUD()
    {
    	createEdges( "static/mymud.edg" );
		recordDescriptions( "static/mymud.msg" );
		recordThings( "static/mymud.thg" );

		System.out.println( "Files read..." );
		System.out.println( vertexMap.size( ) + " vertices\n" );
    }

     // Returns location's description
    public String locationInfo( String loc )
    {
		return getVertex( loc ).toString();
    }

    // Returns MUD's start location
    public String startLocation()
    {
		return startLocation;
    }


    // Add an item in the location
    public void addThing( String loc, String thing )
    {
		Vertex v = getVertex( loc );
		v.addThing( thing );
    }

    // Remove an item from location
    public void delThing( String loc, String thing )
    {
		Vertex v = getVertex( loc );
		v.removeThing( thing );
    }

    // Move between locations
    public String movePlayer(String loc, String dir, String name )
    {
		Vertex v = getVertex( loc );
		Edge e = v.getRoute( dir );
		if (e == null)   // if there is no route in that direction
			return loc;  // no move is made; return current location.
		v.removePlayer(name);
		e._dest.addPlayer( name );
		return e._dest.getName();
    }

    public void addPlayer(String name) {
    	Vertex v = getVertex(this.startLocation);
    	v.addPlayer(name);
    	this.players.add(name);
	}

	public void removePlayer(String loc, String name) {
    	Vertex v = getVertex(loc);
    	v.removePlayer(name);
		this.players.remove(name);
	}

	public String listPlayers() {
    	int player_count = 0;
    	StringBuilder list = new StringBuilder("\nHeroes in this world:\n\t");
    	for(String player : this.players)
			list.append(++player_count).append(". ").append(player).append("\n\t");
		list.append("\n\t").append(player_count).append(" users in the world.\n\n");
		return list.toString();
    }

	// Pick up an item
	public boolean takeThing( String loc, String thing )
	{
		Vertex v = getVertex(loc);
		if (!v.hasThing(thing))
			return false;
		v.removeThing(thing);
		return true;
	}

	// Drop an item
	public void dropThing( String loc, String thing)
	{
		Vertex v = getVertex(loc);
		v.addThing(thing);
	}

	/**
	 * This method enables us to display the entire mud.MUD (mostly used
	 * for testing purposes so that we can check that the structure
	 * defined has been successfully parsed.
	 */
	public String toString()
	{
		String summary = "";
		Iterator iter = vertexMap.keySet().iterator();
		String loc;
		while (iter.hasNext()) {
			loc = (String)iter.next();
			summary = summary + "Node: " + loc;
			summary += ((Vertex)getVertex( loc )).toString();
		}
		summary += "Start location = " + startLocation;
		return summary;
	}

}
