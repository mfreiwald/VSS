# VSS - Verteilte Philosophen

### Start Client

Clients werden mit Angabe einer Broadcast-Adresse gestartet. Anschließend bindet sich der Client automatisch in das Netzwerk ein.

Example:

<code>java edu.hm.cs.vss.Main 192.168.1.255</code>

### Command-Line-Interface

<code>java edu.hm.cs.vss.cli.Main</code>

#### Add Philosophe
<code>java edu.hm.cs.vss.cli.Main add p</code>

<code>java edu.hm.cs.vss.cli.Main add Philosopher</code>


#### Remove Philosophe
<code>java edu.hm.cs.vss.cli.Main remove p</code>

<code>java edu.hm.cs.vss.cli.Main remove Philosopher</code>


#### Add Seat
<code>java edu.hm.cs.vss.cli.Main add s</code>

<code>java edu.hm.cs.vss.cli.Main add Seat</code>


#### Remove Seat
<code>java edu.hm.cs.vss.cli.Main remove s</code>

<code>java edu.hm.cs.vss.cli.Main remove Seat</code>


### Status Output

#### Status Philosophers
<code>java edu.hm.cs.vss.cli.Main status p</code>

<code>java edu.hm.cs.vss.cli.Main status Philosopher</code>


#### Status Seat
<code>java edu.hm.cs.vss.cli.Main status s</code>

<code>java edu.hm.cs.vss.cli.Main status Seat</code>
