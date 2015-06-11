# VSS - Verteilte Philosophen

### Start Client

Clients werden mit Angabe einer Broadcast-Adresse gestartet. Anschließend bindet sich der Client automatisch in das Netzwerk ein.

Example:

<code>java edu.hm.cs.vss.Main 192.168.1.255</code>

### Command-Line-Interface

<code>java edu.hm.cs.vss.cli.Main</code>

#### Add Philosophe
<code>java edu.hm.cs.vss.cli.Main add p [hungry]</code>


#### Remove Philosophe
<code>java edu.hm.cs.vss.cli.Main remove p</code>


#### Add Seat
<code>java edu.hm.cs.vss.cli.Main add s</code>


#### Remove Seat
<code>java edu.hm.cs.vss.cli.Main remove s</code>


### Status Output

#### Status Philosophers
<code>java edu.hm.cs.vss.cli.Main status p</code>


#### Status Seat
<code>java edu.hm.cs.vss.cli.Main status s</code>
