# short-uuid

In this project some ideas for generating 64 bits uuids are been tested.


## UUID10

```
  30-bit timestamp 
   7-bit machine/process identifier,
  20-bit counter, starting with a random value
   7-bit random number
```

For the timestamp part we use the 64 bits of System.currentTimeMillis() and then
we use 27 bits for the seconds and 3 bits for storing milliseconds

With three bits for millisecond we can store eight values:  0/8s, 1/8s, 2/8, 3/8, 4/8, 5/8, 6/8, 7/8

with 27 bit for the seconds we can store 4.2560 years

Usage:

```
  UUID10 uuid10 = new UUID10();
  long next = uuid10.getUUID();
```

## UUID11

```
  33-bit timestamp 
   8-bit machine/process identifier,
  23-bit counter, starting with a random value
```

For the timestamp part we use the 64 bits of System.currentTimeMillis() and then
we use 30 bits for the seconds and 3 bits for storing milliseconds

With three bits for millisecond we can store eight values:  0/8s, 1/8s, 2/8, 3/8, 4/8, 5/8, 6/8, 7/8

with 30 bit for the seconds we can store 34.04 years

Usage:

```
  UUID11 uuid11 = new UUID11();
  long next = uuid11.getUUID();
```

## UUID12

```
  34-bit timestamp 
   8-bit machine/process identifier,
  22-bit counter, starting with a random value
```

For the timestamp part we use the 64 bits of System.currentTimeMillis() and then
we use 31 bits for the seconds and 3 bits for storing milliseconds

With three bits for millisecond we can store eight values:  0/8s, 1/8s, 2/8, 3/8, 4/8, 5/8, 6/8, 7/8

with 31 bit for the seconds we can store 68.096 years

Usage:

```
  UUID12 uuid12 = new UUID12();
  long next = uuid12.getUUID();
```

## UUID13

```
  33-bit timestamp 
   8-bit machine/process identifier,
  23-bit counter, starting with a random value
```

Timestamp number of seconds since 1 Jam 1970

This 33 bit of timestamp will repeat in 272.38 years

Usage:

```
  UUID12 uuid13 = new UUID13();
  long next = uuid13.getUUID();
```