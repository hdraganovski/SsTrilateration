# Android Trilateration

Trilateration with bluetooth, wifi and a combination of both.

# Bluetooth implementation
Bluetooth communication is done with EddystoneURL beacons. Each beacon advertises its position as url in the form 
"https://{x},{y}" where x and y are floating point numbers.

The average error of the measurements are around 20cm with no obsticles on short distances.

# Wifi implementation
I used wifi hotspots on android as beacons. The position is advertised through the name of the network.
The distance is measured with the following formula TODO values

The scans are done TODO value. I cut off the top and bottom outliers and calculate the average of the remaining. The bluetooth scanning uses the same method.

Accuracy TODO value

# Results

# References

# Libraries used
